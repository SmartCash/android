package cc.smartcash.wallet.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
import androidx.lifecycle.ViewModelProvider;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Adapters.WalletSpinnerAdapter;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.Util;
import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ReceiveFragment extends Fragment {

    public static final String TAG = ReceiveFragment.class.getSimpleName();

    private ArrayList<Wallet> walletList;
    private SmartCashApplication smartCashApplication;
    private ArrayList<Coin> coins;

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

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getContext());

        walletList = smartCashApplication.getUser(getContext()).getWallet();

        View view = inflater.inflate(R.layout.fragment_receive, container, false);

        qrCodeImage = view.findViewById(R.id.qrcode_image);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getContext());

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

        walletSpinner.setDropDownWidth(displayMetrics.widthPixels);

        Spinner walletSpinner = getView().findViewById(R.id.wallet_spinner);

        WalletSpinnerAdapter walletAdapter = new WalletSpinnerAdapter(getContext(), walletList);

        walletSpinner.setAdapter(walletAdapter);

        walletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                walletAddress.setText(walletList.get(position).getAddress());
                smartCashApplication.saveWallet(getContext(), walletList.get(position));

                setQRCodeByAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Wallet savedWallet = smartCashApplication.getWallet(getContext());

        if (savedWallet != null) {
            for (int i = 0; i < walletList.size(); i++) {
                if (savedWallet.getWalletId().equals(walletList.get(i).getWalletId())) {
                    walletSpinner.setSelection(i);
                }
            }
        }

        if (NetworkUtil.getInternetStatus(getContext())) {

            CurrentPriceViewModel model = new ViewModelProvider(this).get(CurrentPriceViewModel.class);

            model.getCurrentPrices(getActivity()).observe(this, currentPrices -> {
                if (currentPrices != null) {
                    coins = SmartCashApplication.convertToArrayList(currentPrices);
                } else {
                    Log.e(TAG, "Error to get current prices.");
                }
            });
        } else {
            coins = smartCashApplication.getCurrentPrice(getActivity());
        }
        setAmountListener();
    }

    @OnClick(R.id.btn_copy)
    public void onViewClicked() {
        SmartCashApplication.copyToClipboard(getContext(), walletAddress.getText().toString());
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

            if (actualSelected.getName().equals(getString(R.string.default_crypto))) {
                amountConverted = smartCashApplication.multiplyBigDecimals(BigDecimal.valueOf(Double.parseDouble(txtAmount.getText().toString())), BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", getString(R.string.default_crypto)));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmount.getText().toString());
                double ruleOfThree = amountInTheField / currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmountConverted.setText(String.valueOf(amountConverted));

        }

        setQRCodeByAmount();


    }

    private void setQRCodeByAmount() {

        String amountInSmartCash = txtAmountConverted.getText().toString();

        if (!amountInSmartCash.isEmpty() && Float.parseFloat(amountInSmartCash) > 0) {
            generateQrCode("smartcash:" + walletAddress.getText().toString() + "?amount=" + amountInSmartCash);
        } else {
            generateQrCode("smartcash:" + walletAddress.getText().toString());
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

            if (actualSelected.getName().equals(getString(R.string.default_crypto))) {
                amountConverted = smartCashApplication.multiplyBigDecimals(BigDecimal.valueOf(Double.parseDouble(txtAmountConverted.getText().toString())), BigDecimal.valueOf(actualSelected.getValue()));
                amountLabel.setText(String.format(Locale.getDefault(), "Amount in %s", getString(R.string.default_crypto)));
            } else {

                double currentPrice = actualSelected.getValue();
                double amountInTheField = Double.parseDouble(txtAmountConverted.getText().toString());
                double ruleOfThree = amountInTheField * currentPrice;
                amountConverted = BigDecimal.valueOf(ruleOfThree);
            }

            txtAmount.setText(String.valueOf(amountConverted));

        }

        setQRCodeByAmount();

    }

    private void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {


        try {
            //generating qr code.
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);

            //converting bitmatrix to bitmap
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];

            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    //for black and white
                    pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                    //for custom color
                    //pixels[offset + x] = matrix.get(x, y) ?
                    //        ResourcesCompat.getColor(getResources(),R.color.colorB,null) :WHITE;
                }
            }
            //creating bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            //getting the logo
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_qrcode);

            //setting bitmap to image view
            qrCodeImage.setImageBitmap(mergeBitmaps(logo, bitmap));

        } catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
        }
    }

    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }

    private void generateQrCode(String qrCodeText) {
        try {

            //setting size of qr code
            int width = qrCodeImage.getWidth();
            int height = qrCodeImage.getHeight();
            int smallestDimension = width < height ? width : height;

            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();

            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            createQRCode(qrCodeText, Util.UTF_8, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }

}
