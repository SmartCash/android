package cc.smartcash.wallet.ViewModels;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import cc.smartcash.wallet.Models.LoginResponse;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.UserLogin;
import cc.smartcash.wallet.Models.WebWalletException;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.Services.WebWalletAPIConfig;
import cc.smartcash.wallet.Utils.KEYS;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import retrofit2.Call;
import retrofit2.Response;

public class LoginViewModel {

    public static final String TAG = LoginViewModel.class.getSimpleName();

    public static String getSyncToken(UserLogin userLogin, Context context) {

        String localIP = SmartCashApplication.getIPAddress(true);
        Call<LoginResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getToken(
                userLogin.getUsername(),
                userLogin.getPassword(),
                "password",
                Util.getProperty(KEYS.CONFIG_CLIENT_ID, context),
                userLogin.getTwoFactorAuthentication(),
                "mobile",
                localIP,
                Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context)
        );

        try {
            Response<LoginResponse> r = call.execute();
            LoginResponse body = r.body();

            if (r != null && r.errorBody() != null) {
                WebWalletException exception = WebWalletException.parse(r.errorBody().string());
                return "error:" + exception.getErrorDescription();
            }
            return body.getAccessToken();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static User getSyncUser(String token, Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {
            try {

                Call<WebWalletRootResponse<User>> callUser = new WebWalletAPIConfig().getWebWalletAPIService().getUser("Bearer " + token);

                Response<WebWalletRootResponse<User>> apiResponse = callUser.execute();

                return apiResponse.body().getData();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return null;
    }

}