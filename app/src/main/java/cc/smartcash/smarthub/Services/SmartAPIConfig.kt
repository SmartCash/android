package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Utils.URLS
import retrofit2.Retrofit


class SmartAPIConfig {

    private val retrofit: Retrofit = RetrofitConfig.getClient(URLS.URL_API_PRICE)

    val smartApiService: SmartAPIService
        get() = this.retrofit.create(SmartAPIService::class.java)

}