package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionResponse(

        @SerializedName("data")
        var data: String? = null,

        @SerializedName("error")
        var error: String? = null,

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("isValid")
        var valid: Boolean? = null,

        @SerializedName("error_description")
        var errorDescription: String? = null

) : Serializable 