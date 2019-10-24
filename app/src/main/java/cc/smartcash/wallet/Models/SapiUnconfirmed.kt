package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SapiUnconfirmed : Serializable {

    var delta: Int = 0

    var transactions: List<String>? = null

}