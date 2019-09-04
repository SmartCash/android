package cc.smartcash.wallet.Services;

import java.util.concurrent.TimeUnit;

import cc.smartcash.wallet.Utils.ConstantsURLS;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SAPIConfig {

    private final Retrofit retrofit;

    public SAPIConfig() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
        this.retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(ConstantsURLS.URL_API_SAPI)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public SAPIConfig(boolean scalar) {
        if (scalar) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsURLS.URL_API_SAPI)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        } else {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsURLS.URL_API_SAPI)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
    }

    public SAPIService getSapiService() {
        return this.retrofit.create(SAPIService.class);
    }

}