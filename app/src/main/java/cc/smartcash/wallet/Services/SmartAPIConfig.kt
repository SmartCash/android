package cc.smartcash.wallet.Services

import cc.smartcash.wallet.Utils.URLS
import retrofit2.Retrofit


class SmartAPIConfig {

    private val retrofit: Retrofit = RetrofitConfig.getClient(URLS.URL_API_PRICE)

    val smartApiService: SmartAPIService
        get() = this.retrofit.create(SmartAPIService::class.java)

}