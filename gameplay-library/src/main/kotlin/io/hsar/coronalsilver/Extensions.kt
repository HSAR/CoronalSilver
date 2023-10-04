package io.hsar.coronalsilver

fun <T> Map<T, Double>.plusWithSummation(other: Map<T, Double>): Map<T, Double> {
    val intersect = this.keys.intersect(other.keys)
        .associateWith { key -> this[key]!! + other[key]!! }

    return this + other + intersect
}