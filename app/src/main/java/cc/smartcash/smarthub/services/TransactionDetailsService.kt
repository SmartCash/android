package cc.smartcash.smarthub.services

import cc.smartcash.smarthub.models.TransactionDetails
import cc.smartcash.smarthub.models.TransactionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TransactionDetailsService {

    @POST("wallet/paymentfee")
    fun getDetails(@Header("Authorization") auth: String, @Body transactionDetails: TransactionDetails): Call<TransactionResponse>
}
