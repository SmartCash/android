package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartApiLast(

        @Expose
        @JsonProperty("id")
        @get:JsonProperty("id")
        @set:JsonProperty("id")
        @SerializedName("id")
        var id: String? = null,

        @Expose
        @JsonProperty("created")
        @get:JsonProperty("created")
        @set:JsonProperty("created")
        @SerializedName("created")
        var created: String? = null


) : Serializable
