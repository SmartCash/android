package cc.smartcash.smarthub.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.Models.Coin
import cc.smartcash.smarthub.Utils.ApiUtil
import cc.smartcash.smarthub.Utils.NetworkUtil
import cc.smartcash.smarthub.Utils.SmartCashApplication
import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Response
import java.io.IOException
import java.util.*

class CurrentPriceViewModel : ViewModel() {

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