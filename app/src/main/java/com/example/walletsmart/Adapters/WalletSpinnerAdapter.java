package com.example.walletsmart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.walletsmart.R;
import com.example.walletsmart.Models.Wallet;

public class WalletSpinnerAdapter extends ArrayAdapter<Wallet> {
    private Integer count;
    private Boolean check;
    private ArrayList<Wallet> wallets;

    public WalletSpinnerAdapter(Context context, ArrayList<Wallet> walletList) {
        super(context, 0, walletList);
        this.count = 0;
        this.wallets = walletList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.wallet_spinner_item, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.txt_name);
        TextView textViewBalance = convertView.findViewById(R.id.txt_balance);
        TextView textViewAddress = convertView.findViewById(R.id.txt_to_address);
        TextView textViewHash = convertView.findViewById(R.id.txt_hash);
        Button btnShowHash = convertView.findViewById(R.id.btn_open_details);
        CardView roundIcon = convertView.findViewById(R.id.round_icon);

        if (wallets.get(position).getPosition() % 3 == 1) {
            @SuppressLint("ResourceType") String color = convertView.getResources().getString(R.color.btnGreen);
            roundIcon.setCardBackgroundColor(Color.parseColor(color));
            count++;
        } else if (wallets.get(position).getPosition() % 3 == 2) {
            @SuppressLint("ResourceType") String color = convertView.getResources().getString(R.color.btnYellon);
            roundIcon.setCardBackgroundColor(Color.parseColor(color));
            count++;
        } else if (wallets.get(position).getPosition() % 3 == 0) {
            @SuppressLint("ResourceType") String color = convertView.getResources().getString(R.color.btnRed);
            roundIcon.setCardBackgroundColor(Color.parseColor(color));
            count = 0;
        }

        Wallet currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getDisplayName());
            textViewBalance.setText(String.valueOf(currentItem.getBalance()));
            textViewAddress.setText(currentItem.getAddress());
            textViewAddress.setText(currentItem.getAddress());
        }


        return convertView;
    }
}