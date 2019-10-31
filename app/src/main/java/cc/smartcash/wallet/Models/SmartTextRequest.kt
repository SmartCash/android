package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartTextRequest(

        @JsonProperty("userWebwallet")
        @get:JsonProperty("userWebwallet")
        @set:JsonProperty("userWebwallet")
        var userWebwallet: String? = null,

        @JsonProperty("destinationEmail")
        @get:JsonProperty("destinationEmail")
        @set:JsonProperty("destinationEmail")
        var destinationEmail: String? = null,

        @JsonProperty("addressRefunded")
        @get:JsonProperty("addressRefunded")
        @set:JsonProperty("addressRefunded")
        var addressRefunded: String? = null,

        @JsonProperty("amountSmart")
        @get:JsonProperty("amountSmart")
        @set:JsonProperty("amountSmart")
        var amountSmart: String? = null,

        @JsonProperty("typeSend")
        @get:JsonProperty("typeSend")
        @set:JsonProperty("typeSend")
        var typeSend: String? = null,

        @JsonProperty("phoneNumber")
        @get:JsonProperty("phoneNumber")
        @set:JsonProperty("phoneNumber")
        var phoneNumber: String? = null,

        @JsonProperty("messageToSend")
        @get:JsonProperty("messageToSend")
        @set:JsonProperty("messageToSend")
        var messageToSend: String? = null


) : Serializable