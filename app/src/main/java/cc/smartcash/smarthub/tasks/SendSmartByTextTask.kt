package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.SmartTextRequest
import cc.smartcash.smarthub.Models.SmartTextRoot
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.Utils.Util
import cc.smartcash.smarthub.ViewModels.WalletViewModel

class SendSmartByTextTask(context: Context, sendPayment: SendPayment, pre: () -> Unit, pos: (smartTextRoot: SmartTextRoot?) -> Unit) : AsyncTask<SmartTextRequest, Int, SmartTextRoot>() {

    private var appContext: Context = context
    private var smartCashApplication: SmartCashApplication
    private var sendPaymentRequest = sendPayment

    val preLoad = pre
    val posLoad = pos

    init {
        this.smartCashApplication = SmartCashApplication(appContext)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        preLoad()
    }

    override fun doInBackground(vararg users: SmartTextRequest): SmartTextRoot? {
        return WalletViewModel.sendSyncSmartText(this.appContext, smartCashApplication.getToken()!!, users[0])
    }

    override fun onPostExecute(smartTextRoot: SmartTextRoot?) {
        super.onPostExecute(smartTextRoot)
        val sendPayment = smartTextRoot?.let { Util.fillSendSendSmartByWebWalletRequestBySmartTextReponse(it) }
        sendPayment?.apply {
            //TODO: Send to Email test error
            //this.email = sendPaymentRequest.email
            //this.userKey = sendPaymentRequest.userKey
            //this.code = sendPaymentRequest.code
        }
        posLoad(smartTextRoot)
    }
}

