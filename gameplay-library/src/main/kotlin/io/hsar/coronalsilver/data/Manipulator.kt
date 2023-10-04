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
sealed interface Manipulator : Consumer, Provider {
    val weapons: List<Weapon>
}

data class Grabber(
    override val name: String, val mass: Double, val powerConsumed: Double, val actionPointsMinor: Int, override val weapons: List<Weapon>
) : Named, Manipulator {
    override val consumed: Map<Resource, Double> = mapOf(
        Resource.MASS to mass,
        Resource.POWER to powerConsumed
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.WEAPON_SOCKET to weapons.size.toDouble(),
        Resource.ACTION_MINOR to actionPointsMinor.toDouble(),
    )
}

data class Mount(
    override val name: String, val mass: Double, val powerConsumed: Double, override val weapons: List<Weapon>
) :
    Named, Manipulator {
    override val consumed: Map<Resource, Double> = mapOf(
        Resource.MASS to mass,
        Resource.POWER to powerConsumed
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.WEAPON_SOCKET to weapons.size.toDouble(),
    )
}