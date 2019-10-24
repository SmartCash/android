package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiTransaction : Serializable {

    @JsonProperty("Txid")
    var txId: String? = null

    @JsonProperty("BlockHash")
    var blockHash: String? = null

    @JsonProperty("Version")
    var version: Int = 0

    @JsonProperty("Time")
    var time: String? = null

    @SerializedName("Json")
    var json: String? = null

    @SerializedName("Confirmations")
    var confirmations: Int = 0

}
