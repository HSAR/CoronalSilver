package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.ArmPair
import io.hsar.coronalsilver.data.Component
import io.hsar.coronalsilver.data.Grabber
import io.hsar.coronalsilver.data.Hand
import io.hsar.coronalsilver.data.Manipulators
import io.hsar.coronalsilver.data.Mount
import io.hsar.coronalsilver.data.Weapon

sealed interface StoredManipulators<out T : Manipulators> : StoredComponent<T>

data class StoredArmPair(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
) : StoredManipulators<ArmPair> {
    override fun bind(subcomponents: Map<String, Component?>) = ArmPair(
        name = name, manufacturer = manufacturer, desc = desc,
        handL = subcomponents["handL"] as Hand,
        handR = subcomponents["handR"] as Hand,
    )
}

sealed interface StoredHand<out T : Hand> : StoredComponent<T>

data class StoredGrabber(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double, val actionPointsMinor: Int
) : StoredHand<Grabber> {
    override fun bind(subcomponents: Map<String, Component?>) = Grabber(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, powerConsumed, actionPointsMinor,
        weapon = subcomponents["weapon"] as? Weapon
    )
}

data class StoredMount(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double
) : StoredHand<Mount> {
    override fun bind(subcomponents: Map<String, Component?>) = Mount(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, powerConsumed,
        weapon = subcomponents["weapon"] as Weapon
    )
}