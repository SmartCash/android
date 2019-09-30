package cc.smartcash.wallet.Services;

import android.os.Build;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    public static Retrofit getClient(String url) {

        List tlsSpecs = Arrays.asList(ConnectionSpec.MODERN_TLS);
        if(Build.VERSION.SDK_INT < 21) {
            tlsSpecs = Arrays.asList(ConnectionSpec.COMPATIBLE_TLS);
        }


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .connectionSpecs(tlsSpecs)
                .build();

        return new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}