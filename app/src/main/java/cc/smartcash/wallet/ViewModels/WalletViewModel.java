package cc.smartcash.wallet.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import cc.smartcash.wallet.Models.SendPayment;
import cc.smartcash.wallet.Models.TransactionResponse;
import cc.smartcash.wallet.Models.WalletPaymentFeeRequest;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.Utils.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletViewModel extends ViewModel {

    public static final String TAG = WalletViewModel.class.getSimpleName();

    private MutableLiveData<TransactionResponse> returnResponse;

    public LiveData<TransactionResponse> sendPayment(Context context, String token, SendPayment sendPayment) {

        returnResponse = new MutableLiveData<>();
        loadSendPayment(context, token, sendPayment);


        return returnResponse;
    }

    private void loadSendPayment(Context context, String token, SendPayment sendPayment) {

        WalletPaymentFeeRequest feeRequest = new WalletPaymentFeeRequest();
        feeRequest.setAmount(sendPayment.getAmount());
        feeRequest.setFromAddress(sendPayment.getFromAddress());
        feeRequest.setToAddress(sendPayment.getToAddress());
        feeRequest.setRecurrenceType(3);
        feeRequest.setPassword(sendPayment.getUserKey());

        Call<WebWalletRootResponse<Double>> callFee = new ApiUtils(context).getWalletService().getFee("Bearer " + token, feeRequest);

        callFee.enqueue(new Callback<WebWalletRootResponse<Double>>() {
            @Override
            public void onResponse(Call<WebWalletRootResponse<Double>> call, Response<WebWalletRootResponse<Double>> response) {

                if (response != null && response.body() != null && response.body().getData() != null) {
                    Double fee = response.body().getData();
                    sendPayment.setAmount(sendPayment.getAmount() + fee);
                }

                Call<TransactionResponse> callSendPayment = new ApiUtils(context).getWalletService().sentPayment("Bearer " + token, sendPayment);

                callSendPayment.enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
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
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {

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