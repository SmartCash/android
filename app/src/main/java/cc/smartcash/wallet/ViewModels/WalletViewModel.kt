package cc.smartcash.wallet.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cc.smartcash.wallet.Models.*
import cc.smartcash.wallet.utils.ApiUtil
import cc.smartcash.wallet.utils.KEYS
import cc.smartcash.wallet.utils.Util
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class WalletViewModel : ViewModel() {

    private var returnResponse: MutableLiveData<WebWalletRootResponse<String>>? = null

    fun sendPayment(context: Context, token: String, sendPayment: SendPayment): LiveData<WebWalletRootResponse<String>> {
        returnResponse = MutableLiveData()
        loadSendPayment(context, token, sendPayment)
        return returnResponse as MutableLiveData<WebWalletRootResponse<String>>
    }


    private fun loadSendPayment(context: Context, token: String, sendPayment: SendPayment) {

        val feeRequest = WalletPaymentFeeRequest()
        feeRequest.amount = sendPayment.amount
        feeRequest.fromAddress = sendPayment.fromAddress
        feeRequest.toAddress = sendPayment.toAddress
        feeRequest.recurrenceType = 3
        feeRequest.password = sendPayment.userKey

        val callFee = ApiUtil.walletService.getFee("Bearer $token", feeRequest)

        callFee.enqueue(object : Callback<WebWalletRootResponse<Double>> {
            override fun onResponse(call: Call<WebWalletRootResponse<Double>>, response: Response<WebWalletRootResponse<Double>>) {

                if (response.body() != null && response.body()!!.data != null) {
                    val fee = response.body()!!.data
                    sendPayment.amount = sendPayment.amount!! + fee!!
                }

                val callSendPayment = ApiUtil.walletService.sentPayment("Bearer $token", sendPayment)

                callSendPayment.enqueue(object : Callback<WebWalletRootResponse<String>> {
                    override fun onResponse(call: Call<WebWalletRootResponse<String>>, response: Response<WebWalletRootResponse<String>>) {
                        if (response.isSuccessful) {
                            returnResponse!!.setValue(response.body())
                        } else {
                            try {
                                returnResponse!!.value = null

                                if (response.errorBody() != null) {

                                    val jObjError = JSONObject(response.errorBody()!!.string())
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show()

                                } else if (response.raw().code() == 401) {

                                    Toast.makeText(context, "The request was not authorized by the server.", Toast.LENGTH_LONG).show()

                                }

                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<WebWalletRootResponse<String>>, t: Throwable) {

                        t.printStackTrace()
                        t.message
                        Log.e(TAG, t.message, t)
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                        returnResponse!!.value = null
                    }
                })


            }

            override fun onFailure(call: Call<WebWalletRootResponse<Double>>, t: Throwable) {

            }
        })


    }

    companion object {

        val TAG: String? = WalletViewModel::class.java.simpleName

        fun getSyncFee(context: Context, token: String, sendPayment: SendPayment): WebWalletRootResponse<Double>? {

            val feeRequest = WalletPaymentFeeRequest()
            feeRequest.amount = sendPayment.amount ?: 0.0
            feeRequest.fromAddress = sendPayment.fromAddress
            feeRequest.toAddress = sendPayment.toAddress
            feeRequest.recurrenceType = 3
            feeRequest.password = sendPayment.userKey

            token.replace("\"", "")

            val callFee = ApiUtil.walletService.getFee("Bearer $token", feeRequest)
            return Util.getWebWalletResponse(callFee)
        }

        fun sendSyncTransaction(context: Context, token: String, sendPayment: SendPayment): WebWalletRootResponse<String>? {
            val callSendPayment = ApiUtil.walletService.sentPayment("Bearer $token", sendPayment)
            return Util.getWebWalletResponse(callSendPayment)
        }

        fun sendSyncSmartText(context: Context, token: String, sendPayment: SmartTextRequest): SmartTextRoot? {
            val callSendPayment = Util.getProperty(KEYS.CONFIG_TOKEN_SEND_BY_TEXT, context)?.let { ApiUtil.smartTextService.sentPayment(it, sendPayment) }

            try {
                val response = callSendPayment?.execute()
                if (response?.isSuccessful!!) {
                    return response.body()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        fun isUserAvailable(user: String): WebWalletRootResponse<Boolean>? {
            val callFee = ApiUtil.webWalletAPIService.isUserAvailable(WebWalletUserAvailableRequest(user))
            return Util.getWebWalletResponse(callFee)
        }
    }

}