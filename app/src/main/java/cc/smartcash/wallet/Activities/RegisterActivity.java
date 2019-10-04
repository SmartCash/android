package cc.smartcash.wallet.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.BuildConfig;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.UserLogin;
import cc.smartcash.wallet.Models.UserRecoveryKey;
import cc.smartcash.wallet.Models.UserRegisterRequest;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Receivers.NetworkReceiver;
import cc.smartcash.wallet.Utils.KEYS;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;
import cc.smartcash.wallet.ViewModels.LoginViewModel;
import cc.smartcash.wallet.ViewModels.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private SmartCashApplication smartCashApplication;
    private NetworkReceiver networkReceiver;

    private UserRecoveryKey userRecoveryKeyMain;

    @BindView(R.id.network_status)
    Switch networkSwitch;

    @BindView(R.id.txt_user)
    EditText txtUser;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.txt_confirm_password)
    EditText txtConfirmPassword;

    @BindView(R.id.txt_pin)
    EditText txtPin;

    @BindView(R.id.txt_confirm_pin)
    EditText txtConfirmPin;

    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.login_content)
    ConstraintLayout loginContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        ButterKnife.bind(this);

        if (this.smartCashApplication == null)
            this.smartCashApplication = new SmartCashApplication(getApplicationContext());

        setDebugInfo();

        setNetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");

        registerReceiver(networkReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserRecoveryKey();
        getCurrentPrices();
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        if (hasErrorOnForm(Util.getString(txtUser), Util.getString(txtPassword), Util.getString(txtConfirmPassword), Util.getString(txtPin), Util.getString(txtConfirmPin)))
            return;
        createNewUser(Util.getString(txtUser), Util.getString(txtPassword));
    }

    private void setNetworkReceiver() {
        networkReceiver = new NetworkReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Log.i(TAG, getString(R.string.register_network_status_change));
                String status = NetworkUtil.getConnectivityStatusString(context);

                boolean internetAvailable = NetworkUtil.getInternetStatus(context);
                networkSwitch.setChecked(internetAvailable);
                networkSwitch.setText(status);

            }

        };
    }

    private void setDebugInfo() {
        if (BuildConfig.DEBUG) {
            // do something for a debug build
            String userUUID = UUID.randomUUID().toString();
            txtUser.setText(userUUID + "@testeandroidmobile.com");
            txtPassword.setText("123456");
            txtConfirmPassword.setText("123456");
            txtPin.setText("1234");
            txtConfirmPin.setText("1234");
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(KEYS.KEY_PIN, this.txtPin.getText().toString());
        startActivity(intent);
    }

    private boolean hasErrorOnForm(String username, String password, String confirm_password, String pin, String confirm_pin) {

        boolean hasError = false;

        if (username.isEmpty()) {
            txtUser.setError(getString(R.string.register_username_error_message));
            hasError = true;
        }
        if (password.isEmpty()) {
            txtPassword.setError(getString(R.string.register_password_error_message));
            hasError = true;
        }
        if (confirm_password.isEmpty()) {
            txtConfirmPassword.setError(getString(R.string.register_password_confirmation_error_message));
            hasError = true;
        }
        if (pin.isEmpty()) {
            txtPin.setError(getString(R.string.register_pin_error_message));
            hasError = true;
        }
        if (confirm_pin.isEmpty()) {
            txtConfirmPin.setError(getString(R.string.register_pin_confirmation_error_message));
            hasError = true;
        }
        if (!pin.equalsIgnoreCase(confirm_pin)) {
            txtConfirmPin.setError(getString(R.string.register_pin_match_error_message));
            hasError = true;
        }
        if (!password.equalsIgnoreCase(confirm_password)) {
            txtPassword.setError(getString(R.string.register_password_match_error_message));
            hasError = true;
        }
        if (!Util.isValidEmail(username)) {
            txtUser.setError(getString(R.string.register_username_pattern_error_message));
            hasError = true;
        }
        return hasError;
    }

    private void createNewUser(String username, String password) {
        UserRegisterRequest newUser = new UserRegisterRequest();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(username);
        new RegisterUserTask().execute(newUser);
    }

    private void encryptAndSavePassword(UserRegisterRequest newUser) {
        //ENCRYPT AND SAVE THE PASSWORD
        try {
            byte[] plainTextToEncrypt = newUser.getPassword().getBytes(StandardCharsets.UTF_8);
            byte[] pinByte = this.txtPin.getText().toString().getBytes(StandardCharsets.UTF_8);
            byte[] cipherTextToEncrypt = smartCashApplication.aead.encrypt(plainTextToEncrypt, pinByte);
            smartCashApplication.saveByte(cipherTextToEncrypt, getApplicationContext(), KEYS.KEY_PASSWORD);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void encryptAndSaveMSK(User user, User user1) {
        //ENCRYPT AND SAVE THE MSK
        try {
            byte[] plainTextToEncrypt = user.getRecoveryKey().getBytes(StandardCharsets.UTF_8);
            byte[] pinByte = this.txtPin.getText().toString().getBytes(StandardCharsets.UTF_8);
            byte[] cipherTextToEncrypt = smartCashApplication.aead.encrypt(plainTextToEncrypt, pinByte);
            user1.setRecoveryKey(Base64.encodeToString(cipherTextToEncrypt, Base64.DEFAULT));
            smartCashApplication.saveMSK(cipherTextToEncrypt);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void lockLoginButton() {
        findViewById(R.id.btn_save).setEnabled(false);
        ((Button) findViewById(R.id.btn_save)).setText(getString(R.string.register_save_button_loading_status));
    }

    private void unlockLoginButton() {
        findViewById(R.id.btn_save).setEnabled(true);
        ((Button) findViewById(R.id.btn_save)).setText(getString(R.string.register_save_button));
    }

    private class RegisterUserTask extends AsyncTask<UserRegisterRequest, Integer, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadingProcess();
        }

        @Override
        protected User doInBackground(UserRegisterRequest... users) {

            if (userRecoveryKeyMain != null) {
                User newUserResponse = UserViewModel.setSyncUser(users[0], userRecoveryKeyMain, getApplicationContext());
                if (newUserResponse != null) {

                    UserLogin userLogin = new UserLogin();
                    userLogin.setUsername(newUserResponse.getUsername());
                    userLogin.setPassword(newUserResponse.getPassword());
                    userLogin.setTwoFactorAuthentication("");

                    String token = LoginViewModel.getSyncToken(userLogin, getApplicationContext());
                    User userLoginResponse = LoginViewModel.getSyncUser(token, getApplicationContext());

                    encryptAndSaveMSK(newUserResponse, userLoginResponse);
                    encryptAndSavePassword(users[0]);

                    saveUser(token, userLoginResponse);

                    return userLoginResponse;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockLoginButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            if (user != null) {
                navigateToMain();
            } else {
                endLoadingProcess();
            }
        }
    }

    private void getCurrentPrices() {
        new PriceTask().execute();
    }

    private void getUserRecoveryKey() {
        new UserRecoveryKeyTask().execute();
    }

    private class PriceTask extends AsyncTask<Void, Integer, ArrayList<Coin>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockLoginButton();
        }

        @Override
        protected ArrayList<Coin> doInBackground(Void... users) {
            return CurrentPriceViewModel.getSyncPrices(getApplicationContext());
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockLoginButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(ArrayList<Coin> coins) {
            super.onPostExecute(coins);
            if (coins != null) {
                smartCashApplication.saveCurrentPrice(getApplicationContext(), coins);
                unlockLoginButton();
            } else {
                endLoadingProcess();
            }
        }
    }

    private class UserRecoveryKeyTask extends AsyncTask<Void, Integer, UserRecoveryKey> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockLoginButton();
        }

        @Override
        protected UserRecoveryKey doInBackground(Void... users) {
            return UserViewModel.getSyncUserRecoveryKey();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockLoginButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(UserRecoveryKey userRecoveryKey) {
            super.onPostExecute(userRecoveryKey);

            if (userRecoveryKey != null) {
                userRecoveryKeyMain = userRecoveryKey;
                Toast.makeText(getApplicationContext(), userRecoveryKeyMain.getRecoveryKey(), Toast.LENGTH_SHORT).show();
                unlockLoginButton();
            } else {
                endLoadingProcess();
            }
        }
    }

    private void startLoadingProcess() {
        findViewById(R.id.login_content).setVisibility(View.GONE);
        findViewById(R.id.login_content).setVisibility(View.INVISIBLE);
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
    }

    private void endLoadingProcess() {
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.loader).setVisibility(View.INVISIBLE);
        findViewById(R.id.login_content).setVisibility(View.VISIBLE);
    }

    private void saveUser(String token, User user) {
        if (user != null) {
            smartCashApplication.saveToken(RegisterActivity.this, token);
            smartCashApplication.saveUser(RegisterActivity.this, user);
        } else {
            smartCashApplication.deleteSharedPreferences(RegisterActivity.this);
        }
    }

}