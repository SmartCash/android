package cc.smartcash.wallet.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import cc.smartcash.wallet.R

class DashboardWalletListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView = itemView.findViewById(R.id.wallet_spinner_item_txt_name)
    var value: TextView = itemView.findViewById(R.id.txt_value)
    var address: TextView
    var btnCopy: ImageView
    var roundIcon: CardView

    init {

        address = itemView.findViewById(R.id.txt_to_address)
        btnCopy = itemView.findViewById(R.id.img_copy)
        roundIcon = itemView.findViewById(R.id.wallet_spinner_item_round_icon)
    }
}