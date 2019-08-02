package com.example.walletsmart.Services;

import com.example.walletsmart.Utils.ConstantsURLS;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SAPIConfig {

    private final Retrofit retrofit;

    public SAPIConfig() {
        this.retrofit = new Retrofit.Builder()
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