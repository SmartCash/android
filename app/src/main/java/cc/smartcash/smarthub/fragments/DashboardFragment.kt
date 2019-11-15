package cc.smartcash.smarthub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import cc.smartcash.smarthub.Adapters.DashboardWalletListAdapter
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.Wallet
import cc.smartcash.smarthub.utils.SmartCashApplication
import java.util.*


class DashboardFragment : Fragment() {

    private var walletList: ArrayList<Wallet>? = null
    private var smartCashApplication: SmartCashApplication? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.smartCashApplication = SmartCashApplication(context!!)
        walletList = smartCashApplication!!.getUser()!!.wallet
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewWallets()
    }

    private fun setupRecyclerViewWallets() {
        val walletRecycler = activity!!.findViewById<RecyclerView>(R.id.wallet_recyclerview)
        val linearLayoutManagerTransactions = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val walletAdapter = DashboardWalletListAdapter(context!!, ArrayList())

        walletRecycler.layoutManager = linearLayoutManagerTransactions
        walletRecycler.adapter = walletAdapter

        walletAdapter.setItems(walletList!!)
    }

    companion object {

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

}
