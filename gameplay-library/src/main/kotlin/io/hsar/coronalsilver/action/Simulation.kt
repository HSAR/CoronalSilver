package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.mech.FireMode
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.mech.Weapon
import io.hsar.coronalsilver.data.pilot.Pilot
import io.hsar.coronalsilver.statistics.AttackResultCalculator
import io.hsar.coronalsilver.statistics.DefendResultCalculator

class Simulation(var world: World) {

    fun fireWeapon(
        attacker: Pair<Pilot, ActiveMech>,
        defender: Pair<Pilot, ActiveMech>,
        weapon: Weapon,
        fireMode: FireMode
    ) {
        val (targetPilot, targetMech) = defender

        val (effectedTarget, targetVector) = attacker
            .let { (pilot, mech) ->
                AttackResultCalculator.roll(mech, pilot, weapon, fireMode)
            }
            .fold((targetMech as Mech) to world.positions[defender]!!) { targetState, hit ->
                val (effectedTargetMech, currentVector) = targetState
                DefendResultCalculator.roll(world.positions[attacker]!!, effectedTargetMech, targetPilot, currentVector, hit)
            }

        if (effectedTarget != targetMech) {
            val updatedPositions = world.positions
                .also { positions ->
                    val position = positions[defender]
                    positions + mapOf((defender.first to effectedTarget) to position)
                }
            world = world.copy(positions = updatedPositions)
        }
    }
}

data class World(val positions: Map<Pair<Pilot, ActiveMech>, Vector> = emptyMap())

data class Vector(val xPos: Double, val yPos: Double, val zPos: Double, val bearing: Double)