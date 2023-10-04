package io.hsar.coronalsilver.data

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Resource

/**
 * TODO: Falloff
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface Effector : Consumer {
    val mass: Double
    val powerConsumed: Double
    val damageType: DamageType
    val caliber: Double

    override val consumed: Map<Resource, Double>
        get() = mapOf(
            Resource.WEAPON_SOCKET to 1.0,
            Resource.AMMO to 1.0,
            Resource.MASS to mass,
            Resource.POWER to powerConsumed
        )
}

enum class DamageType { LASER, KINETIC, PAYLOAD }

class Laser(override val mass: Double, override val powerConsumed: Double, override val caliber: Double) : Effector {
    override val damageType = DamageType.LASER
}

class Kinetic(override val mass: Double, override val caliber: Double) : Effector {
    override val damageType: DamageType = DamageType.KINETIC
    override val powerConsumed: Double = 0.0
}