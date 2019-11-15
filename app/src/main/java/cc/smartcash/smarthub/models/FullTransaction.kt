package cc.smartcash.smarthub.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class FullTransaction(

        var vin: ArrayList<Any> = ArrayList(),
        var vout: ArrayList<Any> = ArrayList(),
        // Getter Methods

        var txid: String? = null,
        var version: Float = 0.toFloat(),
        var locktime: Float = 0.toFloat(),
        var blockhash: String? = null,
        var blockheight: Float = 0.toFloat(),
        var confirmations: Float = 0.toFloat(),
        // Setter Methods

        var time: Long = 0,
        var blocktime: Long = 0,
        var valueOut: Float = 0.toFloat(),
        var size: Float = 0.toFloat(),
        var valueIn: Float = 0.toFloat(),
        var fees: Float = 0.toFloat()

) : Serializable