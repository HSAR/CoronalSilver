package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.data.mech.Ammo
import io.hsar.coronalsilver.data.mech.Autoloader
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.Effector
import io.hsar.coronalsilver.data.mech.FireMode
import io.hsar.coronalsilver.data.mech.FormFactor
import io.hsar.coronalsilver.data.mech.Loading
import io.hsar.coronalsilver.data.mech.Magazine
import io.hsar.coronalsilver.data.mech.Weapon

data class StoredWeapon(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val formFactor: FormFactor, val fireModes: List<FireMode>, val effector: Effector,
) : StoredComponent<Weapon> {
    override fun bind(subcomponents: Map<String, Component?>) = Weapon(
        name = name, manufacturer = manufacturer, desc = desc,
        formFactor, fireModes, effector,
        loading = subcomponents["loading"] as Loading,
    )
}

sealed interface StoredLoading : StoredComponent<Loading>

data class StoredMagazine(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val capacity: Int, val reloadCost: Double
) : StoredComponent<Magazine> {
    override fun bind(subcomponents: Map<String, Component?>) = Magazine(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, capacity, reloadCost,
        ammo = subcomponents["ammo"] as Ammo
    )
}

data class StoredAutoloader(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double
) : StoredComponent<Autoloader> {
    override fun bind(subcomponents: Map<String, Component?>) = Autoloader(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, powerConsumed,
        ammo = subcomponents["ammo"] as Ammo
    )
}

data class StoredAmmo(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val damage: Double,
    val armourPiercing: Double,
) : StoredComponent<Ammo> {
    override fun bind(subcomponents: Map<String, Component?>) = Ammo(
        name = name, manufacturer = manufacturer, desc = desc,
        damage, armourPiercing
    )
}