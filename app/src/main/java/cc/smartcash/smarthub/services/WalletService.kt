package cc.smartcash.smarthub.services

import cc.smartcash.smarthub.models.SendPayment
import cc.smartcash.smarthub.models.WalletPaymentFeeRequest
import cc.smartcash.smarthub.models.WebWalletRootResponse
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