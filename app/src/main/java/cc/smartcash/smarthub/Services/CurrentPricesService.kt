package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.CoinList
import com.fasterxml.jackson.databind.JsonNode

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentPricesService {

    @GET("simple/price")
    fun currentPrices(@Query("ids") ids: String,
                 @Query("vs_currencies") vsCurrencies: String) : Call<Map<String, Map<String, Double>>>

    @GET("simple/supported_vs_currencies")
    fun currencyList(): Call<List<String>>
}