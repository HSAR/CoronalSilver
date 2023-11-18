package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.action.Octant.FRONT
import io.hsar.coronalsilver.action.Octant.FRONT_L
import io.hsar.coronalsilver.action.Octant.FRONT_R
import io.hsar.coronalsilver.action.Octant.L
import io.hsar.coronalsilver.action.Octant.R
import io.hsar.coronalsilver.action.Octant.REAR
import io.hsar.coronalsilver.action.Octant.REAR_L
import io.hsar.coronalsilver.action.Octant.REAR_R
import io.hsar.coronalsilver.action.VectorCalculator.toOctant
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.pilot.Pilot
import io.hsar.coronalsilver.statistics.Reroll
import io.hsar.coronalsilver.statistics.roll
import kotlin.math.max

object DefensiveManeuver {
    fun attemptDefensiveTwist(mech: Mech, pilot: Pilot, incomingBearing: Double): Double {
        val originalOctant = incomingBearing.toOctant()

        // roll for range of twist available, max 2 octants (or 90 degrees)
        val maxTwist = max(2, roll(pilot.reflexes, Reroll.NONE))

        // select best available defensive octant within the available twist range
        return selectTwistTarget(mech, originalOctant, maxTwist)
    }

    private fun selectTwistTarget(mech: Mech, originOctant: Octant, maxTwist: Int): Double = (-maxTwist..maxTwist)
        .map { octantDelta ->
            val ordinal = (originOctant.ordinal + octantDelta + Octant.entries.size) % Octant.entries.size
            Octant.entries[ordinal] to octantDelta
        }
        .maxBy { (possibleOctant, _) ->
            octantHealth(mech, possibleOctant)
        }
        .let { (_, twist) ->
            twist * 45.0
        }

    private fun octantHealth(mech: Mech, octant: Octant): Double = with(mech.chassis.protection) {
        // look up the sections of protection that will be hit with the given octant
        when (octant) {
            FRONT -> listOf(forward)
            FRONT_L -> listOf(forward, left)
            FRONT_R -> listOf(forward, right)
            L -> listOf(left)
            R -> listOf(right)
            REAR -> listOf(rear)
            REAR_L -> listOf(rear, left)
            REAR_R -> listOf(rear, right)
        }.map { protectionLayers ->
            // for each applicable section of protection, sum the remaining health available
            // TODO account for deflection
            protectionLayers.sumOf { layer -> layer.valueRemaining * (1.0 - layer.deflection) }
        }.average() // average out
    }

}