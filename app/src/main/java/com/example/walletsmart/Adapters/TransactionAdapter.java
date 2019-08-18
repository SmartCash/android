
package com.example.walletsmart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletsmart.Activities.MainActivity;
import com.example.walletsmart.Activities.TransactionActivity;
import com.example.walletsmart.Models.Coin;
import com.example.walletsmart.Models.Transaction;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.Utils;
import com.example.walletsmart.ViewHolders.TransactionViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {
    private Context context;
    private ArrayList<Transaction> transactions;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    public void setItems(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, viewGroup, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {
        transactionViewHolder.amount.setText(String.valueOf(this.transactions.get(i).getAmount()));
        transactionViewHolder.direction.setText(this.transactions.get(i).getDirection());
        transactionViewHolder.timestamp.setText(this.transactions.get(i).getTimestamp());
        transactionViewHolder.hash.setText(this.transactions.get(i).getHash());

        Utils utils = new Utils();
        ArrayList<Coin> coins = utils.getCurrentPrice(context);

        if (utils.getActualSelectedCoin(context) == null || utils.getActualSelectedCoin(context).getName().equals("SMART")) {
            for (Coin item : coins) {
                if (item.getName().equals("USD")) {
                    BigDecimal value = utils.converterBigDecimal(BigDecimal.valueOf(this.transactions.get(i).getAmount()), BigDecimal.valueOf(item.getValue()));
                    transactionViewHolder.price.setText(String.format("%.2f", value));
                }
            }
        } else {
            BigDecimal value = utils.converterBigDecimal(BigDecimal.valueOf(this.transactions.get(i).getAmount()), BigDecimal.valueOf(utils.getActualSelectedCoin(context).getValue()));
            transactionViewHolder.price.setText(String.format("%.2f", value));
        }

        transactionViewHolder.hash.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransactionActivity.class);
            intent.putExtra("TransactionHash", this.transactions.get(i).getHash());
            intent.putExtra("Amount", this.transactions.get(i).getAmount());
            intent.putExtra("FromAddress", this.transactions.get(i).getToAddress());

            context.startActivity(intent);
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://insight.smartcash.cc/tx/" + transactionViewHolder.hash.getText()));
//            context.startActivity(browserIntent);
        });

        transactionViewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(transactionViewHolder);
            }
        });

        setDirectionColors(transactionViewHolder, i);
    }

    public void setDirectionColors(TransactionViewHolder transactionViewHolder, int i) {
        String direction = String.valueOf(this.transactions.get(i).getDirection());

        if (direction.equals("Sent")) {
            transactionViewHolder.direction.setBackgroundResource(R.drawable.bg_paid);
            Drawable wrappedDrawable = changeIconColor(R.drawable.ic_arrow_up, R.color.paidColor);
            transactionViewHolder.icon.setBackground(wrappedDrawable);
        }
        else if (direction.equals("Received")) {
            transactionViewHolder.direction.setBackgroundResource(R.drawable.bg_receive);
            Drawable wrappedDrawable = changeIconColor(R.drawable.ic_arrow_down, R.color.receiveColor);
            transactionViewHolder.icon.setBackground(wrappedDrawable);
        }
        else if (direction.equals("Awaiting")) {
            Drawable wrappedDrawable = changeIconColor(R.drawable.bg_awaiting, R.color.awaitingColor);
            transactionViewHolder.direction.setBackground(wrappedDrawable);

        }
    }

    public void setVisibility(TransactionViewHolder transactionViewHolder) {
        if (transactionViewHolder.idView.getVisibility() == View.VISIBLE) {
            transactionViewHolder.idView.setVisibility(View.GONE);
            transactionViewHolder.btnDetails.setImageResource(R.drawable.menu_down);
        } else {
            transactionViewHolder.idView.setVisibility(View.VISIBLE);
            transactionViewHolder.btnDetails.setImageResource(R.drawable.menu_up);
        }
    }

    public Drawable changeIconColor(Integer icon, Integer color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, icon));
        DrawableCompat.setTint(wrappedDrawable, context.getColor(color));
        return wrappedDrawable;
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
