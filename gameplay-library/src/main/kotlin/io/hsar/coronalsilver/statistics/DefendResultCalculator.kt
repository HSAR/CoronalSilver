package io.hsar.coronalsilver.statistics

import io.hsar.coronalsilver.action.DefensiveManeuver
import io.hsar.coronalsilver.action.Vector
import io.hsar.coronalsilver.action.VectorCalculator
import io.hsar.coronalsilver.data.mech.Ammo
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.pilot.Pilot

object DefendResultCalculator {
    fun roll(attackingVector: Vector, defendingMech: Mech, defendingPilot: Pilot, defendingVector: Vector, ammo: Ammo): Pair<Mech, Vector> {
        // establish original attack bearing
        val originalBearing = VectorCalculator.findRelativeBearing(attackingVector, defendingVector)

        // defending pilot attempts to twist defensively
        val vectorDelta = DefensiveManeuver.attemptDefensiveTwist(defendingMech, defendingPilot, originalBearing)
        val vector = if (vectorDelta != 0.0) {
            defendingVector.copy(bearing = defendingVector.bearing + vectorDelta)
        } else {
            defendingVector
        }

        return TODO("Remove shields, destroy armour, return resulting mech") to vector
    }
}
