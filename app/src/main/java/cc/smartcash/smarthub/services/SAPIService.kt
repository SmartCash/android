package cc.smartcash.smarthub.services


import cc.smartcash.smarthub.models.SapiAddressBalance
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SAPIService {

    @GET("address/balance/{address}")
    fun getAddressBalance(@Path("address") address: String): Call<SapiAddressBalance>


}