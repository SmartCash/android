package com.example.walletsmart.Fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletsmart.Activities.MainActivity;
import com.example.walletsmart.Adapters.TransactionAdapter;
import com.example.walletsmart.Adapters.WalletSpinnerAdapter;
import com.example.walletsmart.Models.Transaction;
import com.example.walletsmart.Models.Wallet;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.Utils;
import com.example.walletsmart.ViewModels.UserViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionFragment extends Fragment {

    @BindView(R.id.all_transactions_underline)
    View allTransactionsUnderline;
    @BindView(R.id.received_underline)
    View receivedUnderline;
    @BindView(R.id.awaiting_underline)
    View awaitingUnderline;
    @BindView(R.id.paid_underline)
    View paidUnderline;
    View activeUnderline;
    @BindView(R.id.wallet_spinner)
    Spinner walletSpinner;
    private String activeFilter = null;
    private ArrayList<Wallet> walletList;
    private WalletSpinnerAdapter walletAdapter;
    private ArrayList<Transaction> filteredTransactions = new ArrayList<Transaction>();
    private ArrayList<Transaction> transactions;
    private TransactionAdapter transactionAdapter;
    Utils utils = new Utils();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        walletList = utils.getUser(getContext()).getWallet();
        transactions = walletList.get(0).getTransactions();
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViewTransactions();

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        walletSpinner.setDropDownWidth(displayMetrics.widthPixels);

        activeUnderline = getView().findViewById(R.id.all_transactions_underline);

        Spinner walletSpinner = getView().findViewById(R.id.wallet_spinner);
        walletAdapter = new WalletSpinnerAdapter(getContext(), walletList);
        walletSpinner.setAdapter(walletAdapter);

        walletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Wallet clickedItem = (Wallet) parent.getItemAtPosition(position);
                transactions = clickedItem.getTransactions();
                utils.saveWallet(getContext(), walletList.get(position));
                setTransactions(activeFilter);
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


    private void setupRecyclerViewTransactions() {
        RecyclerView recyclerViewTransactions = getActivity().findViewById(R.id.transaction_recyclerview);
        LinearLayoutManager linearLayoutManagerTransactions = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        transactionAdapter = new TransactionAdapter(getContext(), new ArrayList<Transaction>());

        recyclerViewTransactions.setLayoutManager(linearLayoutManagerTransactions);
        recyclerViewTransactions.setAdapter(transactionAdapter);

        transactionAdapter.setItems(transactions);
    }

    public static TransactionFragment newInstance() {
        return new TransactionFragment();
    }

    @OnClick({R.id.btn_all_transactions, R.id.btn_received, R.id.btn_awaiting, R.id.btn_paid, R.id.btn_att})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_all_transactions:
                changeUnderline(allTransactionsUnderline);
                setTransactions(null);
                break;
            case R.id.btn_received:
                changeUnderline(receivedUnderline);
                setTransactions("Received");
                break;
            case R.id.btn_awaiting:
                changeUnderline(awaitingUnderline);
                setTransactions("Awaiting");
                break;
            case R.id.btn_paid:
                changeUnderline(paidUnderline);
                setTransactions("Sent");
                break;
            case R.id.btn_att:
                updateData();
                break;
        }
    }

    private void updateData() {
        UserViewModel model = ViewModelProviders.of(this).get(UserViewModel.class);

        model.getUser(utils.getToken(getActivity()), getActivity()).observe(this, response -> {
            if (response != null) {
                Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();
                utils.saveUser(getActivity(), response);
                ((MainActivity)getActivity()).setWalletValue();
            } else {
                Log.e("Erro", "Erro ao buscar os valores!");
            }
        });
    }

    public void setTransactions(String filtro) {
        if (filtro == null)
            transactionAdapter.setItems(transactions);
        else {
            filteredTransactions = filterTransactions(filtro);
            transactionAdapter.setItems(filteredTransactions);
        }
        activeFilter = filtro;
    }

    public ArrayList<Transaction> filterTransactions(String filtro) {
        ArrayList<Transaction> filteredT = new ArrayList<Transaction>();

        for (Transaction item : transactions) {
            if (item.getDirection().equals(filtro))
                filteredT.add(item);
        }

        return filteredT;
    }

    public void changeUnderline(View newActiveUnderline) {
        activeUnderline.setVisibility(View.GONE);
        newActiveUnderline.setVisibility(View.VISIBLE);
        activeUnderline = newActiveUnderline;
    }
}
