package cc.smartcash.smarthub.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.*
import cc.smartcash.smarthub.viewModels.LoginViewModel
import com.google.gson.Gson
import org.json.JSONObject
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

    private const val ZERO = "0"

    val date: String
        @SuppressLint("SimpleDateFormat")
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

    private fun getBigDecimal(view: TextView): BigDecimal {
        return BigDecimal.valueOf(getDouble(view))
    }

    private fun getDouble(view: TextView): Double {
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
        return view.text == null || view.text.toString().isEmpty()
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

    private fun amountInCoinConcatenation(context: Context, coinTick: String): String {
        return String.format(Locale.getDefault(), context.getString(R.string.send_amount_in_coin_label).replace("%s", coinTick))
    }

    private fun amountInDefaultCryptoConcatenation(context: Context): String {
        return amountInCoinConcatenation(context, context.getString(R.string.default_crypto))
    }

    fun fillSmartTextRequest(to: String, from: String, amount: String): SmartTextRequest {

        val sendPayment = SmartTextRequest()
        sendPayment.addressRefunded = from
        sendPayment.amountSmart = amount
        when {
            isValidEmail(to) -> {
                sendPayment.destinationEmail = to
                sendPayment.typeSend = KEYS.KEY_SMART_TEXT_EMAIL
            }
            PhoneNumberUtils.isGlobalPhoneNumber(to) -> {
                sendPayment.phoneNumber = to
                sendPayment.typeSend = KEYS.KEY_SMART_TEXT_SMS
            }
            else -> sendPayment.typeSend = KEYS.KEY_SMART_TEXT_LINK
        }
        return sendPayment
    }

    fun fillSmartTextRequest(sendPayment: SendPayment): SmartTextRequest {
        val smartTextRequest = SmartTextRequest()
        smartTextRequest.addressRefunded = sendPayment.fromAddress
        smartTextRequest.amountSmart = sendPayment.amount.toString()
        when {
            isValidEmail(sendPayment.toAddress.toString()) -> {
                smartTextRequest.destinationEmail = sendPayment.toAddress.toString()
                smartTextRequest.typeSend = KEYS.KEY_SMART_TEXT_EMAIL
            }
            PhoneNumberUtils.isGlobalPhoneNumber(sendPayment.toAddress.toString()) -> {
                smartTextRequest.phoneNumber = sendPayment.toAddress.toString()
                smartTextRequest.typeSend = KEYS.KEY_SMART_TEXT_SMS
            }
            else -> smartTextRequest.typeSend = KEYS.KEY_SMART_TEXT_LINK
        }
        return smartTextRequest
    }

    fun fillSendSendSmartByWebWalletRequestBySmartTextResponse(smartTextRoot: SmartTextRoot): SendPayment? {

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

    @SuppressLint("SimpleDateFormat")
    fun getDateFromEpoch(epoch: Long): String {
        val date = Date(epoch * 1000L)
        val format = SimpleDateFormat(KEYS.KEY_DATE_FORMAT)
        format.timeZone = TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE)
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
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
        val coins = smartCashApplication.appPreferences.coins

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
                amountConverted = smartCashApplication.multiplyBigDecimals(getBigDecimal(txtAmountFiat), BigDecimal.valueOf(actualSelected.value!!))
                amountLabel.text = amountInDefaultCryptoConcatenation(context)
            } else {
                val currentPrice = actualSelected.value!!
                val amountInTheField = getDouble(txtAmountFiat)
                val ruleOfThree = amountInTheField / currentPrice
                amountConverted = BigDecimal.valueOf(ruleOfThree)
            }
            txtAmountCrypto.setText(amountConverted.toString())
            val amountWithFee = getBigDecimal(txtAmountCrypto).add(mainFee)
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
        val coins = smartCashApplication.appPreferences.coins

        amountLabel.text = amountInCoinConcatenation(context, actualSelected.name!!)

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
                amountConverted = smartCashApplication.multiplyBigDecimals(getBigDecimal(txtAmountCrypto), BigDecimal.valueOf(actualSelected.value!!))
                amountLabel.text = amountInDefaultCryptoConcatenation(context)
            } else {

                val currentPrice = actualSelected.value!!
                val amountInTheField = getDouble(txtAmountCrypto)
                val ruleOfThree = amountInTheField * currentPrice
                amountConverted = BigDecimal.valueOf(ruleOfThree)
            }
            txtAmountFiat.setText(amountConverted.toString())
            val amountWithFee = getBigDecimal(txtAmountCrypto).add(mainFee)

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
//Nothing to do
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
//Nothing to do
            }
        })

    }

    fun <T> getWebWalletResponse(p: Call<WebWalletRootResponse<T>>): WebWalletRootResponse<T> {

        val responseWebWalletRootResponse: WebWalletRootResponse<T> = WebWalletRootResponse()

        try {
            val r = p.execute()
            responseWebWalletRootResponse.valid = r.isSuccessful
            if (r.isSuccessful) {
                val body = r.body()
                if (body != null) {
                    responseWebWalletRootResponse.data = body.data
                }
            } else {

                val error = r.errorBody()?.string()
                val message = r.message()

                responseWebWalletRootResponse.errorDescription = error
                responseWebWalletRootResponse.error = message

                parseErrorMessage(error, responseWebWalletRootResponse)
            }
        } catch (e: IOException) {
            responseWebWalletRootResponse.errorDescription = e.message
            responseWebWalletRootResponse.error = e.toString()
            Log.e(LoginViewModel.TAG, e.message)
        }
        return responseWebWalletRootResponse
    }

    private fun <T> parseErrorMessage(error: String?, responseWebWalletRootResponse: WebWalletRootResponse<T>) {
        //{"error":"","error_description":""}
        parseErrorMessageFormat1(error, responseWebWalletRootResponse)
        //{"data":null,"error":{"message":"The amount exceeds your balance!"},"status":"BadRequest","isValid":false}
        parseErrorMessageFormat2(error, responseWebWalletRootResponse)
        //{"code":"Property_Required","message":",","details":["amount"]}
        parseErrorMessageFormat3(error, responseWebWalletRootResponse)
    }

    private fun <T> parseErrorMessageFormat3(error: String?, responseWebWalletRootResponse: WebWalletRootResponse<T>) {
        if (
                error != null
                && error.contains("code", true)
                && error.contains("message", true)
                && error.contains("details", true)
        ) {
            try {
                val parsedError = JSONObject(error)
                responseWebWalletRootResponse.error = parsedError.getString("code")

                val details = StringBuilder()

                if (error.contains("message", true)) {
                    details.append(parsedError.getString("message")).append(" ")
                }

                val detailsJson = parsedError.getJSONArray("details")
                if (detailsJson != null) {
                    for (i in 0 until detailsJson.length()) {
                        val item = detailsJson.getString(i)
                        details.append(item).append(" ")
                    }
                }
                responseWebWalletRootResponse.errorDescription = details.toString()
            } catch (e: Exception) {
                responseWebWalletRootResponse.errorDescription = error
                responseWebWalletRootResponse.error = "Unexpected"
            }
        }
    }

    private fun <T> parseErrorMessageFormat2(error: String?, responseWebWalletRootResponse: WebWalletRootResponse<T>) {
        if (
                error != null
                && error.contains("data", true)
                && error.contains("error", true)
                && error.contains("message", true)
                && error.contains("status", true)
                && error.contains("isValid", true)
        ) {
            try {
                val parsedError = JSONObject(error)
                responseWebWalletRootResponse.error = parsedError.getString("status")
                responseWebWalletRootResponse.errorDescription = parsedError.getJSONObject("error").getString("message")
            } catch (e: Exception) {
                responseWebWalletRootResponse.errorDescription = error
                responseWebWalletRootResponse.error = "Unexpected"
            }
        }
    }

    private fun <T> parseErrorMessageFormat1(error: String?, responseWebWalletRootResponse: WebWalletRootResponse<T>) {
        if (error != null && error.contains("error", true) && error.contains("error_description", true)) {

            try {
                val parsedError = Gson().fromJson<WebWalletException>(error, WebWalletException::class.java)
                responseWebWalletRootResponse.errorDescription = parsedError.errorDescription
                responseWebWalletRootResponse.error = parsedError.error
            } catch (e: Exception) {
                responseWebWalletRootResponse.errorDescription = error
                responseWebWalletRootResponse.error = "Unexpected"
            }
        }
    }


    fun <T> showWebWalletException(result: WebWalletRootResponse<T>?, context: Context): Boolean {

        val hasError: Boolean
        val genericError = "The result is null on WebWallet API"

        if (result == null) {
            hasError = true
            Toast.makeText(context, genericError, Toast.LENGTH_LONG).show()
            return hasError
        }
        if (!isNullOrEmpty(result.error) || !isNullOrEmpty(result.errorDescription)) {
            hasError = true
            Toast.makeText(context, result.error + " : " + result.errorDescription, Toast.LENGTH_LONG).show()
            return hasError
        }
        if (result.valid != null && (result.valid!!.not())) {
            hasError = true
            Toast.makeText(context, genericError, Toast.LENGTH_LONG).show()
            return hasError
        }
        if (result.data != null) {
            hasError = false
        } else {
            hasError = true
            Toast.makeText(context, genericError, Toast.LENGTH_LONG).show()
        }
        return hasError
    }

    fun changeImage(image: ImageView, id: Int, context: Context) {
        val iconEye: Drawable? = ContextCompat.getDrawable(context, id)
        image.setImageDrawable(iconEye)
    }

    fun sendEmail(to: String, subject: String, message: String, context: Context) {

        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        email.putExtra(Intent.EXTRA_SUBJECT, subject)
        email.putExtra(Intent.EXTRA_TEXT, message)

        //need this to prompts email client only
        email.type = "message/rfc822"

        context.startActivity(Intent.createChooser(email, "Choose an email client :"))

    }
}
