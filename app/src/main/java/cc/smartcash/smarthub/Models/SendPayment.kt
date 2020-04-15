package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SendPayment(

        @SerializedName("from")
        var from: String? = null,
        @SerializedName("to")
        var to: String? = null,
        @SerializedName("amount")
        var amount: Double? = null,
        @SerializedName("api_key")
        var apiKey: String? = null,
        @SerializedName("api_secret")
        var apiSecret: String? = null,
        @SerializedName("key")
        var privateKey: String? = null
) : Serializable