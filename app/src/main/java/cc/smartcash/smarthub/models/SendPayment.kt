package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SendPayment(

        @Expose
        @JsonProperty("FromAddress")
        @get:JsonProperty("FromAddress")
        @set:JsonProperty("FromAddress")
        @SerializedName("FromAddress")
        var fromAddress: String? = null,


        @Expose
        @JsonProperty("ToAddress")
        @get:JsonProperty("ToAddress")
        @set:JsonProperty("ToAddress")
        @SerializedName("ToAddress")
        var toAddress: String? = null,


        @Expose
        @JsonProperty("Amount")
        @get:JsonProperty("Amount")
        @set:JsonProperty("Amount")
        @SerializedName("Amount")
        var amount: Double? = null,


        @Expose
        @JsonProperty("UserKey")
        @get:JsonProperty("UserKey")
        @set:JsonProperty("UserKey")
        @SerializedName("UserKey")
        var userKey: String? = null,


        @Expose
        @JsonProperty("code")
        @get:JsonProperty("code")
        @set:JsonProperty("code")
        @SerializedName("code")
        var code: String? = null,


        @Expose
        @JsonProperty("email")
        @get:JsonProperty("email")
        @set:JsonProperty("email")
        @SerializedName("email")
        var email: String? = null

) : Serializable