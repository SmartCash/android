package cc.smartcash.wallet.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cc.smartcash.wallet.Adapters.DashboardWalletListAdapter;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.SmartCashApplication;


public class DashboardFragment extends Fragment {

    private ArrayList<Wallet> walletList;
    private SmartCashApplication smartCashApplication;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.smartCashApplication = new SmartCashApplication(getContext());
        walletList = smartCashApplication.getUser(getContext()).getWallet();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViewWallets();
    }

    private void setupRecyclerViewWallets() {
        RecyclerView walletRecyclerview = getActivity().findViewById(R.id.wallet_recyclerview);
        LinearLayoutManager linearLayoutManagerTransactions = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        DashboardWalletListAdapter walletAdapter = new DashboardWalletListAdapter(getContext(), new ArrayList<Wallet>());

        walletRecyclerview.setLayoutManager(linearLayoutManagerTransactions);
        walletRecyclerview.setAdapter(walletAdapter);

        walletAdapter.setItems(walletList);
    }
}
