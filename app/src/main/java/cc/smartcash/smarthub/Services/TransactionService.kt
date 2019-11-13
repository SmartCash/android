package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.FullTransaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface TransactionService {

    @GET
    fun getTransaction(@Url url: String): Call<FullTransaction>
}