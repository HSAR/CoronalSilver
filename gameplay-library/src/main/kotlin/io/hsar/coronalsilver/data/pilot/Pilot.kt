package io.hsar.coronalsilver.data.pilot

data class Pilot(
    /**
     * Used for shooting targets.
     */
    val accuracy: Int,
    /**
     * Used to withstand incoming fire.
     */
    val resilience: Int,
)