package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.action.VectorCalculator.toOctant
import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.mech.ProtectionLayer
import io.hsar.coronalsilver.data.pilot.Pilot
import io.hsar.coronalsilver.statistics.Reroll
import io.hsar.coronalsilver.statistics.roll
import kotlin.math.max

object DefensiveManeuver {
    fun attemptDefensiveTwist(mech: ActiveMech, pilot: Pilot, incomingBearing: Double): Double {
        val originalOctant = incomingBearing.toOctant()

        // roll for range of twist available, max 2 octants (or 90 degrees)
        val maxTwist = max(2, roll(pilot.reflexes, Reroll.NONE))

        // select best available defensive octant within the available twist range
        return selectTwistTarget(mech, originalOctant, maxTwist)
    }

    private fun selectTwistTarget(mech: ActiveMech, originOctant: Octant, maxTwist: Int): Double = (-maxTwist..maxTwist)
        .map { octantDelta ->
            val ordinal = (originOctant.ordinal + octantDelta + Octant.entries.size) % Octant.entries.size
            Octant.entries[ordinal] to octantDelta
        }
        .maxBy { (possibleOctant, _) ->
            getProtectionLayersHealth(getOctantProtection(mech, possibleOctant))
        }
        .let { (_, twist) ->
            twist * 45.0
        }

    fun getOctantProtection(mech: ActiveMech, octant: Octant) = octant.toQuadrants()
        .map { quadrant ->
            mech.chassis.protection[quadrant]!!
        }

    private fun getProtectionLayersHealth(protectionLayers: List<List<ProtectionLayer>>): Double = protectionLayers
        .map { protectionLayers ->
            // for each applicable section of protection, sum the remaining health available
            // TODO account for deflection
            protectionLayers.sumOf { layer -> layer.valueRemaining * (1.0 - layer.deflection) }
        }.average() // average out

}