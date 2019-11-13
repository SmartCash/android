package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.WebWalletRootResponse
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.WalletViewModel


class SendSmartByWebWalletTask(context: Context, pre: () -> Unit, pos: (result: WebWalletRootResponse<String>?) -> Unit) : AsyncTask<SendPayment, Int, WebWalletRootResponse<String>>() {

    private var appContext: Context = context
    private var smartCashApplication: SmartCashApplication

    private val preLoad = pre
    val posLoad = pos

    init {
        this.smartCashApplication = SmartCashApplication(appContext)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        preLoad
    }

    override fun doInBackground(vararg sendPayments: SendPayment): WebWalletRootResponse<String>? {
        return WalletViewModel.sendSyncTransaction(appContext, smartCashApplication.getToken()!!, sendPayments[0])
    }

    override fun onPostExecute(result: WebWalletRootResponse<String>?) {
        super.onPostExecute(result)
        posLoad(result)
    }
}