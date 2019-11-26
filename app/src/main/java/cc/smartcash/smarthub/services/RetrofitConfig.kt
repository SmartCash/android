package cc.smartcash.smarthub.services

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConfig {

    fun getClient(url: String): Retrofit {

        val tlsSpecs: List<*> = listOf(ConnectionSpec.MODERN_TLS)

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .connectionSpecs(tlsSpecs as MutableList<ConnectionSpec>)
                .build()

        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
    }
}