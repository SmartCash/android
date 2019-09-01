package cc.smartcash.wallet.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.DeCryptor;
import cc.smartcash.wallet.Utils.EnCryptor;
import cc.smartcash.wallet.Utils.Utils;

public class PinActivity extends AppCompatActivity {
    private static final String PIN_ALIAS = "AndroidKeyStorePin";
    private static final String PASSWORD_ALIAS = "AndroidKeyStorePassword";
    @BindView(R.id.txt_password)
    EditText txtPin;
    @BindView(R.id.txt_pin)
    EditText txtConfirmPin;
    @BindView(R.id.confirm_pin_label)
    TextView confirmPinLabel;
    @BindView(R.id.forgot_pin_btn)
    TextView forgotPinBtn;
    @BindView(R.id.continue_without_pin)
    TextView continueWithoutToken;
    private EnCryptor encryptor;
    private DeCryptor decryptor;
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

        Intent intent = getIntent();
        String extraPIN = intent.getStringExtra("PIN");

        if (extraPIN != null && !extraPIN.isEmpty())
            encryptAndSavePIN(extraPIN);

        withoutPin = utils.getBoolean(this, "WithoutPin");

        if (withoutPin) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
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
            String decryptedPin = decryptPIN(pin);
            if (decryptedPin.equals(txtPin.getText().toString())) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        } else if (txtPin.getText().toString().equals(txtConfirmPin.getText().toString())) {
            encryptAndSavePIN(txtPin.getText().toString());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    private byte[] encryptAndSavePIN(String decryptedPin) {

        try {

            final byte[] encryptedText = encryptor
                    .encryptText(PIN_ALIAS, decryptedPin, this, "pinIv");

            utils.saveByte(encryptedText, this, "pin");

            return encryptedText;


        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e("Error", "on encrypt: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decryptPIN(byte[] encryptedPin) {
        try {
            String decryptedPin = decryptor
                    .decryptData(PIN_ALIAS, encryptedPin, utils.getByte(this, "pinIv"));

            return decryptedPin;
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e("Error", "on dencrypt: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }


    @OnClick(R.id.forgot_pin_btn)
    public void onForgotPinClicked() {


        new AlertDialog.Builder(this)
                .setTitle("Forgot the PIN?")
                .setMessage("Are you sure you want to redefine your PIN?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Toast.makeText(PinActivity.this, "Redirecting to login...", Toast.LENGTH_SHORT).show();


                        utils.deleteSharedPreferences(PinActivity.this);
                        startActivity(new Intent(PinActivity.this, LoginActivity.class));


                    }
                })
                .setNegativeButton(android.R.string.no, null).show();


    }

    @OnClick(R.id.continue_without_pin)
    public void onContinueWithoutPinClicked() {


        new AlertDialog.Builder(this)
                .setTitle("Forgot the PIN?")
                .setMessage("Are you sure you want to proceed without PIN? You won't be able to use it off-line.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Toast.makeText(PinActivity.this, "Redirecting to dashboard...", Toast.LENGTH_SHORT).show();

                        utils.saveBoolean(PinActivity.this, true, "WithoutPin");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }
}
