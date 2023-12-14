package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.action.Octant.FRONT
import io.hsar.coronalsilver.action.Octant.FRONT_L
import io.hsar.coronalsilver.action.Octant.FRONT_R
import io.hsar.coronalsilver.action.Octant.L
import io.hsar.coronalsilver.action.Octant.R
import io.hsar.coronalsilver.action.Octant.REAR
import io.hsar.coronalsilver.action.Octant.REAR_L
import io.hsar.coronalsilver.action.Octant.REAR_R
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
        1 -> FRONT_R
        2 -> R
        3 -> REAR_R
        4 -> REAR
        5 -> REAR_L
        6 -> L
        7 -> FRONT_L
        else -> throw IllegalArgumentException("Bearing should be in the range 0.0 to 360.0")
    }
}

enum class Quadrant { FRONT, RIGHT, REAR, LEFT }

enum class Octant {
    FRONT, FRONT_R, R, REAR_R, REAR, REAR_L, L, FRONT_L;

    fun toQuadrants() = when (this) {
        FRONT -> listOf(Quadrant.FRONT)
        FRONT_L -> listOf(Quadrant.FRONT, Quadrant.LEFT)
        FRONT_R -> listOf(Quadrant.FRONT, Quadrant.RIGHT)
        L -> listOf(Quadrant.LEFT)
        R -> listOf(Quadrant.RIGHT)
        REAR -> listOf(Quadrant.REAR)
        REAR_L -> listOf(Quadrant.REAR, Quadrant.LEFT)
        REAR_R -> listOf(Quadrant.REAR, Quadrant.RIGHT)
    }
}