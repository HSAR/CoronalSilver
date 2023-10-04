package io.hsar.coronalsilver.data

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Named
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource

data class Weapon(
    override val name: String, val formFactor: FormFactor, val effector: Effector, val loading: Ammo
) : Named, CompositeConsumer, CompositeProvider {
    override val consumers: List<Consumer> = listOf(effector, loading)
    override val providers: List<Provider> = listOf()
}

enum class FormFactor { COMPACT, PDW, RIFLE, HEAVY, SHOULDER_FIRED }

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface Ammo : Consumer {
    val mass: Double
    val powerConsumed: Double

    override val consumed: Map<Resource, Double>
        get() = mapOf(Resource.MASS to mass, Resource.POWER to powerConsumed)
}

/**
 * Light, low capacity.
 */
data class Magazine(override val mass: Double, val capacity: Int, val reloadCost: Double) : Ammo {
    override val powerConsumed: Double = 0.0
}

/**
 * Heavy.
 */
data class Autoloader(override val mass: Double, override val powerConsumed: Double) : Ammo