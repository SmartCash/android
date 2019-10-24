package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiBlock : Serializable {

    @JsonProperty("NumberOfTransactions")
    var numberOfTransactions: String? = null

    @JsonProperty("Hash")
    var hash: String? = null

    @JsonProperty("BlockReward")
    var blockReward: String? = null

    @JsonProperty("Timestamp")
    var timestamp: String? = null

    @JsonProperty("MerkleRoot")
    var merkleRoot: String? = null

    @JsonProperty("Height")
    var height: String? = null

    @JsonProperty("Difficulty")
    var difficulty: String? = null

    @JsonProperty("Bits")
    var bits: String? = null

    @JsonProperty("Version")
    var version: String? = null

    @JsonProperty("Nonce")
    var nonce: String? = null

    @JsonProperty("PreviousBlockNumber")
    var previousBlockNumber: String? = null

    @JsonProperty("PreviousBlockHash")
    var previousBlockHash: String? = null

    @JsonProperty("Json")
    var json: String? = null

    @JsonProperty("Confirmations")
    var confirmations: String? = null

}
