package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SmartApiItem : Serializable {

    var updated: String? = null

    var currencies: SmartApiCurrency? = null

    var ticker: String? = null

    var `object`: String? = null

    var created: String? = null

    var exchange: String? = null

    var id: String? = null

    var name: String? = null
}
