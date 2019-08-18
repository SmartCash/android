package com.example.walletsmart.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.example.walletsmart.Models.Coin;
import com.example.walletsmart.Models.User;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.EnCryptor;
import com.example.walletsmart.Utils.Utils;
import com.example.walletsmart.ViewModels.CurrentPriceViewModel;
import com.example.walletsmart.ViewModels.UserViewModel;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.txt_user)
    EditText txtUser;
    @BindView(R.id.txt_password)
    EditText txtPassword;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.login_content)
    ConstraintLayout loginContent;
    private Utils utils;
    private EnCryptor encryptor;
    private static final String PASSWORD_ALIAS = "AndroidKeyStorePassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);

        encryptor = new EnCryptor();

        this.utils = new Utils();
        String token = utils.getToken(this);
        User user = utils.getUser(this);

        if (token != null && token != "" && user != null) {
            Log.e("token", token);
            this.setVisibility();
            getUser(token);
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        String password = txtPassword.getText().toString();
        String username = txtUser.getText().toString();

        if (password.isEmpty() || username.isEmpty()) {
            return;
        }

        this.setVisibility();

        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getToken(username, password, this).observe(this, token -> {
            if (token != null) {
                utils.saveToken(LoginActivity.this, token);
                getUser(token);
            } else {
                setVisibility();
                Log.e("Erro", "Não foi possível buscar o token!");
            }
        });
    }

    private void savePassword() {
        if (utils.getByte(getApplicationContext(), "password") == null) {
            try {
                final byte[] encryptedText = encryptor
                        .encryptText(PASSWORD_ALIAS, txtPassword.getText().toString(), this, "passwordIv");
                Intent intent = new Intent(getApplicationContext(), PinActivity.class);
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

    public void getUser(String token) {
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getUser(token, LoginActivity.this).observe(LoginActivity.this, user -> {
            if (user != null) {
                utils.saveUser(LoginActivity.this, user);
                getCurrentPrices();
            } else {
                utils.deleteSharedPreferences(LoginActivity.this);
                setVisibility();
                Log.e("Erro", "Não foi possível buscar o usuário!");
            }
        });
    }

    public void getCurrentPrices() {
        CurrentPriceViewModel model = ViewModelProviders.of(this).get(CurrentPriceViewModel.class);

        model.getCurrentPrices(LoginActivity.this).observe(this, currentPrices -> {
            if (currentPrices != null) {
                ArrayList<Coin> coins = Utils.convertToArrayList(currentPrices);
                utils.saveCurrentPrice(this, coins);
                savePassword();
            } else {
                Log.e("Erro", "Erro ao buscar os valores!");
            }
        });
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

    @OnClick(R.id.txt_forget_your_password)
    public void onTxtForgetYourPasswordClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wallet.smartcash.cc/change-password"));
        this.startActivity(browserIntent);
    }
}