package io.hsar.coronalsilver.statistics

import io.hsar.coronalsilver.data.mech.FireMode
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.mech.Weapon
import io.hsar.coronalsilver.data.pilot.Pilot
import kotlin.math.min

object AttackResultCalculator {
    fun roll(mech: Mech, pilot: Pilot, weapon: Weapon, fireMode: FireMode) {
        val hits = StatCalculator.hitChance(mech, pilot, fireMode)
            .let { targetNumber ->
                roll(targetNumber)
            }
            .let { result ->
                val maxHits = when (fireMode) {
                    FireMode.SINGLE -> 1
                    FireMode.BURST -> 3
                    FireMode.AUTO -> 100
                }
                min(result, maxHits)
            }
        TODO("Add damage")
    }

}