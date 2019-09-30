package cc.smartcash.wallet.ViewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.io.IOException;

import cc.smartcash.wallet.Models.LoginResponse;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.Services.WebWalletAPIConfig;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import retrofit2.Call;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public static final String TAG = LoginViewModel.class.getSimpleName();

    public String getSyncToken(String username, String password, Context context) {

        String localIP = SmartCashApplication.getIPAddress(true);

        Call<LoginResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getToken(
                username,
                password,
                "password",
                "81d46070-686b-4975-9c29-9ebc867a3c4e",
                "",
                "mobile",
                localIP,
                "B3EIldyQp5Hl2CXZdP8MeYmDl3gXb3tan4XCNg0ZK0"
        );

        try {
            Response<LoginResponse> r = call.execute();
            LoginResponse body = r.body();
            return body.getAccessToken();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getSyncUser(String token, Context context) {
        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {
            try {

                Call<WebWalletRootResponse<User>> callUser = new WebWalletAPIConfig().getWebWalletAPIService().getUser("Bearer " + token);

                Response<WebWalletRootResponse<User>> apiResponse = callUser.execute();

                return apiResponse.body().getData();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {

        }
        return null;
    }

}