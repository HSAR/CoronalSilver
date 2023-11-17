package io.hsar.coronalsilver.data.mech

data class Ammo(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val damage: Double,
    val armourPiercing: Double,
) : Component