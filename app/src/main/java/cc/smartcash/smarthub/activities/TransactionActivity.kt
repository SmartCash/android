package cc.smartcash.smarthub.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.FullTransaction
import cc.smartcash.smarthub.models.TransactionDetails
import cc.smartcash.smarthub.utils.KEYS
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.URLS
import cc.smartcash.smarthub.viewModels.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : AppCompatActivity() {

    private var hash: String? = null
    private var token: String? = null
    private var smartCashApplication: SmartCashApplication? = null
    private var details: TransactionDetails? = null

    @BindView(R.id.activity_title)
    lateinit var activityTitle: TextView

    @BindView(R.id.summary_title)
    lateinit var summaryTitle: TextView

    @BindView(R.id.size_label)
    lateinit var sizeLabel: TextView

    @BindView(R.id.fee_rate_label)
    lateinit var feeRateLabel: TextView

    @BindView(R.id.received_time_label)
    lateinit var receivedTimeLabel: TextView

    @BindView(R.id.mined_time_label)
    lateinit var minedTimeLabel: TextView

    @BindView(R.id.inclued_in_block_label)
    lateinit var includedInBlockLabel: TextView

    @BindView(R.id.transaction_hash)
    lateinit var transactionHash: TextView

    @BindView(R.id.txt_size)
    lateinit var txtSize: TextView

    @BindView(R.id.txt_fee_rate)
    lateinit var txtFeeRate: TextView

    @BindView(R.id.txt_received_time)
    lateinit var txtReceivedTime: TextView

    @BindView(R.id.txt_mined_time)
    lateinit var txtMinedTime: TextView

    @BindView(R.id.txt_included_in_block)
    lateinit var txtIncludedInBlock: TextView

    @BindView(R.id.login_main_loader)
    lateinit var loader: ProgressBar

    @BindView(R.id.btn_details)
    lateinit var btnDetails: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        ButterKnife.bind(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(this.getColor(R.color.colorAccent)))
        }

        smartCashApplication = SmartCashApplication(applicationContext)

        token = smartCashApplication!!.getToken()

        details = TransactionDetails()

        hash = intent.getStringExtra(KEYS.KEY_TRANSACTION_HASH)
        details!!.amount = intent.getDoubleExtra(KEYS.KEY_TRANSACTION_AMOUNT, 0.0)
        details!!.fromAddress = smartCashApplication!!.getWallet()?.address
        details!!.toAddress = intent.getStringExtra(KEYS.KEY_TRANSACTION_TO_ADDRESS)

        if (hash != null)
            getTransaction(hash.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    @OnClick(R.id.btn_details)
    fun onViewClicked() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URLS.URL_INSIGHT_EXPLORER + this.hash!!))
        this.startActivity(browserIntent)
    }

    private fun setVisibility(isLoading: Boolean?) {
        if (isLoading != null && isLoading.not()) {
            activityTitle.visibility = View.VISIBLE
            transactionHash.visibility = View.VISIBLE
            summaryTitle.visibility = View.VISIBLE
            sizeLabel.visibility = View.VISIBLE
            txtSize.visibility = View.VISIBLE
            feeRateLabel.visibility = View.VISIBLE
            txtFeeRate.visibility = View.VISIBLE
            receivedTimeLabel.visibility = View.VISIBLE
            txtReceivedTime.visibility = View.VISIBLE
            minedTimeLabel.visibility = View.VISIBLE
            txtMinedTime.visibility = View.VISIBLE
            includedInBlockLabel.visibility = View.VISIBLE
            txtIncludedInBlock.visibility = View.VISIBLE
            btnDetails.visibility = View.VISIBLE
            loader.visibility = View.GONE
        }
    }

    private fun setData(transaction: FullTransaction) {

        transactionHash.text = transaction.txid
        txtSize.text = transaction.size.toString()
        txtFeeRate.text = transaction.fees.toString()
        txtReceivedTime.text = getDate(transaction.time)
        txtMinedTime.text = getDate(transaction.blocktime)
        txtIncludedInBlock.text = transaction.blockhash

    }

    private fun getDate(epoch: Long): String {
        val date = Date(epoch * 1000L)
        val format = SimpleDateFormat(KEYS.KEY_DATE_FORMAT)
        format.timeZone = TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE)
        return format.format(date)
    }

    private fun getTransaction(hash: String) {

        ViewModelProviders.of(this).get<TransactionViewModel>(TransactionViewModel::class.java).also {

            it.getTransaction(hash, this@TransactionActivity).observe(this, androidx.lifecycle.Observer {

                transaction ->
                if (transaction != null) {
                    getDetails(transaction)
                    Log.e(TAG, transaction.txid)
                } else {
                    Log.e(TAG, getString(R.string.transaction_error_message))
                }
            })
        }
    }

    private fun getDetails(transaction: FullTransaction?) {
        ViewModelProviders.of(this)[TransactionViewModel::class.java].also {
            it.getTransactionDetails(token!!, this.details!!, this@TransactionActivity).observe(this@TransactionActivity, androidx.lifecycle.Observer { transactionDetails ->
                if (transactionDetails != null) {
                    setVisibility(false)
                    setData(transaction!!)
                    Log.e(TAG, transaction.txid)
                } else {
                    setVisibility(false)
                    setData(transaction!!)
                    Log.e(TAG, getString(R.string.transaction_detail_error_message))
                }
            })
        }
    }

    companion object {
        val TAG: String = TransactionActivity::class.java.simpleName
    }
}
