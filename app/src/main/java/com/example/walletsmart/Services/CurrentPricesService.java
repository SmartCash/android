package com.example.walletsmart.Services;

import com.example.walletsmart.Models.CurrentPrices;
import com.fasterxml.jackson.databind.JsonNode;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrentPricesService {

    @GET("wallet/getcurrentpricewithcoin")
    Call<JsonNode> getCurrentPrices();
}