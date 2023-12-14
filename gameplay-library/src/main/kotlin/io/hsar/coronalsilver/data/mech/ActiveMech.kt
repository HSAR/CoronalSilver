package io.hsar.coronalsilver.data.mech

import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource
import io.hsar.coronalsilver.action.Quadrant
import io.hsar.coronalsilver.plusWithSummation

sealed interface Mech

data class DestroyedMech(
    val name: String,
    val manufacturer: String? = null,
    val desc: String? = null,
) : Mech

data class ActiveMech(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val chassis: Chassis,
    val intelligence: Intelligence,
    val power: Power,
    val mobility: Mobility,
    val manipulators: Manipulators,
) : Mech, Component, CompositeConsumer, CompositeProvider {

    private val allComponents = listOfNotNull(chassis, intelligence, power, mobility, manipulators)

    override val consumers: List<Consumer> = allComponents.filter { it.consumed.any() }
    override val providers: List<Provider> = allComponents.filter { it.provided.any() }

    val weapons = manipulators.weapons

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
 * Chasses mount armour protection and may provide or consume certain resources.
 */
data class Chassis(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    val mass: Double, val signature: Double,
    val protection: Map<Quadrant, List<ProtectionLayer>>,
    val otherConsumed: Map<Resource, Double> = emptyMap(),
    val otherProvided: Map<Resource, Double> = emptyMap(),
) : Component {

    override val consumed = protection.values.flatten()
        .fold(emptyMap<Resource, Double>()) { acc, curr -> acc.plusWithSummation(curr.consumed) }
        .plusWithSummation(
            mapOf(Resource.MASS to mass)
        )
        .plusWithSummation(otherConsumed)

    override val provided = mapOf(
        Resource.SIGNATURE to signature
    ).plusWithSummation(otherConsumed)
}