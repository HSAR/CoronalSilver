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
        firer
            .let { (pilot, mech) ->
                AttackResultCalculator.roll(mech, pilot, weapon, fireMode)
            }
            .map { hit ->
                val (pilot, mech) = target
                DefendResultCalculator.roll(mech, pilot, hit)
            }
    }
}

class World {
    val positions: Map<Pair<Pilot, Mech>, Pair<Int, Int>> = emptyMap()
}