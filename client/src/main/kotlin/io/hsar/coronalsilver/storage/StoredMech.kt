package io.hsar.coronalsilver.storage

import io.hsar.coronalsilver.Resource
import io.hsar.coronalsilver.data.mech.ActiveMech
import io.hsar.coronalsilver.data.mech.Chassis
import io.hsar.coronalsilver.data.mech.Component
import io.hsar.coronalsilver.data.mech.Intelligence
import io.hsar.coronalsilver.data.mech.Manipulators
import io.hsar.coronalsilver.data.mech.Mobility
import io.hsar.coronalsilver.data.mech.Power
import io.hsar.coronalsilver.data.mech.ProtectionScheme

data class StoredMech(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
) : StoredComponent<ActiveMech> {
    override fun bind(subcomponents: Map<String, Component?>) = ActiveMech(
        name = name, manufacturer = manufacturer, desc = desc,
        chassis = subcomponents["chassis"] as Chassis,
        intelligence = subcomponents["intelligence"] as Intelligence,
        power = subcomponents["power"] as Power,
        mobility = subcomponents["mobility"] as Mobility,
        manipulators = subcomponents["manipulators"] as Manipulators,
    )
}

data class StoredChassis(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val signature: Double,
    val otherConsumed: Map<Resource, Double> = emptyMap(),
    val otherProvided: Map<Resource, Double> = emptyMap(),
) : StoredComponent<Chassis> {
    override fun bind(subcomponents: Map<String, Component?>) = Chassis(
        name = name, manufacturer = manufacturer, desc = desc,
        mass, signature,
        protection = (subcomponents["protection"] as ProtectionScheme).toQuadrantMap(),
        otherConsumed, otherProvided
    )
}