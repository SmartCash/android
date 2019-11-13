package cc.smartcash.smarthub.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import cc.smartcash.smarthub.R

class DashboardWalletListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView = itemView.findViewById(R.id.wallet_spinner_item_txt_name)
    var value: TextView = itemView.findViewById(R.id.txt_value)
    var address: TextView = itemView.findViewById(R.id.txt_to_address)
    var btnCopy: ImageView = itemView.findViewById(R.id.img_copy)
    var roundIcon: CardView = itemView.findViewById(R.id.wallet_spinner_item_round_icon)

}