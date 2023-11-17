package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.mech.ArmourPlate
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.EnergyShield
import io.hsar.coronalsilver.data.mech.ProtectionLayer
import io.hsar.coronalsilver.reference.ProtectionSchemeSaved

/**
 * See [ProtectionSchemeSaved] instead of Ref system
 */
sealed interface StoredProtection<out T : ProtectionLayer> : StoredComponent<ProtectionLayer>

data class StoredArmourPlate(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val deflection: Double,
    val valuePerMass: Double,
) : StoredProtection<ArmourPlate> {
    override fun bind(subcomponents: Map<String, Component?>) = ArmourPlate(
        name, manufacturer, desc,
        deflection, valuePerMass,
        mass = 1.0 // placeholder only
    )
}

data class StoredEnergyShield(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double,
    val powerConsumed: Double,
    val valuePerPower: Double,
    val maxValue: Double,
) : StoredProtection<EnergyShield> {
    override fun bind(subcomponents: Map<String, Component?>) = EnergyShield(
        name, manufacturer, desc,
        valueRemaining = maxValue,
        mass, powerConsumed, valuePerPower, maxValue
    )
}