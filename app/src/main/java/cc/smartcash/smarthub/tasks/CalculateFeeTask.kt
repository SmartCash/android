package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.FeeResponse
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.WalletViewModel

class CalculateFeeTask(context: Context, pre: () -> Unit, pos: (fee: FeeResponse?) -> Unit) : AsyncTask<SendPayment, Int, FeeResponse?>() {

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

    override fun doInBackground(vararg users: SendPayment): FeeResponse? {
        return WalletViewModel.getSyncFee(users[0])
    }

    override fun onPostExecute(fee: FeeResponse?) {
        super.onPostExecute(fee)
        posLoad(fee)
    }
}