package cc.smartcash.smarthub.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import cc.smartcash.smarthub.R

class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var amount: TextView = itemView.findViewById(R.id.transaction_item_text_amount_crypto)
    var hash: TextView = itemView.findViewById(R.id.txt_hash)
    var direction: TextView = itemView.findViewById(R.id.transaction_item_text_direction)
    var timestamp: TextView = itemView.findViewById(R.id.transaction_item_text_timestamp)
    var btnDetails: ImageView = itemView.findViewById(R.id.transaction_item_btn_open_details)
    var icon: ImageView = itemView.findViewById(R.id.transaction_item_ic_transaction)
    var idView: View = itemView.findViewById(R.id.id_view)
    var price: TextView = itemView.findViewById(R.id.transaction_item_text_amount_fiat)
    var confirmations: TextView = itemView.findViewById(R.id.transaction_item_confirmation_text)


}

