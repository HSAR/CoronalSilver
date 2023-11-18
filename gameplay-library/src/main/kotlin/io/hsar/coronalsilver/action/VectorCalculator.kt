package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.action.Octant.FRONT
import kotlin.math.atan2

object VectorCalculator {

    /**
     * Returns an angle in degrees (0.0 to 360.0).
     */
    fun findRelativeBearing(origin: Vector, target: Vector): Double {
        val relativeVectorY = origin.yPos - target.yPos
        val relativeVectorX = origin.xPos - target.xPos

        // atan2 usually returns the angle from east/counterclockwise, flip the arguments to convert back to north/clockwise
        val objectiveBearing = Math.toDegrees(atan2(relativeVectorX, relativeVectorY))

        val relativeBearing = objectiveBearing - target.bearing

        return (relativeBearing + 360) % 360.0
    }

    fun Double.toOctant() = when (((this + 22.5) / 45).toInt()) {
        0 -> FRONT
        else -> throw IllegalArgumentException("Bearing should be in the range 0.0 to 360.0")
    }
}

enum class Octant { FRONT, FRONT_R, R, REAR_R, REAR, REAR_L, L, FRONT_L, }