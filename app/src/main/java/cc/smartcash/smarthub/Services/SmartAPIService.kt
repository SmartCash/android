package cc.smartcash.smarthub.Services


import cc.smartcash.smarthub.Models.SmartApiDefaultPrice
import retrofit2.Call
import retrofit2.http.GET

interface SmartAPIService {

    @GET("exchange/currencies")
    fun getDefaultPrices(): Call<SmartApiDefaultPrice>
}