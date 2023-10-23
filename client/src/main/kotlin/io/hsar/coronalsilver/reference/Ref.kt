package io.hsar.coronalsilver.reference

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Named
import io.hsar.coronalsilver.data.Actuator
import io.hsar.coronalsilver.data.Chassis
import io.hsar.coronalsilver.data.Component
import io.hsar.coronalsilver.data.Frame
import io.hsar.coronalsilver.data.Hand
import io.hsar.coronalsilver.data.Intelligence
import io.hsar.coronalsilver.data.Loading
import io.hsar.coronalsilver.data.Manipulators
import io.hsar.coronalsilver.data.Mech
import io.hsar.coronalsilver.data.Mobility
import io.hsar.coronalsilver.data.Power
import io.hsar.coronalsilver.data.Processor
import io.hsar.coronalsilver.data.Sensor
import io.hsar.coronalsilver.data.Weapon
import io.hsar.coronalsilver.storage.StoredActuator
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
) : Named {
    private fun bindSubcomponents(storedComponents: Map<String, StoredComponent<Component>>): Map<String, Component?> =
        subcomponents
            .mapValues { (_, v) -> v?.bind(storedComponents) }

    fun bind(storedComponents: Map<String, StoredComponent<Component>>): C {
        require(storedComponents.containsKey(name)) { "Expected stored components to contain $name but it did not." }
        return (storedComponents[name]!! as S).bind(
            subcomponents = bindSubcomponents(storedComponents)
        )
    }
}

data class MechRef(
    override val name: String,
    override val subcomponents: Map<String, Ref<StoredComponent<Component>, Component>?> = emptyMap(),
) : Ref<StoredMech, Mech>()

data class ChassisRef(override val name: String) : Ref<StoredChassis, Chassis>()

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