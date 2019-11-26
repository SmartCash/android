package cc.smartcash.smarthub.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.Wallet
import cc.smartcash.smarthub.viewHolders.WalletDialogViewHolder
import java.util.*

class WalletDialogAdapter(private val context: Context, private var wallets: ArrayList<Wallet>?, private val txtAddress: EditText, private val dialog: AlertDialog) : RecyclerView.Adapter<WalletDialogViewHolder>() {

    fun setItems(wallets: ArrayList<Wallet>) {
        this.wallets = wallets
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WalletDialogViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.wallet_dialog_item, viewGroup, false)
        return WalletDialogViewHolder(view)
    }

    override fun onBindViewHolder(walletDialogViewHolder: WalletDialogViewHolder, i: Int) {
        walletDialogViewHolder.name.text = this.wallets!![i].displayName.toString()
        walletDialogViewHolder.btnSelect.setOnClickListener { v ->
            this.txtAddress.setText(this.wallets!![i].address)
            dialog.hide()
        }
    }

    override fun getItemCount(): Int {
        return wallets!!.size
    }
}
