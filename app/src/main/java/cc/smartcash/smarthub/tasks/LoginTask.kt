package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.Coin
import cc.smartcash.smarthub.models.User
import cc.smartcash.smarthub.models.UserLogin
import cc.smartcash.smarthub.models.WebWalletRootResponse
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.Util
import cc.smartcash.smarthub.viewModels.LoginViewModel

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

        val webWalletGetSyncUserRootResponse: WebWalletRootResponse<User?> = WebWalletRootResponse()

        if (users.isEmpty()) {
            token = smartCashApplication.getToken()
        } else {
            webWalletGetSyncTokenRootResponse = LoginViewModel.getSyncToken(users[0], this.appContext)
        }
        if (webWalletGetSyncTokenRootResponse.valid!!) {
            token = webWalletGetSyncTokenRootResponse.data
        }
        if (!Util.isNullOrEmpty(token)) {

            val user = LoginViewModel.getSyncUser(token!!, this.appContext)

            return if (user.valid!!) {
                saveUser(token, user.data, appContext, smartCashApplication)
                webWalletGetSyncUserRootResponse.valid = user.valid
                webWalletGetSyncUserRootResponse.error = user.error
                webWalletGetSyncUserRootResponse.errorDescription = user.errorDescription
                webWalletGetSyncUserRootResponse.data = user.data
                webWalletGetSyncUserRootResponse
            } else {
                webWalletGetSyncUserRootResponse.valid = user.valid
                webWalletGetSyncUserRootResponse.error = user.error
                webWalletGetSyncUserRootResponse.errorDescription = user.errorDescription
                smartCashApplication.deleteSharedPreferences()
                webWalletGetSyncUserRootResponse
            }

        } else {
            webWalletGetSyncUserRootResponse.valid = webWalletGetSyncTokenRootResponse.valid
            webWalletGetSyncUserRootResponse.error = webWalletGetSyncTokenRootResponse.error
            webWalletGetSyncUserRootResponse.errorDescription = webWalletGetSyncTokenRootResponse.errorDescription
            smartCashApplication.deleteSharedPreferences()
        }
        return webWalletGetSyncUserRootResponse
    }

    companion object {
        fun saveUser(token: String, user: User?, appContext: Context, smartCashApplication: SmartCashApplication) {
            if (user != null) {
                smartCashApplication.saveToken(token)
                smartCashApplication.saveUser(user)
                smartCashApplication.saveWallet(user.wallet!![0])
                smartCashApplication.saveActualSelectedCoin(Coin(appContext.getString(R.string.default_crypto), 0.0))
                smartCashApplication.saveWithoutPIN(false)
            } else {
                smartCashApplication.deleteSharedPreferences()
            }
        }
    }

    override fun onPostExecute(user: WebWalletRootResponse<User?>) {
        super.onPostExecute(user)
        posLoad(user)
    }
}
