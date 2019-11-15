package cc.smartcash.smarthub.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.models.Coin
import cc.smartcash.smarthub.utils.ApiUtil
import cc.smartcash.smarthub.utils.NetworkUtil
import cc.smartcash.smarthub.utils.SmartCashApplication
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
                val response: Response<JsonNode>?
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