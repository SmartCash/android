package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class WebWalletUserAvailableRequest(
        @JsonProperty("data")
        @SerializedName("data")
        @Expose
        @get:JsonProperty("data")
        @set:JsonProperty("data")
        var data: String? = null
) : Serializable