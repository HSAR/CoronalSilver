package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.Component
import io.hsar.coronalsilver.data.Intelligence
import io.hsar.coronalsilver.data.Processor
import io.hsar.coronalsilver.data.Sensor

data class StoredIntelligence(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
) : StoredComponent<Intelligence> {
    override fun bind(subcomponents: Map<String, Component?>) = Intelligence(
        name = name, manufacturer = manufacturer, desc = desc,
        sensor = subcomponents["sensor"] as Sensor,
        processor = subcomponents["processor"] as Processor,
    )
}

data class StoredSensor(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double, val sensorQuality: Double
) : StoredComponent<Sensor> {
    override fun bind(subcomponents: Map<String, Component?>) = Sensor(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, powerConsumed, sensorQuality
    )
}

data class StoredProcessor(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double, val actionPointsMajor: Int, val actionPointsMinor: Int
) : StoredComponent<Processor> {
    override fun bind(subcomponents: Map<String, Component?>) = Processor(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, powerConsumed, actionPointsMajor, actionPointsMinor
    )
}