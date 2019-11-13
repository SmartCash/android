package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class WalletPaymentFeeRequest(

        @JsonProperty("email")
        @get:JsonProperty("email")
        @set:JsonProperty("email")
        var email: Any? = null,

        @JsonProperty("fromAddress")
        @get:JsonProperty("fromAddress")
        @set:JsonProperty("fromAddress")
        var fromAddress: String? = null,

        @JsonProperty("toAddress")
        @get:JsonProperty("toAddress")
        @set:JsonProperty("toAddress")
        var toAddress: String? = null,

        @JsonProperty("amount")
        @get:JsonProperty("amount")
        @set:JsonProperty("amount")
        var amount: Double? = null,

        @JsonProperty("password")
        @get:JsonProperty("password")
        @set:JsonProperty("password")
        var password: String? = null,

        @JsonProperty("code")
        @get:JsonProperty("code")
        @set:JsonProperty("code")
        var code: Any? = null,

        @JsonProperty("destinationEmail")
        @get:JsonProperty("destinationEmail")
        @set:JsonProperty("destinationEmail")
        var destinationEmail: String? = null,

        @JsonProperty("phoneNumber")
        @get:JsonProperty("phoneNumber")
        @set:JsonProperty("phoneNumber")
        var phoneNumber: String? = null,

        @JsonProperty("startDate")
        @get:JsonProperty("startDate")
        @set:JsonProperty("startDate")
        var startDate: String? = null,

        @JsonProperty("endDate")
        @get:JsonProperty("endDate")
        @set:JsonProperty("endDate")
        var endDate: String? = null,

        @JsonProperty("transactionDate")
        @get:JsonProperty("transactionDate")
        @set:JsonProperty("transactionDate")
        var transactionDate: String? = null,

        @JsonProperty("recurrenceType")
        @get:JsonProperty("recurrenceType")
        @set:JsonProperty("recurrenceType")
        var recurrenceType: Int? = null,

        @JsonProperty("recurringLabel")
        @get:JsonProperty("recurringLabel")
        @set:JsonProperty("recurringLabel")
        var recurringLabel: String? = null

) : Serializable 