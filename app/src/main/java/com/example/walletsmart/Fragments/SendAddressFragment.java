package com.example.walletsmart.Fragments;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.walletsmart.Activities.MainActivity;
import com.example.walletsmart.Adapters.CoinSpinnerAdapter;
import com.example.walletsmart.Adapters.WalletDialogAdapter;
import com.example.walletsmart.Models.Coin;
import com.example.walletsmart.Models.SendPayment;
import com.example.walletsmart.Models.Wallet;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.DeCryptor;
import com.example.walletsmart.Utils.Utils;
import com.example.walletsmart.ViewModels.CurrentPriceViewModel;
import com.example.walletsmart.ViewModels.UserViewModel;
import com.example.walletsmart.ViewModels.WalletViewModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SendAddressFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {
    @BindView(R.id.txt_to_address)
    EditText txtToAddress;
    @BindView(R.id.currency_spinner)
    Spinner currencySpinner;
    @BindView(R.id.amount_label)
    TextView amountLabel;
    @BindView(R.id.txt_amount_converted)
    EditText txtAmountConverted;
    @BindView(R.id.txt_amount)
    EditText txtAmount;
    @BindView(R.id.txt_pin)
    EditText txtPin;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.pin_label)
    TextView pinLabel;
    @BindView(R.id.btn_eye)
    ImageView btnEye;
    private WalletDialogAdapter walletAdapter;
    private ArrayList<Wallet> walletList;
    private Utils utils;
    private String token;
    private String email;
    private Wallet selectedWallet;
    private String currentPrices;
    private ArrayList<Coin> coins;
    private CoinSpinnerAdapter adapter;
    private Coin actualSelected;
    private static final int RC_CAMERA_PERM = 123;
    private AlertDialog dialog;
    private BigDecimal amountConverted = BigDecimal.valueOf(0.0);
    private DeCryptor decryptor;
    private static final String PIN_ALIAS = "AndroidKeyStorePin";
    private static final String PASSWORD_ALIAS = "AndroidKeyStorePassword";
    private boolean withoutPin;
    private boolean isPasswordVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_address, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static SendAddressFragment newInstance() {
        return new SendAddressFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAmountListener();
        txtAmountConverted.setEnabled(false);
        utils = new Utils();
        token = utils.getToken(getActivity());
        email = utils.getUser(getActivity()).getEmail();
        walletList = utils.getUser(getActivity()).getWallet();
        coins = new ArrayList();
        sendButton.setText(getContext().getResources().getString(R.string.send_button, amountConverted));

        withoutPin = utils.getBoolean(getActivity(), "WithoutPin");

        if (withoutPin) {
            pinLabel.setText(getResources().getString(R.string.password_label));
            txtPin.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtPin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        }

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }

        CurrentPriceViewModel model = ViewModelProviders.of(this).get(CurrentPriceViewModel.class);

        model.getCurrentPrices(getActivity()).observe(this, currentPrices -> {
            if (currentPrices != null) {
                SendAddressFragment.this.currentPrices = currentPrices;
                coins = Utils.convertToArrayList(currentPrices);
                setupCoinSpinner();
            } else {
                Log.e("Erro", "Erro ao buscar os valores!");
            }
        });
    }

    private void setAmountListener() {
        txtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtAmount.getText().toString().isEmpty()) {
                    return;
                } else {
                    BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtAmount.getText().toString()));
                    BigDecimal tax = BigDecimal.valueOf(0.001);
                    BigDecimal finalValue = amount.add(tax);
                    amountConverted = utils.converterBigDecimal(finalValue, BigDecimal.valueOf(actualSelected.getValue()));
                    txtAmountConverted.setText(String.valueOf(amountConverted));
                    sendButton.setText(getContext().getResources().getString(R.string.send_button, amountConverted));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setupCoinSpinner() {
        adapter = new CoinSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, coins);
        currencySpinner.setAdapter(adapter);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Coin coin = adapter.getItem(position);
                actualSelected = coin;
                amountLabel.setText("Amount in " + coin.getName() + ":");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @OnClick(R.id.btn_qr_code)
    public void onBtnQrCodeClicked() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            AlertDialog.Builder scanQrCodeDialog = new AlertDialog.Builder(getContext());
            View scanQrCodeView = LayoutInflater.from(getContext()).inflate(R.layout.scan_qrcode_dialog, null);

            Button btnCancel = scanQrCodeView.findViewById(R.id.btn_cancel);
            QRCodeReaderView qrCodeLayout = scanQrCodeView.findViewById(R.id.qr_code_scan);
            qrCodeLayout.setBackCamera();
            qrCodeLayout.startCamera();
            qrCodeLayout.setOnQRCodeReadListener(this);

            scanQrCodeDialog.setView(scanQrCodeView);
            dialog = scanQrCodeDialog.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            btnCancel.setOnClickListener(v1 -> {
                dialog.hide();
            });

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

        title.setText(String.valueOf(this.walletList.size() + " Wallets"));

        walletListDialog.setView(walletListView);
        AlertDialog dialog = walletListDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setupRecyclerViewWallets(recycler, dialog);

        btnCancel.setOnClickListener(v1 -> {
            dialog.hide();
        });

        dialog.show();
    }

    private void setupRecyclerViewWallets(RecyclerView recyclerView, AlertDialog dialog) {
        RecyclerView recyclerViewWallets = recyclerView;
        LinearLayoutManager linearLayoutManagerTransactions = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        walletAdapter = new WalletDialogAdapter(getContext(), new ArrayList<Wallet>(), this.txtToAddress, dialog);

        recyclerViewWallets.setLayoutManager(linearLayoutManagerTransactions);
        recyclerViewWallets.setAdapter(walletAdapter);

        walletAdapter.setItems(walletList);
    }

    @OnClick(R.id.send_button)
    public void onViewClicked() {
        String password;

        if (!withoutPin) {
            password = verifyPin();
        } else {
            password = txtPin.getText().toString();
        }

        if (!password.equals("")) {
            selectedWallet = utils.getWallet(getActivity());

            Float amount = Float.parseFloat(txtAmountConverted.getText().toString());

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
                    Log.e("Sucesso", "Sucesso");
                    updateData();
                    clearInputs();
                } else {
                    Log.e("Erro", "Erro ao buscar os valores!");
                    clearInputs();
                }
            });
        }
    }

    private void clearInputs() {
        txtToAddress.setText("");
        txtAmount.setText("");
        txtAmountConverted.setText(String.valueOf(0.0));
        txtPin.setText("");
        sendButton.setText(getContext().getResources().getString(R.string.send_button, amountConverted));
    }

    private void updateData() {
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getUser(utils.getToken(getActivity()), getActivity()).observe(this, response -> {
            if (response != null) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                utils.saveUser(getActivity(), response);
                ((MainActivity) getActivity()).setWalletValue();
            } else {
                Log.e("Erro", "Erro ao buscar os valores!");
            }
        });
    }

    private String verifyPin() {
        byte[] cryptedPin = utils.getByte(getActivity(), "pin");
        byte[] cryptedPassword = utils.getByte(getActivity(), "password");

        try {
            String decryptedPin = decryptor
                    .decryptData(PIN_ALIAS, cryptedPin, utils.getByte(getActivity(), "pinIv"));
            if (decryptedPin.equals(txtPin.getText().toString())) {
                try {
                    String decryptedPassword = decryptor
                            .decryptData(PASSWORD_ALIAS, cryptedPassword, utils.getByte(getActivity(), "passwordIv"));
                    return decryptedPassword;
                } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                        KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                        IOException | InvalidKeyException e) {
                    Log.e("Error", "on encrypt: " + e.getMessage(), e);
                } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e("Error", "on encrypt: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        txtToAddress.setText(text);
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

