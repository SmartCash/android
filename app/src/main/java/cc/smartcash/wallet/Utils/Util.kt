package cc.smartcash.wallet.Utils

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import cc.smartcash.wallet.Models.*
import cc.smartcash.wallet.R
import cc.smartcash.wallet.ViewModels.LoginViewModel
import com.google.gson.Gson
import retrofit2.Call
import java.io.IOException
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Util {

    val TAG: String? = Util::class.java.simpleName
    const val UTF_8 = "UTF-8"
    private const val prefixQueryStringQrCode = "smartcash:"
    private const val amountQueryStringQrCode = "?amount="

    const val ZERO = "0"

    val date: String
        get() {
            val date = Date()
            val format = SimpleDateFormat(KEYS.KEY_DATE_FORMAT)
            format.timeZone = TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE)
            return format.format(date)
        }

    private fun parseQrCodeWithValue(qrCodeString: String): String {


        val startPrefix = qrCodeString.indexOf(prefixQueryStringQrCode)
        val endPrefix = qrCodeString.indexOf(amountQueryStringQrCode)

        val startAmount = qrCodeString.indexOf(amountQueryStringQrCode)
        val endAmount = qrCodeString.length


        val address = qrCodeString.substring(startPrefix + prefixQueryStringQrCode.length, endPrefix)
        val amount = qrCodeString.substring(startAmount + amountQueryStringQrCode.length, endAmount)

        return "$address-$amount"

    }

    private fun parseQrCodeWithoutValue(qrCodeString: String): String {

        val startPrefix = qrCodeString.indexOf(prefixQueryStringQrCode)
        val endPrefix = qrCodeString.length

        val address = qrCodeString.substring(startPrefix + prefixQueryStringQrCode.length, endPrefix)
        val amount = "0"

        return "$address-$amount"

    }

    fun parseQrCode(qrCodeString: String): String {

        return if (qrCodeString.indexOf(prefixQueryStringQrCode) >= 0 && qrCodeString.indexOf(amountQueryStringQrCode) >= 0) {
            parseQrCodeWithValue(qrCodeString)
        } else if (qrCodeString.contains(prefixQueryStringQrCode) && !qrCodeString.contains(amountQueryStringQrCode)) {
            parseQrCodeWithoutValue(qrCodeString)
        } else {
            qrCodeString
        }
    }

    fun getProperty(key: String, context: Context): String? {
        try {
            val properties = Properties()
            val assetManager = context.assets
            val inputStream = assetManager.open("config.properties")
            properties.load(inputStream)
            return properties.getProperty(key)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message)
        }

        return null
    }

    fun getString(view: TextView): String {
        return view.text.toString()
    }

    fun getBigDecimal(view: TextView): BigDecimal {
        return BigDecimal.valueOf(getDouble(view))
    }

    fun getDouble(view: TextView): Double {
        return try {
            java.lang.Double.parseDouble(getString(view))
        } catch (e: java.lang.Exception) {
            0.0
        }
    }

    fun compareString(textView: TextView, textView2: TextView): Boolean {
        return getString(textView).equals(getString(textView2), ignoreCase = true)
    }

    @JvmStatic
    fun isNullOrEmpty(view: TextView): Boolean {
        return view.text == null || view.text.toString() == null || view.text.toString().isEmpty()
    }

    @JvmStatic
    fun isNullOrEmpty(text: String?): Boolean {
        return text == null || text.isEmpty()
    }

    fun isTaskComplete(progress: Int): Boolean {
        return progress * 2 == 100 //percentage of the progress and the process
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun amountInCoinConcatenation(context: Context, coinTick: String): String {
        return String.format(Locale.getDefault(), context.getString(R.string.send_amount_in_coin_label).replace("%s", coinTick))
    }

    fun amountInDefaultCryptoConcatenation(context: Context): String {
        return amountInCoinConcatenation(context, context.getString(R.string.default_crypto))
    }

    fun fillSmartTextRequest(to: String, from: String, amount: String): SmartTextRequest {

        var sendPayment = SmartTextRequest()
        sendPayment.addressRefunded = from
        sendPayment.amountSmart = amount
        when {
            Util.isValidEmail(to) -> {
                sendPayment.destinationEmail = to
                sendPayment.typeSend = KEYS.KEY_SMARTTEXT_EMAIL
            }
            PhoneNumberUtils.isGlobalPhoneNumber(to) -> {
                sendPayment.phoneNumber = to
                sendPayment.typeSend = KEYS.KEY_SMARTTEXT_SMS
            }
            else -> sendPayment.typeSend = KEYS.KEY_SMARTTEXT_LINK
        }
        return sendPayment
    }

    fun fillSmartTextRequest(sendPayment: SendPayment): SmartTextRequest {
        var smartTextRequest = SmartTextRequest()
        smartTextRequest.addressRefunded = sendPayment.fromAddress
        smartTextRequest.amountSmart = sendPayment.amount.toString()
        when {
            Util.isValidEmail(sendPayment.toAddress.toString()) -> {
                smartTextRequest.destinationEmail = sendPayment.toAddress.toString()
                smartTextRequest.typeSend = KEYS.KEY_SMARTTEXT_EMAIL
            }
            PhoneNumberUtils.isGlobalPhoneNumber(sendPayment.toAddress.toString()) -> {
                smartTextRequest.phoneNumber = sendPayment.toAddress.toString()
                smartTextRequest.typeSend = KEYS.KEY_SMARTTEXT_SMS
            }
            else -> smartTextRequest.typeSend = KEYS.KEY_SMARTTEXT_LINK
        }
        return smartTextRequest
    }

    fun fillSendSendSmartByWebWalletRequestBySmartTextReponse(smartTextRoot: SmartTextRoot): SendPayment? {

        if (smartTextRoot.data != null) {

            val data = smartTextRoot.data

            val sendPayment = SendPayment()
            sendPayment.amount = data!!.amountSmartWithFee
            sendPayment.fromAddress = data.addressRefunded
            sendPayment.toAddress = data.generatedAddress

            val order = StringBuilder()
            order.append("https://smartext.me/Order/" + data.orderID!!)

            return sendPayment
        }
        return null
    }

    fun fillSendSendSmartByWebWalletRequest(to: String, from: String, amount: String): SendPayment? {
        return null
    }

    fun getDateFromEpoch(epoch: Long): String {
        val date = Date(epoch * 1000L)
        val format = SimpleDateFormat(KEYS.KEY_DATE_FORMAT)
        format.timeZone = TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE)
        return format.format(date)
    }

    fun getDate(date: String?): Date? {

        if (isNullOrEmpty(date)) return null

        val format = SimpleDateFormat(KEYS.KEY_DATE_FORMAT)
        format.timeZone = TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE)

        try {
            return format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun dateDiff(d1: Date, d2: Date): Long {
        val diff = d2.time - d1.time//as given
        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

        return TimeUnit.MILLISECONDS.toMinutes(diff)
    }

    fun dateDiffFromNow(d1: Date?): Long {

        if (d1 == null) return java.lang.Long.MAX_VALUE

        val diff = Date().time - d1.time//as given
        return TimeUnit.MILLISECONDS.toMinutes(diff)
    }

    fun calculateFromFiatToSmart(
            context: Context,
            smartCashApplication: SmartCashApplication,
            amountLabel: TextView,
            txtAmountFiat: EditText,
            txtAmountCrypto: EditText,
            mainFee: BigDecimal?,
            sendButton: Button?
    ) {
        val amountConverted: BigDecimal
        var actualSelected = smartCashApplication.getActualSelectedCoin(context)
        val coins = smartCashApplication.AppPreferences.Coins

        amountLabel.text = amountInCoinConcatenation(context, actualSelected.name!!)

        for (i in coins!!.indices) {
            if (coins[i].name!!.equals(actualSelected.name!!, ignoreCase = true)) {
                actualSelected = coins[i]
                break
            }
        }

        if (isNullOrEmpty(txtAmountFiat)) {
            txtAmountCrypto.setText(ZERO)
        } else {
            if (actualSelected.name == context.getString(R.string.default_crypto)) {
                amountConverted = smartCashApplication.multiplyBigDecimals(Util.getBigDecimal(txtAmountFiat), BigDecimal.valueOf(actualSelected.value!!))
                amountLabel.text = Util.amountInDefaultCryptoConcatenation(context)
            } else {
                val currentPrice = actualSelected.value!!
                val amountInTheField = Util.getDouble(txtAmountFiat)
                val ruleOfThree = amountInTheField / currentPrice
                amountConverted = BigDecimal.valueOf(ruleOfThree)
            }
            txtAmountCrypto.setText(amountConverted.toString())
            val amountWithFee = Util.getBigDecimal(txtAmountCrypto).add(mainFee)
            if (sendButton != null)
                sendButton.text = context.getString(R.string.send_button_label).replace("%f", smartCashApplication.formatNumberByDefaultCrypto(java.lang.Double.parseDouble(amountWithFee.toString())))
        }
    }

    fun calculateFromSmartToFiat(
            context: Context,
            smartCashApplication: SmartCashApplication,
            amountLabel: TextView,
            txtAmountFiat: EditText,
            txtAmountCrypto: EditText,
            mainFee: BigDecimal?,
            sendButton: Button?
    ) {

        val amountConverted: BigDecimal
        var actualSelected = smartCashApplication.getActualSelectedCoin(context)
        val coins = smartCashApplication.AppPreferences.Coins

        amountLabel.text = Util.amountInCoinConcatenation(context, actualSelected.name!!)

        for (i in coins!!.indices) {
            if (coins[i].name!!.equals(actualSelected.name!!, ignoreCase = true)) {
                actualSelected = coins[i]
                break
            }
        }

        if (isNullOrEmpty(txtAmountCrypto)) {
            txtAmountFiat.setText(ZERO)
        } else {
            if (actualSelected.name == context.getString(R.string.default_crypto)) {
                amountConverted = smartCashApplication.multiplyBigDecimals(Util.getBigDecimal(txtAmountCrypto), BigDecimal.valueOf(actualSelected.value!!))
                amountLabel.text = amountInDefaultCryptoConcatenation(context)
            } else {

                val currentPrice = actualSelected.value!!
                val amountInTheField = getDouble(txtAmountCrypto)
                val ruleOfThree = amountInTheField * currentPrice
                amountConverted = BigDecimal.valueOf(ruleOfThree)
            }
            txtAmountFiat.setText(amountConverted.toString())
            val amountWithFee = Util.getBigDecimal(txtAmountCrypto).add(mainFee)

            if (sendButton != null)
                sendButton.text = context.getString(R.string.send_button_label).replace("%f", smartCashApplication.formatNumberByDefaultCrypto(java.lang.Double.parseDouble(amountWithFee.toString())))
        }
    }


    fun setAmountListener(
            context: Context,
            smartCashApplication: SmartCashApplication,
            amountLabel: TextView,
            txtAmountFiat: EditText,
            txtAmountCrypto: EditText,
            mainFee: BigDecimal?,
            sendButton: Button?,
            setQrCode: (() -> Unit?)?
    ) {

        val actualSelected = smartCashApplication.getActualSelectedCoin(context)

        amountLabel.text = String.format(Locale.getDefault(), context.getString(R.string.send_amount_in_coin_label), actualSelected.name)

        txtAmountFiat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                amountLabel.text = String.format(Locale.getDefault(), context.getString(R.string.send_amount_in_coin_label), actualSelected.name)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txtAmountFiat.isFocused) {
                    calculateFromFiatToSmart(
                            context,
                            smartCashApplication,
                            amountLabel,
                            txtAmountFiat,
                            txtAmountCrypto,
                            mainFee,
                            sendButton
                    )
                    if (setQrCode != null) setQrCode()
                }
            }

            override fun afterTextChanged(s: Editable) {
                amountLabel.text = String.format(Locale.getDefault(), context.getString(R.string.send_amount_in_coin_label), actualSelected.name)
            }
        })

        txtAmountCrypto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txtAmountCrypto.isFocused) {
                    calculateFromSmartToFiat(
                            context,
                            smartCashApplication,
                            amountLabel,
                            txtAmountFiat,
                            txtAmountCrypto,
                            mainFee,
                            sendButton
                    )
                    if (setQrCode != null) setQrCode()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    fun <T> getResponse(p: Call<WebWalletRootResponse<T>>): WebWalletRootResponse<T> {

        var responseWebWalletRootResponse: WebWalletRootResponse<T> = WebWalletRootResponse()

        try {
            val r = p.execute()
            responseWebWalletRootResponse.valid = r.isSuccessful
            if (r.isSuccessful) {
                val body = r.body()
                if (body != null) {
                    responseWebWalletRootResponse.data = body.data
                }
            } else {
                try {
                    var ex = Gson().fromJson<WebWalletException>(r.message(), WebWalletException::class.java)
                    responseWebWalletRootResponse.errorDescription = ex.errorDescription
                    responseWebWalletRootResponse.error = ex.error

                } catch (e: Exception) {
                    responseWebWalletRootResponse.errorDescription = e.message
                    responseWebWalletRootResponse.error = e.toString()
                    Log.e(LoginViewModel.TAG, e.message)
                }
            }
        } catch (e: IOException) {
            responseWebWalletRootResponse.errorDescription = e.message
            responseWebWalletRootResponse.error = e.toString()
            Log.e(LoginViewModel.TAG, e.message)
        }
        return responseWebWalletRootResponse
    }
}
