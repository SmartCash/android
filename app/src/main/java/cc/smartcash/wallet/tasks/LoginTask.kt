package cc.smartcash.wallet.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.wallet.Models.Coin
import cc.smartcash.wallet.Models.User
import cc.smartcash.wallet.Models.UserLogin
import cc.smartcash.wallet.R
import cc.smartcash.wallet.Utils.SmartCashApplication
import cc.smartcash.wallet.ViewModels.LoginViewModel

abstract class LoginTask(context: Context, pre: () -> Unit, pos: () -> Unit) : AsyncTask<UserLogin, Int, User>(), DelegateLoginTaskTask {

    abstract override fun beforeExecution()

    abstract override fun afterExecution(result: User?)

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

    override fun doInBackground(vararg users: UserLogin): User? {

        var token: String? = ""
        if (users.isEmpty()) {
            token = smartCashApplication.getToken(this.appContext)
        } else {

            val userLogin = UserLogin()
            userLogin.password = users[0].password
            userLogin.username = users[0].username
            userLogin.twoFactorAuthentication = users[0].twoFactorAuthentication

            token = LoginViewModel.getSyncToken(userLogin, this.appContext)
        }
        if (token == null || token.isEmpty() || token.contains("error:")) {
            smartCashApplication.deleteSharedPreferences(this.appContext)
            val error = User()
            error.firstName = "ErrorOfLogin"
            error.lastName = token!!.replace("error:", "")
            return error
        }

        val user = LoginViewModel.getSyncUser(token, this.appContext)
        saveUser(token, user)
        return user
    }

    private fun saveUser(token: String, user: User?) {
        if (user != null) {
            smartCashApplication.saveToken(this.appContext, token)
            smartCashApplication.saveUser(this.appContext, user)
            smartCashApplication.saveWallet(this.appContext, user.wallet!![0])
            smartCashApplication.saveActualSelectedCoin(this.appContext, Coin(this.appContext.getString(R.string.default_crypto), 0.0))
        } else {
            smartCashApplication.deleteSharedPreferences(this.appContext)
        }
    }

    override fun onPostExecute(user: User?) {
        super.onPostExecute(user)
        posLoad()
        afterExecution(user)
    }
}
