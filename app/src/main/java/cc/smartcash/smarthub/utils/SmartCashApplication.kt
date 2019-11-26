package cc.smartcash.smarthub.utils

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.App
import cc.smartcash.smarthub.models.Coin
import cc.smartcash.smarthub.models.User
import cc.smartcash.smarthub.models.Wallet
import com.google.crypto.tink.Aead
import com.google.crypto.tink.Config
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.math.BigDecimal
import java.net.NetworkInterface
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SmartCashApplication(context: Context) : Application() {

    var mPrefs: SharedPreferences? = null
    private var context: Context? = null

    private val tag = SmartCashApplication::class.java.toString()
    private val prefFileName = "smartcash_wallet"
    private val tinkKeysetName = "smartcash_wallet_keyset"
    private val masterKeyUri = "android-keystore://smartcash_wallet_master_key"
    private val gSon = Gson()

    var aead: Aead

    val allValues: Map<String, *>
        get() {
            val keys = this.mPrefs?.all
            if (keys != null) {
                for ((key, value) in keys) {
                    println(key + ": " + value.toString())
                }
            }
            return keys!!
        }

    val msk: ByteArray?
        get() {
            val string = context!!.getSharedPreferences(KEYS.KEY_MSK, Context.MODE_PRIVATE).getString(KEYS.KEY_MSK, "")
            return if (string != null && string.isNotEmpty()) string.toByteArray(Charset.forName("ISO-8859-1")) else null
        }

    val appPreferences: App get() = App(this.context!!)

    init {
        try {
            this.context = context
            this.mPrefs = context.getSharedPreferences(KEYS.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
            Config.register(TinkConfig.LATEST)
            aead = getOrGenerateNewKeysetHandle(context).getPrimitive(Aead::class.java)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    //region getFromSharedPreferences


    fun getActualSelectedCoin(context: Context): Coin {
        var coin = Coin(context.getString(R.string.default_fiat), 0.toDouble())

        val json = this.mPrefs?.getString(KEYS.KEY_CURRENT_SELECTED_COIN, "")

        if (json != null) {
            val coinAux = gSon.fromJson(json, Coin::class.java)
            if (coinAux != null)
                coin = coinAux
            else
                saveActualSelectedCoin(coin)
        }
        return coin
    }

    fun getCurrentPrice(): ArrayList<Coin>? {

        val fromPref = this.mPrefs?.getString(KEYS.KEY_CURRENT_PRICES, "")

        if (Util.isNullOrEmpty(fromPref)) return null

        return ArrayList(GsonBuilder().create().fromJson(fromPref, Array<Coin>::class.java).toList())
    }


    fun getUser(): User? {
        return gSon.fromJson(this.mPrefs?.getString(KEYS.KEY_USER, ""), User::class.java)
    }

    fun getWallet(): Wallet? = gSon.fromJson(this.mPrefs?.getString(KEYS.KEY_WALLET, ""), Wallet::class.java) //SharedEditor<smarthub>().get(context, this.mPrefs!!, KEYS.KEY_WALLET)

    fun getBoolean(key: String): Boolean? = this.mPrefs?.getBoolean(key, false)

    fun get(key: String): String? = this.mPrefs?.getString(key, null)

    fun getToken(): String? = this.mPrefs?.getString(KEYS.KEY_TOKEN, "")

    fun getDecryptedMSK(pin: String): String {

        var decryptedText = ""
        val encryptedPassword = this.msk

        if (encryptedPassword != null) {

            try {

                aead = getOrGenerateNewKeysetHandle(context).getPrimitive(Aead::class.java)

                val decryptedPin = aead.decrypt(encryptedPassword, pin.toByteArray(StandardCharsets.UTF_8))

                decryptedText = String(decryptedPin, StandardCharsets.UTF_8)


            } catch (ex: Exception) {
                decryptedText = ""
                Log.e(tag, ex.message)
            }

        }
        return decryptedText
    }

    fun getByte(key: String): ByteArray? {

        val string = this.mPrefs?.getString(key, "")
        val bytes: ByteArray

        return if (string == null || string === "")
            null
        else {
            bytes = string.toByteArray(Charset.forName("ISO-8859-1"))
            bytes
        }
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    fun getOrGenerateNewKeysetHandle(context: Context?): KeysetHandle {
        return AndroidKeysetManager.Builder()
                .withSharedPref(context!!, tinkKeysetName, prefFileName)
                .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
                .withMasterKeyUri(masterKeyUri)
                .build()
                .keysetHandle
    }

    fun getDecryptedPassword(context: Context, pin: String): String {

        var decryptedText = ""
        val encryptedPassword = this.getByte(KEYS.KEY_PASSWORD)

        if (encryptedPassword != null) {

            try {

                aead = getOrGenerateNewKeysetHandle(context).getPrimitive(Aead::class.java)

                val decryptedPin = aead.decrypt(encryptedPassword, pin.toByteArray(StandardCharsets.UTF_8))

                decryptedText = String(decryptedPin, StandardCharsets.UTF_8)


            } catch (ex: Exception) {
                decryptedText = ""
                Log.e(tag, ex.message)
            }

        }
        return decryptedText
    }

    fun getString(key: String): String? = this.mPrefs?.getString(key, "")

    //endregion

    //region Save to Shared Preferences

    fun saveActualSelectedCoin(coin: Coin) =
            SharedEditor<Coin>().saveGson(this.mPrefs!!, coin, KEYS.KEY_CURRENT_SELECTED_COIN)

    fun saveCurrentPrice(coins: ArrayList<Coin>) =
            SharedEditor<ArrayList<Coin>>().saveGson(this.mPrefs!!, coins, KEYS.KEY_CURRENT_PRICES)

    fun saveUser(user: User) =
            SharedEditor<User>().saveGson(this.mPrefs!!, user, KEYS.KEY_USER)

    fun saveWallet(wallet: Wallet) =
            SharedEditor<Wallet>().saveGson(this.mPrefs!!, wallet, KEYS.KEY_WALLET)

    fun saveToken(token: String) = SharedEditor<String>().saveString(this.mPrefs!!, token, KEYS.KEY_TOKEN)

    fun saveWithoutPIN(flag: Boolean) = SharedEditor<String>().saveBoolean(this.mPrefs!!, flag, KEYS.KEY_WITHOUT_PIN)

    fun saveMSK(bytes: ByteArray) =
            this.context!!.getSharedPreferences(KEYS.KEY_MSK, Context.MODE_PRIVATE).edit().putString(KEYS.KEY_MSK, String(bytes, Charset.forName("ISO-8859-1"))).apply()

    fun saveByte(bytes: ByteArray, key: String) =
            this.mPrefs?.edit()?.putString(key, String(bytes, Charset.forName("ISO-8859-1")))?.apply()

    fun saveString(stringText: String, key: String) = this.mPrefs?.edit()?.putString(key, stringText)?.apply()

    //endregion

    //region delete shared preferences

    fun deleteSharedPreferences() =
            this.mPrefs?.edit()?.clear()?.apply()

    fun deleteMSK() =
            this.context!!.getSharedPreferences(KEYS.KEY_MSK, Context.MODE_PRIVATE).edit().remove(KEYS.KEY_MSK).apply()

    //endregion

    //region UTIL

    fun checkAllNecessaryKeys(): Boolean {
        val keys = allValues
        return (keys.containsKey(KEYS.KEY_TOKEN)
                && keys.containsKey(KEYS.KEY_USER)
                && keys.containsKey(KEYS.KEY_WALLET)
                && keys.containsKey(KEYS.KEY_CURRENT_PRICES)
                && keys.containsKey(KEYS.KEY_TIME_PRICE_WAS_UPDATED)
                && keys.containsKey(KEYS.KEY_WITHOUT_PIN)
                && keys[KEYS.KEY_TOKEN] != null
                && keys[KEYS.KEY_USER] != null
                && keys[KEYS.KEY_WALLET] != null
                && keys[KEYS.KEY_CURRENT_PRICES] != null
                && keys[KEYS.KEY_TIME_PRICE_WAS_UPDATED] != null
                && keys[KEYS.KEY_WITHOUT_PIN] != null
                )
    }

    fun getKeysThatDoesNotExists(): String {
        val error = StringBuilder()
        val keys = allValues
        if (keys.containsKey(KEYS.KEY_TOKEN).not()) {
            error.appendln("KEY_TOKEN: NOK")
        }
        if (keys.containsKey(KEYS.KEY_USER).not()) {
            error.appendln("KEY_USER: NOK")
        }
        if (keys.containsKey(KEYS.KEY_WALLET).not()) {
            error.appendln("KEY_WALLET: NOK")
        }
        if (keys.containsKey(KEYS.KEY_CURRENT_PRICES).not()) {
            error.appendln("KEY_CURRENT_PRICES")
        }
        if (keys.containsKey(KEYS.KEY_TIME_PRICE_WAS_UPDATED).not()) {
            error.appendln("KEY_TIME_PRICE_WAS_UPDATED")
        }
        if (keys.containsKey(KEYS.KEY_WITHOUT_PIN).not()) {
            error.appendln("KEY_WITHOUT_PIN")
        }
        return error.toString()
    }


    fun formatNumberBySelectedCurrencyCode(numberToFormat: Double): String {
        val currency = if (this.getActualSelectedCoin(context!!).name!!.equals(context!!.getString(R.string.default_crypto), ignoreCase = true)) {
            Currency.getInstance(context!!.getString(R.string.default_fiat))

        } else {
            Currency.getInstance(getActualSelectedCoin(context!!).name)
        }
        val format = NumberFormat.getInstance()
        format.maximumFractionDigits = 8

        format.currency = currency
        var symbol = currency!!.symbol
        if (getActualSelectedCoin(context!!).name!!.equals(context!!.getString(R.string.default_crypto), ignoreCase = true)) {
            symbol = context!!.getString(R.string.smartCash)
        }
        println(symbol + " " + format.format(numberToFormat))
        return symbol + " " + format.format(numberToFormat)
    }

    fun formatNumberByDefaultCrypto(numberToFormat: Double): String {
        val format = NumberFormat.getInstance()
        format.maximumFractionDigits = 8
        val currency = Currency.getInstance(context!!.getString(R.string.default_fiat))
        format.currency = currency
        val symbol = context!!.getString(R.string.smartCash)
        return symbol + " " + format.format(numberToFormat)
    }

    fun getCurrentValueByRate(amount: Double, value: Double): Double = amount * value

    fun multiplyBigDecimals(amount: BigDecimal, value: BigDecimal): BigDecimal = amount.multiply(value)

    //endregion

    //region STATIC METHODS

    companion object {
        fun copyToClipboard(context: Context, text: String) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(text, text)
            clipboardManager.primaryClip = clipData

            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
        }

        fun getIPAddress(useIPv4: Boolean): String {
            try {
                val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (networkInterface in interfaces) {
                    val networkAddresses = Collections.list(networkInterface.inetAddresses)
                    for (inetAddress in networkAddresses) {
                        if (!inetAddress.isLoopbackAddress) {
                            val stringAddress = inetAddress.hostAddress

                            val isIPv4 = stringAddress.indexOf(':') < 0

                            if (useIPv4) {
                                if (isIPv4)
                                    return stringAddress
                            } else {
                                if (!isIPv4) {
                                    val delimiter = stringAddress.indexOf('%') // drop ip6 zone suffix
                                    return if (delimiter < 0) stringAddress.toUpperCase() else stringAddress.substring(0, delimiter).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
                //Nothing to do
            }
            // for now eat exceptions
            return ""
        }

        fun convertToArrayList(string: String): ArrayList<Coin> {
            val newPrices: String = string.replace("{", "").replace("}", "")
            val arrayListStrings: ArrayList<String>
            val coins = ArrayList<Coin>()

            arrayListStrings = ArrayList(listOf(*newPrices.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))

            for (element in arrayListStrings) {
                val singleCoin = element.replace("\"", "")
                val arrayListKeyAndValue = ArrayList(listOf(*singleCoin.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
                val coin = Coin(arrayListKeyAndValue[0], java.lang.Double.parseDouble(arrayListKeyAndValue[1]))
                coins.add(coin)
            }

            coins.sortWith(Comparator { p1, p2 -> p1.name!!.compareTo(p2.name!!) })

            val smartcash = Coin("SMART", 1.0)

            coins.add(0, smartcash)

            return coins
        }


    }

    //endregion

    class SharedEditor<T> {

        fun saveGson(sharedPreferences: SharedPreferences, obj: T, key: String) {
            val prefsEditor = sharedPreferences.edit()
            val json = Gson().toJson(obj)
            prefsEditor?.putString(key, json)
            prefsEditor?.apply()
        }

        fun saveString(sharedPreferences: SharedPreferences, obj: String, key: String) {
            sharedPreferences.edit().putString(key, obj).apply()
        }

        fun saveBoolean(sharedPreferences: SharedPreferences, obj: Boolean, key: String) {
            sharedPreferences.edit().putBoolean(key, obj).apply()
        }

        fun <T> get(sharedPreferences: SharedPreferences, key: String): T? {
            val stringSharedPreferences = sharedPreferences.getString(key, "")
            if (Util.isNullOrEmpty(stringSharedPreferences)) return null
            val turnsType = object : TypeToken<T>() {}.type


            return GsonBuilder().create().fromJson<T>(stringSharedPreferences, turnsType)
        }
    }
}
