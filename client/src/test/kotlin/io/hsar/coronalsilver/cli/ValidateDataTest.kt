package io.hsar.coronalsilver.cli

import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.reference.MechRef
import io.hsar.coronalsilver.storage.StoredComponent
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ValidateDataTest {

    @Test
    fun `full mech parses and validates correctly`() {
        resource("complete/chassis.json")
            .let { OBJECT_MAPPER.readValue<Map<String, ActiveMech>>(it) }
            .map { (_, mech) -> mech.validate() }
            .map { assertThat("Validation test should be successful but instead was: $it", it.isSuccess) }
    }

    @Test
    fun `mech ref parses, binds and validates correctly`() {
        val storedComponents = resource("components/components.json")
            .let { OBJECT_MAPPER.readValue<List<StoredComponent<Component>>>(it) }
            .associateBy { it.name }

        val test = resource("refs/hermes.json")
            .let { OBJECT_MAPPER.readValue<MechRef>(it).bind(storedComponents) }
            .validate()
        assertThat("Validation test should be successful but instead was: $test", test.isSuccess, `is`(true))
    }

    private fun resource(input: String) = this::class.java.classLoader.getResource(input)!!.readText()

}