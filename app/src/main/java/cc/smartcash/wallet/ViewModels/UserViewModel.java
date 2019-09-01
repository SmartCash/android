package cc.smartcash.wallet.ViewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import cc.smartcash.wallet.Models.LoginResponse;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.UserRecoveryKey;
import cc.smartcash.wallet.Models.UserRegisterRequest;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.Services.WebWalletAPIConfig;
import cc.smartcash.wallet.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    public static final String TAG = UserViewModel.class.getSimpleName();

    private MutableLiveData<String> token;

    private MutableLiveData<User> user;

    private MutableLiveData<UserRecoveryKey> userRecoveryKey;

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

    public LiveData<User> setUser(UserRegisterRequest newUser, Context context) {

        user = new MutableLiveData<User>();

        createUser(newUser, context);

        return user;
    }

    public void loadUser(String token, Context context) {
        Call<WebWalletRootResponse<User>> call = new WebWalletAPIConfig().getWebWalletAPIService().getUser("Bearer " + token);

        call.enqueue(new Callback<WebWalletRootResponse<User>>() {
            @Override
            public void onResponse(Call<WebWalletRootResponse<User>> call, Response<WebWalletRootResponse<User>> response) {
                if (response.isSuccessful()) {
                    WebWalletRootResponse<User> apiResponse = response.body();
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
            public void onFailure(Call<WebWalletRootResponse<User>> call, Throwable t) {
                Log.e("WebWalletAPIService", "Erro ao buscar o usuário:" + t.getMessage());
                user.setValue(null);
            }
        });
    }

    public void saveUser(UserRegisterRequest newUser, UserRecoveryKey userRecoveryKey, Context context) {

        newUser.setRecoveryKey(userRecoveryKey.getRecoveryKey());

        Call<WebWalletRootResponse<User>> call = new WebWalletAPIConfig().getWebWalletAPIService().setUser(newUser);

        call.enqueue(new Callback<WebWalletRootResponse<User>>() {
            @Override
            public void onResponse(Call<WebWalletRootResponse<User>> call, Response<WebWalletRootResponse<User>> response) {
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
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<WebWalletRootResponse<User>> call, Throwable t) {
                Log.e("WebWalletAPIService", "Erro ao buscar o usuário:" + t.getMessage());
                user.setValue(null);
            }
        });
    }

    public void loadToken(String username, String password, Context context) {

        String localIP = null;
        try {
            localIP = Utils.getIPAddress(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (localIP == null || localIP.isEmpty()) localIP = "192.168.0.255";

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
                if (response.isSuccessful()) {
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

    public void createUser(UserRegisterRequest newUser, Context context) {

        try {

            userRecoveryKey = new MutableLiveData<UserRecoveryKey>();

            Call<WebWalletRootResponse<UserRecoveryKey>> callUserRecoveryKey = new WebWalletAPIConfig().getWebWalletAPIService().getNewMasterSecurityKey();

            callUserRecoveryKey.enqueue(new Callback<WebWalletRootResponse<UserRecoveryKey>>() {
                @Override
                public void onResponse(Call<WebWalletRootResponse<UserRecoveryKey>> call, Response<WebWalletRootResponse<UserRecoveryKey>> response) {
                    if (response.isSuccessful()) {
                        WebWalletRootResponse<UserRecoveryKey> apiResponse = response.body();
                        userRecoveryKey.setValue(apiResponse.getData());


                        saveUser(newUser, apiResponse.getData(), context);


                        Toast.makeText(context, apiResponse.getData().getRecoveryKey(), Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            userRecoveryKey.setValue(null);
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
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

}