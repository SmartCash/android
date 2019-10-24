package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class FullTransaction : Serializable {

    internal var vin = ArrayList<Any>()
    internal var vout = ArrayList<Any>()
    // Getter Methods

    var txid: String? = null
    var version: Float = 0.toFloat()
    var locktime: Float = 0.toFloat()
    var blockhash: String? = null
    var blockheight: Float = 0.toFloat()
    var confirmations: Float = 0.toFloat()
    // Setter Methods

    var time: Long = 0
    var blocktime: Long = 0
    var valueOut: Float = 0.toFloat()
    var size: Float = 0.toFloat()
    var valueIn: Float = 0.toFloat()
    var fees: Float = 0.toFloat()
}