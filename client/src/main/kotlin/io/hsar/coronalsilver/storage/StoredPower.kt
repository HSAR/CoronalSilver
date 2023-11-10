package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.mech.Battery
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.Power
import io.hsar.coronalsilver.data.mech.Reactor

sealed class StoredPower<out T : Power> : StoredComponent<T>

data class StoredBattery(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val maxOutput: Double, val mass: Double,
    val signature: Double,
    val capacity: Double,
) : StoredPower<Battery>() {
    override fun bind(subcomponents: Map<String, Component?>) = Battery(
        name = name, manufacturer = manufacturer, desc = desc,
        maxOutput, mass, signature, capacity,
    )
}

data class StoredReactor(
    override val name: String, override val manufacturer: String?, override val desc: String?,
    val maxOutput: Double, val mass: Double,
    val signature: Double,
) : StoredPower<Reactor>() {
    override fun bind(subcomponents: Map<String, Component?>) = Reactor(
        name = name, manufacturer = manufacturer, desc = desc,
        maxOutput, mass, signature,
    )
}
