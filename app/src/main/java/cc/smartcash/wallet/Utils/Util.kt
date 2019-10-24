package cc.smartcash.wallet.Utils

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import cc.smartcash.wallet.Models.SendPayment
import cc.smartcash.wallet.Models.SmartTextRequest
import cc.smartcash.wallet.Models.SmartTextRoot
import cc.smartcash.wallet.R
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
        return java.lang.Double.parseDouble(getString(view))
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

}
