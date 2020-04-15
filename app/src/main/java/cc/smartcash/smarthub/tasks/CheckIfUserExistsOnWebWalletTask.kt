package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.WebWalletRootResponse
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.WalletViewModel


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
        return WalletViewModel.isUserAvailable(users[0].to.toString())
    }

    override fun onPostExecute(booleanWebWalletRootResponse: WebWalletRootResponse<Boolean>?) {
        super.onPostExecute(booleanWebWalletRootResponse)
        posLoad(booleanWebWalletRootResponse)
    }
}