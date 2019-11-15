package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartApiDefaultPrice(

        var count: Int = 0,

        var items: List<SmartApiItem>? = null,

        var last: SmartApiLast? = null,

        var isError: Boolean = false,

        var requestId: String? = null,

        var resource: String? = null,

        var status: Int = 0,

        var version: String? = null,

        var execution: Double = 0.toDouble()

) : Serializable
