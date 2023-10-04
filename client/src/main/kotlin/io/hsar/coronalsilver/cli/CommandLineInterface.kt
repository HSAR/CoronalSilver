package io.hsar.coronalsilver.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.coronalsilver.data.Chassis
import java.io.File
import kotlin.system.exitProcess

abstract class Command(val name: String) {
    abstract fun run()
}

class CommandLineInterface : Command("validate") {

    @Parameter(
        names = ["--data"],
        description = "Path to an input file describing chassis",
        required = true,
        converter = FileConverter::class
    )
    private lateinit var chassisFilePaths: List<File>

    override fun run() {
        val chassisFiles = chassisFilePaths.flatMap { filePath ->
            if (filePath.isDirectory) {
                (filePath.listFiles() ?: throw IllegalStateException("Failed to retrieve files in directory: $filePath"))
                    .toList()
            } else {
                listOf(filePath)
            }
        }
            .map { filePath ->
                OBJECT_MAPPER.readValue<List<Chassis>>(filePath.readText())
            }
    }
}

val OBJECT_MAPPER = jacksonObjectMapper()
    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    .enable(JsonParser.Feature.ALLOW_COMMENTS)
    .enable(SerializationFeature.INDENT_OUTPUT)!!

fun main(args: Array<String>) {
    val instances: Map<String, Command> = listOf(
        CommandLineInterface()
    )
        .associateBy { it.name }
    val commander = JCommander()
    instances.forEach { (name, command) -> commander.addCommand(name, command) }

    if (args.isEmpty()) {
        commander.usage()
        System.err.println("Expected some arguments")
        exitProcess(1)
    }

    try {
        commander.parse(*args)
        val command = instances[commander.parsedCommand]
        command!!.run()
    } catch (e: Exception) {
        e.printStackTrace()
        exitProcess(1)
    }
}
