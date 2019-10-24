package cc.smartcash.wallet.Models

import android.content.Context
import cc.smartcash.wallet.Utils.KEYS
import cc.smartcash.wallet.Utils.SmartCashApplication
import java.util.*

class App(context: Context) {

    val context = context
    val smartCashApplication: SmartCashApplication = SmartCashApplication(context)

    val Wallet: ArrayList<Wallet>?
        get() {
            val walletList = smartCashApplication.getUser(context)!!.wallet
            return walletList
        }

    val WithoutPin: Boolean
        get() {
            val pin = smartCashApplication.getBoolean(context, KEYS.KEY_WITHOUT_PIN)
            if (pin == null) {
                return false
            }
            return pin
        }

    val Coins: ArrayList<Coin>?
        get() {

            return smartCashApplication.getCurrentPrice(context)
        }

    val Token: String?
        get() {
            return smartCashApplication.getToken(context)
        }

    val User: User?
        get() {
            return smartCashApplication.getUser(context)
        }

    val Email: String? get() = User?.email

    val ActualSelectedCoin: Coin
        get() = smartCashApplication.getActualSelectedCoin(context)
}