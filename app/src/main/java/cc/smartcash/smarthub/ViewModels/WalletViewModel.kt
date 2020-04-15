package cc.smartcash.smarthub.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.Models.*
import cc.smartcash.smarthub.Services.SAPIConfig
import cc.smartcash.smarthub.Utils.ApiUtil
import cc.smartcash.smarthub.Utils.KEYS
import cc.smartcash.smarthub.Utils.Util
import java.io.IOException

class WalletViewModel : ViewModel() {

    fun getBalance(address: String): SapiAddressBalance? {
        return SAPIConfig().sapiService.getAddressBalance(address).execute().body()
    }

    companion object {

        val TAG: String? = WalletViewModel::class.java.simpleName

        fun getSyncFee(sendPayment: SendPayment): FeeResponse? {

            val feeRequest = WalletPaymentFeeRequest()
            feeRequest.amount = sendPayment.amount ?: 0.0
            feeRequest.from = sendPayment.from
            feeRequest.apiKey = sendPayment.apiKey
            feeRequest.apiSecret = sendPayment.apiSecret

            if (feeRequest.amount!!.compareTo(0) <= 0) return FeeResponse(0.002)

            val callFee = ApiUtil.walletService.getFee(feeRequest)

            return Util.getGenericResponse(callFee)
        }

        fun sendSyncTransaction(sendPayment: SendPayment): SendResponse? {
            val callSendPayment = ApiUtil.walletService.sentPayment(sendPayment)
            return Util.getGenericResponse(callSendPayment)
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