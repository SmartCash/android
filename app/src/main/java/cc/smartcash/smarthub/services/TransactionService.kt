package cc.smartcash.smarthub.services

import cc.smartcash.smarthub.models.FullTransaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface TransactionService {

    @GET
    fun getTransaction(@Url url: String): Call<FullTransaction>
}
