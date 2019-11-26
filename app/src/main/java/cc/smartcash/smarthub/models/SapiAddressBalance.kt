package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SapiAddressBalance(

        @Expose
        @JsonProperty("address")
        @get:JsonProperty("address")
        @set:JsonProperty("address")
        @SerializedName("address")
        var address: String? = null,

        @Expose
        @JsonProperty("received")
        @get:JsonProperty("received")
        @set:JsonProperty("received")
        @SerializedName("received")
        var received: Double = 0.toDouble(),


        @Expose
        @JsonProperty("sent")
        @get:JsonProperty("sent")
        @set:JsonProperty("sent")
        @SerializedName("sent")
        var sent: Int = 0,

        @Expose
        @JsonProperty("balance")
        @get:JsonProperty("balance")
        @set:JsonProperty("balance")
        @SerializedName("balance")
        var balance: Double = 0.toDouble(),

        @Expose
        @JsonProperty("unconfirmed")
        @get:JsonProperty("unconfirmed")
        @set:JsonProperty("unconfirmed")
        @SerializedName("unconfirmed")
        var unconfirmed: SapiUnconfirmed? = null
) : Serializable