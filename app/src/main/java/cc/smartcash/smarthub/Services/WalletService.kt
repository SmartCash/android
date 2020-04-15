package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WalletService {

    @POST("send")
    fun sentPayment(@Body sendPayment: SendPayment): Call<SendResponse>

    @POST("fee")
    fun getFee(@Body feeRequest: WalletPaymentFeeRequest): Call<FeeResponse>

}