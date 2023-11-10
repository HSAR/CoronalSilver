package io.hsar.coronalsilver.data.mech

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Resource

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface Power : Component {
    val maxOutput: Double
}

/**
 * Light but finite.
 */
data class Battery(
    override val name: String,
    override val manufacturer: String? = null,
    override val desc: String? = null,
    override val maxOutput: Double, val mass: Double,
    val signature: Double,
    val capacity: Double,
) : Component, Power {
    override val consumed = mapOf(
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.POWER to maxOutput,
        Resource.SIGNATURE to signature,
    )
}

/**
 * Heavy, infinite.
 */
data class Reactor(
    override val name: String, override val manufacturer: String?, override val desc: String?,
    override val maxOutput: Double, val mass: Double,
    val signature: Double,
) : Component, Power {
    override val consumed = mapOf(
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.POWER to maxOutput,
        Resource.SIGNATURE to signature,
    )
}