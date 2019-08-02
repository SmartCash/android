package com.example.walletsmart.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walletsmart.Adapters.WalletSpinnerAdapter;
import com.example.walletsmart.Models.Wallet;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveFragment extends Fragment {
    @BindView(R.id.qrcode_image)
    SimpleDraweeView qrCodeImage;
    @BindView(R.id.wallet_address)
    TextView walletAddress;
    private ArrayList<Wallet> walletList;
    private WalletSpinnerAdapter walletAdapter;
    private Utils utils = new Utils();
    @BindView(R.id.wallet_spinner)
    Spinner walletSpinner;

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
            for(int i = 0; i < walletList.size(); i++) {
                if (savedWallet.getWalletId().equals(walletList.get(i).getWalletId())) {
                    walletSpinner.setSelection(i);
                }
            }
        }
    }

    public void setImage(String url) {
        Uri uri = Uri.parse(url);
        qrCodeImage.setImageURI(uri);
    }

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
    }

    @OnClick(R.id.btn_copy)
    public void onViewClicked() {
        utils.copyToClipboard(getContext(), walletAddress.getText().toString());
    }
}
