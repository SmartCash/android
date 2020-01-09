package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SapiAddressBalance(
        var address: String? = null,

        var received: Double = 0.toDouble(),

        var sent: Int = 0,

        var balance: Double = 0.toDouble()

        //var unconfirmed: SapiUnconfirmed? = null
) : Serializable