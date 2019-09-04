package cc.smartcash.wallet.Services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build();

    private Retrofit retrofit = null;

    public Retrofit getClient(String url) {
        this.retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit;
    }
}