package cc.smartcash.wallet.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cc.smartcash.wallet.Models.LoginResponse;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.UserRecoveryKey;
import cc.smartcash.wallet.Models.UserRegisterRequest;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.Services.WebWalletAPIConfig;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    public static final String TAG = UserViewModel.class.getSimpleName();

    private MutableLiveData<String> token;

    private MutableLiveData<User> user;

    private MutableLiveData<UserRecoveryKey> userRecoveryKey;

    public LiveData<String> getToken(String username, String password, Context context) {
        token = new MutableLiveData<>();

        try {
            loadToken(username, password, context);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return token;
    }

    public LiveData<User> getUser(String token, Context context) {
        user = new MutableLiveData<>();
        loadUser(token, context);

        return user;
    }

    public LiveData<User> setUser(UserRegisterRequest newUser, Context context) {

        user = new MutableLiveData<>();

        createUser(newUser, context);

        return user;
    }

    public void loadUser(String token, Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {

            Call<WebWalletRootResponse<User>> call = new WebWalletAPIConfig().getWebWalletAPIService().getUser("Bearer " + token);

            call.enqueue(new Callback<WebWalletRootResponse<User>>() {
                @Override
                public void onResponse(Call<WebWalletRootResponse<User>> call, Response<WebWalletRootResponse<User>> response) {
                    if (response != null) {
                        if (response.isSuccessful()) {
                            WebWalletRootResponse<User> apiResponse = response.body();
                            user.setValue(apiResponse.getData());
                        } else {
                            try {
                                user.setValue(null);
                                setResponseError(context, response, "message");
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.e(TAG, "Response is null");
                    }
                }

                @Override
                public void onFailure(Call<WebWalletRootResponse<User>> call, Throwable t) {
                    Log.e("WebWalletAPIService", "Erro ao buscar o usuário:" + t.getMessage());
                    user.setValue(null);
                }


            });
        } else {


        }

    }

    public void saveUser(UserRegisterRequest newUser, UserRecoveryKey userRecoveryKey, Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {

            newUser.setRecoveryKey(userRecoveryKey.getRecoveryKey());

            Call<WebWalletRootResponse<User>> call = new WebWalletAPIConfig().getWebWalletAPIService().setUser(newUser);

            call.enqueue(new Callback<WebWalletRootResponse<User>>() {
                @Override
                public void onResponse(Call<WebWalletRootResponse<User>> call, Response<WebWalletRootResponse<User>> response) {

                    if (response != null) {
                        if (response.isSuccessful()) {

                            WebWalletRootResponse<User> apiResponse = response.body();

                            User userResponse = apiResponse.getData();

                            userResponse.setRecoveryKey(userRecoveryKey.getRecoveryKey());
                            userResponse.setPassword(newUser.getPassword());

                            user.setValue(userResponse);

                            Log.i(TAG, userResponse.getUsername());

                            Toast.makeText(context, apiResponse.getData().getUsername(), Toast.LENGTH_LONG).show();

                        } else {
                            try {
                                user.setValue(null);
                                setResponseError(context, response, "message");
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.e(TAG, "Response is null");
                    }
                }

                @Override
                public void onFailure(Call<WebWalletRootResponse<User>> call, Throwable t) {

                    Toast.makeText(context, "Error to try to retreive the user: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error to try to retreive the user" + t.getMessage());
                    user.setValue(null);
                }
            });
        }
    }

    public void loadToken(String username, String password, Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {

            String localIP = null;
            try {
                localIP = SmartCashApplication.getIPAddress(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (localIP == null || localIP.isEmpty()) localIP = "100.003.102.100";

            Call<LoginResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getToken(
                    Util.getProperty(Keys.CONFIG_TEST_USER, getContext()),
                    Util.getProperty(Keys.CONFIG_TEST_PASS, getContext()),
                    "password",
                    Util.getProperty(Keys.CONFIG_CLIENT_ID, getContext()),

                    "",
                    "mobile",
                    localIP,
                    Util.getProperty(Keys.CONFIG_CLIENT_SECRET, getContext())
            );

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse apiResponse = response.body();
                        Log.e("Access token", "" + apiResponse.getAccessToken());
                        token.setValue(apiResponse.getAccessToken());
                    } else {
                        try {
                            token.setValue(null);
                            setResponseError(context, response, "error_description");
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    Toast.makeText(context, "Error to try to retreive the TOKEN: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error to try to retreive the TOKEN:" + t.getMessage());
                    token.setValue(null);
                }
            });
        }
    }

    private Context getContext() {
        return null;
    }

    public void createUser(UserRegisterRequest newUser, Context context) {

        try {

            userRecoveryKey = new MutableLiveData<>();

            Call<WebWalletRootResponse<UserRecoveryKey>> callUserRecoveryKey = new WebWalletAPIConfig().getWebWalletAPIService().getNewMasterSecurityKey();

            callUserRecoveryKey.enqueue(new Callback<WebWalletRootResponse<UserRecoveryKey>>() {
                @Override
                public void onResponse(Call<WebWalletRootResponse<UserRecoveryKey>> call, Response<WebWalletRootResponse<UserRecoveryKey>> response) {

                    if (response != null) {

                        if (response.isSuccessful()) {
                            WebWalletRootResponse<UserRecoveryKey> apiResponse = response.body();
                            userRecoveryKey.setValue(apiResponse.getData());


                            saveUser(newUser, apiResponse.getData(), context);


                            Toast.makeText(context, apiResponse.getData().getRecoveryKey(), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                userRecoveryKey.setValue(null);
                                setResponseError(context, response, "message");
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.e(TAG, "Response is null");
                    }
                }

                @Override
                public void onFailure(Call<WebWalletRootResponse<UserRecoveryKey>> call, Throwable t) {
                    Log.e("WebWalletAPIService", "Erreor while getting the recovery key" + t.getMessage());
                    user.setValue(null);
                }
            });


        } catch (Exception e) {
            this.user.setValue(null);
            e.printStackTrace();
        }

    }

    public static UserRecoveryKey getSyncUserRecoveryKey() {
        try {

            Call<WebWalletRootResponse<UserRecoveryKey>> callUserRecoveryKey = new WebWalletAPIConfig().getWebWalletAPIService().getNewMasterSecurityKey();

            Response<WebWalletRootResponse<UserRecoveryKey>> response = callUserRecoveryKey.execute();

            if (response != null && response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                return response.body().getData();
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return null;
    }

    public static User setSyncUser(UserRegisterRequest newUser, UserRecoveryKey userRecoveryKey, Context context) {

        boolean isInternetOn = NetworkUtil.getInternetStatus(context);

        if (isInternetOn) {

            newUser.setRecoveryKey(userRecoveryKey.getRecoveryKey());

            Call<WebWalletRootResponse<User>> call = new WebWalletAPIConfig().getWebWalletAPIService().setUser(newUser);

            try {
                Response<WebWalletRootResponse<User>> response = call.execute();

                if (response != null && response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                    User userResponse = response.body().getData();
                    userResponse.setRecoveryKey(userRecoveryKey.getRecoveryKey());
                    userResponse.setPassword(newUser.getPassword());

                    return userResponse;
                }

            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
        return null;
    }

    private void setResponseError(Context context, Response response, String message) throws JSONException, IOException {
        if (response != null && response.errorBody() != null && response.errorBody().toString() != null && !response.errorBody().toString().isEmpty() && !response.errorBody().toString().toLowerCase().contains("okhttp3")) {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            Toast.makeText(context, jObjError.getString(message), Toast.LENGTH_LONG).show();
        }
    }

}