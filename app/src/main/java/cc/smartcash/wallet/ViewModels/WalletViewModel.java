package cc.smartcash.wallet.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.io.IOException;

import cc.smartcash.wallet.Models.SendPayment;
import cc.smartcash.wallet.Models.SmartTextRequest;
import cc.smartcash.wallet.Models.SmartTextRoot;
import cc.smartcash.wallet.Models.WalletPaymentFeeRequest;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.Models.WebWalletUserAvailableRequest;
import cc.smartcash.wallet.Utils.ApiUtil;
import cc.smartcash.wallet.Utils.KEYS;
import cc.smartcash.wallet.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletViewModel extends ViewModel {

    public static final String TAG = WalletViewModel.class.getSimpleName();

    private MutableLiveData<WebWalletRootResponse<String>> returnResponse;

    public static WebWalletRootResponse<Double> getSyncFee(Context context, String token, SendPayment sendPayment) {

        WalletPaymentFeeRequest feeRequest = new WalletPaymentFeeRequest();
        feeRequest.setAmount(sendPayment.getAmount());
        feeRequest.setFromAddress(sendPayment.getFromAddress());
        feeRequest.setToAddress(sendPayment.getToAddress());
        feeRequest.setRecurrenceType(3);
        feeRequest.setPassword(sendPayment.getUserKey());

        Call<WebWalletRootResponse<Double>> callFee = ApiUtil.getWalletService().getFee("Bearer " + token, feeRequest);

        try {
            Response<WebWalletRootResponse<Double>> r = callFee.execute();
            WebWalletRootResponse<Double> body = r.body();
            return body;

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static WebWalletRootResponse<String> sendSyncTransaction(Context context, String token, SendPayment sendPayment) {
        Call<WebWalletRootResponse<String>> callSendPayment = ApiUtil.getWalletService().sentPayment("Bearer " + token, sendPayment);
        try {
            Response<WebWalletRootResponse<String>> response = callSendPayment.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SmartTextRoot sendSyncSmartText(Context context, String token, SmartTextRequest sendPayment) {
        Call<SmartTextRoot> callSendPayment = ApiUtil.getSmartTextService().sentPayment(Util.getProperty(KEYS.CONFIG_TOKEN_SEND_BY_TEXT, context), sendPayment);
        try {
            Response<SmartTextRoot> response = callSendPayment.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<WebWalletRootResponse<String>> sendPayment(Context context, String token, SendPayment sendPayment) {
        returnResponse = new MutableLiveData<>();
        loadSendPayment(context, token, sendPayment);
        return returnResponse;
    }

    public static WebWalletRootResponse<Boolean> isUserAvailable(String user) {

        Call<WebWalletRootResponse<Boolean>> callFee = ApiUtil.getWebWalletAPIService().isUserAvailable(new WebWalletUserAvailableRequest(user));

        try {
            Response<WebWalletRootResponse<Boolean>> r = callFee.execute();
            WebWalletRootResponse<Boolean> body = r.body();
            return body;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }



    private void loadSendPayment(Context context, String token, SendPayment sendPayment) {

        WalletPaymentFeeRequest feeRequest = new WalletPaymentFeeRequest();
        feeRequest.setAmount(sendPayment.getAmount());
        feeRequest.setFromAddress(sendPayment.getFromAddress());
        feeRequest.setToAddress(sendPayment.getToAddress());
        feeRequest.setRecurrenceType(3);
        feeRequest.setPassword(sendPayment.getUserKey());

        Call<WebWalletRootResponse<Double>> callFee = ApiUtil.getWalletService().getFee("Bearer " + token, feeRequest);

        callFee.enqueue(new Callback<WebWalletRootResponse<Double>>() {
            @Override
            public void onResponse(Call<WebWalletRootResponse<Double>> call, Response<WebWalletRootResponse<Double>> response) {

                if (response != null && response.body() != null && response.body().getData() != null) {
                    Double fee = response.body().getData();
                    sendPayment.setAmount(sendPayment.getAmount() + fee);
                }

                Call<WebWalletRootResponse<String>> callSendPayment = ApiUtil.getWalletService().sentPayment("Bearer " + token, sendPayment);

                callSendPayment.enqueue(new Callback<WebWalletRootResponse<String>>() {
                    @Override
                    public void onResponse(Call<WebWalletRootResponse<String>> call, Response<WebWalletRootResponse<String>> response) {
                        if (response.isSuccessful()) {
                            returnResponse.setValue(response.body());
                        } else {
                            try {
                                returnResponse.setValue(null);

                                if (response.errorBody() != null) {

                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                } else if (response.raw().code() == 401) {

                                    Toast.makeText(context, "The request was not authorized by the server.", Toast.LENGTH_LONG).show();

                                }

                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WebWalletRootResponse<String>> call, Throwable t) {

                        t.printStackTrace();
                        t.getMessage();
                        Log.e(TAG, t.getMessage(), t);
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                        returnResponse.setValue(null);
                    }
                });


            }

            @Override
            public void onFailure(Call<WebWalletRootResponse<Double>> call, Throwable t) {

            }
        });


    }

}