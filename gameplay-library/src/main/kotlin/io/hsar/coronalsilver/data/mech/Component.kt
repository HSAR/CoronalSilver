package io.hsar.coronalsilver.data.mech

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.hsar.coronalsilver.Consumer
import io.hsar.coronalsilver.Provider

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
interface Component : Consumer, Provider {
    val name: String
    val manufacturer: String?
    val desc: String?

    fun getFullItemName(): String =
        if (manufacturer != null) {
            "${manufacturer?.filter { it.isUpperCase() }} $name"
        } else {
            name
        }
}