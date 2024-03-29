package io.hsar.coronalsilver.reference

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Named
import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.mech.Actuator
import io.hsar.coronalsilver.data.mech.Ammo
import io.hsar.coronalsilver.data.mech.Chassis
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.Frame
import io.hsar.coronalsilver.data.mech.Hand
import io.hsar.coronalsilver.data.mech.Intelligence
import io.hsar.coronalsilver.data.mech.Loading
import io.hsar.coronalsilver.data.mech.Manipulators
import io.hsar.coronalsilver.data.mech.Mobility
import io.hsar.coronalsilver.data.mech.Power
import io.hsar.coronalsilver.data.mech.Processor
import io.hsar.coronalsilver.data.mech.Sensor
import io.hsar.coronalsilver.data.mech.Weapon
import io.hsar.coronalsilver.storage.StoredActuator
import io.hsar.coronalsilver.storage.StoredAmmo
import io.hsar.coronalsilver.storage.StoredChassis
import io.hsar.coronalsilver.storage.StoredComponent
import io.hsar.coronalsilver.storage.StoredFrame
import io.hsar.coronalsilver.storage.StoredHand
import io.hsar.coronalsilver.storage.StoredIntelligence
import io.hsar.coronalsilver.storage.StoredLoading
import io.hsar.coronalsilver.storage.StoredManipulators
import io.hsar.coronalsilver.storage.StoredMech
import io.hsar.coronalsilver.storage.StoredMobility
import io.hsar.coronalsilver.storage.StoredPower
import io.hsar.coronalsilver.storage.StoredProcessor
import io.hsar.coronalsilver.storage.StoredSensor
import io.hsar.coronalsilver.storage.StoredWeapon

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed class Ref<in S : StoredComponent<C>, out C : Component>(
    open val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?> = emptyMap(),
) : Named, Saved<C> {
    private fun bindSubcomponents(storedComponents: Map<String, StoredComponent<Component>>): Map<String, Component?> =
        subcomponents
            .mapValues { (_, v) -> v?.bind(storedComponents) }

    override fun bind(
        storedComponents: Map<String, StoredComponent<Component>>,
    ): C {
        require(storedComponents.containsKey(name)) { "Expected stored components to contain $name but it did not." }
        return (storedComponents[name]!! as S).bind(
            subcomponents = bindSubcomponents(storedComponents)
        )
    }
}

data class MechRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?>,
) : Ref<StoredMech, ActiveMech>()

data class ChassisRef(
    override val name: String,
    val protection: ProtectionSchemeSaved,
) : Ref<StoredChassis, Chassis>() {
    override fun bind(storedComponents: Map<String, StoredComponent<Component>>): Chassis {
        return (storedComponents[name]!! as StoredChassis).run {
            Chassis(
                name = name, manufacturer = manufacturer, desc = desc,
                mass, signature,
                protection = protection.bind(storedComponents).toQuadrantMap(),
                otherConsumed, otherProvided
            )
        }
    }
}

data class IntelligenceRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?>,
) : Ref<StoredIntelligence, Intelligence>()

data class SensorRef(override val name: String) : Ref<StoredSensor, Sensor>()

data class ProcessorRef(override val name: String) : Ref<StoredProcessor, Processor>()

data class PowerRef(override val name: String) : Ref<StoredPower<Power>, Power>()

data class MobilityRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?>,
) : Ref<StoredMobility, Mobility>()

data class FrameRef(override val name: String) : Ref<StoredFrame, Frame>()

data class ActuatorRef(override val name: String) : Ref<StoredActuator, Actuator>()

data class ManipulatorsRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?>,
) : Ref<StoredManipulators<Manipulators>, Manipulators>()

data class HandRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?>,
) : Ref<StoredHand<Hand>, Hand>()

data class WeaponRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?>,
) : Ref<StoredWeapon, Weapon>()

data class LoadingRef(override val name: String) : Ref<StoredLoading, Loading>()

data class AmmoRef(override val name: String) : Ref<StoredAmmo, Ammo>()