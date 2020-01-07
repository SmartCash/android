package cc.smartcash.smarthub.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class FullTransaction(

        var vin: ArrayList<Vin> = ArrayList(),
        var vout: ArrayList<Vout> = ArrayList(),
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

@JsonIgnoreProperties(ignoreUnknown = true)
data class FullTransactionList(
        var pagesTotal: Long = 0,
        var txs: ArrayList<FullTransaction> = ArrayList()

) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Vin (

        val txid : String,
        val vout : Int,
        val sequence : Int,
        val n : Int,
        //val scriptSig : ScriptSig,
        val addr : String,
        val valueSat : Int,
        val value : Double,
        val doubleSpentTxID : String
)  : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Vout(
    val value : Double,
    val n : Int,
    //val scriptPubKey : ScriptPubKey,
    val spentTxId : String,
    val spentIndex : String,
    val spentHeight : String
)  : Serializable