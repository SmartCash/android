package cc.smartcash.wallet.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import cc.smartcash.wallet.Activities.LoginActivity
import cc.smartcash.wallet.Models.Coin
import cc.smartcash.wallet.Utils.KEYS
import cc.smartcash.wallet.Utils.SmartCashApplication
import cc.smartcash.wallet.Utils.Util
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel
import java.util.*

class PriceTask(context: Context, pre: () -> Unit, pos: () -> Unit) : AsyncTask<Void, Int, ArrayList<Coin>>() {

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

        val datePriceWasUpdated = Util.getDate(this.smartCashApplication.getString(appContext, KEYS.KEY_TIME_PRICE_WAS_UPDATED))
        Log.d(LoginActivity.TAG, "Date price was updated: $datePriceWasUpdated")

        if (datePriceWasUpdated == null || Util.dateDiffFromNow(datePriceWasUpdated) > 60) {
            val syncPrices = CurrentPriceViewModel.getSyncPrices(appContext)
            this.smartCashApplication.saveString(this.appContext, Util.date, KEYS.KEY_TIME_PRICE_WAS_UPDATED)
            this.smartCashApplication.saveCurrentPrice(this.appContext, syncPrices!!)
        }
        return this.smartCashApplication.getCurrentPrice(this.appContext)
    }

    override fun onPostExecute(coins: ArrayList<Coin>?) {
        super.onPostExecute(coins)
        posLoad()
    }
}