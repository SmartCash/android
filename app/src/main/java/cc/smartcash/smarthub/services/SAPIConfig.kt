package cc.smartcash.smarthub.services

import cc.smartcash.smarthub.utils.URLS
import retrofit2.Retrofit


class SAPIConfig {

    private val retrofit: Retrofit

    val sapiService: SAPIService
        get() = this.retrofit.create(SAPIService::class.java)

    constructor() {

        this.retrofit = RetrofitConfig.getClient(URLS.URL_API_SAPI)
    }

}