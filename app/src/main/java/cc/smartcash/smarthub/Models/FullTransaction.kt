package cc.smartcash.smarthub.Models

import android.util.Log
import cc.smartcash.smarthub.Utils.KEYS
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import java.text.SimpleDateFormat


class FullTransaction constructor() {

    var vin: ArrayList<Any> = ArrayList()
    var vout: ArrayList<Any> = ArrayList()

    var txid: String? = null
    var version: Float = 0.toFloat()
    var locktime: Float = 0.toFloat()
    var blockhash: String? = null
    var blockheight: Float = 0.toFloat()
    var confirmations: Float = 0.toFloat()

    var time: Long = 0
    var blocktime: Long = 0
    var valueOut: Float = 0.toFloat()
    var size: Float = 0.toFloat()
    var valueIn: Float = 0.toFloat()
    var fees: Float = 0.toFloat()

    companion object {
        val gson = Gson()

        fun getDirection(transaction: FullTransaction, address: String) : String {
            transaction.vin.forEach {
                val jsonString = gson.toJson(it)
                val jObjResponse = JSONObject(jsonString.toString())

                if(jObjResponse.get("addr").toString() == address)
                    return "Sent"
            }

            return "Received"
        }

        fun getAmount(transaction: FullTransaction, address: String) : Double {
            var isIn = false
            var totalIn: Double = 0.toDouble()
            var totalOut: Double = 0.toDouble()

            transaction.vin.forEach {
                val jsonString = gson.toJson(it)
                val jObjResponse = JSONObject(jsonString.toString())

                if(jObjResponse.get("addr").toString() == address){
                    totalIn = jObjResponse.get("value").toString().toDouble()
                    isIn = true
                }
            }

            var vout = transaction.vout.first()
            val jsonString = gson.toJson(vout)
            val jObjResponse = JSONObject(jsonString.toString())

            totalOut = jObjResponse.get("value").toString().toDouble()

            if(isIn)
                return totalIn
            else
                return totalOut
        }

        fun getDate(epoch: Long): String {
            val date = Date(epoch * 1000L)
            val format = SimpleDateFormat(KEYS.KEY_DATE_FORMAT)
            format.timeZone = TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE)
            return format.format(date)
        }
    }
}

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
        val scriptSig : scriptSig,
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

@JsonIgnoreProperties(ignoreUnknown = true)
data class scriptSig(
        val hex : String,
        val asm : String
)  : Serializable

