package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class WebWalletException(


        @JsonProperty("error")
        @SerializedName("error")
        @Expose
        @get:JsonProperty("error")
        @get:SerializedName("error")
        @set:JsonProperty("error")
        @set:SerializedName("error")
        var error: String? = null,


        @JsonProperty("error_description")
        @SerializedName("error_description")
        @Expose
        @get:JsonProperty("error_description")
        @get:SerializedName("error_description")
        @set:JsonProperty("error_description")
        @set:SerializedName("error_description")
        var errorDescription: String? = null


) : Serializable