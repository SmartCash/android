package com.example.walletsmart.Services;


import com.example.walletsmart.Models.SapiAddressBalance;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SAPIService {

    @GET("address/balance/{address}")
    Call<SapiAddressBalance> GetAddressBalance(@Path("address") String address);


}