package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.mech.Weapon
import io.hsar.coronalsilver.data.pilot.Pilot

class Simulation(var world: World) {

    fun fireWeapon(
        firer: Pair<Pilot, Mech>,
        weapon: Weapon,
        target: Pair<Pilot, Mech>,
    ) {

    }
}

class World {
    val positions: Map<Pair<Pilot, Mech>, Pair<Int, Int>> = emptyMap()
}