package com.example.walletsmart.Services;

import com.example.walletsmart.Models.ApiResponse;
import com.example.walletsmart.Models.FullTransaction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface TransactionService {

    @GET
    Call<FullTransaction> getTransaction(@Url String url);
}
