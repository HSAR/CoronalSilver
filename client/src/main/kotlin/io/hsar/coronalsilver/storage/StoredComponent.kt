package io.hsar.coronalsilver.storage

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.data.mech.Component
import kotlin.reflect.KClass

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface StoredComponent<out T : Component> {
    val name: String
    val manufacturer: String?
    val desc: String?
    val subcomponents: Map<String, KClass<StoredComponent<Component>>>
        get() = emptyMap()

    fun bind(subcomponents: Map<String, Component?>): T
}