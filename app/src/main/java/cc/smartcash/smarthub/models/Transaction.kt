package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Transaction(
        var hash: String? = null,
        var timestamp: String? = null,
        var amount: Double? = null,
        var direction: String? = null,
        var toAddress: String? = null,
        var isPending: Boolean? = null,
        var blockindex: Int? = null,
        var isNew: Boolean? = null,
        var isConfirmed: Boolean? = null,
        var orderData: String? = null

) : Serializable 
