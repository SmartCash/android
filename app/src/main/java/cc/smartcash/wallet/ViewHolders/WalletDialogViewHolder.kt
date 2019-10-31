package cc.smartcash.wallet.ViewHolders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import cc.smartcash.wallet.R

class WalletDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView = itemView.findViewById(R.id.wallet_dialog_item_wallet_name)
    var btnSelect: Button = itemView.findViewById(R.id.wallet_dialog_item_btn_select)
    var title: TextView? = itemView.findViewById(R.id.show_wallets_dialog_title)

}
