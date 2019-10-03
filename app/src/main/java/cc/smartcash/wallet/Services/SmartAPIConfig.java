package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Utils.ConstantsURLS;
import retrofit2.Retrofit;


public class SmartAPIConfig {

    private final Retrofit retrofit;

    public SmartAPIConfig() {
        this.retrofit = RetrofitConfig.getClient(ConstantsURLS.URL_API_PRICE);
    }

    public SmartAPIService getSmartApiService() {
        return this.retrofit.create(SmartAPIService.class);
    }

}