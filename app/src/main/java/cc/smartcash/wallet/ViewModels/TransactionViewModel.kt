package cc.smartcash.wallet.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cc.smartcash.wallet.Models.FullTransaction
import cc.smartcash.wallet.Models.TransactionDetails
import cc.smartcash.wallet.Models.TransactionResponse
import cc.smartcash.wallet.Utils.ApiUtil
import cc.smartcash.wallet.Utils.URLS
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class TransactionViewModel : ViewModel() {

    private var transaction: MutableLiveData<FullTransaction>? = null
    private var transactionDetails: MutableLiveData<TransactionResponse>? = null

    fun getTransaction(hash: String, context: Context): LiveData<FullTransaction> {
        transaction = MutableLiveData()
        loadTransaction(hash, context)

        return transaction as MutableLiveData<FullTransaction>
    }

    fun getTransactionDetails(token: String, details: TransactionDetails, context: Context): LiveData<TransactionResponse> {
        transactionDetails = MutableLiveData()
        loadDetails(token, details, context)

        return this.transactionDetails!!
    }

    private fun loadDetails(token: String, details: TransactionDetails, context: Context) {
        val call = ApiUtil.transactionDetailsService.getDetails("Bearer $token", details)

        call.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    transactionDetails!!.setValue(apiResponse)
                } else {
                    try {
                        transactionDetails!!.value = null
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Log.e(TAG, "Erro ao buscar os detalhes da transaction:" + t.message)
                transactionDetails!!.value = null
            }
        })
    }

    private fun loadTransaction(hash: String, context: Context) {
        val call = ApiUtil.transactionService.getTransaction(URLS.URL_INSIGHT_EXPLORER_API + hash)

        call.enqueue(object : Callback<FullTransaction> {
            override fun onResponse(call: Call<FullTransaction>, response: Response<FullTransaction>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    transaction!!.setValue(apiResponse)
                } else {
                    try {
                        transaction!!.value = null
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<FullTransaction>, t: Throwable) {
                Log.e(TAG, "Erro ao buscar a transaction:" + t.message)
                transaction!!.value = null
            }
        })
    }

    companion object {

        val TAG: String? = TransactionViewModel::class.java.simpleName

        fun getSyncTransaction(hash: String): FullTransaction? {
            val call = ApiUtil.transactionService.getTransaction(URLS.URL_INSIGHT_EXPLORER_API + hash)
            try {
                val fullTransactionResponse = call.execute()
                if (fullTransactionResponse.isSuccessful) {
                    return fullTransactionResponse.body()
                } else {
                    try {

                        val jObjError = JSONObject(fullTransactionResponse.errorBody()!!.string())
                        Log.e(TAG, jObjError.getString("message"))
                    } catch (e: Exception) {
                        Log.e(TAG, e.message)
                    }

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }
    }
}
