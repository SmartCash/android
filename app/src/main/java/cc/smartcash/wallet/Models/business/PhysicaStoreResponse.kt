package cc.smartcash.wallet.Models.business

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class PhysicalStoreResponse(

        @Expose
        @JsonProperty("website")
        @get:JsonProperty("website")
        @set:JsonProperty("website")
        @SerializedName("website") var website: String?,

        @Expose
        @JsonProperty("updated")
        @get:JsonProperty("updated")
        @set:JsonProperty("updated")
        @SerializedName("updated") var updated: String?,

        @Expose
        @JsonProperty("object")
        @get:JsonProperty("object")
        @set:JsonProperty("object")
        @SerializedName("object") var objectType: String?,

        @Expose
        @JsonProperty("category")
        @get:JsonProperty("category")
        @set:JsonProperty("category")
        @SerializedName("category") var category: PhysicalStoreCategoryResponse?,

        @Expose
        @JsonProperty("created")
        @get:JsonProperty("created")
        @set:JsonProperty("created")
        @SerializedName("created") var created: String?,

        @Expose
        @JsonProperty("description")
        @get:JsonProperty("description")
        @set:JsonProperty("description")
        @SerializedName("description") var description: String?,

        @Expose
        @JsonProperty("id")
        @get:JsonProperty("id")
        @set:JsonProperty("id")
        @SerializedName("id") var id: String?,

        @Expose
        @JsonProperty("name")
        @get:JsonProperty("name")
        @set:JsonProperty("name")
        @SerializedName("name") var name: String?

) : Serializable