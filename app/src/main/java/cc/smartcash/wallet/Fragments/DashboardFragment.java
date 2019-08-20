package cc.smartcash.wallet.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.smartcash.wallet.Adapters.DashboardWalletListAdapter;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.Utils;


public class DashboardFragment extends Fragment {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    private ArrayList<Wallet> walletList;
    private Utils utils = new Utils();
    private DashboardWalletListAdapter walletAdapter;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        walletList = utils.getUser(getContext()).getWallet();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitle.setText(getResources().getString(R.string.dashboard_title, walletList.size()));
        setupRecyclerViewWallets();
    }

    private void setupRecyclerViewWallets() {
        RecyclerView walletRecyclerview = getActivity().findViewById(R.id.wallet_recyclerview);
        LinearLayoutManager linearLayoutManagerTransactions = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        walletAdapter = new DashboardWalletListAdapter(getContext(), new ArrayList<Wallet>());

        walletRecyclerview.setLayoutManager(linearLayoutManagerTransactions);
        walletRecyclerview.setAdapter(walletAdapter);

        walletAdapter.setItems(walletList);
    }
}
