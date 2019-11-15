package cc.smartcash.smarthub.models.business

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class PhysicalStoreCategoryResponse(

        @Expose
        @JsonProperty("name")
        @get:JsonProperty("name")
        @set:JsonProperty("name")
        @SerializedName("name")
        var name: String?,

        @Expose
        @JsonProperty("code")
        @get:JsonProperty("code")
        @set:JsonProperty("code")
        @SerializedName("code")
        var code: Int?

) : Serializable