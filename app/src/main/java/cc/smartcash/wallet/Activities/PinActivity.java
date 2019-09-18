package cc.smartcash.wallet.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.crypto.tink.Aead;

import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.SmartCashApplication;

public class PinActivity extends AppCompatActivity {

    public static final String TAG = PinActivity.class.getSimpleName();

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

    private SmartCashApplication smartCashApplication;
    private String currentPassword;
    private byte[] encryptedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_activity);
        ButterKnife.bind(this);

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getApplicationContext());

        Intent intent = getIntent();
        String extraPASSWORD = intent.getStringExtra(Keys.KEY_PASSWORD);

        if (extraPASSWORD != null && !extraPASSWORD.isEmpty()) {
            this.currentPassword = extraPASSWORD;
        } else {

            encryptedPassword = this.smartCashApplication.getByte(getApplicationContext(), Keys.KEY_PASSWORD);

        }

        if (encryptedPassword != null) {
            txtConfirmPin.setVisibility(View.GONE);
            confirmPinLabel.setVisibility(View.GONE);
            forgotPinBtn.setVisibility(View.VISIBLE);
            continueWithoutToken.setVisibility(View.GONE);
        }


        boolean withoutPin = smartCashApplication.getBoolean(this, Keys.KEY_WITHOUT_PIN);

        if (withoutPin) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {

        if (encryptedPassword != null) {

            try {

                smartCashApplication.aead = smartCashApplication.getOrGenerateNewKeysetHandle(getApplicationContext()).getPrimitive(Aead.class);

                byte[] decryptedPin = smartCashApplication.aead.decrypt(encryptedPassword, txtPin.getText().toString().getBytes(StandardCharsets.UTF_8));

                String decryptedText = new String(decryptedPin, StandardCharsets.UTF_8);

                Log.i(TAG, "The decrypted text is: " + decryptedText);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        } else if (txtPin.getText().toString().equals(txtConfirmPin.getText().toString())) {

            //ENCRYPT THE PASSWORD

            try {

                if (smartCashApplication == null)
                    smartCashApplication = new SmartCashApplication(getApplicationContext());

                byte[] plainTextToEncrypt = this.currentPassword.getBytes(StandardCharsets.UTF_8);
                byte[] pin = this.txtConfirmPin.getText().toString().getBytes(StandardCharsets.UTF_8);
                byte[] cipherTextToEncrypt = smartCashApplication.aead.encrypt(plainTextToEncrypt, pin);
                smartCashApplication.saveByte(cipherTextToEncrypt, getApplicationContext(), Keys.KEY_PASSWORD);

                String encryptedText = Base64.encodeToString(cipherTextToEncrypt, Base64.DEFAULT);
                Log.i(TAG, "The encrypted text is: " + encryptedText);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }


        }


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


                        smartCashApplication.deleteSharedPreferences(PinActivity.this);
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

                        smartCashApplication.saveBoolean(PinActivity.this, true, "WithoutPin");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}
