package io.hsar.coronalsilver.data.pilot

data class Pilot(
    /**
     * Used for shooting targets.
     */
    val accuracy: Int,
    /**
     * Used for defensive and reactive maneuvers.
     */
    val reflexes: Int,
)