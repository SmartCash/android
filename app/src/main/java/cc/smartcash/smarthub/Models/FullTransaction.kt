package cc.smartcash.smarthub.Models

import cc.smartcash.smarthub.Utils.KEYS
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*
import com.google.gson.Gson
import org.json.JSONObject
import java.text.SimpleDateFormat


@JsonIgnoreProperties(ignoreUnknown = true)
class FullTransaction constructor() {
    var vin: ArrayList<Any> = ArrayList()
    var vout: ArrayList<Any> = ArrayList()

    var txid: String? = null
    var version: Float = 0.toFloat()
    var locktime: Float = 0.toFloat()
    var blockhash: String? = null
    var blockheight: Float = 0.toFloat()
    var confirmations: Float = 0.toFloat()
    val isCoinBase : Boolean = false

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

                try{
                    if(jObjResponse.get("addr").toString() == address)
                        return "Sent"
                } catch(ex: Exception){
                    return "From Mined"
                }
            }

            return "Received"
        }

        private fun getSentAmount(transaction: FullTransaction, address: String): Double{
            var sent = 0.0

            transaction.vout.forEach {
                val jsonString = gson.toJson(it)
                val output = Gson().fromJson(jsonString, Vout::class.java)
                val valueOut = output.value

                output.scriptPubKey.addresses.forEach{
                    if(it != address){
                        sent += valueOut
                    }
                }
            }

            return sent
        }

        private fun getReceivedAmount(transaction: FullTransaction, address: String): Double {
            var received = 0.0

            transaction.vout.forEach{
                val jsonString = gson.toJson(it)
                val output = Gson().fromJson(jsonString, Vout::class.java)
                val valueOut = output.value

                output.scriptPubKey.addresses.forEach{
                    if(it == address){
                        received += valueOut
                    }
                }
            }

            return received
        }

        private fun getMinedAmount(transaction: FullTransaction): Double {
            val jsonString = gson.toJson( transaction.vout.get(0))
            val output = Gson().fromJson(jsonString, Vout::class.java)
            return output.value
        }


        fun getAmount(transaction: FullTransaction, address: String): Double {
            val direction = getDirection(transaction, address)

            if (direction == "Received") {
                return getReceivedAmount(transaction, address)
            } else if(direction == "From Mined"){
                return getMinedAmount(transaction)
            }
            return getSentAmount(transaction, address)
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
    val scriptPubKey : ScriptPubKey,
    val spentTxId : String,
    val spentIndex : String,
    val spentHeight : String
)  : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class scriptSig(
        val hex : String,
        val asm : String
)  : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class ScriptPubKey (
    val hex : String,
    val asm : String,
    val addresses : List<String>,
    val type : String
) : Serializable

