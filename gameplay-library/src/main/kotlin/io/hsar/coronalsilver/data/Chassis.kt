package io.hsar.coronalsilver.data

import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Named
import io.hsar.coronalsilver.Provider

data class Chassis(
    override val name: String,
    val intelligence: Intelligence,
    val power: Power,
    val mobility: Mobility,
    val manipulators: List<Manipulator>
) : Named, CompositeConsumer, CompositeProvider {

    override val consumers: List<Consumer> = listOf(intelligence, power, mobility, manipulators).filterIsInstance(Consumer::class.java)
    override val providers: List<Provider> = listOf(intelligence, power, mobility, manipulators).filterIsInstance(Provider::class.java)

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