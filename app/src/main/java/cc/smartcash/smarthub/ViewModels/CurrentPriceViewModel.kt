package cc.smartcash.smarthub.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.Models.Coin
import cc.smartcash.smarthub.Utils.ApiUtil
import cc.smartcash.smarthub.Utils.NetworkUtil
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.Utils.Util
import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class CurrentPriceViewModel : ViewModel() {

    companion object {
        fun getSyncPrices(context: Context): ArrayList<Coin>? {

            val isInternetOn = NetworkUtil.getInternetStatus(context)
            val listCoins: ArrayList<Coin>? = ArrayList()
            val smartCashApplication = SmartCashApplication(context)

            if (isInternetOn) {
                //Load avaliable currencies
                val callCurrency = ApiUtil.currentPricesService.currencyList()
                val responseCurrency = callCurrency.execute()
                var currencies = ""

                responseCurrency.body()!!.forEach {
                    currencies = it + "," + currencies
                }

                val call = ApiUtil.currentPricesService.currentPrices("SMARTCASH", Util.removeLastChar(currencies))
                var response: Response<Map<String, Map<String, Double>>>? = null
                try {

                    response = call.execute()

                    if (response!!.isSuccessful) {

                        val returnMap = response.body() as Map<String, Map<String, Double>>
                        val smartMap = returnMap["smartcash"]

                        smartMap!!.forEach{
                            val coin = Coin(it.key, it.value)
                            listCoins!!.add(coin)
                        }

                        listCoins!!.sortBy { it.name }

                        return ArrayList(listCoins)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null

        }
    }
}