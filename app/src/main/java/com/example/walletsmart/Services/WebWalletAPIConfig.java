package com.example.walletsmart.Services;

import com.example.walletsmart.Utils.ConstantsURLS;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class WebWalletAPIConfig {

    private final Retrofit retrofit;

    public WebWalletAPIConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsURLS.URL_API_WEBWALLET)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public WebWalletAPIService getWebWalletAPIService() {
        return this.retrofit.create(WebWalletAPIService.class);
    }


}