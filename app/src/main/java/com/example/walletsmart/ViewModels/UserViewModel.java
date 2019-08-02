package com.example.walletsmart.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walletsmart.Models.ApiResponse;
import com.example.walletsmart.Models.LoginResponse;
import com.example.walletsmart.Models.User;
import com.example.walletsmart.Services.WebWalletAPIConfig;
import com.example.walletsmart.Utils.Utils;

import org.json.JSONObject;

import java.net.InetAddress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> token;
    private MutableLiveData<User> user;

    public LiveData<String> getToken(String username, String password, Context context) {
        token = new MutableLiveData<String>();

        try {
            loadToken(username, password, context);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return token;
    }

    public LiveData<User> getUser(String token, Context context) {
        user = new MutableLiveData<User>();
        loadUser(token, context);

        return user;
    }

    public void loadUser(String token, Context context) {
        Call<ApiResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getUser("Bearer " + token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    user.setValue(apiResponse.getData());
                } else {
                    try {
                        user.setValue(null);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("WebWalletAPIService", "Erro ao buscar o usuário:" + t.getMessage());
                user.setValue(null);
            }
        });
    }


    public void loadToken(String username, String password, Context context) {

        String localIP = null;
        try{
            localIP = Utils.getIPAddress(true);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        if(localIP == null || localIP.isEmpty()) localIP = "192.168.0.255";

        Call<LoginResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getToken(
                username,
                password,
                "password",
                "81d46070-686b-4975-9c29-9ebc867a3c4e",
                "",
                "mobile",
                "100.003.102.100",
                "B3EIldyQp5Hl2CXZdP8MeYmDl3gXb3tan4XCNg0ZK0"
        );

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    LoginResponse apiResponse = response.body();
                    Log.e("Access token", "" + apiResponse.getAccessToken());
                    token.setValue(apiResponse.getAccessToken());
                } else {
                    try {
                        token.setValue(null);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("error_description"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("WebWalletAPIService", "Erro ao buscar o token:" + t.getMessage());
                token.setValue(null);
            }
        });
    }
}