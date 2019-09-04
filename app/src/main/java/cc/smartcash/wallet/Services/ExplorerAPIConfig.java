package cc.smartcash.wallet.Services;

import java.util.concurrent.TimeUnit;

import cc.smartcash.wallet.Utils.ConstantsURLS;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ExplorerAPIConfig {

    private final Retrofit retrofit;

    public ExplorerAPIConfig() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
        this.retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(ConstantsURLS.URL_API_EXPLORER)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ExplorerAPIConfig(boolean scalar) {
        if (scalar) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsURLS.URL_API_EXPLORER)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        } else {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsURLS.URL_API_EXPLORER)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
    }

    public ExplorerAPIService getExplorerService() {
        return this.retrofit.create(ExplorerAPIService.class);
    }

}