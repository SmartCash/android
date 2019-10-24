package cc.smartcash.wallet.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cc.smartcash.wallet.Models.Coin
import cc.smartcash.wallet.Utils.ApiUtil
import cc.smartcash.wallet.Utils.NetworkUtil
import cc.smartcash.wallet.Utils.SmartCashApplication
import com.fasterxml.jackson.databind.JsonNode
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class CurrentPriceViewModel : ViewModel() {

    private var currentPrices: MutableLiveData<String>? = null

    fun getCurrentPrices(context: Context): LiveData<String> {
        if (currentPrices == null) {
            currentPrices = MutableLiveData()
            loadCurrentPrices(context)
        }

        return currentPrices as MutableLiveData<String>
    }

    private fun loadCurrentPrices(context: Context) {

        val isInternetOn = NetworkUtil.getInternetStatus(context)

        if (isInternetOn) {

            val call = ApiUtil.currentPricesService.currentPrices

            call.enqueue(object : Callback<JsonNode> {
                override fun onResponse(call: Call<JsonNode>, response: Response<JsonNode>) {
                    if (response.isSuccessful) {
                        currentPrices!!.setValue(Objects.requireNonNull<JsonNode>(response.body()).toString())
                    } else {
                        try {
                            currentPrices!!.value = null
                            val jObjError = JSONObject(Objects.requireNonNull<ResponseBody>(response.errorBody()).string())
                            Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonNode>, t: Throwable) {
                    currentPrices!!.value = null
                }
            })
        }
    }

    companion object {

        fun getSyncPrices(context: Context): ArrayList<Coin>? {

            val isInternetOn = NetworkUtil.getInternetStatus(context)

            if (isInternetOn) {

                val call = ApiUtil.currentPricesService.currentPrices
                var response: Response<JsonNode>? = null
                try {

                    response = call.execute()

                    if (response!!.isSuccessful) {

                        return SmartCashApplication.convertToArrayList(Objects.requireNonNull<JsonNode>(response.body()).toString())

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null

        }
    }
}