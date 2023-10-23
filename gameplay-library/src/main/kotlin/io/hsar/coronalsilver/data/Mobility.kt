package io.hsar.coronalsilver.data

import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource

data class Mobility(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val frame: Frame, val actuator: Actuator
) : Component, CompositeProvider {
    override val providers: List<Provider> = listOf(frame)
}

/**
 * Physical legs, must be sturdy enough to support the mass
 */
data class Frame(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val maxMass: Double, val maxAcceleration: Double, val maxVelocity: Double
) : Component, Provider {
    override val provided: Map<Resource, Double> = mapOf(Resource.MASS to maxMass)
}

data class Actuator(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val powerConsumed: Double, val mass: Double, val acceleration: Double
) : Component, Consumer, Provider {
    override val consumed = mapOf(
        Resource.POWER to powerConsumed,
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(Resource.MOVEMENT to acceleration)
}