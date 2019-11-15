package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartApiLast(
        var id: String? = null,
        var created: String? = null
) : Serializable
