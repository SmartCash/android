package com.example.walletsmart.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletsmart.R;

public class WalletDialogViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public Button btnSelect;
    public TextView title;

    public WalletDialogViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.wallet_dialog_title);
        name = itemView.findViewById(R.id.wallet_name);
        btnSelect = itemView.findViewById(R.id.btn_select);
    }
}
