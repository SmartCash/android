package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartApiItem(

        @Expose
        @JsonProperty("updated")
        @get:JsonProperty("updated")
        @set:JsonProperty("updated")
        @SerializedName("updated")
        var updated: String? = null,


        @Expose
        @JsonProperty("currencies")
        @get:JsonProperty("currencies")
        @set:JsonProperty("currencies")
        @SerializedName("currencies")
        var currencies: SmartApiCurrency? = null,

        @Expose
        @JsonProperty("ticker")
        @get:JsonProperty("ticker")
        @set:JsonProperty("ticker")
        @SerializedName("ticker")
        var ticker: String? = null,

        @Expose
        @JsonProperty("object")
        @get:JsonProperty("object")
        @set:JsonProperty("object")
        @SerializedName("object")
        var data: String? = null,

        @Expose
        @JsonProperty("created")
        @get:JsonProperty("created")
        @set:JsonProperty("created")
        @SerializedName("created")
        var created: String? = null,

        @Expose
        @JsonProperty("exchange")
        @get:JsonProperty("exchange")
        @set:JsonProperty("exchange")
        @SerializedName("exchange")
        var exchange: String? = null,

        @Expose
        @JsonProperty("id")
        @get:JsonProperty("id")
        @set:JsonProperty("id")
        @SerializedName("id")
        var id: String? = null,

        @Expose
        @JsonProperty("name")
        @get:JsonProperty("name")
        @set:JsonProperty("name")
        @SerializedName("name")
        var name: String? = null

) : Serializable
