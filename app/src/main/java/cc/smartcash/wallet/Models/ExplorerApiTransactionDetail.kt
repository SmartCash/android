package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ExplorerApiTransactionDetail : Serializable {

    @JsonProperty("Transaction")
    var transaction: ExplorerApiTransaction? = null

    @JsonProperty("Inputs")
    var inputs: List<ExplorerApiTransactionInput>? = null

    @JsonProperty("Outputs")
    var outputs: List<ExplorerApiTransactionOutput>? = null


}
