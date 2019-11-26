package cc.smartcash.smarthub.viewHolders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import cc.smartcash.smarthub.R

class WalletDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView = itemView.findViewById(R.id.wallet_dialog_item_wallet_name)
    var btnSelect: Button = itemView.findViewById(R.id.wallet_dialog_item_btn_select)

}
