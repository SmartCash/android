package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiTransactionInput : Serializable {

    @JsonProperty("TxidIn")
    var txidIn: String? = null

    @JsonProperty("IndexIn")
    var indexIn: Int = 0

    @JsonProperty("TxidOut")
    var txIdOut: String? = null

    @JsonProperty("IndexOut")
    var indexOut: Int = 0

    @JsonProperty("Address")
    var address: String? = null

    @JsonProperty("Value")
    var value: Int = 0

    @JsonProperty("Json")
    var json: String? = null

}