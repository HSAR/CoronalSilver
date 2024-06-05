package io.hsar.coronalsilver.action

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.pilot.Pilot
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class SimulationTest {

    val TEST_SIMULATION = Simulation(
        world = World(
            positions = mapOf(
                attacker to Vector(0.0, 0.0, 0.0, 90.0),
                defender to Vector(1.0, 0.0, 0.0, 270.0),
            )
        )
    )

    @Test
    fun `firing a weapon results in damage`() {
        // Arrange
        val simulation = TEST_SIMULATION

        // Act
        simulation.fireWeapon(attacker = attacker, defender = defender, weapon = attacker.second.weapons.first(), fireMode = attacker.second.weapons.first().fireModes.first())

        // Assert
        assertThat("Should now be two different mechs due to one being damaged", simulation.world.positions.keys.toSet().size, `is`(2))
    }

    companion object {

        private fun resource(input: String) = this::class.java.classLoader.getResource(input)!!.readText()

        val defaultMech = resource("mechs/hermes.json")
            .let { jacksonObjectMapper().readValue<Map<String, ActiveMech>>(it) }["OS Hermes"]!!

        val attacker = Pilot(accuracy = 1.0, reflexes = 0.0) to defaultMech
        val defender = Pilot(accuracy = 0.0, reflexes = 0.0) to defaultMech
    }
}