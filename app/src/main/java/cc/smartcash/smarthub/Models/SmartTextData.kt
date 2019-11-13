package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartTextData(


        @JsonProperty("status")
        @get:JsonProperty("status")
        @set:JsonProperty("status")
        var status: String? = null,

        @JsonProperty("amoutSmart")
        @get:JsonProperty("amoutSmart")
        @set:JsonProperty("amoutSmart")
        var amoutSmart: Int? = null,

        @JsonProperty("createDate")
        @get:JsonProperty("createDate")
        @set:JsonProperty("createDate")
        var createDate: String? = null,

        @JsonProperty("withdrawalDate")
        @get:JsonProperty("withdrawalDate")
        @set:JsonProperty("withdrawalDate")
        var withdrawalDate: String? = null,

        @JsonProperty("depositDate")
        @get:JsonProperty("depositDate")
        @set:JsonProperty("depositDate")
        var depositDate: String? = null,

        @JsonProperty("destinationEmail")
        @get:JsonProperty("destinationEmail")
        @set:JsonProperty("destinationEmail")
        var destinationEmail: String? = null,

        @JsonProperty("phoneNumber")
        @get:JsonProperty("phoneNumber")
        @set:JsonProperty("phoneNumber")
        var phoneNumber: Any? = null,

        @JsonProperty("orderID")
        @get:JsonProperty("orderID")
        @set:JsonProperty("orderID")
        var orderID: String? = null,

        @JsonProperty("generatedAddress")
        @get:JsonProperty("generatedAddress")
        @set:JsonProperty("generatedAddress")
        var generatedAddress: String? = null,

        @JsonProperty("txIdDeposit")
        @get:JsonProperty("txIdDeposit")
        @set:JsonProperty("txIdDeposit")
        var txIdDeposit: Any? = null,

        @JsonProperty("txIdWithdrawal")
        @get:JsonProperty("txIdWithdrawal")
        @set:JsonProperty("txIdWithdrawal")
        var txIdWithdrawal: Any? = null,

        @JsonProperty("addressForWithdrawal")
        @get:JsonProperty("addressForWithdrawal")
        @set:JsonProperty("addressForWithdrawal")
        var addressForWithdrawal: String? = null,

        @JsonProperty("addressRefunded")
        @get:JsonProperty("addressRefunded")
        @set:JsonProperty("addressRefunded")
        var addressRefunded: String? = null,

        @JsonProperty("refundedDate")
        @get:JsonProperty("refundedDate")
        @set:JsonProperty("refundedDate")
        var refundedDate: String? = null,

        @JsonProperty("txIdRefunded")
        @get:JsonProperty("txIdRefunded")
        @set:JsonProperty("txIdRefunded")
        var txIdRefunded: Any? = null,

        @JsonProperty("refundedReason")
        @get:JsonProperty("refundedReason")
        @set:JsonProperty("refundedReason")
        var refundedReason: Any? = null,

        @JsonProperty("amountSmartSent")
        @get:JsonProperty("amountSmartSent")
        @set:JsonProperty("amountSmartSent")
        var amountSmartSent: Any? = null,

        @JsonProperty("amountSmartWithFee")
        @get:JsonProperty("amountSmartWithFee")
        @set:JsonProperty("amountSmartWithFee")
        var amountSmartWithFee: Double? = null,

        @JsonProperty("typeSend")
        @get:JsonProperty("typeSend")
        @set:JsonProperty("typeSend")
        var typeSend: String? = null,

        @JsonProperty("urlWithdrawal")
        @get:JsonProperty("urlWithdrawal")
        @set:JsonProperty("urlWithdrawal")
        var urlWithdrawal: String? = null


) : Serializable