package cc.smartcash.wallet.Services

import cc.smartcash.wallet.Utils.URLS
import retrofit2.Retrofit

class WebWalletAPIConfig {

    private val retrofit: Retrofit = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET)

    val webWalletAPIService: WebWalletAPIService
        get() = this.retrofit.create(WebWalletAPIService::class.java)

}