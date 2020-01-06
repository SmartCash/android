package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.*
import cc.smartcash.smarthub.ViewModels.WalletViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletService {

    @POST("wallet/sendpayment")
    fun sentPayment(@Header("Authorization") auth: String, @Body sendPayment: SendPayment): Call<WebWalletRootResponse<String>>

    @POST("wallet/paymentfee")
    fun getFee(@Header("Authorization") auth: String, @Body feeRequest: WalletPaymentFeeRequest): Call<WebWalletRootResponse<Double>>

}