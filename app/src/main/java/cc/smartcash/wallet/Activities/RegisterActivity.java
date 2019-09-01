package cc.smartcash.wallet.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.UserRegisterRequest;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Receivers.NetworkReceiver;
import cc.smartcash.wallet.Utils.EnCryptor;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.Utils;
import cc.smartcash.wallet.ViewModels.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String PASSWORD_ALIAS = "AndroidKeyStorePassword";
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
    private Utils utils;
    private EnCryptor encryptor;
    private NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        ButterKnife.bind(this);

        encryptor = new EnCryptor();
        this.utils = new Utils();

        String useruuid = UUID.randomUUID().toString();
        txtUser.setText(useruuid + "@testeandroidmobile.com");
        txtPassword.setText("123456");
        txtConfirmPassword.setText("123456");
        txtPin.setText("1234");
        txtConfirmPin.setText("1234");

        networkReceiver = new NetworkReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Log.i(TAG, "The status of the network has changed");
                String status = NetworkUtil.getConnectivityStatusString(context);

                boolean internetAvailable = NetworkUtil.getInternetStatus(context);
                networkSwitch.setChecked(internetAvailable);
                networkSwitch.setText(status);

            }

        };

        String token = utils.getToken(this);
        User user = utils.getUser(this);

        if (token != null && token != "" && user != null) {

            Log.e("token", token);

            this.setVisibility();

        } else {

            loginContent.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Error to retreive the Token or the user", Toast.LENGTH_SHORT).show();

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

    @OnClick(R.id.btn_save)
    public void onViewClicked() {

        String username = txtUser.getText().toString();
        String password = txtPassword.getText().toString();
        String confirm_password = txtConfirmPassword.getText().toString();
        String pin = txtPin.getText().toString();
        String confirm_pin = txtConfirmPin.getText().toString();

        boolean hasError = false;

        if (username.isEmpty()) {
            txtUser.setError("The USERNAME can't be empty");
            hasError = true;
        }
        if (password.isEmpty()) {
            txtPassword.setError("The PASSWORD can't be empty");
            hasError = true;
        }
        if (confirm_password.isEmpty()) {
            txtConfirmPassword.setError("The confirmation of the PASSWORD can't be empty");
            hasError = true;
        }
        if (pin.isEmpty()) {
            txtPin.setError("The PIN can't be empty");
            hasError = true;
        }
        if (confirm_pin.isEmpty()) {
            txtConfirmPin.setError("The confirmation of the PIN can't be empty");
            hasError = true;
        }
        if (!pin.equalsIgnoreCase(confirm_pin)) {
            txtConfirmPin.setError("The PIN does not match");
            hasError = true;
        }
        if (!password.equalsIgnoreCase(confirm_password)) {
            txtPassword.setError("The PASSWORD does not match");
            hasError = true;
        }

        if (hasError) return;

        UserRegisterRequest newUser = new UserRegisterRequest();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(username);

        this.setVisibility();

        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.setUser(newUser, this).observe(this, user -> {
            if (user != null) {

                if (user.getWallet() == null) {

                    model.getToken(user.getUsername(), user.getPassword(), this).observe(this, t -> {

                        model.getUser(t, this).observe(this, user1 -> {
                            user1.setRecoveryKey(user.getRecoveryKey());
                            utils.saveUser(RegisterActivity.this, user1);
                            savePassword();
                        });

                    });

                } else {
                    setVisibility();
                    Log.e(TAG, "It was not possible to register the user");
                }

            } else {
                setVisibility();
                Log.e(TAG, "It was not possible to register the user");
            }
        });
    }

    private void savePassword() {
        if (utils.getByte(getApplicationContext(), "password") == null) {
            try {
                final byte[] encryptedText = encryptor
                        .encryptText(PASSWORD_ALIAS, txtPassword.getText().toString(), this, "passwordIv");
                Intent intent = new Intent(getApplicationContext(), PinActivity.class);
                intent.putExtra("PIN", txtPin.getText().toString());
                startActivity(intent);
                utils.saveByte(encryptedText, this, "password");
            } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                    KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
                Log.e("Error", "on encrypt: " + e.getMessage(), e);
            } catch (InvalidAlgorithmParameterException | SignatureException |
                    IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), PinActivity.class);
            startActivity(intent);
        }
    }

    public void setVisibility() {
        if (loader.getVisibility() == View.VISIBLE) {
            loginContent.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        } else {
            loginContent.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        }
    }


}