package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartTextRoot(

        @JsonProperty("status")
        @get:JsonProperty("status")
        @set:JsonProperty("status")
        var status: String? = null,

        @JsonProperty("data")
        @get:JsonProperty("data")
        @set:JsonProperty("data")
        var data: SmartTextData? = null

) : Serializable