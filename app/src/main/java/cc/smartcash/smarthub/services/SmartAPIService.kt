package cc.smartcash.smarthub.services


import cc.smartcash.smarthub.models.SmartApiDefaultPrice
import retrofit2.Call
import retrofit2.http.GET

interface SmartAPIService {

    @GET("exchange/currencies")
    fun getDefaultPrices(): Call<SmartApiDefaultPrice>
}