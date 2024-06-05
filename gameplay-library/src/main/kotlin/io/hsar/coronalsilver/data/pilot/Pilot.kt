package io.hsar.coronalsilver.data.pilot

data class Pilot(
    /**
     * Used for shooting targets. Ranges from 0-1.
     */
    val accuracy: Double,
    /**
     * Used for defensive and reactive maneuvers. Ranges from 0-1.
     */
    val reflexes: Double,
)