package io.hsar.coronalsilver.data

import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource
import io.hsar.coronalsilver.plusWithSummation

sealed interface Manipulators : Component, CompositeConsumer, CompositeProvider

data class ArmPair(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val handL: Hand,
    val handR: Hand,
) : Manipulators {
    override val consumers: List<Consumer> = listOf(handL, handR)
    override val providers: List<Provider> = listOf(handL, handR)
}

sealed interface Hand : Component {
    val weapon: Weapon?
}

data class Grabber(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double, val actionPointsMinor: Int,
    override val weapon: Weapon? = null
) : Hand {
    override val consumed: Map<Resource, Double> = mapOf(
        Resource.MASS to mass,
        Resource.POWER to powerConsumed
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.ACTION_MINOR to actionPointsMinor.toDouble(),
    )
}

data class Mount(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val powerConsumed: Double,
    override val weapon: Weapon
) : Hand {
    override val consumed: Map<Resource, Double> = mapOf(
        Resource.MASS to mass,
        Resource.POWER to powerConsumed
    )
        .plusWithSummation(weapon.consumed)

    override val provided: Map<Resource, Double> = emptyMap<Resource, Double>()
        .plusWithSummation(weapon.provided)
}