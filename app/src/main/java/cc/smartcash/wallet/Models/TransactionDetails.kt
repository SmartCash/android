package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class TransactionDetails : Serializable {

    @SerializedName("fromAddress")
    var fromAddress: String? = null

    @SerializedName("toAddress")
    var toAddress: String? = null

    @SerializedName("Amount")
    var amount: Double? = null

}
