package cc.smartcash.wallet.Services;

import java.util.concurrent.TimeUnit;

import cc.smartcash.wallet.Utils.ConstantsURLS;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class SmartAPIConfig {

    private final Retrofit retrofit;

    public SmartAPIConfig() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
        this.retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(ConstantsURLS.URL_API_PRICE)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public SmartAPIService getSmartApiService() {
        return this.retrofit.create(SmartAPIService.class);
    }

}