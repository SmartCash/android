package cc.smartcash.smarthub.Models

import java.io.Serializable
import com.fasterxml.jackson.annotation.JsonProperty



data class Coin(var name: String?, var value: Double?) : Serializable

class CoinList {
    @JsonProperty("id")
    private val id: String? = null
    @JsonProperty("symbol")
    private val symbol: String? = null
    @JsonProperty("name")
    private val name: String? = null

}