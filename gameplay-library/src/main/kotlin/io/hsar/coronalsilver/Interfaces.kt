package io.hsar.coronalsilver

interface Named {
    val name: String
}

interface Provider {
    val provided: Map<Resource, Double>
}

interface CompositeProvider : Provider {
    val providers: List<Provider>

    override val provided: Map<Resource, Double>
        get() = providers.fold(emptyMap()) { acc, curr -> acc.plusWithSummation(curr.provided) }
}

interface Consumer {
    val consumed: Map<Resource, Double>
}

interface CompositeConsumer : Consumer {
    val consumers: List<Consumer>

    override val consumed: Map<Resource, Double>
        get() = consumers.fold(emptyMap()) { acc, curr -> acc.plusWithSummation(curr.consumed) }
}

enum class Resource {
    POWER, MOVEMENT, ACTION_MAJOR, ACTION_MINOR, SIGNATURE, AMMO, MASS, SENSOR, WEAPON_SOCKET
}