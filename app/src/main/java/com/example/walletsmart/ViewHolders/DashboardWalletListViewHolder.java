package com.example.walletsmart.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletsmart.R;

public class DashboardWalletListViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView value;
    public TextView address;
    public ImageView btnCopy;
    public CardView roundIcon;

    public DashboardWalletListViewHolder (@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.txt_name);
        value = itemView.findViewById(R.id.txt_value);
        address = itemView.findViewById(R.id.txt_to_address);
        btnCopy = itemView.findViewById(R.id.img_copy);
        roundIcon = itemView.findViewById(R.id.round_icon);
    }
}