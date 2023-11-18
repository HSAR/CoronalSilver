package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.data.mech.FireMode
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.mech.Weapon
import io.hsar.coronalsilver.data.pilot.Pilot
import io.hsar.coronalsilver.statistics.AttackResultCalculator
import io.hsar.coronalsilver.statistics.DefendResultCalculator

class Simulation(var world: World) {

    fun fireWeapon(
        target: Pair<Pilot, Mech>,
        firer: Pair<Pilot, Mech>,
        weapon: Weapon,
        fireMode: FireMode
    ) {
        val (targetPilot, targetMech) = target

        val (effectedTarget, targetVector) = firer
            .let { (pilot, mech) ->
                AttackResultCalculator.roll(mech, pilot, weapon, fireMode)
            }
            .fold(targetMech to world.positions[target]!!) { targetState, hit ->
                val (effectedTargetMech, currentVector) = targetState
                DefendResultCalculator.roll(world.positions[firer]!!, effectedTargetMech, targetPilot, currentVector, hit)
            }

        if (effectedTarget != targetMech) {
            val updatedPositions = world.positions
                .also { positions ->
                    val position = positions[target]
                    positions + mapOf((target.first to effectedTarget) to position)
                }
            world = world.copy(positions = updatedPositions)
        }
    }
}

data class World(val positions: Map<Pair<Pilot, Mech>, Vector> = emptyMap())

data class Vector(val xPos: Double, val yPos: Double, val zPos: Double, val bearing: Double)