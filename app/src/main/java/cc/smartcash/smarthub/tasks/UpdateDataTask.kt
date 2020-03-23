package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import cc.smartcash.smarthub.Activities.MainActivity
import cc.smartcash.smarthub.Fragments.SendAddressFragment
import cc.smartcash.smarthub.Models.FullTransaction
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.User
import cc.smartcash.smarthub.Models.Wallet
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.LoginViewModel
import cc.smartcash.smarthub.ViewModels.UserViewModel
import java.util.*

class UpdateDataTask(context: Context, pre: () -> Unit, pos: () -> Unit) : AsyncTask<String?, Int, String?>() {
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

    override fun doInBackground(vararg users: String?): String? {
        val response: User? = UserViewModel.loadSyncUser(smartCashApplication!!.getToken()!!, appContext)
        smartCashApplication!!.saveUser(appContext, response!!)
        return " "
    }

    override fun onPostExecute(response: String?) {
        super.onPostExecute(response)
        posLoad()
    }
}