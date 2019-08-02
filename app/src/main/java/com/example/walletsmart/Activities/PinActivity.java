package com.example.walletsmart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walletsmart.R;
import com.example.walletsmart.Utils.DeCryptor;
import com.example.walletsmart.Utils.EnCryptor;
import com.example.walletsmart.Utils.Utils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinActivity extends AppCompatActivity {
    @BindView(R.id.txt_pin)
    EditText txtPin;
    @BindView(R.id.txt_confirm_pin)
    EditText txtConfirmPin;
    @BindView(R.id.confirm_pin_label)
    TextView confirmPinLabel;
    @BindView(R.id.forgot_pin_btn)
    TextView forgotPinBtn;
    @BindView(R.id.continue_without_pin)
    TextView continueWithoutToken;

    private EnCryptor encryptor;
    private DeCryptor decryptor;
    private static final String PIN_ALIAS = "AndroidKeyStorePin";
    private static final String PASSWORD_ALIAS = "AndroidKeyStorePassword";
    private Utils utils;
    private byte[] pin;
    private boolean withoutPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_activity);
        ButterKnife.bind(this);

        encryptor = new EnCryptor();
        utils = new Utils();

        withoutPin = utils.getBoolean(this, "WithoutPin");

        if (withoutPin) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }


        pin = utils.getByte(this, "pin");

        if (pin != null) {
            txtConfirmPin.setVisibility(View.GONE);
            confirmPinLabel.setVisibility(View.GONE);
            forgotPinBtn.setVisibility(View.VISIBLE);
            continueWithoutToken.setVisibility(View.GONE);
        }

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (pin != null) {
            try {
                String decryptedPin = decryptor
                        .decryptData(PIN_ALIAS, pin, utils.getByte(this, "pinIv"));
                if (decryptedPin.equals(txtPin.getText().toString())) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                    KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                    IOException | InvalidKeyException e) {
                Log.e("Error", "on dencrypt: " + e.getMessage(), e);
            } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        } else if (txtPin.getText().toString().equals(txtConfirmPin.getText().toString())) {
            try {
                final byte[] encryptedText = encryptor
                        .encryptText(PIN_ALIAS, txtPin.getText().toString(), this, "pinIv");

                utils.saveByte(encryptedText, this, "pin");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                    KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
                Log.e("Error", "on encrypt: " + e.getMessage(), e);
            } catch (InvalidAlgorithmParameterException | SignatureException |
                    IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.forgot_pin_btn)
    public void onForgotPinClicked() {
        utils.deleteSharedPreferences(this);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.continue_without_pin)
    public void onContinueWithoutPinClicked() {
        utils.saveBoolean(this, true, "WithoutPin");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
