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

    private ArrayList<Wallet> walletList;
    private WalletSpinnerAdapter walletAdapter;
    private SmartCashApplication smartCashApplication;

    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    @BindView(R.id.wallet_spinner)
    Spinner walletSpinner;

    public static SendFragment newInstance() {
        return new SendFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getContext());

        walletList = smartCashApplication.getUser(getContext()).getWallet();
        smartCashApplication.saveWallet(getContext(), walletList.get(0));
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getContext());
        Fragment addressFragment = SendAddressFragment.newInstance();
        openFragment(addressFragment);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        walletSpinner.setDropDownWidth(displayMetrics.widthPixels);

        Spinner walletSpinner = getView().findViewById(R.id.wallet_spinner);
        walletAdapter = new WalletSpinnerAdapter(getContext(), walletList);
        walletSpinner.setAdapter(walletAdapter);

        walletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smartCashApplication.saveWallet(getContext(), walletList.get(position));
                Log.e(TAG, walletList.get(position).getAddress());
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

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
