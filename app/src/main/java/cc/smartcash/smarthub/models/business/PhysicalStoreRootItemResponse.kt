package cc.smartcash.smarthub.models.business

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class PhysicalStoreRootItemResponse(

        @Expose
        @JsonProperty("count")
        @get:JsonProperty("count")
        @set:JsonProperty("count")
        @SerializedName("count")
        var count: Int?,

        @Expose
        @JsonProperty("items")
        @get:JsonProperty("items")
        @set:JsonProperty("items")
        @SerializedName("items")
        var stores: List<PhysicalStoreResponse?>?,

        @Expose
        @JsonProperty("exists")
        @get:JsonProperty("exists")
        @set:JsonProperty("exists")
        @SerializedName("exists")
        var exists: Boolean?

) : Serializable