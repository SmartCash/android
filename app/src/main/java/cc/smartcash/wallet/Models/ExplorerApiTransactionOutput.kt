package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiTransactionOutput : Serializable {

    @JsonProperty("Txid")
    var txid: String? = null

    @JsonProperty("Index")
    var index: Int = 0

    @JsonProperty("Address")
    var address: String? = null

    @JsonProperty("Value")
    var value: Double = 0.toDouble()

    @JsonProperty("Json")
    var json: String? = null

}
