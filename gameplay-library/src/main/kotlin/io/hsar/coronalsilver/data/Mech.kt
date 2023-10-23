package io.hsar.coronalsilver.data

import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource
import io.hsar.coronalsilver.plusWithSummation

data class Mech(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val chassis: Chassis,
    val intelligence: Intelligence,
    val power: Power,
    val mobility: Mobility,
    val manipulators: Manipulators,
) : Component, CompositeConsumer, CompositeProvider {

    private val allComponents = listOfNotNull(chassis, intelligence, power, mobility, manipulators)

    override val consumers: List<Consumer> = allComponents.filterIsInstance(Consumer::class.java)
    override val providers: List<Provider> = allComponents.filterIsInstance(Provider::class.java)

    fun validate(): Result<Boolean> {
        val intersect = this.provided.keys.intersect(this.consumed.keys)

        val missing = this.consumed.keys - intersect
        if (missing.any()) {
            return Result.failure(IllegalArgumentException("Consumes but does not provide $missing"))
        }

        this.consumed.mapNotNull { (key, consumed) ->
            val provided = this.provided[key]!!
            if (provided < consumed) {
                "Insufficient $key: Consumes $consumed, provided $provided"
            } else {
                null
            }
        }.any { return Result.failure(IllegalArgumentException("Provided is insufficient: $it")) }

        return Result.success(true)
    }
}

/**
 * The chassis itself may provide or consume certain resources.
 */
data class Chassis(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val signature: Double,
    val otherConsumed: Map<Resource, Double> = emptyMap(),
    val otherProvided: Map<Resource, Double> = emptyMap(),
) : Component, Consumer, Provider {
    override val consumed = mapOf(
        Resource.MASS to mass,
    ).plusWithSummation(otherConsumed)

    override val provided = mapOf(
        Resource.SIGNATURE to signature
    ).plusWithSummation(otherConsumed)
}