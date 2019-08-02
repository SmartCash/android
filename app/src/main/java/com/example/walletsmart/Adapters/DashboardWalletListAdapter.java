package com.example.walletsmart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletsmart.Models.Wallet;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.Utils;
import com.example.walletsmart.ViewHolders.DashboardWalletListViewHolder;

import java.util.ArrayList;

public class DashboardWalletListAdapter extends RecyclerView.Adapter<DashboardWalletListViewHolder> {
    private Context context;
    private ArrayList<Wallet> wallets;
    private Integer count;

    public DashboardWalletListAdapter(Context context, ArrayList<Wallet> wallets) {
        this.context = context;
        this.wallets = wallets;
        this.count = 0;
    }

    public void setItems(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardWalletListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_wallet_item, viewGroup, false);
        return new DashboardWalletListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardWalletListViewHolder walletViewHolder, int i) {
        walletViewHolder.name.setText(this.wallets.get(i).getDisplayName());
        walletViewHolder.value.setText(String.valueOf(this.wallets.get(i).getBalance()));
        walletViewHolder.address.setText(this.wallets.get(i).getAddress());
        walletViewHolder.btnCopy.setOnClickListener(v -> {
            Utils.copyToClipboard(context, this.wallets.get(i).getAddress());
        });

        if (count == 0) {
            @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.btnGreen);
            walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(color));
            Log.e("cont", String.valueOf(count));
            count++;
        } else if (count == 1) {
            @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.btnYellon);
            walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(color));
            Log.e("cont", String.valueOf(count));
            count++;
        } else if (count == 2) {
            @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.btnRed);
            walletViewHolder.roundIcon.setCardBackgroundColor(Color.parseColor(color));
            Log.e("cont", String.valueOf(count));
            count = 0;
        }
    }
    @Override
    public int getItemCount() {
        return wallets.size();
    }
}
