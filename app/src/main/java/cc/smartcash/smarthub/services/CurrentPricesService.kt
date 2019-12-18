package cc.smartcash.smarthub.services

import com.fasterxml.jackson.databind.JsonNode

import retrofit2.Call
import retrofit2.http.GET

interface CurrentPricesService {

    @get:GET("wallet/getcurrentpricewithcoin")
    val currentPrices: Call<JsonNode>
}