package com.example.walletsmart.Services;


import com.example.walletsmart.Models.ExplorerApiAddressBalance;
import com.example.walletsmart.Models.ExplorerApiAddressBalanceWithTxs;
import com.example.walletsmart.Models.ExplorerApiBlock;
import com.example.walletsmart.Models.ExplorerApiTransactionDetail;
import com.google.gson.JsonObject;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExplorerAPIService {

    @GET("GetLatestBlockHeight")
    Call<String> GetLatestBlockHeight();

    @GET("GetAddressBalance/{address}")
    Call<ExplorerApiAddressBalance[]> GetAddressBalance(@Path("address") String address);

    @GET("GetAddressBalanceWithTransactions/{address}")
    Call<ExplorerApiAddressBalanceWithTxs> GetAddressBalanceWithTransactions(@Path("address") String address);

    @GET("GetBlockById/{height}")
    Call<ExplorerApiBlock> GetBlockById(@Path("height") String height);

    @GET("GetBlockByHash/{hash}")
    Call<ExplorerApiBlock> GetBlockByHash(@Path("hash") String hash);

    @GET("GetTransactionDetail/{txid}")
    Call<ExplorerApiTransactionDetail[]> GetTransactionDetail(@Path("txid") String txid);

}