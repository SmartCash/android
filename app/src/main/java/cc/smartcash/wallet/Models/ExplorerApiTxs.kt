package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiTxs : Serializable {

    @JsonProperty("Time")
    var time: String? = null

    @JsonProperty("Txid")
    var txId: String? = null


}




