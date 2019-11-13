package cc.smartcash.smarthub.Services


import cc.smartcash.smarthub.Models.SapiAddressBalance
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SAPIService {

    @GET("address/balance/{address}")
    fun getAddressBalance(@Path("address") address: String): Call<SapiAddressBalance>


}