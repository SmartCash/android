package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Transaction(

        @Expose
        @JsonProperty("hash")
        @get:JsonProperty("hash")
        @set:JsonProperty("hash")
        @SerializedName("hash")
        var hash: String? = null,

        @Expose
        @JsonProperty("timestamp")
        @get:JsonProperty("timestamp")
        @set:JsonProperty("timestamp")
        @SerializedName("timestamp")
        var timestamp: String? = null,

        @Expose
        @JsonProperty("amount")
        @get:JsonProperty("amount")
        @set:JsonProperty("amount")
        @SerializedName("amount")
        var amount: Double? = null,

        @Expose
        @JsonProperty("direction")
        @get:JsonProperty("direction")
        @set:JsonProperty("direction")
        @SerializedName("direction")
        var direction: String? = null,

        @Expose
        @JsonProperty("toAddress")
        @get:JsonProperty("toAddress")
        @set:JsonProperty("toAddress")
        @SerializedName("toAddress")
        var toAddress: String? = null,

        @Expose
        @JsonProperty("isPending")
        @get:JsonProperty("isPending")
        @set:JsonProperty("isPending")
        @SerializedName("isPending")
        var isPending: Boolean? = null,

        @Expose
        @JsonProperty("blockindex")
        @get:JsonProperty("blockindex")
        @set:JsonProperty("blockindex")
        @SerializedName("blockindex")
        var blockIndex: Int? = null,

        @Expose
        @JsonProperty("isNew")
        @get:JsonProperty("isNew")
        @set:JsonProperty("isNew")
        @SerializedName("isNew")
        var isNew: Boolean? = null,

        @Expose
        @JsonProperty("isConfirmed")
        @get:JsonProperty("isConfirmed")
        @set:JsonProperty("isConfirmed")
        @SerializedName("isConfirmed")
        var isConfirmed: Boolean? = null,

        @Expose
        @JsonProperty("orderData")
        @get:JsonProperty("orderData")
        @set:JsonProperty("orderData")
        @SerializedName("orderData")
        var orderData: String? = null

) : Serializable 
