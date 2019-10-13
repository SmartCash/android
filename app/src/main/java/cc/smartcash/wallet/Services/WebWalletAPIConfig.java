package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Utils.URLS;
import retrofit2.Retrofit;

public class WebWalletAPIConfig {

    private final Retrofit retrofit;

    public WebWalletAPIConfig() {
        this.retrofit = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET);
    }

    public WebWalletAPIService getWebWalletAPIService() {
        return this.retrofit.create(WebWalletAPIService.class);
    }
}