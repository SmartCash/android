package cc.smartcash.smarthub.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.Wallet
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.viewHolders.DashboardWalletListViewHolder
import java.util.*

class DashboardWalletListAdapter(private val context: Context, private var wallets: ArrayList<Wallet>?) : RecyclerView.Adapter<DashboardWalletListViewHolder>() {

    private var count: Int = 0

    fun setItems(wallets: ArrayList<Wallet>) {
        this.wallets = wallets
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DashboardWalletListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_wallet_item, viewGroup, false)
        return DashboardWalletListViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(walletViewHolder: DashboardWalletListViewHolder, i: Int) {

        val app = SmartCashApplication(context)
        val actualSelectedCoin = app.getActualSelectedCoin(context)

        val fiatValue = if (actualSelectedCoin.name == context.getString(R.string.default_crypto)) {
            val currentPrice = app.getCurrentPrice()
            app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.wallets!![i].balance!!, currentPrice!![0].value!!))
        } else {
            app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.wallets!![i].balance!!, actualSelectedCoin.value!!))
        }

        walletViewHolder.name.text = this.wallets!![i].displayName
        walletViewHolder.value.text = String.format("%s (%s)", app.formatNumberByDefaultCrypto(this.wallets!![i].balance!!), fiatValue)
        walletViewHolder.address.text = this.wallets!![i].address
        walletViewHolder.btnCopy.setOnClickListener { v -> SmartCashApplication.copyToClipboard(context, this.wallets!![i].address!!) }

        when (count) {
            0 -> {
                setBackgroundColor(walletViewHolder, context.resources.getString(R.color.btnGreen))
                count++
            }
            1 -> {
                setBackgroundColor(walletViewHolder, context.resources.getString(R.color.btnYellow))
                count++
            }
            2 -> {
                setBackgroundColor(walletViewHolder, context.resources.getString(R.color.btnRed))
                count = 0
            }
        }
    }

    private fun setBackgroundColor(walletViewHolder: DashboardWalletListViewHolder, colorName: String) {
        walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(colorName))
    }


    override fun getItemCount(): Int {
        return wallets!!.size
    }

}
