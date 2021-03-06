package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SapiUnconfirmed(
        var delta: Int = 0,

        var transactions: List<String>? = null
) : Serializable