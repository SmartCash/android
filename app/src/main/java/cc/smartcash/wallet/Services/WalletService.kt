package cc.smartcash.wallet.Services

import cc.smartcash.wallet.Models.SendPayment
import cc.smartcash.wallet.Models.WalletPaymentFeeRequest
import cc.smartcash.wallet.Models.WebWalletRootResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletService {

    @POST("wallet/sendpayment")
    fun sentPayment(@Header("Authorization") auth: String, @Body sendPayment: SendPayment): Call<WebWalletRootResponse<String>>

    @POST("wallet/paymentfee")
    fun getFee(@Header("Authorization") auth: String, @Body feeRequest: WalletPaymentFeeRequest): Call<WebWalletRootResponse<Double>>
}