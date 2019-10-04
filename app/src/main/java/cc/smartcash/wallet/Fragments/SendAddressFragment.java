package cc.smartcash.wallet.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Activities.MainActivity;
import cc.smartcash.wallet.Adapters.WalletDialogAdapter;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.SendPayment;
import cc.smartcash.wallet.Models.SmartTextData;
import cc.smartcash.wallet.Models.SmartTextRequest;
import cc.smartcash.wallet.Models.SmartTextRoot;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.KEYS;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;
import cc.smartcash.wallet.ViewModels.UserViewModel;
import cc.smartcash.wallet.ViewModels.WalletViewModel;
import pub.devrel.easypermissions.EasyPermissions;

public class SendAddressFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    public static final String TAG = SendAddressFragment.class.getSimpleName();
    public static final String ZERO = "0";
    public static final String QR_SEPARATOR = "-";
    private static final int RC_CAMERA_PERM = 123;
    private static final int MAX_LENGTH_PASSWORD = 123;

    private ArrayList<Wallet> walletList;
    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CAMERA};
    private String token;
    private String email;
    private ArrayList<Coin> coins;
    private AlertDialog dialog;
    private BigDecimal amountConverted = BigDecimal.valueOf(0.0);
    String password;
    private boolean withoutPin;
    private boolean isPasswordVisible = false;
    private SmartCashApplication smartCashApplication;
    @BindView(R.id.txt_amount_converted)
    EditText txtAmountCrypto;

    @BindView(R.id.txt_to_address)
    EditText txtToAddress;

    @BindView(R.id.amount_label)
    TextView amountLabel;
    @BindView(R.id.txt_amount)
    EditText txtAmountFiat;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.txt_password)
    EditText txtPin;

    @BindView(R.id.send_button)
    Button sendButton;

    @BindView(R.id.pin_label)
    TextView pinLabel;

    @BindView(R.id.btn_eye)
    ImageView btnEye;
    private BigDecimal mainFee = BigDecimal.valueOf(0.0);

    public static SendAddressFragment newInstance() {
        return new SendAddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_address, container, false);
        ButterKnife.bind(this, view);

        this.smartCashApplication = new SmartCashApplication(getContext());
        if (NetworkUtil.getInternetStatus(getContext())) {
            getCurrentPrices();
        } else {
            sendButton.setText(getString(R.string.send_save_button_network_error_message));
            sendButton.setActivated(false);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EasyPermissions.requestPermissions(this, getString(R.string.send_camera_permission_label), RC_CAMERA_PERM, perms);
        setFeeOnButton();
        if (withoutPin) {
            pinLabel.setText(getResources().getString(R.string.send_password_label));
            txtPin.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtPin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH_PASSWORD)});
        }
        setAmountListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferences();
        new FeeTask().execute();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        if (text == null || text.isEmpty()) return;

        String parsedQr = Util.parseQrCode(text);

        if (parsedQr == null) return;
        if (parsedQr.length() == 0) return;

        if (parsedQr.indexOf(QR_SEPARATOR) == -1) {
            txtToAddress.setText(text);
            txtAmountFiat.setText(ZERO);
            txtAmountCrypto.setText(ZERO);
        } else {
            String[] parts = parsedQr.split(QR_SEPARATOR);
            txtToAddress.setText(parts[0]);
            txtAmountCrypto.setText(parts[1]);
        }
        calculateFromSmartToFiat();

        dialog.hide();
    }

    @OnClick(R.id.btn_qr_code)
    public void onBtnQrCodeClicked() {

        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getActivity()), perms)) {
            AlertDialog.Builder scanQrCodeDialog = new AlertDialog.Builder(getContext());
            View scanQrCodeView = LayoutInflater.from(getContext()).inflate(R.layout.scan_qrcode_dialog, null);

            Button btnCancel = scanQrCodeView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(v1 -> {
                dialog.dismiss();
                dialog.hide();
            });

            QRCodeReaderView qrCodeLayout = scanQrCodeView.findViewById(R.id.qr_code_scan);
            qrCodeLayout.setBackCamera();
            qrCodeLayout.startCamera();
            qrCodeLayout.setOnQRCodeReadListener(this);

            scanQrCodeDialog.setView(scanQrCodeView);

            dialog = scanQrCodeDialog.create();

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.send_camera_permission_label), RC_CAMERA_PERM, perms);
        }
    }

    @OnClick(R.id.btn_wallet)
    public void onBtnWalletClicked() {
        AlertDialog.Builder walletListDialog = new AlertDialog.Builder(getContext());
        View walletListView = LayoutInflater.from(getContext()).inflate(R.layout.show_wallets_dialog, null);

        Button btnCancel = walletListView.findViewById(R.id.btn_cancel);
        RecyclerView recycler = walletListView.findViewById(R.id.wallet_list);
        TextView title = walletListView.findViewById(R.id.wallet_dialog_title);

        title.setText(getString(R.string.send_wallet_dialog_title_label).replace("%s", String.valueOf(this.walletList.size())));

        walletListDialog.setView(walletListView);
        AlertDialog dialog = walletListDialog.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setupRecyclerViewWallets(recycler, dialog);

        btnCancel.setOnClickListener(v1 -> dialog.hide());

        dialog.show();
    }

    @OnClick(R.id.send_button)
    public void onViewClicked() {

        if (!NetworkUtil.getInternetStatus(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.send_save_button_network_error_message), Toast.LENGTH_SHORT).show();
            return;
        }
        if (Util.isNullOrEmpty(txtToAddress)) {
            txtToAddress.setError(getString(R.string.send_address_error_message));
            Toast.makeText(getContext(), getString(R.string.send_address_error_message), Toast.LENGTH_LONG).show();
            return;
        } else if (Util.isNullOrEmpty(txtAmountFiat) || Util.getString(txtAmountFiat).equals(ZERO)) {
            txtAmountFiat.setError(getString(R.string.send_amount_fiat_error_message));
            Toast.makeText(getContext(), getString(R.string.send_amount_fiat_error_message), Toast.LENGTH_LONG).show();
            return;
        } else if (Util.isNullOrEmpty(txtAmountCrypto) || Util.getString(txtAmountCrypto).equals(ZERO)) {
            txtAmountCrypto.setError(getString(R.string.send_amount_crypto_error_message));
            Toast.makeText(getContext(), getString(R.string.send_amount_crypto_error_message), Toast.LENGTH_LONG).show();
            return;
        } else if (Util.isNullOrEmpty(txtPin)) {
            txtPin.setError(getString(R.string.send_pin_error_message));
            Toast.makeText(getContext(), getString(R.string.send_pin_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        Double amount = Double.parseDouble(Util.getString(txtAmountCrypto));
        Wallet selectedWallet = smartCashApplication.getWallet(getActivity());

        if (selectedWallet.getBalance() < amount) {
            Toast.makeText(getContext(), getString(R.string.send_insufficient_balance_error_message), Toast.LENGTH_LONG).show();
            return;
        }
        if (amount == 0) {
            Toast.makeText(getContext(), getString(R.string.send_min_amount_to_send_error_message), Toast.LENGTH_LONG).show();
            return;
        }


        if (!withoutPin) {
            password = verifyPin();
        } else {
            password = Util.getString(txtPin);
        }

        if (!password.isEmpty()) {

            if (selectedWallet.getAddress().equalsIgnoreCase(Util.getString(txtToAddress))) {
                txtToAddress.setError(getString(R.string.send_different_address_to_send_error_message));
                Toast.makeText(getContext(), getString(R.string.send_different_address_to_send_error_message), Toast.LENGTH_LONG).show();
                return;
            }

            SendPayment sendPayment = new SendPayment();
            sendPayment.setFromAddress(selectedWallet.getAddress());
            sendPayment.setToAddress(txtToAddress.getText().toString());
            sendPayment.setAmount(amount);
            sendPayment.setEmail(this.email);
            sendPayment.setUserKey(password);

            if (PhoneNumberUtils.isGlobalPhoneNumber(Util.getString(txtToAddress))) {
                new SmartTextOrderTask().execute();
            } else if (Util.isValidEmail(Util.getString(txtToAddress))) {
                new SmartSendTask().execute(sendPayment);
            } else {
                new SmartSendTask().execute(sendPayment);
            }
        } else {
            //ERROR
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.send_pin_verification_dialog_title_error_message))
                    .setMessage(getString(R.string.send_pin_verification_dialog_message_error_message))
                    .setPositiveButton(getString(R.string.send_pin_verification_dialog_ok_button), (dialog, id) -> {
                        dialog.cancel();
                    }).show();
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick(R.id.btn_eye)
    public void onBtnEyeClicked() {
        if (isPasswordVisible) {
            txtPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPasswordVisible = false;
            btnEye.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));
        } else {
            txtPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPasswordVisible = true;
            btnEye.setImageDrawable(getResources().getDrawable(R.drawable.eye_off));
        }

        txtPin.setSelection(txtPin.getText().length());
    }

    private void getPreferences() {

        this.token = smartCashApplication.getToken(getContext());
        this.email = smartCashApplication.getUser(getContext()).getEmail();
        this.walletList = smartCashApplication.getUser(getContext()).getWallet();
        this.withoutPin = smartCashApplication.getBoolean(getContext(), KEYS.KEY_WITHOUT_PIN);
        this.coins = smartCashApplication.getCurrentPrice(getContext());
    }

    private void setupRecyclerViewWallets(RecyclerView recyclerView, AlertDialog dialog) {
        LinearLayoutManager linearLayoutManagerTransactions = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        WalletDialogAdapter walletAdapter = new WalletDialogAdapter(getContext(), new ArrayList<>(), this.txtToAddress, dialog);

        recyclerView.setLayoutManager(linearLayoutManagerTransactions);
        recyclerView.setAdapter(walletAdapter);

        walletAdapter.setItems(walletList);
    }

    private void clearInputs() {
        txtToAddress.setText("");
        txtAmountFiat.setText("");
        txtAmountCrypto.setText(String.valueOf(0.0));
        txtPin.setText("");
        sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button_label, amountConverted));
    }

    private void updateData() {
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getUser(smartCashApplication.getToken(getActivity()), getActivity()).observe(this, response -> {
            if (response != null) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                smartCashApplication.saveUser(getActivity(), response);
                ((MainActivity) Objects.requireNonNull(getActivity())).setWalletValue();
            } else {
                Log.e(TAG, "Error to get the user.");
            }
        });
    }

    private String verifyPin() {

        if (withoutPin) {
            return txtPin.getText().toString();
        }

        return this.smartCashApplication.getDecryptedPassword(getActivity(), txtPin.getText().toString());
    }

    private void setAmountListener() {

        Coin actualSelected = smartCashApplication.getActualSelectedCoin(getContext());
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        txtAmountFiat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtAmountFiat.isFocused())
                    calculateFromFiatToSmart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));
            }
        });


        txtAmountCrypto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtAmountCrypto.isFocused())
                    calculateFromSmartToFiat();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void calculateFromFiatToSmart() {
        BigDecimal amountConverted;
        Coin actualSelected = smartCashApplication.getActualSelectedCoin(getContext());
        amountLabel.setText(Util.amountInCoinConcatenation(getContext(), actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (Util.isNullOrEmpty(txtAmountFiat)) {
            txtAmountCrypto.setText(ZERO);
        } else {
            if (actualSelected.getName().equals(getString(R.string.default_crypto))) {
                amountConverted = smartCashApplication.multiplyBigDecimals(Util.getBigDecimal(txtAmountFiat), BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(Util.amountInDefaultCryptoConcatenation(getContext()));
            } else {
                double currentPrice = actualSelected.getValue();
                double amountInTheField = Util.getDouble(txtAmountFiat);
                double ruleOfThree = amountInTheField / currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }
            txtAmountCrypto.setText(String.valueOf(amountConverted));
            BigDecimal amountWithFee = Util.getBigDecimal(txtAmountCrypto).add(mainFee);
            sendButton.setText(getString(R.string.send_button_label, amountWithFee));
        }
    }

    private void calculateFromSmartToFiat() {

        BigDecimal amountConverted;
        Coin actualSelected = smartCashApplication.getActualSelectedCoin(getContext());
        amountLabel.setText(Util.amountInCoinConcatenation(getContext(), actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (Util.isNullOrEmpty(txtAmountCrypto)) {
            txtAmountFiat.setText(ZERO);
        } else {
            if (actualSelected.getName().equals(getString(R.string.default_crypto))) {
                amountConverted = smartCashApplication.multiplyBigDecimals(Util.getBigDecimal(txtAmountCrypto), BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(Util.amountInDefaultCryptoConcatenation(getContext()));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Util.getDouble(txtAmountCrypto);
                double ruleOfThree = amountInTheField * currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }
            txtAmountFiat.setText(String.valueOf(amountConverted));
            BigDecimal amountWithFee = Util.getBigDecimal(txtAmountCrypto).add(mainFee);
            sendButton.setText(getString(R.string.send_button_label, amountWithFee));
        }
    }

    private void getCurrentPrices() {
        new PriceTask().execute();
    }

    private void lockSendButton() {
        sendButton.setEnabled(false);
        sendButton.setText(getString(R.string.send_save_button_loading_status));
        loader.setVisibility(View.VISIBLE);
    }

    private void unlockSendButton() {
        sendButton.setEnabled(true);
        setFeeOnButton();
        loader.setVisibility(View.GONE);
    }

    private void setFeeOnButton() {
        sendButton.setText(getString(R.string.send_button_label).replace("%f", mainFee.toString()));
    }

    private class PriceTask extends AsyncTask<Void, Integer, ArrayList<Coin>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockSendButton();
        }

        @Override
        protected ArrayList<Coin> doInBackground(Void... users) {
            return CurrentPriceViewModel.getSyncPrices(getContext());
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockSendButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(ArrayList<Coin> coins) {
            super.onPostExecute(coins);
            if (coins != null) {
                smartCashApplication.saveCurrentPrice(getContext(), coins);
                unlockSendButton();
            } else {
                unlockSendButton();
            }
        }
    }

    private class FeeTask extends AsyncTask<Void, Integer, WebWalletRootResponse<Double>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockSendButton();
        }

        @Override
        protected WebWalletRootResponse<Double> doInBackground(Void... users) {

            SendPayment sendPayment = new SendPayment();
            sendPayment.setFromAddress(smartCashApplication.getWallet(getActivity()).getAddress());
            sendPayment.setToAddress(Util.getString(txtToAddress));

            return WalletViewModel.getSyncFee(getContext(), smartCashApplication.getToken(getActivity()), sendPayment);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockSendButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(WebWalletRootResponse<Double> fee) {
            super.onPostExecute(fee);

            if (fee != null) {
                if (fee.getData() != null) {
                    mainFee = BigDecimal.valueOf(fee.getData());
                    sendButton.setText(getString(R.string.send_button_label, mainFee));
                    Log.d(TAG, fee.getData().toString());
                } else if (!fee.getError().isEmpty()) {
                    Log.e(TAG, fee.getError());
                }
                unlockSendButton();
            } else {
                unlockSendButton();
            }
        }
    }

    private class SmartTextOrderTask extends AsyncTask<SmartTextRequest, Integer, SmartTextRoot> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockSendButton();
        }

        @Override
        protected SmartTextRoot doInBackground(SmartTextRequest... users) {

            SmartTextRequest sendPayment = new SmartTextRequest();

            sendPayment.setAddressRefunded(smartCashApplication.getWallet(getActivity()).getAddress());
            sendPayment.setAmountSmart(Util.getString(txtAmountCrypto));

            if (Util.isValidEmail(Util.getString(txtToAddress))) {
                sendPayment.setDestinationEmail(Util.getString(txtToAddress));
                sendPayment.setTypeSend(KEYS.KEY_SMARTTEXT_EMAIL);
            } else if (PhoneNumberUtils.isGlobalPhoneNumber(Util.getString(txtToAddress))) {
                sendPayment.setPhoneNumber(Util.getString(txtToAddress));
                sendPayment.setTypeSend(KEYS.KEY_SMARTTEXT_SMS);
            } else {
                sendPayment.setTypeSend(KEYS.KEY_SMARTTEXT_LINK);
            }

            return WalletViewModel.sendSyncSmartText(getContext(), smartCashApplication.getToken(getActivity()), sendPayment);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockSendButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(SmartTextRoot smartTextRoot) {
            super.onPostExecute(smartTextRoot);
            if (smartTextRoot != null) {
                if (smartTextRoot.getData() != null) {

                    SmartTextData data = smartTextRoot.getData();
                    SendPayment sendPayment = new SendPayment();
                    sendPayment.setAmount(data.getAmountSmartWithFee());
                    sendPayment.setFromAddress(data.getAddressRefunded());
                    sendPayment.setToAddress(data.getGeneratedAddress());
                    sendPayment.setEmail(email);
                    sendPayment.setUserKey(password);

                    new SmartSendTask().execute(sendPayment);
                    StringBuilder order = new StringBuilder();
                    order.append("https://smartext.me/Order/" + data.getOrderID());
                    Log.d(TAG, order.toString());
                }
                unlockSendButton();
            } else {
                unlockSendButton();
            }
        }
    }

    private class SmartSendTask extends AsyncTask<SendPayment, Integer, WebWalletRootResponse<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockSendButton();
        }

        @Override
        protected WebWalletRootResponse<String> doInBackground(SendPayment... sendPayments) {
            return WalletViewModel.sendSyncTransaction(getContext(), smartCashApplication.getToken(getActivity()), sendPayments[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockSendButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(WebWalletRootResponse<String> result) {
            super.onPostExecute(result);
            if (result != null) {
                if (result.getData() != null) {
                    updateData();
                    clearInputs();
                    Log.d(TAG, result.getData());
                }
                unlockSendButton();
            } else {
                if (Util.isValidEmail(Util.getString(txtToAddress))) {
                    new SmartTextOrderTask().execute();
                }
                unlockSendButton();
            }
        }
    }
}

