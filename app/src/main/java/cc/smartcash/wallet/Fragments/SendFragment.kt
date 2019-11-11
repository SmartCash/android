package cc.smartcash.wallet.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import cc.smartcash.wallet.Adapters.WalletSpinnerAdapter
import cc.smartcash.wallet.Models.Wallet
import cc.smartcash.wallet.R
import cc.smartcash.wallet.utils.SmartCashApplication
import java.util.*

class SendFragment : Fragment() {

    private var walletList: ArrayList<Wallet>? = null
    private var walletAdapter: WalletSpinnerAdapter? = null
    private var smartCashApplication: SmartCashApplication? = null

    @BindView(R.id.frame_layout)
    lateinit var frameLayout: FrameLayout

    @BindView(R.id.scroll_view)
    lateinit var scrollView: ScrollView

    @BindView(R.id.fragment_send_wallet_spinner)
    lateinit var walletSpinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)

        walletList = smartCashApplication!!.getUser()!!.wallet
        smartCashApplication!!.saveWallet(walletList!![0])
        val view = inflater.inflate(R.layout.fragment_send, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)
        val addressFragment = SendAddressFragment.newInstance()
        openFragment(addressFragment)

        val displayMetrics = this.resources.displayMetrics
        walletSpinner.dropDownWidth = displayMetrics.widthPixels

        val walletSpinner = getView()!!.findViewById<Spinner>(R.id.fragment_send_wallet_spinner)
        walletAdapter = WalletSpinnerAdapter(this.context!!, this.walletList!!)
        walletSpinner.adapter = walletAdapter

        walletSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                smartCashApplication!!.saveWallet(walletList!![position])
                Log.e(TAG, walletList!![position].address)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val savedWallet = smartCashApplication!!.getWallet()

        if (savedWallet != null) {
            for (i in walletList!!.indices) {
                if (savedWallet.walletId == walletList!![i].walletId) {
                    walletSpinner.setSelection(i)
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {

        val TAG: String? = SendFragment::class.java.simpleName

        fun newInstance(): SendFragment {
            return SendFragment()
        }
    }
}
