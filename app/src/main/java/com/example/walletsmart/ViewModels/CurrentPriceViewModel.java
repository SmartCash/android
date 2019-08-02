package com.example.walletsmart.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.walletsmart.Utils.ApiUtils;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentPriceViewModel extends ViewModel {

    private MutableLiveData<String> currentPrices;

    public LiveData<String> getCurrentPrices(Context context) {
        if (currentPrices == null) {
            currentPrices = new MutableLiveData<String>();
            loadCurrentPrices(context);
        }

        return currentPrices;
    }

    private void loadCurrentPrices(Context context) {
        Call<JsonNode> call = new ApiUtils(context).getCurrentPricesService().getCurrentPrices();

        call.enqueue(new Callback<JsonNode>() {
            @Override
            public void onResponse(Call<JsonNode> call, Response<JsonNode> response) {
                if(response.isSuccessful()) {
                    currentPrices.setValue(response.body().toString());
                } else {
                    try {
                        currentPrices.setValue(null);
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonNode> call, Throwable t) {
                currentPrices.setValue(null);
            }
        });
    }
}