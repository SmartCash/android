package com.example.walletsmart.Services;


import com.example.walletsmart.Models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {

    @GET("user/my")
    Call<ApiResponse> getUser(@Header("Authorization") String auth);
}
