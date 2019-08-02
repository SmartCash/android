package com.example.walletsmart.Services;

import com.example.walletsmart.Utils.ConstantsURLS;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class SmartAPIConfig {

    private final Retrofit retrofit;

    public SmartAPIConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsURLS.URL_API_PRICE)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public SmartAPIService getSmartApiService() {
        return this.retrofit.create(SmartAPIService.class);
    }

}