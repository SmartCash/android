package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import cc.smartcash.smarthub.activities.LoginActivity
import cc.smartcash.smarthub.models.Coin
import cc.smartcash.smarthub.utils.KEYS
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.Util
import cc.smartcash.smarthub.viewModels.CurrentPriceViewModel
import java.util.*

class PriceTask(context: Context, pre: () -> Unit, pos: (coins: ArrayList<Coin>?) -> Unit) : AsyncTask<Void, Int, ArrayList<Coin>>() {

    private var appContext: Context = context
    private var smartCashApplication: SmartCashApplication

    val preLoad = pre
    val posLoad = pos

    init {
        this.smartCashApplication = SmartCashApplication(appContext)

    }

    override fun onPreExecute() {
        super.onPreExecute()
        preLoad()
    }

    override fun doInBackground(vararg params: Void?): ArrayList<Coin>? {

        try {
            val datePriceWasUpdated = Util.getDate(this.smartCashApplication.getString(KEYS.KEY_TIME_PRICE_WAS_UPDATED))
            Log.d(LoginActivity.TAG, "Date price was updated: $datePriceWasUpdated")

            if (datePriceWasUpdated == null || Util.dateDiffFromNow(datePriceWasUpdated) > 60) {
                val syncPrices = CurrentPriceViewModel.getSyncPrices(appContext)
                        ?: return this.smartCashApplication.getCurrentPrice()

                this.smartCashApplication.saveString(Util.date, KEYS.KEY_TIME_PRICE_WAS_UPDATED)
                this.smartCashApplication.saveCurrentPrice(syncPrices)
            }
            return this.smartCashApplication.getCurrentPrice()
        } catch (e: Exception) {
            return null
        }
    }

    override fun onPostExecute(coins: ArrayList<Coin>?) {
        super.onPostExecute(coins)
        posLoad(coins)
    }
}