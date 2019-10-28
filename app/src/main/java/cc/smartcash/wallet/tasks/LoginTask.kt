package cc.smartcash.wallet.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.wallet.Models.Coin
import cc.smartcash.wallet.Models.User
import cc.smartcash.wallet.Models.UserLogin
import cc.smartcash.wallet.Models.WebWalletRootResponse
import cc.smartcash.wallet.R
import cc.smartcash.wallet.Utils.SmartCashApplication
import cc.smartcash.wallet.Utils.Util
import cc.smartcash.wallet.ViewModels.LoginViewModel

class LoginTask(context: Context, pre: () -> Unit, pos: (user: WebWalletRootResponse<User?>) -> Unit) : AsyncTask<UserLogin, Int, WebWalletRootResponse<User?>>() {

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

    override fun doInBackground(vararg users: UserLogin): WebWalletRootResponse<User?> {

        var token: String? = ""

        var webWalletGetSyncTokenRootResponse: WebWalletRootResponse<String?> = WebWalletRootResponse()

        var webWalletGetSyncUserRootResponse: WebWalletRootResponse<User?> = WebWalletRootResponse()

        if (users.isEmpty()) {
            token = smartCashApplication.getToken(this.appContext)
        } else {
            webWalletGetSyncTokenRootResponse = LoginViewModel.getSyncToken(users[0], this.appContext)
        }
        if (webWalletGetSyncTokenRootResponse.valid!!) {
            token = webWalletGetSyncTokenRootResponse.data
        }
        if (!Util.isNullOrEmpty(token)) {

            val user = LoginViewModel.getSyncUser(token!!, this.appContext)

            if (user != null && user.valid!!) {
                saveUser(token, user.data, appContext, smartCashApplication)
                webWalletGetSyncUserRootResponse.valid = user.valid
                webWalletGetSyncUserRootResponse.error = user.error
                webWalletGetSyncUserRootResponse.errorDescription = user.errorDescription
                webWalletGetSyncUserRootResponse.data = user.data
                return webWalletGetSyncUserRootResponse
            } else {
                webWalletGetSyncUserRootResponse.valid = user.valid
                webWalletGetSyncUserRootResponse.error = user.error
                webWalletGetSyncUserRootResponse.errorDescription = user.errorDescription
                smartCashApplication.deleteSharedPreferences(this.appContext)
                return webWalletGetSyncUserRootResponse
            }

        } else {
            webWalletGetSyncUserRootResponse.valid = webWalletGetSyncTokenRootResponse.valid
            webWalletGetSyncUserRootResponse.error = webWalletGetSyncTokenRootResponse.error
            webWalletGetSyncUserRootResponse.errorDescription = webWalletGetSyncTokenRootResponse.errorDescription
            smartCashApplication.deleteSharedPreferences(this.appContext)
        }
        return webWalletGetSyncUserRootResponse
    }

    companion object {
        fun saveUser(token: String, user: User?, appContext: Context, smartCashApplication: SmartCashApplication) {
            if (user != null) {
                smartCashApplication.saveToken(appContext, token)
                smartCashApplication.saveUser(appContext, user)
                smartCashApplication.saveWallet(appContext, user.wallet!![0])
                smartCashApplication.saveActualSelectedCoin(appContext, Coin(appContext.getString(R.string.default_crypto), 0.0))
            } else {
                smartCashApplication.deleteSharedPreferences(appContext)
            }
        }
    }

    override fun onPostExecute(user: WebWalletRootResponse<User?>) {
        super.onPostExecute(user)
        posLoad(user)
    }
}
