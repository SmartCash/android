package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.models.User
import cc.smartcash.smarthub.models.UserLogin
import cc.smartcash.smarthub.models.WebWalletRootResponse
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.Util
import cc.smartcash.smarthub.viewModels.LoginViewModel

class UserTask(context: Context, pre: () -> Unit, pos: (user: WebWalletRootResponse<User?>) -> Unit) : AsyncTask<UserLogin, Int, WebWalletRootResponse<User?>>() {

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

        val webWalletGetSyncUserRootResponse: WebWalletRootResponse<User?> = WebWalletRootResponse()

        val token: String? = smartCashApplication.getToken()

        if (!Util.isNullOrEmpty(token)) {

            val user = LoginViewModel.getSyncUser(token!!, this.appContext)

            return if (user.valid!!) {
                saveUser(user.data, appContext, smartCashApplication)
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
            webWalletGetSyncUserRootResponse.valid = false
            webWalletGetSyncUserRootResponse.error = "No token"
            webWalletGetSyncUserRootResponse.errorDescription = "You need a fresh token"
            smartCashApplication.deleteSharedPreferences()
        }
        return webWalletGetSyncUserRootResponse
    }

    companion object {
        fun saveUser(user: User?, appContext: Context, smartCashApplication: SmartCashApplication) {
            if (user != null) {
                smartCashApplication.saveUser(user)
                smartCashApplication.saveWallet(user.wallet!![0])
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
