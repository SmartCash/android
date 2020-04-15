package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cc.smartcash.smarthub.Activities.TransactionActivity
import cc.smartcash.smarthub.Models.*
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.Services.SAPIConfig
import cc.smartcash.smarthub.Services.SAPIService
import cc.smartcash.smarthub.Services.TransactionService
import cc.smartcash.smarthub.Utils.ApiUtil
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.Utils.Util
import cc.smartcash.smarthub.ViewModels.LoginViewModel
import cc.smartcash.smarthub.ViewModels.TransactionViewModel
import cc.smartcash.smarthub.ViewModels.WalletViewModel
import kotlin.math.log

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

            val user = LoginViewModel.getSyncUser(token!!, this.appContext, users[0])

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
        private var transactionsAddress: FullTransactionList? = FullTransactionList()

        fun saveUser(token: String, user: User?, appContext: Context, smartCashApplication: SmartCashApplication) {
            if (user != null) {
                //Get Balance from a new API
                user.wallet!!.forEach {
                   val call = WalletViewModel().getBalance(it.address!!)
                    it.balance = call?.balance
                    it.totalReceived = call?.received
                    it.totalSent = call?.sent!!.toDouble()

                    //Get Transactions from a new API
                    transactionsAddress = TransactionViewModel().getTransactions(it.address!!, appContext)

                    if(transactionsAddress != null){
                        if(transactionsAddress!!.txs.count() > 10)
                            it.transactions = ArrayList(transactionsAddress!!.txs.take(10))
                        else
                            it.transactions = transactionsAddress!!.txs
                    }
                }

                smartCashApplication.saveToken(appContext, token)
                smartCashApplication.saveUser(appContext, user)
                smartCashApplication.saveWallet(appContext, user.wallet!![0])
                smartCashApplication.saveActualSelectedCoin(appContext, Coin(appContext.getString(R.string.default_crypto), 1.0))
                smartCashApplication.saveWithoutPIN(appContext, false)
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
