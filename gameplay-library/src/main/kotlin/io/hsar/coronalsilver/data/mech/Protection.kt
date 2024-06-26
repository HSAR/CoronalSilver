package io.hsar.coronalsilver.data.mech

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Resource
import io.hsar.coronalsilver.action.Quadrant

data class ProtectionScheme(
    val forward: List<ProtectionLayer>,
    val left: List<ProtectionLayer>,
    val right: List<ProtectionLayer>,
    val rear: List<ProtectionLayer>,
) : Component, CompositeConsumer {
    override val name: String = "Protection Scheme"
    override val manufacturer: String? = null
    override val desc: String? = null

    override val consumers: List<Consumer> = listOf(forward, left, right, rear).flatten()

    fun toQuadrantMap() = listOf(Quadrant.FRONT to this.forward, Quadrant.RIGHT to this.right, Quadrant.REAR to this.rear, Quadrant.LEFT to this.left)
        .toMap()
}

/**
 * @param deflection - base probability to deflect
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface ProtectionLayer : Component {
    val valueRemaining: Double
    val deflection: Double

    /**
     * @return Pair where first element is the resulting protection, null if destroyed, and second is remaining damage to pass to next protection layer.
     */
    fun applyDamage(damageValue: Double): Pair<ProtectionLayer?, Double>
}

/**
 * In general, heavy armour has high deflection and low defense while light armour is the reverse
 *
 * @param mass - used as hitpoints for physical armour
 * @param valuePerMass - defense per unit of mass
 */
data class ArmourPlate(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    override val deflection: Double,
    val valuePerMass: Double,
    val mass: Double,
) : ProtectionLayer {
    override val valueRemaining = mass * valuePerMass

    override val consumed = mapOf(
        Resource.MASS to mass,
    )

    override fun applyDamage(damageValue: Double) =
        if (this.valueRemaining >= damageValue) {
            val massToReduce = damageValue / valuePerMass
            this.copy(mass = this.mass - massToReduce) to 0.0
        } else {
            null to damageValue - valueRemaining
        }
}

/**
 * Shields constantly regenerate value and are immune to armour piercing but never deflect and have low maxValue
 *
 * @param valuePerPower - value regenerated per power consumed
 */
data class EnergyShield(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    override val valueRemaining: Double,
    val mass: Double,
    val powerConsumed: Double,
    val valuePerPower: Double,
    val maxValue: Double,
) : ProtectionLayer {
    override val deflection: Double = 0.0

    override val consumed = mapOf(
        Resource.POWER to powerConsumed,
        Resource.MASS to mass,
    )

    override fun applyDamage(damageValue: Double) =
        if (this.valueRemaining >= damageValue) {
            this.copy(valueRemaining = this.valueRemaining - damageValue) to 0.0
        } else {
            this.copy(valueRemaining = 0.0) to damageValue - valueRemaining
        }
}