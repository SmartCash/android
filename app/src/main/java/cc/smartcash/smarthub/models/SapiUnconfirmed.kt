package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SapiUnconfirmed(


        @Expose
        @JsonProperty("delta")
        @get:JsonProperty("delta")
        @set:JsonProperty("delta")
        @SerializedName("delta")
        var delta: Int = 0,

        @Expose
        @JsonProperty("transactions")
        @get:JsonProperty("transactions")
        @set:JsonProperty("transactions")
        @SerializedName("transactions")
        var transactions: List<String>? = null


) : Serializable