package io.hsar.coronalsilver.data

import io.hsar.coronalsilver.CompositeConsumer
import io.hsar.coronalsilver.CompositeProvider
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Named
import io.hsar.coronalsilver.Provider
import io.hsar.coronalsilver.Resource

data class Intelligence(
    override val name: String, val sensors: List<Sensor>, val processor: Processor
) : Named, CompositeConsumer, CompositeProvider {
    override val consumers: List<Consumer> = sensors + processor
    override val providers: List<Provider> = sensors + processor
}

data class Sensor(
    override val name: String, val mass: Double, val powerConsumed: Double, val sensorQuality: Double
) : Named, Consumer, Provider {
    override val consumed = mapOf(
        Resource.POWER to powerConsumed,
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.SENSOR to sensorQuality,
    )
}

data class Processor(
    override val name: String, val mass: Double, val powerConsumed: Double, val actionPointsMajor: Int, val actionPointsMinor: Int
) : Named, Consumer, Provider {
    override val consumed = mapOf(
        Resource.POWER to powerConsumed,
        Resource.MASS to mass,
    )

    override val provided: Map<Resource, Double> = mapOf(
        Resource.ACTION_MAJOR to actionPointsMajor.toDouble(),
        Resource.ACTION_MINOR to actionPointsMinor.toDouble(),
    )
}
