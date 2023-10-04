package io.hsar.coronalsilver.data

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Named
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface Power : Provider, Consumer

/**
 * Light but finite.
 */
data class Battery(override val name: String, val capacity: Double, val maxOutput: Double, val mass: Double) : Named, Power {
    override val consumed = mapOf(
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.POWER to maxOutput,
        Resource.MASS to mass,
    )
}

/**
 * Heavy, infinite.
 */
data class Reactor(override val name: String, val maxOutput: Double, val mass: Double) : Named, Power {
    override val consumed = mapOf(
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.POWER to maxOutput,
        Resource.MASS to mass,
    )
}