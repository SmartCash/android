package cc.smartcash.smarthub.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.Wallet
import cc.smartcash.smarthub.utils.SmartCashApplication
import java.util.*

class WalletSpinnerAdapter(context: Context, private val wallets: ArrayList<Wallet>) : ArrayAdapter<Wallet>(context, 0, wallets) {

    private var count2: Int = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.wallet_spinner_item, parent, false
            )
        }

        val textViewName = convertView!!.findViewById<TextView>(R.id.wallet_spinner_item_txt_name)
        val textViewBalance = convertView.findViewById<TextView>(R.id.wallet_spinner_item_txt_balance)
        val textViewAddress = convertView.findViewById<TextView>(R.id.txt_to_address)
        val roundIcon = convertView.findViewById<CardView>(R.id.wallet_spinner_item_round_icon)

        when {
            wallets[position].position!! % 3 == 1 -> {
                @SuppressLint("ResourceType") val color = convertView.resources.getString(R.color.btnGreen)
                roundIcon.setCardBackgroundColor(Color.parseColor(color))
                count2++
            }
            wallets[position].position!! % 3 == 2 -> {
                @SuppressLint("ResourceType") val color = convertView.resources.getString(R.color.btnYellow)
                roundIcon.setCardBackgroundColor(Color.parseColor(color))
                count2++
            }
            wallets[position].position!! % 3 == 0 -> {
                @SuppressLint("ResourceType") val color = convertView.resources.getString(R.color.btnRed)
                roundIcon.setCardBackgroundColor(Color.parseColor(color))
                count2 = 0
            }
        }

        val currentItem = getItem(position)

        if (currentItem != null) {


            val app = SmartCashApplication(context)
            val actualSelectedCoin = app.getActualSelectedCoin(context)

            val fiatValue = if (actualSelectedCoin.name == context.getString(R.string.default_crypto)) {
                val currentPrice = app.getCurrentPrice()
                app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(currentItem.balance!!, currentPrice!![0].value!!))
            } else {
                app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(currentItem.balance!!, actualSelectedCoin.value!!))
            }

            textViewName.text = currentItem.displayName
            textViewBalance.text = app.formatNumberByDefaultCrypto(currentItem.balance!!) + " (" + fiatValue + ")"
            textViewAddress.text = currentItem.address
            textViewAddress.text = currentItem.address
        }


        return convertView
    }
}