package io.hsar.coronalsilver.reference

import io.hsar.coronalsilver.data.mech.ArmourPlate
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.EnergyShield
import io.hsar.coronalsilver.data.mech.ProtectionLayer
import io.hsar.coronalsilver.data.mech.ProtectionScheme
import io.hsar.coronalsilver.storage.StoredArmourPlate
import io.hsar.coronalsilver.storage.StoredComponent
import io.hsar.coronalsilver.storage.StoredEnergyShield
import io.hsar.coronalsilver.storage.StoredProtection

interface Saved<out T : Component> {
    fun bind(
        storedComponents: Map<String, StoredComponent<Component>>,
    ): T
}

data class ProtectionSchemeSaved(
    val forward: List<ProtectionLayerSaved>,
    val left: List<ProtectionLayerSaved>,
    val right: List<ProtectionLayerSaved>,
    val rear: List<ProtectionLayerSaved>,
) : Saved<ProtectionScheme> {
    override fun bind(storedComponents: Map<String, StoredComponent<Component>>) = ProtectionScheme(
        forward = lookupProtectionLayer(storedComponents, forward),
        left = lookupProtectionLayer(storedComponents, left),
        right = lookupProtectionLayer(storedComponents, right),
        rear = lookupProtectionLayer(storedComponents, rear),
    )

    private fun lookupProtectionLayer(storedComponents: Map<String, StoredComponent<Component>>, savedLayers: List<ProtectionLayerSaved>): List<ProtectionLayer> =
        savedLayers.map { (name, value) ->
            when (val protection = storedComponents[name] as StoredProtection<*>) {
                is StoredArmourPlate -> ArmourPlate(
                    protection.name, protection.manufacturer, protection.desc,
                    protection.deflection, protection.valuePerMass,
                    mass = value
                )

                is StoredEnergyShield -> EnergyShield(
                    protection.name, protection.manufacturer, protection.desc,
                    valueRemaining = protection.maxValue,
                    protection.mass, protection.powerConsumed, protection.valuePerPower, protection.maxValue
                )
            }
        }
}

data class ProtectionLayerSaved(val name: String, val value: Double)