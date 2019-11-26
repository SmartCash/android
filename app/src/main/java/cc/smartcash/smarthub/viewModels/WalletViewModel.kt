package cc.smartcash.smarthub.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.models.*
import cc.smartcash.smarthub.utils.ApiUtil
import cc.smartcash.smarthub.utils.KEYS
import cc.smartcash.smarthub.utils.Util
import java.io.IOException

class WalletViewModel : ViewModel() {

    companion object {

        val TAG: String? = WalletViewModel::class.java.simpleName

        fun getSyncFee(context: Context, token: String, sendPayment: SendPayment): WebWalletRootResponse<Double>? {

            val feeRequest = WalletPaymentFeeRequest()
            feeRequest.amount = sendPayment.amount ?: 0.0
            feeRequest.fromAddress = sendPayment.fromAddress
            feeRequest.toAddress = sendPayment.toAddress
            feeRequest.recurrenceType = 3
            feeRequest.password = sendPayment.userKey

            token.replace("\"", "")

            val callFee = ApiUtil.walletService.getFee("Bearer $token", feeRequest)
            return Util.getWebWalletResponse(callFee)
        }

        fun sendSyncTransaction(context: Context, token: String, sendPayment: SendPayment): WebWalletRootResponse<String>? {
            val callSendPayment = ApiUtil.walletService.sentPayment("Bearer $token", sendPayment)
            return Util.getWebWalletResponse(callSendPayment)
        }

        fun sendSyncSmartText(context: Context, token: String, sendPayment: SmartTextRequest): SmartTextRoot? {
            val callSendPayment = Util.getProperty(KEYS.CONFIG_TOKEN_SEND_BY_TEXT, context)?.let { ApiUtil.smartTextService.sentPayment(it, sendPayment) }

            try {
                val response = callSendPayment?.execute()
                if (response?.isSuccessful!!) {
                    return response.body()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        fun isUserAvailable(user: String): WebWalletRootResponse<Boolean>? {
            val callFee = ApiUtil.webWalletAPIService.isUserAvailable(WebWalletUserAvailableRequest(user))
            return Util.getWebWalletResponse(callFee)
        }
    }

}