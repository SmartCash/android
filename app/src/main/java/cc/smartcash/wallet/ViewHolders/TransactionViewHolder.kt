package cc.smartcash.wallet.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import cc.smartcash.wallet.R

class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var amount: TextView = itemView.findViewById(R.id.transaction_item_text_amount_crypto)
    var hash: TextView
    var direction: TextView = itemView.findViewById(R.id.transaction_item_text_direction)
    var timestamp: TextView
    var btnDetails: ImageView
    private var divider: View
    private var hashTitle: TextView
    var icon: ImageView
    private var contentView: View
    var idView: View
    var price: TextView
    var confirmations: TextView

    init {

        timestamp = itemView.findViewById(R.id.transaction_item_text_timestamp)
        hash = itemView.findViewById(R.id.txt_hash)
        icon = itemView.findViewById(R.id.transaction_item_ic_transaction)
        btnDetails = itemView.findViewById(R.id.transaction_item_btn_open_details)
        divider = itemView.findViewById(R.id.divider)
        hashTitle = itemView.findViewById(R.id.transaction_id_title)
        contentView = itemView.findViewById(R.id.content_view)
        idView = itemView.findViewById(R.id.id_view)
        price = itemView.findViewById(R.id.transaction_item_text_amount_fiat)
        confirmations = itemView.findViewById(R.id.transaction_item_confirmation_text)
    }
}

