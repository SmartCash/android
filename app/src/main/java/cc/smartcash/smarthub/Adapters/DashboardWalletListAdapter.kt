package cc.smartcash.smarthub.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.smartcash.smarthub.Models.Wallet
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.ViewHolders.DashboardWalletListViewHolder
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

    override fun onBindViewHolder(walletViewHolder: DashboardWalletListViewHolder, i: Int) {

        val app = SmartCashApplication(context)
        val actualSelectedCoin = app.getActualSelectedCoin(context)

        var fiatValue = ""

        fiatValue = if (actualSelectedCoin == null || actualSelectedCoin.name == context.getString(R.string.default_crypto)) {
            val currentPrice = app.getCurrentPrice()
            app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.wallets!![i].balance!!, currentPrice!![0].value!!))
        } else {
            app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.wallets!![i].balance!!, actualSelectedCoin.value!!))
        }

        walletViewHolder.name.text = this.wallets!![i].displayName
        walletViewHolder.value.text = app.formatNumberByDefaultCrypto(this.wallets!![i].balance!!) + " (" + fiatValue + ")"
        walletViewHolder.address.text = this.wallets!![i].address
        walletViewHolder.btnCopy.setOnClickListener { v -> SmartCashApplication.copyToClipboard(context, this.wallets!![i].address!!) }

        when (count) {
            0 -> {
                @SuppressLint("ResourceType") val color = context.resources.getString(R.color.btnGreen)
                walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(color))
                Log.e("cont", count.toString())
                count++
            }
            1 -> {
                @SuppressLint("ResourceType") val color = context.resources.getString(R.color.btnYellon)
                walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(color))
                Log.e("cont", count.toString())
                count++
            }
            2 -> {
                @SuppressLint("ResourceType") val color = context.resources.getString(R.color.btnRed)
                walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(color))
                Log.e("cont", count.toString())
                count = 0
            }
        }
    }

    override fun getItemCount(): Int {
        return wallets!!.size
    }

}
