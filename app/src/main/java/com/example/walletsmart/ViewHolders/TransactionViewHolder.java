package com.example.walletsmart.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletsmart.R;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    public TextView amount;
    public TextView hash;
    public TextView direction;
    public TextView timestamp;
    public ImageView btnDetails;
    public View divider;
    public TextView hashTitle;
    public ImageView icon;
    public View contentView;
    public View idView;
    public TextView price;

    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);

        amount = itemView.findViewById(R.id.text_amount);
        direction = itemView.findViewById(R.id.text_direction);
        timestamp = itemView.findViewById(R.id.txt_timestamp);
        hash = itemView.findViewById(R.id.txt_hash);
        icon = itemView.findViewById(R.id.ic_transaction);
        btnDetails = itemView.findViewById(R.id.btn_open_details);
        divider = itemView.findViewById(R.id.divider);
        hashTitle = itemView.findViewById(R.id.transaction_id_title);
        contentView = itemView.findViewById(R.id.content_view);
        idView = itemView.findViewById(R.id.id_view);
        price = itemView.findViewById(R.id.txt_dolar);
    }
}

