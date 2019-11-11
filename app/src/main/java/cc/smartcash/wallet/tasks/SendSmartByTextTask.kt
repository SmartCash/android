package cc.smartcash.wallet.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.wallet.Models.SendPayment
import cc.smartcash.wallet.Models.SmartTextRequest
import cc.smartcash.wallet.Models.SmartTextRoot
import cc.smartcash.wallet.ViewModels.WalletViewModel
import cc.smartcash.wallet.utils.SmartCashApplication
import cc.smartcash.wallet.utils.Util

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
        val sendPayment = smartTextRoot?.let { Util.fillSendSendSmartByWebWalletRequestBySmartTextResponse(it) }
        sendPayment?.apply {
            this.email = sendPaymentRequest.email
            this.userKey = sendPaymentRequest.userKey
            this.code = sendPaymentRequest.code
        }
        //TODO:Send using web wallet
        posLoad(smartTextRoot)
    }
}

