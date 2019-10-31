package cc.smartcash.wallet.Models

import android.content.Context
import cc.smartcash.wallet.Utils.KEYS
import cc.smartcash.wallet.Utils.SmartCashApplication
import java.util.*

class App(val context: Context) {


    val smartCashApplication: SmartCashApplication = SmartCashApplication(context)

    val wallet: ArrayList<Wallet>?
        get() {
            return smartCashApplication.getUser()!!.wallet
        }

    val withoutPin: Boolean
        get() {
            return smartCashApplication.getBoolean(KEYS.KEY_WITHOUT_PIN)!!
        }

    val coins: ArrayList<Coin>?
        get() {
            return smartCashApplication.getCurrentPrice()
        }

    val token: String?
        get() {
            return smartCashApplication.getToken()
        }

    val user: User?
        get() {
            return smartCashApplication.getUser()
        }

    val Email: String? get() = user?.email

    val ActualSelectedCoin: Coin
        get() = smartCashApplication.getActualSelectedCoin(context)

}