package io.hsar.coronalsilver.cli

import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.coronalsilver.data.Chassis
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ValidateDataTest {

    @Test
    fun `data parses and validates correctly`() {
        this::class.java.classLoader.getResource("json/chassis.json")!!.readText()
            .let { OBJECT_MAPPER.readValue<List<Chassis>>(it) }
            .map { it.validate() }
            .map { assertThat("Validated correctly", it.isSuccess) }
    }

}