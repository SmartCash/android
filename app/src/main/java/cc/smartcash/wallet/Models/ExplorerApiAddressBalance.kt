package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiAddressBalance : Serializable {

    @SerializedName("address")
    var address: String? = null

    @SerializedName("balance")
    var balance: Double = 0.toDouble()

    @SerializedName("sent")
    var sent: Double = 0.toDouble()

    @SerializedName("received")
    var received: Double = 0.toDouble()

}
