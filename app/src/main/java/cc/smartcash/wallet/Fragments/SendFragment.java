package cc.smartcash.wallet.Fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.smartcash.wallet.Adapters.WalletSpinnerAdapter;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.SmartCashApplication;


public class SendFragment extends Fragment {

    public static final String TAG = SendFragment.class.getSimpleName();


    //    @BindView(R.id.address_underline)
//    View addressUnderline;
//    @BindView(R.id.sms_underline)
//    View smsUnderline;
//    @BindView(R.id.email_underline)
//    View emailUnderline;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.wallet_spinner)
    Spinner walletSpinner;
    //    private View activeUnderline;
    private ArrayList<Wallet> walletList;
    private WalletSpinnerAdapter walletAdapter;
    private SmartCashApplication smartCashApplication;

    public static SendFragment newInstance() {
        return new SendFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getContext());
        walletList = smartCashApplication.getUser(getContext()).getWallet();
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getContext());
//        activeUnderline = getView().findViewById(R.id.address_underline);
        Fragment addressFrament = SendAddressFragment.newInstance();
        openFragment(addressFrament);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        walletSpinner.setDropDownWidth(displayMetrics.widthPixels);

        Spinner walletSpinner = getView().findViewById(R.id.wallet_spinner);
        walletAdapter = new WalletSpinnerAdapter(getContext(), walletList);
        walletSpinner.setAdapter(walletAdapter);

        walletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smartCashApplication.saveWallet(getContext(), walletList.get(position));
                Log.e("ATUAL", walletList.get(position).getAddress());
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
    }


/*    public void changeUnderline(View newActiveUnderline) {
        activeUnderline.setVisibility(View.GONE);
        newActiveUnderline.setVisibility(View.VISIBLE);
        activeUnderline = newActiveUnderline;
    }*/

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
//

//    @OnClick({R.id.btn_address, R.id.btn_sms, R.id.btn_email})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_address:
//                Fragment addressFragment = SendAddressFragment.newInstance();
//                openFragment(addressFragment);
//                changeUnderline(addressUnderline);
//                break;
//            case R.id.btn_sms:
//                Fragment smsFragment = SendSmsFragment.newInstance();
//                openFragment(smsFragment);
//                changeUnderline(smsUnderline);
//                break;
//            case R.id.btn_email:
//                Fragment emailFragment = SendEmailFragment.newInstance();
//                openFragment(emailFragment);
//                changeUnderline(emailUnderline);
//                break;
//        }
//    }


//    @OnClick({R.id.btn_address, R.id.btn_sms, R.id.btn_email})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_address:
//                Fragment addressFragment = SendAddressFragment.newInstance();
//                openFragment(addressFragment);
//                changeUnderline(addressUnderline);
//                break;
//            case R.id.btn_sms:
//                Fragment smsFragment = SendSmsFragment.newInstance();
//                openFragment(smsFragment);
//                changeUnderline(smsUnderline);
//                break;
//            case R.id.btn_email:
//                Fragment emailFragment = SendEmailFragment.newInstance();
//                openFragment(emailFragment);
//                changeUnderline(emailUnderline);
//                break;
//        }
//    }
}
