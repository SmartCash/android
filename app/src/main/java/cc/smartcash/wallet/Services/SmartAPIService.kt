package cc.smartcash.wallet.Services


import cc.smartcash.wallet.Models.SmartApiDefaultPrice
import retrofit2.Call
import retrofit2.http.GET

interface SmartAPIService {

    @GET("exchange/currencies")
    fun getDefaultPrices(): Call<SmartApiDefaultPrice>
}