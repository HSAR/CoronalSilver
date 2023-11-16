package io.hsar.coronalsilver.statistics

import io.hsar.coronalsilver.data.mech.FireMode
import io.hsar.coronalsilver.data.mech.FireMode.AUTO
import io.hsar.coronalsilver.data.mech.FireMode.BURST
import io.hsar.coronalsilver.data.mech.FireMode.SINGLE
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.pilot.Pilot

object StatCalculator {

    fun hitChance(mech: Mech, pilot: Pilot, fireMode: FireMode): Int {
        // TODO mech bonuses to pilot for firing - targeting system
        return when (fireMode) {
            SINGLE -> 1.0 to 0
            BURST -> 1.0 to -10
            AUTO -> 0.9 to -20
        }.let { (multiplier, constant) ->
            (pilot.accuracy * multiplier) + constant
        }.toInt()
    }
}