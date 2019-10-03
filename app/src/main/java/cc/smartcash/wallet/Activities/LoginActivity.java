package cc.smartcash.wallet.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.BuildConfig;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.UserLogin;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Receivers.NetworkReceiver;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;
import cc.smartcash.wallet.ViewModels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private SmartCashApplication smartCashApplication;
    private boolean internetAvailable;
    private NetworkReceiver networkReceiver;
    private boolean isPasswordVisible = false;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.btn_eye)
    ImageView btnEye;

    @BindView(R.id.network_status)
    Switch networkSwitch;

    @BindView(R.id.txt_user)
    EditText txtUser;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.txt_two_factor_auth)
    EditText txt2fa;

    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.login_content)
    ConstraintLayout loginContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);

        startLoadingProcess();

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getApplicationContext());

        setDebugInfo();

        setNetworkReceiver();

        if (isOnLocalDB()) {
            startActivity(new Intent(getApplicationContext(), PinActivity.class));
        }

        endLoadingProcess();
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

        getCurrentPrices();
    }

    @OnClick(R.id.btn_login)
    protected void onViewClicked() {

        if (!internetAvailable) {
            Toast.makeText(getApplicationContext(), "You must be on-line to log-in.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserLogin userLogin = new UserLogin();
        userLogin.setUsername(Util.getString(txtUser));
        userLogin.setPassword(Util.getString(txtPassword));
        userLogin.setTwoFactorAuthentication(Util.getString(txt2fa));

        if (Util.isNullOrEmpty(txtUser) || Util.isNullOrEmpty(txtPassword)) {
            txtUser.setError("The username can't be empty");
            txtPassword.setError("The password can't be empty");
            return;
        }

        new LoginTask().execute(userLogin);
    }

    @OnClick(R.id.btn_register)
    protected void register() {

        if (!internetAvailable) {
            Toast.makeText(getApplicationContext(), "You must be on-line to register.", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(myIntent);

    }

    @OnClick(R.id.btn_forgot_password)
    protected void onTxtForgetYourPasswordClicked() {

        if (!internetAvailable) {
            Toast.makeText(getApplicationContext(), "You must be on-line to change your password.", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wallet.smartcash.cc/change-password"));
        this.startActivity(browserIntent);
    }

    @OnClick(R.id.btn_eye)
    public void onBtnEyeClicked() {
        if (isPasswordVisible) {
            txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPasswordVisible = false;
            btnEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));
        } else {
            txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPasswordVisible = true;
            btnEye.setImageDrawable(getResources().getDrawable(R.drawable.eye_off));
        }

        txtPassword.setSelection(txtPassword.getText().length());
    }

    private void setDebugInfo() {
        if (BuildConfig.DEBUG) {
            txtUser.setText(Util.getProperty(Keys.CONFIG_TEST_USER, getApplicationContext()));
            txtPassword.setText(Util.getProperty(Keys.CONFIG_TEST_PASS, getApplicationContext()));
        }
    }

    private void setNetworkReceiver() {
        internetAvailable = NetworkUtil.getInternetStatus(this);
        networkReceiver = new NetworkReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String status = NetworkUtil.getConnectivityStatusString(context);
                internetAvailable = NetworkUtil.getInternetStatus(context);
                networkSwitch.setChecked(internetAvailable);
                networkSwitch.setText(status);

                Log.d(TAG, "The status of the network has changed");
            }
        };
    }

    private void getCurrentPrices() {

        new PriceTask().execute();

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

    private boolean isOnLocalDB() {
        String token = smartCashApplication.getToken(this);
        User user = smartCashApplication.getUser(this);
        byte[] password = smartCashApplication.getByte(this, Keys.KEY_PASSWORD);
        return (token != null && !token.isEmpty() && user != null && password != null);
    }

    private void navigateToPinActivity() {
        Intent intent = new Intent(getApplicationContext(), PinActivity.class);
        intent.putExtra(Keys.KEY_PASSWORD, txtPassword.getText().toString());
        startActivity(intent);
    }

    private void saveUser(String token, User user) {
        if (user != null) {
            smartCashApplication.saveToken(LoginActivity.this, token);
            smartCashApplication.saveUser(LoginActivity.this, user);
            Log.d(TAG, "Users OK");
        } else {
            smartCashApplication.deleteSharedPreferences(LoginActivity.this);
            Log.e(TAG, "Error to getUser");
        }
    }

    private void lockLoginButton() {

        btnLogin.setEnabled(false);
        btnLogin.setText("loading...");

        findViewById(R.id.btn_register).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_forgot_password).setVisibility(View.INVISIBLE);
    }

    private void unlockLoginButton() {

        btnLogin.setEnabled(true);
        btnLogin.setText("ENTER");
        findViewById(R.id.btn_register).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_forgot_password).setVisibility(View.VISIBLE);

    }

    private class LoginTask extends AsyncTask<UserLogin, Integer, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoadingProcess();
        }

        @Override
        protected User doInBackground(UserLogin... users) {

            String token = "";
            if (users.length == 0) {
                token = smartCashApplication.getToken(getApplicationContext());
            } else {

                UserLogin userLogin = new UserLogin();
                userLogin.setPassword(users[0].getPassword());
                userLogin.setUsername(users[0].getUsername());
                userLogin.setTwoFactorAuthentication(users[0].getTwoFactorAuthentication());

                token = LoginViewModel.getSyncToken(userLogin, getApplicationContext());
            }
            if (token == null || token.isEmpty() || token.contains("error:")) {
                smartCashApplication.deleteSharedPreferences(getApplicationContext());
                User error = new User();
                error.setFirstName("ErrorOfLogin");
                error.setLastName(token.replace("error:", ""));
                return error;
            }

            User user = LoginViewModel.getSyncUser(token, getApplicationContext());
            saveUser(token, user);
            return user;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int pct = (values[0] * 2);

            Log.d(TAG, "on progress update...");
            Log.d(TAG, String.valueOf(values[0]));
            Log.d(TAG, (pct + "%"));

            if (pct < 100) {
                startLoadingProcess();
            }
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                if (user.getFirstName() != null && user.getFirstName().equalsIgnoreCase("ErrorOfLogin")) {
                    //TODO: Think about a better way of returning an error from that

                    if (user.getLastName() != null) {
                        if (user.getLastName().toLowerCase().contains("2fa")) {
                            txt2fa.setError(user.getLastName());
                        }
                        Toast.makeText(getApplicationContext(), user.getLastName(), Toast.LENGTH_LONG).show();
                    }

                    endLoadingProcess();
                } else
                    navigateToPinActivity();
            } else {
                endLoadingProcess();
            }
        }
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int pct = (values[0] * 2);

            Log.d(TAG, "on progress update...");
            Log.d(TAG, String.valueOf(values[0]));
            Log.d(TAG, (pct + "%"));

            if (pct < 100) {
                lockLoginButton();
            }
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

}