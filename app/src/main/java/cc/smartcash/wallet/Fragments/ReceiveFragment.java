package cc.smartcash.wallet.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Adapters.WalletSpinnerAdapter;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.Utils;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;

public class ReceiveFragment extends Fragment {

    @BindView(R.id.qrcode_image)
    SimpleDraweeView qrCodeImage;

    @BindView(R.id.wallet_address)
    TextView walletAddress;

    @BindView(R.id.wallet_spinner)
    Spinner walletSpinner;

    @BindView(R.id.txt_amount_converted)
    EditText txtAmountConverted;

    @BindView(R.id.txt_amount)
    EditText txtAmount;

    @BindView(R.id.amount_label)
    TextView amountLabel;

    @BindView(R.id.amount_with_label)
    TextView amount_with_label;

    private ArrayList<Wallet> walletList;

    private WalletSpinnerAdapter walletAdapter;

    private Utils utils = new Utils();

    private ArrayList<Coin> coins;

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        walletList = utils.getUser(getContext()).getWallet();

        View view = inflater.inflate(R.layout.fragment_receive, container, false);

        qrCodeImage = view.findViewById(R.id.qrcode_image);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

        walletSpinner.setDropDownWidth(displayMetrics.widthPixels);

        Spinner walletSpinner = getView().findViewById(R.id.wallet_spinner);

        walletAdapter = new WalletSpinnerAdapter(getContext(), walletList);

        walletSpinner.setAdapter(walletAdapter);

        walletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setImage(walletList.get(position).getQrCode());
                walletAddress.setText(walletList.get(position).getAddress());
                utils.saveWallet(getContext(), walletList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Wallet savedWallet = utils.getWallet(getContext());

        if (savedWallet != null) {
            for (int i = 0; i < walletList.size(); i++) {
                if (savedWallet.getWalletId().equals(walletList.get(i).getWalletId())) {
                    walletSpinner.setSelection(i);
                }
            }
        }

        if (NetworkUtil.getInternetStatus(getContext())) {
            CurrentPriceViewModel model = ViewModelProviders.of(this).get(CurrentPriceViewModel.class);

            model.getCurrentPrices(getActivity()).observe(this, currentPrices -> {
                if (currentPrices != null) {
                    coins = Utils.convertToArrayList(currentPrices);
                } else {
                    Log.e(getContext().getString(R.string.tag_log_error), "Error to get current prices.");
                }
            });
        } else {
            coins = utils.getCurrentPrice(getActivity());
        }
        setAmountListener();
    }

    public void setImage(String url) {
        Uri uri = Uri.parse(url);
        qrCodeImage.setImageURI(uri);
    }

    @OnClick(R.id.btn_copy)
    public void onViewClicked() {
        Utils.copyToClipboard(getContext(), walletAddress.getText().toString());
    }

    private void setAmountListener() {

        Coin actualSelected = utils.getActualSelectedCoin(getContext());
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
        Coin actualSelected = utils.getActualSelectedCoin(getContext());
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (!txtAmount.getText().toString().isEmpty()) {

            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtAmount.getText().toString()));

            BigDecimal finalValue = amount;

            if (actualSelected.getName().equals("SMART")) {
                amountConverted = utils.converterBigDecimal(finalValue, BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", "SMART"));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmount.getText().toString());
                double ruleOfThree = amountInTheField / currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmountConverted.setText(String.valueOf(amountConverted));

        }
    }

    private void calculateFromSmartToFiat() {

        BigDecimal amountConverted;
        Coin actualSelected = utils.getActualSelectedCoin(getContext());
        amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", actualSelected.getName()));

        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i).getName().equalsIgnoreCase(actualSelected.getName())) {
                actualSelected = coins.get(i);
                break;
            }
        }

        if (!txtAmountConverted.getText().toString().isEmpty()) {

            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString()));

            BigDecimal finalValue = amount;

            if (actualSelected.getName().equals("SMART")) {
                amountConverted = utils.converterBigDecimal(finalValue, BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", "SMART"));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmountConverted.getText().toString());
                double ruleOfThree = amountInTheField * currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmount.setText(String.valueOf(amountConverted));

        }
    }
}
