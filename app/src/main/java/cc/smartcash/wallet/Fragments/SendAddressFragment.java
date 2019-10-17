package cc.smartcash.wallet.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    //region declarations
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
    @BindView(R.id.txt_password)
    EditText txtPassword;
    private SmartCashApplication smartCashApplication;
    @BindView(R.id.txt_amount_converted)
    EditText txtAmountCrypto;

    @BindView(R.id.send_txt_to_address)
    EditText txtToAddress;

    @BindView(R.id.amount_label)
    TextView amountLabel;
    @BindView(R.id.txt_amount)
    EditText txtAmountFiat;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.btn_eye2)
    ImageView btnEye2;

    @BindView(R.id.send_button_fragment)
    Button sendButton;

    @BindView(R.id.pin_label)
    TextView pinLabel;

    @BindView(R.id.btn_eye)
    ImageView btnEye;
    @BindView(R.id.txt_two_fa)
    EditText txtTwoFa;
    private boolean isTwoFaVisible = false;
    private BigDecimal mainFee;

    private boolean isOnline;

    public static SendAddressFragment newInstance() {
        return new SendAddressFragment();
    }
    //endregion

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.smartCashApplication = new SmartCashApplication(getContext());

        this.mainFee = BigDecimal.valueOf(0.0);

        this.isOnline = NetworkUtil.getInternetStatus(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_address, container, false);
        ButterKnife.bind(this, view);


        getPreferences();

        if (!isOnline) {
            sendButton.setText(getString(R.string.send_save_button_network_error_message));
            sendButton.setActivated(false);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EasyPermissions.requestPermissions(this, getString(R.string.send_camera_permission_label), RC_CAMERA_PERM, perms);

        if (withoutPin) {
            pinLabel.setText(getResources().getString(R.string.send_password_label));
            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH_PASSWORD)});
        }
        setFeeOnButton();
        setAmountListener();

        hideConfirmationFields();
    }

    @Override
    public void onStart() {
        super.onStart();
        getCurrentFeeAndPrices();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

            Button btnCancel = scanQrCodeView.findViewById(R.id.wallet_dialog_cancel_button);
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

        Button btnCancel = walletListView.findViewById(R.id.wallet_dialog_cancel_button);
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

    @OnClick(R.id.send_button_fragment)
    public void onViewClicked() {

        if (!isOnline) {
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

        if (Util.isNullOrEmpty(txtPassword)) {

            showConfirmationFields();

            txtPassword.setError(getString(R.string.send_pin_error_message));
            Toast.makeText(getContext(), getString(R.string.send_pin_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        if (!withoutPin) {
            password = verifyPin();
        } else {
            password = Util.getString(txtPassword);
        }

        if (!password.isEmpty()) {

            if (selectedWallet.getAddress().equalsIgnoreCase(Util.getString(txtToAddress))) {
                txtToAddress.setError(getString(R.string.send_different_address_to_send_error_message));
                Toast.makeText(getContext(), getString(R.string.send_different_address_to_send_error_message), Toast.LENGTH_LONG).show();
                return;
            }

            new isUserAvailableTask().execute();

        } else {
            //ERROR
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.send_pin_verification_dialog_title_error_message))
                    .setMessage(getString(R.string.send_pin_verification_dialog_message_error_message))
                    .setPositiveButton(getString(R.string.send_pin_verification_dialog_ok_button), (dialog, id) -> dialog.cancel()).show();
        }
    }

    private SendPayment getSendPayment() {
        Double amount = Double.parseDouble(Util.getString(txtAmountCrypto));
        Wallet selectedWallet = smartCashApplication.getWallet(getActivity());
        SendPayment sendPayment = new SendPayment();
        sendPayment.setFromAddress(selectedWallet.getAddress());
        sendPayment.setToAddress(txtToAddress.getText().toString());
        sendPayment.setAmount(amount);
        sendPayment.setEmail(this.email);
        sendPayment.setUserKey(password);

        if (!Util.isNullOrEmpty(txtTwoFa))
            sendPayment.setCode(Util.getString(txtTwoFa));

        return sendPayment;
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

    @OnClick(R.id.btn_eye2)
    public void onBtnEye2Clicked() {
        if (isPasswordVisible) {
            txtTwoFa.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isTwoFaVisible = false;
            btnEye2.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));
        } else {
            txtTwoFa.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isTwoFaVisible = true;
            btnEye2.setImageDrawable(getResources().getDrawable(R.drawable.eye_off));
        }
        txtTwoFa.setSelection(txtTwoFa.getText().length());
    }

    private void hideConfirmationFields() {

        getView().findViewById(R.id.first_card).setVisibility(View.VISIBLE);

        getView().findViewById(R.id.second_card).setVisibility(View.VISIBLE);

        getView().findViewById(R.id.pin_label).setVisibility(View.GONE);

        getView().findViewById(R.id.relativelayout).setVisibility(View.GONE);

        getView().findViewById(R.id.relativelayout2).setVisibility(View.GONE);
    }

    private void showConfirmationFields() {

        getView().findViewById(R.id.first_card).setVisibility(View.GONE);

        getView().findViewById(R.id.second_card).setVisibility(View.GONE);

        getView().findViewById(R.id.pin_label).setVisibility(View.VISIBLE);

        getView().findViewById(R.id.relativelayout).setVisibility(View.VISIBLE);

        getView().findViewById(R.id.relativelayout2).setVisibility(View.VISIBLE);


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
        txtPassword.setText("");
        setFeeOnButton(amountConverted.toString());
    }

    private void updateData() {
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getUser(smartCashApplication.getToken(getActivity()), getActivity()).observe(this, response -> {
            if (response != null) {
                Toast.makeText(getActivity(), getString(R.string.send_message_success_return), Toast.LENGTH_LONG).show();
                smartCashApplication.saveUser(getActivity(), response);
                ((MainActivity) Objects.requireNonNull(getActivity())).setWalletValue();

                navigateToTransaction();


            } else {
                Log.e(TAG, getString(R.string.send_message_error_return));
            }
        });
    }

    private void navigateToTransaction() {
        BottomNavigationView nav = getActivity().findViewById(R.id.navigationView);
        nav.setSelectedItemId(R.id.nav_trans);
        openFragment(TransactionFragment.newInstance());
    }

    private String verifyPin() {

        if (withoutPin) {
            return txtPassword.getText().toString();
        }

        return this.smartCashApplication.getDecryptedPassword(getActivity(), txtPassword.getText().toString());
    }

    private void setAmountListener() {

        Coin actualSelected = smartCashApplication.getActualSelectedCoin(getContext());

        amountLabel.setText(String.format(Locale.getDefault(), getString(R.string.send_amount_in_coin_label), actualSelected.getName()));

        txtAmountFiat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                amountLabel.setText(String.format(Locale.getDefault(), getString(R.string.send_amount_in_coin_label), actualSelected.getName()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtAmountFiat.isFocused())
                    calculateFromFiatToSmart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                amountLabel.setText(String.format(Locale.getDefault(), getString(R.string.send_amount_in_coin_label), actualSelected.getName()));
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
            setFeeOnButton(amountWithFee.toString());
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
            setFeeOnButton(amountWithFee.toString());
        }
    }

    private void getCurrentFeeAndPrices() {
        if (isOnline) {
            new FeeAndTask().execute();
        }
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

    private void setFeeOnButton(String fee) {
        sendButton.setText(getString(R.string.send_button_label).replace("%f", smartCashApplication.formatNumberByDefaultCrypto(Double.parseDouble(fee))));
    }

    private void setFeeOnButton() {
        setFeeOnButton(mainFee.toString());
    }

    private void sendByWebWallet() {
        new SmartSendTask().execute(getSendPayment());
    }

    private void sendBySmartTextOrder() {
        if (Util.isValidEmail(Util.getString(txtToAddress)) || PhoneNumberUtils.isGlobalPhoneNumber(Util.getString(txtToAddress))) {
            //If we have a valid email or a phone number, then try to send by SmartText
            new SmartTextOrderTask().execute();
        } else {
            //if not, we still have the possibility of a valid SmartAddress, so try web wallet
            sendByWebWallet();
        }
    }

    private class FeeAndTask extends AsyncTask<Void, Integer, WebWalletRootResponse<Double>> {

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
            WebWalletRootResponse<Double> fee = WalletViewModel.getSyncFee(getContext(), smartCashApplication.getToken(getActivity()), sendPayment);

            Date datePriceWasUpdated = Util.getDate(SmartCashApplication.getString(getContext(), KEYS.KEY_TIME_PRICE_WAS_UPDATED));
            Log.d(TAG, "Date price was updated: " + datePriceWasUpdated);
            if (Util.dateDiffFromNow(datePriceWasUpdated) > 60 || coins == null) {
                coins = CurrentPriceViewModel.getSyncPrices(getContext());
                SmartCashApplication.saveString(getContext(), Util.getDate(), KEYS.KEY_TIME_PRICE_WAS_UPDATED);
                smartCashApplication.saveCurrentPrice(getContext(), coins);
            }
            return fee;

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
                    setFeeOnButton();
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
            if (smartTextRoot != null && smartTextRoot.getData() != null) {

                    SmartTextData data = smartTextRoot.getData();
                    SendPayment sendPayment = new SendPayment();
                    sendPayment.setAmount(data.getAmountSmartWithFee());
                    sendPayment.setFromAddress(data.getAddressRefunded());
                    sendPayment.setToAddress(data.getGeneratedAddress());
                    sendPayment.setEmail(email);
                    sendPayment.setUserKey(password);

                if (!Util.isNullOrEmpty(txtTwoFa))
                    sendPayment.setCode(Util.getString(txtTwoFa));

                    new SmartSendTask().execute(sendPayment);
                    StringBuilder order = new StringBuilder();
                    order.append("https://smartext.me/Order/" + data.getOrderID());
                    Log.d(TAG, order.toString());

            } else {
                Toast.makeText(getContext(), getString(R.string.send_web_wallet_error_message), Toast.LENGTH_LONG).show();
            }
            unlockSendButton();
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
            if (result == null) {
                Toast.makeText(getContext(), getString(R.string.send_web_wallet_error_message), Toast.LENGTH_LONG).show();
                unlockSendButton();
                return;
            }
            if (result.getValid() != null && !result.getValid()) {
                Toast.makeText(getContext(), getString(R.string.send_web_wallet_error_message), Toast.LENGTH_LONG).show();
                unlockSendButton();
                return;
            }
            if (!Util.isNullOrEmpty(result.getError())) {
                Toast.makeText(getContext(), result.getError(), Toast.LENGTH_LONG).show();
                unlockSendButton();
                return;
            }
            if (!Util.isNullOrEmpty(result.getErrorDescription())) {
                Toast.makeText(getContext(), result.getErrorDescription(), Toast.LENGTH_LONG).show();
                unlockSendButton();
                return;
            }
            if (result != null) {
                if (result.getData() != null) {
                    updateData();
                    clearInputs();
                    Log.d(TAG, result.getData());
                } else {
                    Toast.makeText(getContext(), getString(R.string.send_web_wallet_error_message), Toast.LENGTH_LONG).show();
                }
                unlockSendButton();
            } else {
                unlockSendButton();
            }
        }
    }

    private class isUserAvailableTask extends AsyncTask<Void, Integer, WebWalletRootResponse<Boolean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockSendButton();
        }

        @Override
        protected WebWalletRootResponse<Boolean> doInBackground(Void... users) {
            return WalletViewModel.isUserAvailable(Util.getString(txtToAddress));
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (!Util.isTaskComplete(progress[0])) lockSendButton(); //percentage of the progress
        }

        @Override
        protected void onPostExecute(WebWalletRootResponse<Boolean> booleanWebWalletRootResponse) {
            super.onPostExecute(booleanWebWalletRootResponse);
            if (booleanWebWalletRootResponse != null && booleanWebWalletRootResponse.getData()) {
                //SEND BY EMAIL/SMS/LINK
                sendBySmartTextOrder();
            } else {
                //SEND TO WEB WALLET
                sendByWebWallet();
            }
        }
    }
}

