package cc.smartcash.wallet.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import cc.smartcash.wallet.Models.FullTransaction
import cc.smartcash.wallet.Models.Transaction
import cc.smartcash.wallet.R
import cc.smartcash.wallet.Utils.SmartCashApplication
import cc.smartcash.wallet.Utils.URLS
import cc.smartcash.wallet.ViewHolders.TransactionViewHolder
import cc.smartcash.wallet.ViewModels.TransactionViewModel
import java.util.*

class TransactionAdapter(private val context: Context, private var transactions: ArrayList<Transaction>?) : RecyclerView.Adapter<TransactionViewHolder>() {
    private var transactionViewHolder: TransactionViewHolder? = null

    fun setItems(transactions: ArrayList<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_item, viewGroup, false)
        transactionViewHolder = TransactionViewHolder(view)
        return transactionViewHolder!!
    }

    override fun onBindViewHolder(transactionViewHolder: TransactionViewHolder, i: Int) {

        val app = SmartCashApplication(context)
        val actualSelectedCoin = app.getActualSelectedCoin(context)

        var fiatValue = ""

        fiatValue = if (actualSelectedCoin == null || actualSelectedCoin.name == context.getString(R.string.default_crypto)) {
            val currentPrice = app.getCurrentPrice()
            app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.transactions!![i].amount!!, currentPrice!![0].value!!))
        } else {
            app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.transactions!![i].amount!!, actualSelectedCoin.value!!))
        }

        transactionViewHolder.amount.text = app.formatNumberByDefaultCrypto(this.transactions!![i].amount!!)
        transactionViewHolder.direction.text = this.transactions!![i].direction
        transactionViewHolder.timestamp.text = this.transactions!![i].timestamp
        transactionViewHolder.hash.text = this.transactions!![i].hash
        transactionViewHolder.price.text = " ($fiatValue)"
        transactionViewHolder.hash.setOnClickListener { v ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URLS.URL_INSIGHT_EXPLORER + this.transactions!![i].hash!!))
            context.startActivity(browserIntent)
        }

        transactionViewHolder.btnDetails.setOnClickListener { v ->
            setVisibility(transactionViewHolder)

            val transactionParameter = TransactionParameter(transactionViewHolder, transactions!![i].hash!!, i)
            TransactionTask().execute(transactionParameter)

        }

        setDirectionColors(transactionViewHolder, i)
    }

    private inner class TransactionParameter(val transactionViewHolder: TransactionViewHolder, val hash: String, val position: Int) {
        var transaction: FullTransaction? = null
    }

    private inner class TransactionTask : AsyncTask<TransactionParameter, Int, TransactionParameter>() {

        override fun doInBackground(vararg parameters: TransactionParameter): TransactionParameter? {
            (context as Activity).runOnUiThread { parameters[0].transactionViewHolder.confirmations.text = "loading..." }
            try {
                val syncTransaction = TransactionViewModel.getSyncTransaction(parameters[0].hash)
                parameters[0].transaction = syncTransaction
                return parameters[0]
            } catch (ex: Exception) {
                context.runOnUiThread { parameters[0].transactionViewHolder.confirmations.text = "0" }
            }

            return null
        }

        override fun onPostExecute(transactionResult: TransactionParameter) {
            super.onPostExecute(transactionResult)
            try {
                transactionResult.transactionViewHolder.confirmations.text = transactionResult.transaction!!.confirmations.toString()
            } catch (ex: Exception) {
                transactionResult.transactionViewHolder.confirmations.text = "0"
            }

        }
    }

    private fun setDirectionColors(transactionViewHolder: TransactionViewHolder, i: Int) {

        when (transactions!![i].direction.toString()) {
            "Sent" -> {
                transactionViewHolder.direction.setBackgroundResource(R.drawable.bg_paid)
                val wrappedDrawable = changeIconColor(R.drawable.ic_arrow_up, R.color.paidColor)
                transactionViewHolder.icon.background = wrappedDrawable
            }
            "Received" -> {
                transactionViewHolder.direction.setBackgroundResource(R.drawable.bg_receive)
                val wrappedDrawable = changeIconColor(R.drawable.ic_arrow_down, R.color.receiveColor)
                transactionViewHolder.icon.background = wrappedDrawable
            }
            "Awaiting" -> {
                val wrappedDrawable = changeIconColor(R.drawable.bg_awaiting, R.color.awaitingColor)
                transactionViewHolder.direction.background = wrappedDrawable

            }
        }
    }

    private fun setVisibility(transactionViewHolder: TransactionViewHolder) {
        if (transactionViewHolder.idView.visibility == View.VISIBLE) {
            transactionViewHolder.idView.visibility = View.GONE
            transactionViewHolder.btnDetails.setImageResource(R.drawable.menu_down)
        } else {
            transactionViewHolder.idView.visibility = View.VISIBLE
            transactionViewHolder.btnDetails.setImageResource(R.drawable.menu_up)
        }
    }

    private fun changeIconColor(icon: Int?, color: Int?): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, icon!!)!!)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(wrappedDrawable, context.getColor(color!!))
        }
        return wrappedDrawable
    }

    override fun getItemCount(): Int {
        return transactions!!.size
    }

    companion object {


        val TAG: String? = TransactionAdapter::class.java.simpleName
    }
}
