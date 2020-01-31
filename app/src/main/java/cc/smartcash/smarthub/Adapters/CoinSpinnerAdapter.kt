package cc.smartcash.smarthub.Adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cc.smartcash.smarthub.Models.Coin
import java.util.*

class CoinSpinnerAdapter(context: Context, position: Int, private val coins: ArrayList<Coin>) : ArrayAdapter<Coin>(context, position, coins) {

    override fun getCount(): Int {
        return coins.size
    }

    override fun getItem(position: Int): Coin? {
        return coins[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        setCoinName(label, position)

        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        setCoinName(label, position)

        return label
    }

    private fun setCoinName(label: TextView, position: Int) {
        label.setTextColor(Color.BLACK)
        label.text = (this.coins[position].name?.toUpperCase()) ?: this.coins[position].name
    }
}