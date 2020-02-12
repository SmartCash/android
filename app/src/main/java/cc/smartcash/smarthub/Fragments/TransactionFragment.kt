package cc.smartcash.smarthub.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.smarthub.Activities.MainActivity
import cc.smartcash.smarthub.Adapters.TransactionAdapter
import cc.smartcash.smarthub.Adapters.WalletSpinnerAdapter
import cc.smartcash.smarthub.Models.FullTransaction
import cc.smartcash.smarthub.Models.Wallet
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewModels.UserViewModel
import cc.smartcash.smarthub.tasks.TransactionTask
import cc.smartcash.smarthub.tasks.UpdateDataTask
import java.util.*

class TransactionFragment : Fragment() {

    private var smartCashApplication: SmartCashApplication? = null
    private var activeFilter: String? = null
    private var walletList: ArrayList<Wallet>? = null
    private var walletAdapter: WalletSpinnerAdapter? = null
    private var filteredTransactions = ArrayList<FullTransaction>()
    private var transactions: ArrayList<FullTransaction>? = null
    private var transactionAdapter: TransactionAdapter? = null

    @BindView(R.id.all_transactions_underline)
    lateinit var allTransactionsUnderline: View

    @BindView(R.id.received_underline)
    lateinit var receivedUnderline: View

    @BindView(R.id.awaiting_underline)
    lateinit var awaitingUnderline: View

    @BindView(R.id.paid_underline)
    lateinit var paidUnderline: View

    @BindView(R.id.fragment_receive_wallet_spinner)
    lateinit var walletSpinner: Spinner

    @BindView(R.id.login_main_loader)
    lateinit var loader: ProgressBar

    @BindView(R.id.frameLayout)
    lateinit var listTransactions: FrameLayout

    private var activeUnderline: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)
        walletList = smartCashApplication!!.getUser()!!.wallet

        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        ButterKnife.bind(this, view)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)

        val displayMetrics = this.resources.displayMetrics
        walletSpinner.dropDownWidth = displayMetrics.widthPixels

        activeUnderline = getView()!!.findViewById(R.id.all_transactions_underline)
        walletSpinner = getView()!!.findViewById<Spinner>(R.id.fragment_receive_wallet_spinner)

         walletAdapter = WalletSpinnerAdapter(context!!, walletList!!)
        walletSpinner.adapter = walletAdapter

        val savedWallet = smartCashApplication!!.getWallet()

        if (savedWallet != null) {
            for (i in walletList!!.indices) {
                if (savedWallet.walletId == walletList!![i].walletId) {
                    walletSpinner.setSelection(i)
                }
            }
        }

        TransactionTask(context!!, ::showLoader, ::afterLoadTransactionsTask).execute()
    }

    private fun afterLoadTransactionsTask(transactionsResponse: ArrayList<FullTransaction>?) {
        transactions = transactionsResponse

        walletSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val clickedItem = parent.getItemAtPosition(position) as Wallet
                transactions = clickedItem.transactions
                smartCashApplication!!.saveWallet(context!!, walletList!![position])
                setTransactions(activeFilter)
                setupRecyclerViewTransactions()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        setupRecyclerViewTransactions()
        hiddenLoader()
    }

    private fun showLoader() {
        listTransactions.visibility = View.GONE
        loader.visibility = View.VISIBLE
    }

    private fun hiddenLoader() {
        loader.visibility = View.GONE
        listTransactions.visibility = View.VISIBLE
    }



    @OnClick(R.id.btn_all_transactions, R.id.btn_received, R.id.btn_awaiting, R.id.btn_paid, R.id.btn_att)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_all_transactions -> {
                changeUnderline(allTransactionsUnderline)
                setTransactions(null)
            }
            R.id.btn_received -> {
                changeUnderline(receivedUnderline)
                setTransactions("Received")
            }
            R.id.btn_awaiting -> {
                changeUnderline(awaitingUnderline)
                setTransactions("Awaiting")
            }
            R.id.btn_paid -> {
                changeUnderline(paidUnderline)
                setTransactions("Sent")
            }
            R.id.btn_att -> updateData()
        }
    }

    private fun setupRecyclerViewTransactions() {
        try{
            val recyclerViewTransactions = activity!!.findViewById<RecyclerView>(R.id.transaction_recyclerview)
            val linearLayoutManagerTransactions = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            transactionAdapter = TransactionAdapter(context!!, ArrayList(), smartCashApplication!!.getWallet()!!.address!!)

            recyclerViewTransactions.layoutManager = linearLayoutManagerTransactions
            recyclerViewTransactions.adapter = transactionAdapter

            if(transactions!! != null)
                transactionAdapter!!.setItems(transactions!!)
        } catch (e: KotlinNullPointerException){
            android.widget.Toast.makeText(activity, "Error on Load Transactions, Try Login Again!", Toast.LENGTH_LONG).show()
        }
    }

    /*
    private fun updateData() {
        ViewModelProviders.of(this).get<UserViewModel>(UserViewModel::class.java).also {

            it.getUser(smartCashApplication!!.getToken()!!, activity!!).observe(this, androidx.lifecycle.Observer { response ->
                if (response != null) {
                    Toast.makeText(activity, getString(R.string.transaction_updated_message), Toast.LENGTH_LONG).show()

                    smartCashApplication!!.saveUser(activity!!, response)
                    (activity as MainActivity).setWalletValue()
                } else {
                    Log.e(TAG, getString(R.string.transaction_update_error_message))
                }
            })
        }
    }
     */

    private fun updateData() {
        UpdateDataTask(context!!, ::showLoader, ::afterUpdateTask).execute()
    }

    private fun afterUpdateTask() {
        Toast.makeText(activity, getString(R.string.send_message_success_return), Toast.LENGTH_LONG).show()
        hiddenLoader()
    }

    fun setTransactions(filtro: String?) {
        if (filtro == null)
            transactionAdapter!!.setItems(transactions!!)
        else {
            filteredTransactions = filterTransactions(filtro)
            transactionAdapter!!.setItems(filteredTransactions)
        }
        activeFilter = filtro
    }

    private fun filterTransactions(filtro: String): ArrayList<FullTransaction> {
        val filteredT = ArrayList<FullTransaction>()

        for (item in transactions!!) {
            if (FullTransaction.getDirection(item, smartCashApplication!!.getWallet()!!.address!!) == filtro)
                filteredT.add(item)
        }

        return filteredT
    }

    private fun changeUnderline(newActiveUnderline: View) {
        activeUnderline?.visibility = View.GONE
        newActiveUnderline.visibility = View.VISIBLE
        activeUnderline = newActiveUnderline
    }

    companion object {

        val TAG: String? = TransactionFragment::class.java.simpleName

        fun newInstance(): TransactionFragment {
            return TransactionFragment()
        }
    }
}
