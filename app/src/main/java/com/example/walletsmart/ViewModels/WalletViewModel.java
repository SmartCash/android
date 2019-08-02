package com.example.walletsmart.ViewModels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walletsmart.Models.ApiResponse;
import com.example.walletsmart.Models.SendPayment;
import com.example.walletsmart.Models.TransactionResponse;
import com.example.walletsmart.Utils.ApiUtils;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletViewModel extends ViewModel {

    private MutableLiveData<TransactionResponse> returnResponse;

    public LiveData<TransactionResponse> sendPayment(Context context, String token, SendPayment sendPayment) {

        returnResponse = new MutableLiveData<TransactionResponse>();
        loadSendPayment(context, token, sendPayment);


        return returnResponse;
    }

    private void loadSendPayment(Context context, String token, SendPayment sendPayment) {
        Call<TransactionResponse> call = new ApiUtils(context).getWalletService().sentPayment("Bearer " + token, sendPayment);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if(response.isSuccessful()) {
                    returnResponse.setValue(response.body());
                } else {
                    try {
                        returnResponse.setValue(null);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                returnResponse.setValue(null);
            }
        });
    }
}