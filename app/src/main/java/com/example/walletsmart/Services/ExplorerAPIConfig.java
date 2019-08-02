package com.example.walletsmart.Services;

import com.example.walletsmart.Utils.ConstantsURLS;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ExplorerAPIConfig {

    private final Retrofit retrofit;

    public ExplorerAPIConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsURLS.URL_API_EXPLORER)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ExplorerAPIConfig(boolean scalar){
        if(scalar){
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