package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.mech.Actuator
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.Frame
import io.hsar.coronalsilver.data.mech.Mobility

data class StoredMobility(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
) : StoredComponent<Mobility> {
    override fun bind(subcomponents: Map<String, Component?>) = Mobility(
        name = name, manufacturer = manufacturer, desc = desc,
        frame = subcomponents["frame"] as Frame,
        actuator = subcomponents["actuator"] as Actuator,
    )
}

data class StoredFrame(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val maxMass: Double, val maxAcceleration: Double, val maxVelocity: Double
) : StoredComponent<Frame> {
    override fun bind(subcomponents: Map<String, Component?>) = Frame(
        name = name, manufacturer = manufacturer, desc = desc,
        maxMass, maxAcceleration, maxVelocity,
    )
}

data class StoredActuator(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val powerConsumed: Double, val mass: Double, val acceleration: Double
) : StoredComponent<Actuator> {
    override fun bind(subcomponents: Map<String, Component?>) = Actuator(
        name = name, manufacturer = manufacturer, desc = desc,
        powerConsumed, mass, acceleration,
    )
}