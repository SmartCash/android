package cc.smartcash.wallet.Services

import cc.smartcash.wallet.utils.URLS
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class SAPIConfig {

    private val retrofit: Retrofit

    val sapiService: SAPIService
        get() = this.retrofit.create(SAPIService::class.java)

    constructor() {

        this.retrofit = RetrofitConfig.getClient(URLS.URL_API_SAPI)
    }

    constructor(scalar: Boolean) {
        if (scalar) {
            this.retrofit = Retrofit.Builder()
                    .baseUrl(URLS.URL_API_SAPI)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        } else {
            this.retrofit = RetrofitConfig.getClient(URLS.URL_API_SAPI)
        }
    }

}