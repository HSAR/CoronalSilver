package io.hsar.coronalsilver.data.mech

import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource

data class Weapon(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val formFactor: FormFactor, val fireModes: List<FireMode>, val effector: Effector,
    val loading: Loading,
) : Component, CompositeConsumer, CompositeProvider {
    override val consumers: List<Consumer> = listOf(effector, loading)
    override val providers: List<Provider> = listOf(loading)
}

enum class FormFactor { COMPACT, PDW, RIFLE, HEAVY, SHOULDER_FIRED }

enum class FireMode { SINGLE, BURST, AUTO }

sealed interface Loading : Component {
    val mass: Double
    val powerConsumed: Double
    val ammo: Ammo

    override val consumed: Map<Resource, Double>
        get() = mapOf(Resource.MASS to mass, Resource.POWER to powerConsumed)

    override val provided: Map<Resource, Double>
        get() = mapOf(Resource.AMMO to 1.0)
}

/**
 * Light, low capacity.
 */
data class Magazine(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    override val mass: Double, val capacity: Int, val reloadCost: Double,
    override val ammo: Ammo,
) : Loading {
    override val powerConsumed: Double = 0.0
}

/**
 * Heavy.
 */
data class Autoloader(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    override val mass: Double, override val powerConsumed: Double,
    override val ammo: Ammo,
) : Loading