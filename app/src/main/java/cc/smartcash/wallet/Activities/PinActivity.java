package cc.smartcash.wallet.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.crypto.tink.Aead;

import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Receivers.NetworkReceiver;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.NetworkUtil;
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

    @BindView(R.id.btn_eye)
    ImageView btnEye;

    private SmartCashApplication smartCashApplication;
    private String currentPassword;
    private byte[] encryptedPassword;
    private boolean internetAvailable;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_activity);
        ButterKnife.bind(this);

        setNetworkBroadCastReceiver();

        setSmartCashApplication();

        getPassword();

        setConfirmationTextsInvisible();

        setNavigationWithoutPin();
    }

    private void setSmartCashApplication() {
        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getApplicationContext());
    }

    private void getPassword() {
        Intent intent = getIntent();
        String extraPASSWORD = intent.getStringExtra(Keys.KEY_PASSWORD);
        if (extraPASSWORD != null && !extraPASSWORD.isEmpty()) {
            this.currentPassword = extraPASSWORD;
        } else {
            encryptedPassword = this.smartCashApplication.getByte(getApplicationContext(), Keys.KEY_PASSWORD);
        }
    }

    private void setConfirmationTextsInvisible() {
        if (encryptedPassword != null) {
            txtConfirmPin.setVisibility(View.GONE);
            confirmPinLabel.setVisibility(View.GONE);
            forgotPinBtn.setVisibility(View.VISIBLE);
            continueWithoutToken.setVisibility(View.GONE);
        }
    }

    private void setNavigationWithoutPin() {
        boolean withoutPin = smartCashApplication.getBoolean(this, Keys.KEY_WITHOUT_PIN);
        if (withoutPin) {
            internetAvailable = NetworkUtil.getInternetStatus(this);
            if (!internetAvailable) navigateToLogin();
            if (isOnLocalDB(withoutPin)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                navigateToLogin();
            }
        }
    }

    private void setNetworkBroadCastReceiver() {

        new NetworkReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                internetAvailable = NetworkUtil.getInternetStatus(context);

                Log.d(TAG, "The status of the network has changed");
            }
        };
    }

    private boolean isOnLocalDB(boolean withoutPin) {

        String token = smartCashApplication.getToken(this);

        User user = smartCashApplication.getUser(this);
        if (!withoutPin) {
            byte[] pin = smartCashApplication.getByte(this, Keys.KEY_PASSWORD);

            return (token != null && !token.isEmpty() && user != null && pin != null);
        } else {
            return (token != null && !token.isEmpty() && user != null);
        }
    }

    private void navigateToLogin() {
        Toast.makeText(PinActivity.this, "Redirecting to login...", Toast.LENGTH_SHORT).show();
        smartCashApplication.deleteSharedPreferences(PinActivity.this);
        startActivity(new Intent(PinActivity.this, LoginActivity.class));
    }

    private void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(Keys.KEY_PIN, this.txtPin.getText().toString());
        startActivity(intent);

    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (encryptedPassword != null) {
            try {
                smartCashApplication.aead = smartCashApplication.getOrGenerateNewKeysetHandle(getApplicationContext()).getPrimitive(Aead.class);
                smartCashApplication.aead.decrypt(encryptedPassword, txtPin.getText().toString().getBytes(StandardCharsets.UTF_8));
                navigateToMain();
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        } else if (txtPin.getText().toString().equals(txtConfirmPin.getText().toString())) {

            //ENCRYPT THE PASSWORD
            try {
                setSmartCashApplication();
                byte[] plainTextToEncrypt = this.currentPassword.getBytes(StandardCharsets.UTF_8);
                byte[] pin = this.txtConfirmPin.getText().toString().getBytes(StandardCharsets.UTF_8);
                byte[] cipherTextToEncrypt = smartCashApplication.aead.encrypt(plainTextToEncrypt, pin);
                smartCashApplication.saveByte(cipherTextToEncrypt, getApplicationContext(), Keys.KEY_PASSWORD);
                navigateToMain();
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
                .setTitle("Continue WITHOUT the PIN?")
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

    @OnClick(R.id.btn_eye)
    public void onBtnEyeClicked() {
        if (isPasswordVisible) {
            txtPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            txtConfirmPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPasswordVisible = false;
            btnEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));
        } else {
            txtPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            txtConfirmPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPasswordVisible = true;
            btnEye.setImageDrawable(getResources().getDrawable(R.drawable.eye_off));
        }

        txtPin.setSelection(txtPin.getText().length());
        txtConfirmPin.setSelection(txtConfirmPin.getText().length());
    }
}
