package io.hsar.coronalsilver.cli

import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.coronalsilver.data.Component
import io.hsar.coronalsilver.data.Mech
import io.hsar.coronalsilver.reference.MechRef
import io.hsar.coronalsilver.storage.StoredComponent
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ValidateDataTest {

    @Test
    fun `full mech parses and validates correctly`() {
        resource("complete/chassis.json")
            .let { OBJECT_MAPPER.readValue<Map<String, Mech>>(it) }
            .map { (_, mech) -> mech.validate() }
            .map { assertThat("Validated correctly", it.isSuccess) }
    }

    @Test
    fun `mech ref parses, binds and validates correctly`() {
        val storedComponents = resource("components/components.json")
            .let { OBJECT_MAPPER.readValue<List<StoredComponent<Component>>>(it) }
            .associateBy { it.name }

        val test = resource("refs/hermes.json")
            .let { OBJECT_MAPPER.readValue<MechRef>(it).bind(storedComponents) }
            .validate()
        assertThat("test", test.isSuccess)
    }

    private fun resource(input: String) = this::class.java.classLoader.getResource(input)!!.readText()

}