package cc.smartcash.wallet.Models.business

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class BusinessListRootResponse(

        @Expose
        @JsonProperty("items")
        @get:JsonProperty("items")
        @set:JsonProperty("items")
        @SerializedName("items") var items: List<PhysicalStoreRootItemResponse?>?,

        @Expose
        @JsonProperty("isError")
        @get:JsonProperty("isError")
        @set:JsonProperty("isError")
        @SerializedName("isError") var isError: Boolean?,

        @Expose
        @JsonProperty("requestId")
        @get:JsonProperty("requestId")
        @set:JsonProperty("requestId")
        @SerializedName("requestId") var requestId: String?,

        @Expose
        @JsonProperty("resource")
        @get:JsonProperty("resource")
        @set:JsonProperty("resource")
        @SerializedName("resource") var resource: String?,

        @Expose
        @JsonProperty("status")
        @get:JsonProperty("status")
        @set:JsonProperty("status")
        @SerializedName("status") var status: Int?,

        @Expose
        @JsonProperty("version")
        @get:JsonProperty("version")
        @set:JsonProperty("version")
        @SerializedName("version") var version: String?,

        @Expose
        @JsonProperty("execution")
        @get:JsonProperty("execution")
        @set:JsonProperty("execution")
        @SerializedName("execution") var execution: Double?

) : Serializable