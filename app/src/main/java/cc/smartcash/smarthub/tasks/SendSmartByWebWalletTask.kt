package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.SendResponse
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.WalletViewModel


class SendSmartByWebWalletTask(context: Context, pre: () -> Unit, pos: (result: SendResponse?) -> Unit) : AsyncTask<SendPayment, Int, SendResponse>() {

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

    override fun doInBackground(vararg sendPayments: SendPayment): SendResponse? {
        return WalletViewModel.sendSyncTransaction(sendPayments[0])
    }

    override fun onPostExecute(result: SendResponse?) {
        super.onPostExecute(result)
        posLoad(result)
    }
}