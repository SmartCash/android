package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.WebWalletRootResponse
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.WalletViewModel

class CalculateFeeTask(context: Context, pre: () -> Unit, pos: (fee: WebWalletRootResponse<Double>?) -> Unit) : AsyncTask<SendPayment, Int, WebWalletRootResponse<Double>>() {

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

    override fun doInBackground(vararg users: SendPayment): WebWalletRootResponse<Double>? {
        return WalletViewModel.getSyncFee(appContext, smartCashApplication.getToken()!!, users[0])
    }

    override fun onPostExecute(fee: WebWalletRootResponse<Double>?) {
        super.onPostExecute(fee)
        posLoad(fee)
    }
}