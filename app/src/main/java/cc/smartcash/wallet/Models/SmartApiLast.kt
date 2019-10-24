package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SmartApiLast : Serializable {
    var id: String? = null

    var created: String? = null
}
