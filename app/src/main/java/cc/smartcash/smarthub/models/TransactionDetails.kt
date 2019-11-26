package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionDetails(

        @Expose
        @JsonProperty("fromAddress")
        @get:JsonProperty("fromAddress")
        @set:JsonProperty("fromAddress")
        @SerializedName("fromAddress")
        var fromAddress: String? = null,

        @Expose
        @JsonProperty("toAddress")
        @get:JsonProperty("toAddress")
        @set:JsonProperty("toAddress")
        @SerializedName("toAddress")
        var toAddress: String? = null,

        @Expose
        @JsonProperty("Amount")
        @get:JsonProperty("Amount")
        @set:JsonProperty("Amount")
        @SerializedName("Amount")
        var amount: Double? = null

) : Serializable