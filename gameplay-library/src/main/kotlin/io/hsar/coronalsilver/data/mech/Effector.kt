package io.hsar.coronalsilver.data.mech

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource

/**
 * TODO: Falloff
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface Effector : Consumer, Provider {
    val mass: Double
    val powerConsumed: Double
    val damageType: DamageType
    val damage: Double

    override val consumed: Map<Resource, Double>
        get() = mapOf(
            Resource.AMMO to 1.0,
            Resource.MASS to mass,
            Resource.POWER to powerConsumed
        )
}

enum class DamageType { LASER, KINETIC, PAYLOAD }

class Laser(override val mass: Double, override val powerConsumed: Double, override val damage: Double) : Effector {
    override val damageType = DamageType.LASER

    override val provided: Map<Resource, Double>
        get() = mapOf(Resource.ACCURACY to 10.0)
}

class Kinetic(override val mass: Double, override val damage: Double) : Effector {
    override val damageType: DamageType = DamageType.KINETIC
    override val powerConsumed: Double = 0.0
}