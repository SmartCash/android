package cc.smartcash.smarthub.tasks

import android.content.Context
import android.os.AsyncTask
import cc.smartcash.smarthub.Models.FullTransaction
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.Wallet
import cc.smartcash.smarthub.Utils.SmartCashApplication
import java.util.ArrayList

class TransactionTask (context: Context, pre: () -> Unit, pos: (ArrayList<FullTransaction>?) -> Unit) : AsyncTask<SendPayment, Int, ArrayList<FullTransaction>>()  {
    private var appContext: Context = context
    private var smartCashApplication: SmartCashApplication

    val preLoad = pre
    val posLoad = pos
    private var walletList: ArrayList<Wallet>? = null

    init {
        this.smartCashApplication = SmartCashApplication(appContext)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        preLoad()
    }

    override fun doInBackground(vararg users: SendPayment): ArrayList<FullTransaction>? {
        walletList = smartCashApplication!!.getUser()!!.wallet
        return walletList!![0].transactions
    }

    override fun onPostExecute(transactionsResponse: ArrayList<FullTransaction>?) {
        super.onPostExecute(transactionsResponse)
        posLoad(transactionsResponse)
    }
}