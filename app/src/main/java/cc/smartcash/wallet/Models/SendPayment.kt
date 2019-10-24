package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SendPayment : Serializable {
    @SerializedName("FromAddress")
    var fromAddress: String? = null
    @SerializedName("ToAddress")
    var toAddress: String? = null
    @SerializedName("Amount")
    var amount: Double? = null
    @SerializedName("UserKey")
    var userKey: String? = null
    @SerializedName("code")
    var code: String? = null
    @SerializedName("Email")
    var email: String? = null
}