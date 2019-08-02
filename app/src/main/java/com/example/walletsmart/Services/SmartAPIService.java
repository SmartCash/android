package com.example.walletsmart.Services;


import com.example.walletsmart.Models.SmartApiDefaultPrice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SmartAPIService {

    @GET("exchange/currencies")
    Call<SmartApiDefaultPrice> GetDefaultPrices();
}