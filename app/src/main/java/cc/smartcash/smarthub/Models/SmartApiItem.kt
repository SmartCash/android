package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartApiItem(

        var updated: String? = null,

        var currencies: SmartApiCurrency? = null,

        var ticker: String? = null,

        var `object`: String? = null,

        var created: String? = null,

        var exchange: String? = null,

        var id: String? = null,

        var name: String? = null

) : Serializable
