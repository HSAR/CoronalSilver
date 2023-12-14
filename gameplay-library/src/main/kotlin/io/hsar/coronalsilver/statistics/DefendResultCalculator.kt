package io.hsar.coronalsilver.statistics

import io.hsar.coronalsilver.action.DefensiveManeuver
import io.hsar.coronalsilver.action.Vector
import io.hsar.coronalsilver.action.VectorCalculator
import io.hsar.coronalsilver.action.VectorCalculator.toOctant
import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.mech.Ammo
import io.hsar.coronalsilver.data.mech.DestroyedMech
import io.hsar.coronalsilver.data.mech.Mech
import io.hsar.coronalsilver.data.mech.ProtectionLayer
import io.hsar.coronalsilver.data.pilot.Pilot

object DefendResultCalculator {
    fun roll(attackingVector: Vector, defendingMech: Mech, defendingPilot: Pilot, defendingVector: Vector, ammo: Ammo): Pair<Mech, Vector> {
        return when (defendingMech) {
            is DestroyedMech -> defendingMech to defendingVector
            is ActiveMech -> {
                // establish original attack bearing
                val originalBearing = VectorCalculator.findRelativeBearing(attackingVector, defendingVector)

                // defending pilot attempts to twist defensively
                val vectorDelta = DefensiveManeuver.attemptDefensiveTwist(defendingMech, defendingPilot, originalBearing)
                val (vector, bearing) = if (vectorDelta != 0.0) {
                    defendingVector.copy(bearing = defendingVector.bearing + vectorDelta) to originalBearing + vectorDelta
                } else {
                    defendingVector to originalBearing
                }

                val attackedOctant = bearing.toOctant()
                val attackedQuadrant = attackedOctant.toQuadrants().random()
                val attackedProtection = defendingMech.chassis.protection[attackedQuadrant]!!

                val resultingMech = damageProtection(attackedProtection, ammo).let { (resultingProtection, spillingDamage) ->
                    if (spillingDamage > 0.0) {
                        // TODO: Components are destroyed before completely destroying the mech
                        DestroyedMech(defendingMech.name, defendingMech.manufacturer, defendingMech.desc)
                    } else {
                        val newProtections = defendingMech.chassis.protection.toMutableMap().also { it[attackedQuadrant] = resultingProtection }
                        defendingMech.copy(chassis = defendingMech.chassis.copy(protection = newProtections))
                    }
                }

                resultingMech to vector
            }
        }
    }

    /**
     * Damages and removes protection layers.
     * @return the state of protection layers after the attack and remaining damage if protection has been penetrated.
     */
    fun damageProtection(attackedProtection: List<ProtectionLayer>, ammo: Ammo): Pair<List<ProtectionLayer>, Double> {
        var remainingDamage = ammo.damage
        var remainingProtection = attackedProtection

        while (remainingDamage > 0.0 && remainingProtection.any()) {
            var currentProtection = remainingProtection.first()
            // if there is at least 1.0 value remaining, attempt deflection
            if (currentProtection.valueRemaining >= 1.0) {
                if (isDeflected(currentProtection, ammo)) {
                    return remainingProtection to 0.0
                }
            }

            // apply damage
            while (remainingDamage > 0.0) {
                currentProtection.applyDamage(remainingDamage).let { (resultingProtection, spillingDamage) ->
                    if (resultingProtection == null) remainingProtection = remainingProtection.takeLast(remainingProtection.size - 1)
                    remainingDamage = spillingDamage
                }
            }
        }

        return remainingProtection to remainingDamage
    }

    private fun isDeflected(protectionLayer: ProtectionLayer, ammo: Ammo) =
        (protectionLayer.deflection - ammo.armourPiercing)
            .let { protectionChance ->
                roll(protectionChance * 100) > 0
            }
}
