package com.example.walletsmart.Services;

import com.example.walletsmart.Models.ApiResponse;
import com.example.walletsmart.Models.TransactionDetails;
import com.example.walletsmart.Models.TransactionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TransactionDetailsService {

    @POST("wallet/paymentfee")
    Call<TransactionResponse> getDetails(@Header("Authorization") String auth, @Body TransactionDetails transactionDetails);
}
