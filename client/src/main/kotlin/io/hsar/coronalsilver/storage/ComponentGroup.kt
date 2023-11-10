package io.hsar.coronalsilver.storage

import com.fasterxml.jackson.module.kotlin.convertValue
import io.hsar.coronalsilver.cli.OBJECT_MAPPER
import io.hsar.coronalsilver.data.mech.Component

data class ComponentGroup(
    val comment: String? = null,
    val masterComponent: Map<String, Any>,
    val components: List<Map<String, Any>>
) {
    fun render(): List<StoredComponent<Component>> = components
        .map { masterComponent + it }
        .map { OBJECT_MAPPER.convertValue(it) }
}