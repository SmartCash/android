package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Utils.ConstantsURLS;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SAPIConfig {

    private final Retrofit retrofit;

    public SAPIConfig() {

        this.retrofit = RetrofitConfig.getClient(ConstantsURLS.URL_API_SAPI);
    }

    public SAPIConfig(boolean scalar) {
        if (scalar) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsURLS.URL_API_SAPI)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        } else {
            this.retrofit = RetrofitConfig.getClient(ConstantsURLS.URL_API_SAPI);
        }
    }

    public SAPIService getSapiService() {
        return this.retrofit.create(SAPIService.class);
    }

}