package cc.smartcash.wallet.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.wallet.Models.SendPayment
import cc.smartcash.wallet.Models.WebWalletRootResponse
import cc.smartcash.wallet.ViewModels.WalletViewModel
import cc.smartcash.wallet.utils.SmartCashApplication


class CheckIfUserExistsOnWebWalletTask(context: Context, pre: () -> Unit, pos: (WebWalletRootResponse<Boolean>?) -> Unit) : AsyncTask<SendPayment, Int, WebWalletRootResponse<Boolean>>() {

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

    override fun doInBackground(vararg users: SendPayment): WebWalletRootResponse<Boolean>? {
        return WalletViewModel.isUserAvailable(users[0].toAddress.toString())
    }

    override fun onPostExecute(booleanWebWalletRootResponse: WebWalletRootResponse<Boolean>?) {
        super.onPostExecute(booleanWebWalletRootResponse)
        posLoad(booleanWebWalletRootResponse)
    }
}