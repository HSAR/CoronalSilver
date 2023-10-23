package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.Autoloader
import io.hsar.coronalsilver.data.Component
import io.hsar.coronalsilver.data.Effector
import io.hsar.coronalsilver.data.FormFactor
import io.hsar.coronalsilver.data.Loading
import io.hsar.coronalsilver.data.Magazine
import io.hsar.coronalsilver.data.Weapon

data class StoredWeapon(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val formFactor: FormFactor,
    val effector: Effector,
) : StoredComponent<Weapon> {
    override fun bind(subcomponents: Map<String, Component?>) = Weapon(
        name = name, manufacturer = manufacturer, desc = desc,
        formFactor, effector,
        loading = subcomponents["loading"] as Loading,
    )
}

sealed interface StoredLoading : StoredComponent<Loading>

data class StoredMagazine(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val caliber: String,
    val mass: Double, val capacity: Int, val reloadCost: Double
) : StoredComponent<Magazine> {
    override fun bind(subcomponents: Map<String, Component?>) = Magazine(
        name = name, manufacturer = manufacturer, desc = desc,
        caliber, mass, capacity, reloadCost
    )
}

data class StoredAutoloader(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val caliber: String,
    val mass: Double, val powerConsumed: Double
) : StoredComponent<Autoloader> {
    override fun bind(subcomponents: Map<String, Component?>) = Autoloader(
        name = name, manufacturer = manufacturer, desc = desc,
        caliber, mass, powerConsumed,
    )
}