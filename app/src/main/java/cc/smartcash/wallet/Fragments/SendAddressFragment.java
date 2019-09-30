package cc.smartcash.wallet.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;
import cc.smartcash.wallet.ViewModels.UserViewModel;
import cc.smartcash.wallet.ViewModels.WalletViewModel;
import pub.devrel.easypermissions.EasyPermissions;

public class SendAddressFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    public static final String TAG = SendAddressFragment.class.getSimpleName();

    private static final int RC_CAMERA_PERM = 123;

    private ArrayList<Wallet> walletList;
    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CAMERA};
    private String token;
    private String email;
    private ArrayList<Coin> coins;
    private AlertDialog dialog;
    private BigDecimal amountConverted = BigDecimal.valueOf(0.0);
    private boolean withoutPin;
    private boolean isPasswordVisible = false;
    private SmartCashApplication smartCashApplication;

    @BindView(R.id.txt_to_address)
    EditText txtToAddress;

    @BindView(R.id.amount_label)
    TextView amountLabel;

    @BindView(R.id.txt_amount_converted)
    EditText txtAmountConverted;

    @BindView(R.id.txt_amount)
    EditText txtAmount;

    @BindView(R.id.txt_password)
    EditText txtPin;

    @BindView(R.id.send_button)
    Button sendButton;

    @BindView(R.id.pin_label)
    TextView pinLabel;

    @BindView(R.id.btn_eye)
    ImageView btnEye;

    public static SendAddressFragment newInstance() {
        return new SendAddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_address, container, false);
        ButterKnife.bind(this, view);

        this.smartCashApplication = new SmartCashApplication(getContext());
        this.token = smartCashApplication.getToken(getContext());
        this.email = smartCashApplication.getUser(getContext()).getEmail();
        this.walletList = smartCashApplication.getUser(getContext()).getWallet();
        this.withoutPin = smartCashApplication.getBoolean(getContext(), Keys.KEY_WITHOUT_PIN);
        this.coins = smartCashApplication.getCurrentPrice(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EasyPermissions.requestPermissions(this, "Need access to your camera",
                RC_CAMERA_PERM, perms);

        sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button, amountConverted));

        if (withoutPin) {
            pinLabel.setText(getResources().getString(R.string.password_label));
            txtPin.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtPin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        }

        if (NetworkUtil.getInternetStatus(getContext())) {
            CurrentPriceViewModel model = ViewModelProviders.of(this).get(CurrentPriceViewModel.class);
            model.getCurrentPrices(getActivity()).observe(this, currentPrices -> {
                if (currentPrices != null) {
                    coins = SmartCashApplication.convertToArrayList(currentPrices);
                } else {
                    Log.e(getContext().getString(R.string.tag_log_error), "Error to get current prices.");
                }
            });
        } else {
            sendButton.setText("You are off line!");
            sendButton.setActivated(false);
        }

        setAmountListener();
    }

    private void setAmountListener() {

        Coin actualSelected = smartCashApplication.getActualSelectedCoin(getContext());
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        txtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtAmount.isFocused())
                    calculateFromFiatToSmart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));
            }
        });


        txtAmountConverted.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtAmountConverted.isFocused())
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
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (txtAmount.getText().toString().isEmpty()) {
            txtAmountConverted.setText("0");
        }

        if (!txtAmount.getText().toString().isEmpty()) {

            if (actualSelected.getName().equals("SMART")) {
                amountConverted = smartCashApplication.converterBigDecimal(BigDecimal.valueOf(Double.parseDouble(txtAmount.getText().toString())), BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", "SMART"));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmount.getText().toString());
                double ruleOfThree = amountInTheField / currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmountConverted.setText(String.valueOf(amountConverted));

            BigDecimal amountWithFee = BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString())).add(BigDecimal.valueOf(0.001));
            sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button, amountWithFee));

        }

    }

    private void calculateFromSmartToFiat() {

        BigDecimal amountConverted;
        Coin actualSelected = smartCashApplication.getActualSelectedCoin(getContext());
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (txtAmountConverted.getText().toString().isEmpty()) {
            txtAmount.setText("0");
        }

        if (!txtAmountConverted.getText().toString().isEmpty()) {

            if (actualSelected.getName().equals("SMART")) {
                amountConverted = smartCashApplication.converterBigDecimal(BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString())), BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", "SMART"));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmountConverted.getText().toString());
                double ruleOfThree = amountInTheField * currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmount.setText(String.valueOf(amountConverted));

            BigDecimal amountWithFee = BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString())).add(BigDecimal.valueOf(0.001));


            sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button, amountWithFee));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
            EasyPermissions.requestPermissions(this, "Need access to your camera",
                    RC_CAMERA_PERM, perms);
        }
    }

    @OnClick(R.id.btn_wallet)
    public void onBtnWalletClicked() {
        AlertDialog.Builder walletListDialog = new AlertDialog.Builder(getContext());
        View walletListView = LayoutInflater.from(getContext()).inflate(R.layout.show_wallets_dialog, null);

        Button btnCancel = walletListView.findViewById(R.id.btn_cancel);
        RecyclerView recycler = walletListView.findViewById(R.id.wallet_list);
        TextView title = walletListView.findViewById(R.id.wallet_dialog_title);

        title.setText(String.format(Locale.getDefault(), "%s Wallets", this.walletList.size()));

        walletListDialog.setView(walletListView);
        AlertDialog dialog = walletListDialog.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setupRecyclerViewWallets(recycler, dialog);

        btnCancel.setOnClickListener(v1 -> dialog.hide());

        dialog.show();
    }

    private void setupRecyclerViewWallets(RecyclerView recyclerView, AlertDialog dialog) {
        LinearLayoutManager linearLayoutManagerTransactions = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        WalletDialogAdapter walletAdapter = new WalletDialogAdapter(getContext(), new ArrayList<>(), this.txtToAddress, dialog);

        recyclerView.setLayoutManager(linearLayoutManagerTransactions);
        recyclerView.setAdapter(walletAdapter);

        walletAdapter.setItems(walletList);
    }

    @OnClick(R.id.send_button)
    public void onViewClicked() {

        if (!NetworkUtil.getInternetStatus(getActivity())) {
            Toast.makeText(getActivity(), "You must be on-line to send.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (txtToAddress.getText().length() == 0) {
            Toast.makeText(getContext(), "The address to send can't be empty.", Toast.LENGTH_LONG).show();
            return;
        } else if (txtAmount.getText().length() == 0 || txtAmount.getText().toString().equals("0")) {
            Toast.makeText(getContext(), "The amount to send can't be empty or ZERO.", Toast.LENGTH_LONG).show();
            return;
        } else if (txtAmountConverted.getText().length() == 0 || txtAmountConverted.getText().toString().equals("0")) {
            Toast.makeText(getContext(), "The amount to send can't be empty or ZERO.", Toast.LENGTH_LONG).show();
            return;
        } else if (txtPin.getText().length() == 0) {
            Toast.makeText(getContext(), "The PIN can't be empty.", Toast.LENGTH_LONG).show();
            return;
        }

        Double amount = Double.parseDouble(txtAmountConverted.getText().toString());
        Wallet selectedWallet = smartCashApplication.getWallet(getActivity());

        if (selectedWallet.getBalance() < amount) {
            Toast.makeText(getContext(), "The amount MUST be less than the current balance.", Toast.LENGTH_LONG).show();
            return;
        }
        if (amount == 0) {
            Toast.makeText(getContext(), "The amount MUST be greater than ZERO.", Toast.LENGTH_LONG).show();
            return;
        }

        String password;

        if (!withoutPin) {
            password = verifyPin();
        } else {
            password = txtPin.getText().toString();
        }

        if (!password.equals("")) {

            if (selectedWallet.getAddress().equalsIgnoreCase(txtToAddress.getText().toString())) {
                Toast.makeText(getContext(), "The address from MUST be different of the destination address.", Toast.LENGTH_LONG).show();
                return;
            }

            SendPayment sendPayment = new SendPayment();
            sendPayment.setFromAddress(selectedWallet.getAddress());
            sendPayment.setToAddress(txtToAddress.getText().toString());
            sendPayment.setAmount(amount);
            sendPayment.setCode("");
            sendPayment.setEmail(this.email);
            sendPayment.setUserKey(password);

            WalletViewModel model = ViewModelProviders.of(this).get(WalletViewModel.class);
            model.sendPayment(getActivity(), token, sendPayment).observe(this, apiResponse -> {
                if (apiResponse != null) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("SmartCash sent.")
                            .setMessage("Your SmartCash was sent with success.")
                            .setPositiveButton("OK", (dialog, id) -> {
                                Log.d(getResources().getString(R.string.tag_log_success), "Success");
                                updateData();
                                clearInputs();
                                Fragment transactionFragment = TransactionFragment.newInstance();
                                openFragment(transactionFragment);
                                dialog.cancel();
                            }).show();

                } else {
                    Log.e(getResources().getString(R.string.tag_log_error), "Error to send the payment.");
                }
            });
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void clearInputs() {
        txtToAddress.setText("");
        txtAmount.setText("");
        txtAmountConverted.setText(String.valueOf(0.0));
        txtPin.setText("");
        sendButton.setText(Objects.requireNonNull(getContext()).getResources().getString(R.string.send_button, amountConverted));
    }

    private void updateData() {
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getUser(smartCashApplication.getToken(getActivity()), getActivity()).observe(this, response -> {
            if (response != null) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                smartCashApplication.saveUser(getActivity(), response);
                ((MainActivity) Objects.requireNonNull(getActivity())).setWalletValue();
            } else {
                Log.e(getResources().getString(R.string.tag_log_error), "Error to get the user.");
            }
        });
    }

    private String verifyPin() {

        if (withoutPin) {
            return txtPin.getText().toString();
        }

        return this.smartCashApplication.getDecryptedPassword(getActivity(), txtPin.getText().toString());
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        if (text == null || text.isEmpty()) return;

        String parsedQr = Util.parseQrCode(text);

        if (parsedQr == null) return;
        if (parsedQr.length() == 0) return;

        if (parsedQr.indexOf("-") == -1) {
            txtToAddress.setText(text);
            txtAmount.setText(0);
            txtAmountConverted.setText(0);
        } else {
            String[] parts = parsedQr.split("-");
            txtToAddress.setText(parts[0]);
            txtAmountConverted.setText(parts[1]);
        }
        calculateFromSmartToFiat();

        dialog.hide();
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
}

