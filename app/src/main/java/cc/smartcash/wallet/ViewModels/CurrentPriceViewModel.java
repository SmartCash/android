package cc.smartcash.wallet.ViewModels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Utils.ApiUtils;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentPriceViewModel extends ViewModel {

    private MutableLiveData<String> currentPrices;

    public LiveData<String> getCurrentPrices(Context context) {
        if (currentPrices == null) {
            currentPrices = new MutableLiveData<>();
            loadCurrentPrices(context);
        }

        return currentPrices;
    }

    public static ArrayList<Coin> getSyncPrices(Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {

            Call<JsonNode> call = new ApiUtils(context).getCurrentPricesService().getCurrentPrices();
            Response<JsonNode> response = null;
            try {

                response = call.execute();

                if (response.isSuccessful()) {

                    return SmartCashApplication.convertToArrayList(response.body().toString());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    private void loadCurrentPrices(Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {

            Call<JsonNode> call = new ApiUtils(context).getCurrentPricesService().getCurrentPrices();

            call.enqueue(new Callback<JsonNode>() {
                @Override
                public void onResponse(Call<JsonNode> call, Response<JsonNode> response) {
                    if (response.isSuccessful()) {
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
}