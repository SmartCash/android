package cc.smartcash.wallet.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Receivers.NetworkReceiver;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;
import cc.smartcash.wallet.ViewModels.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private SmartCashApplication smartCashApplication;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_eye)
    ImageView btnEye;
    private boolean internetAvailable;

    @BindView(R.id.network_status)
    Switch networkSwitch;

    @BindView(R.id.txt_user)
    EditText txtUser;

    @BindView(R.id.txt_password)
    EditText txtPassword;

    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.login_content)
    ConstraintLayout loginContent;
    private NetworkReceiver networkReceiver;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);

        startProcess();

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getApplicationContext());

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

        if (isOnLocalDB()) {
            startActivity(new Intent(getApplicationContext(), PinActivity.class));
        } else {
            getUser(smartCashApplication.getToken(this));
        }
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

        String password = txtPassword.getText().toString();
        String username = txtUser.getText().toString();

        if (password.isEmpty() || username.isEmpty()) {

            txtUser.setError("The username can't be empty");
            txtPassword.setError("The password can't be empty");
            return;
        }

        startProcess();
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);
        model.getToken(username, password, this).observe(this, token -> {
            try {
                if (token != null) {
                    smartCashApplication.saveToken(LoginActivity.this, token);
                    Log.d(TAG, token);
                    getUser(token);
                } else {
                    Log.e(TAG, "Error to get the token!");
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {

            }
        });
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

    private void startProcess() {
        findViewById(R.id.login_content).setVisibility(View.GONE);
        findViewById(R.id.login_content).setVisibility(View.INVISIBLE);
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
    }

    private void endProcess() {
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.loader).setVisibility(View.INVISIBLE);
        findViewById(R.id.login_content).setVisibility(View.VISIBLE);
    }

    private boolean isOnLocalDB() {
        String token = smartCashApplication.getToken(this);
        User user = smartCashApplication.getUser(this);
        byte[] pin = smartCashApplication.getByte(this, Keys.KEY_PASSWORD);
        return (token != null && !token.isEmpty() && user != null && pin != null);
    }

    private void navigateToPinActivity() {
        Intent intent = new Intent(getApplicationContext(), PinActivity.class);
        intent.putExtra(Keys.KEY_PASSWORD, txtPassword.getText().toString());
        startActivity(intent);
    }

    private void getUser(String token) {
        if (token == null || token.isEmpty()) {
            endProcess();
        }
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);
        model.getUser(token, LoginActivity.this).observe(LoginActivity.this, user -> {
            try {
                if (user != null) {
                    smartCashApplication.saveUser(LoginActivity.this, user);
                    Log.d(TAG, "Users OK");
                    navigateToPinActivity();
                } else {
                    smartCashApplication.deleteSharedPreferences(LoginActivity.this);
                    Log.e(TAG, "Error to getUser");
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                endProcess();
            }
        });
    }

    private void getCurrentPrices() {
        CurrentPriceViewModel model = ViewModelProviders.of(this).get(CurrentPriceViewModel.class);
        model.getCurrentPrices(LoginActivity.this).observe(this, currentPrices -> {
            if (currentPrices != null) {
                ArrayList<Coin> coins = SmartCashApplication.convertToArrayList(currentPrices);
                smartCashApplication.saveCurrentPrice(this, coins);
                Log.d(TAG, "Prices OK");
            } else {
                Toast.makeText(getApplicationContext(), "Error to get the current prices!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error to get current prices!");
            }
        });
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
}