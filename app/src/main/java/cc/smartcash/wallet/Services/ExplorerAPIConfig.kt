package cc.smartcash.wallet.Services

import cc.smartcash.wallet.Utils.URLS
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class ExplorerAPIConfig {

    private val retrofit: Retrofit

    val explorerService: ExplorerAPIService
        get() = this.retrofit.create(ExplorerAPIService::class.java)

    constructor() {
        this.retrofit = RetrofitConfig.getClient(URLS.URL_API_EXPLORER)
    }

    constructor(scalar: Boolean) {
        if (scalar) {
            this.retrofit = Retrofit.Builder()
                    .baseUrl(URLS.URL_API_EXPLORER)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
        } else {
            this.retrofit = RetrofitConfig.getClient(URLS.URL_API_EXPLORER)
        }
    }

}