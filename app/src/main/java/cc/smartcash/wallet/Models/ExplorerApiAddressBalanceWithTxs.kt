package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiAddressBalanceWithTxs : Serializable {

    @JsonProperty("AddressBalance")
    var addressbalance: ExplorerApiAddressBalance? = null

    @JsonProperty("Txs")
    var txs: List<ExplorerApiTxs>? = null

    constructor()

    constructor(addressbalance: ExplorerApiAddressBalance, txs: List<ExplorerApiTxs>) {
        this.addressbalance = addressbalance
        this.txs = txs
    }


}
