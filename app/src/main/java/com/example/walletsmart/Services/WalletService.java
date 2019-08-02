package com.example.walletsmart.Services;

import com.example.walletsmart.Models.ApiResponse;
import com.example.walletsmart.Models.SendPayment;
import com.example.walletsmart.Models.TransactionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WalletService {

    @POST("wallet/sendpayment")
    Call<TransactionResponse> sentPayment(@Header("Authorization") String auth, @Body SendPayment sendPayment);
}