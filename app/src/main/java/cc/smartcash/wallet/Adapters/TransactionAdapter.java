package cc.smartcash.wallet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.FullTransaction;
import cc.smartcash.wallet.Models.Transaction;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.URLS;
import cc.smartcash.wallet.ViewHolders.TransactionViewHolder;
import cc.smartcash.wallet.ViewModels.TransactionViewModel;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {


    public static final String TAG = TransactionAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Transaction> transactions;
    private TransactionViewHolder transactionViewHolder;

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
        transactionViewHolder = new TransactionViewHolder(view);
        return transactionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {

        SmartCashApplication app = new SmartCashApplication(context);
        Coin actualSelectedCoin = app.getActualSelectedCoin(context);

        String fiatValue = "";

        if (actualSelectedCoin == null || actualSelectedCoin.getName().equals(context.getString(R.string.default_crypto))) {
            ArrayList<Coin> currentPrice = app.getCurrentPrice(context);
            fiatValue = (app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.transactions.get(i).getAmount(), currentPrice.get(0).getValue())));
        } else {
            fiatValue = (app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(this.transactions.get(i).getAmount(), actualSelectedCoin.getValue())));
        }

        transactionViewHolder.amount.setText(app.formatNumberByDefaultCrypto(this.transactions.get(i).getAmount()));
        transactionViewHolder.direction.setText(this.transactions.get(i).getDirection());
        transactionViewHolder.timestamp.setText(this.transactions.get(i).getTimestamp());
        transactionViewHolder.hash.setText(this.transactions.get(i).getHash());
        transactionViewHolder.price.setText(" (" + fiatValue + ")");
        transactionViewHolder.hash.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLS.URL_INSIGHT_EXPLORER + this.transactions.get(i).getHash()));
            context.startActivity(browserIntent);
        });

        transactionViewHolder.btnDetails.setOnClickListener(v -> {
            setVisibility(transactionViewHolder);

            TransactionParameter transactionParameter = new TransactionParameter(transactionViewHolder, transactions.get(i).getHash(), i);
            new TransactionTask().execute(transactionParameter);

        });

        setDirectionColors(transactionViewHolder, i);
    }

    private class TransactionParameter {
        private TransactionViewHolder transactionViewHolder;
        private String hash;
        private int position;
        private FullTransaction transaction;

        public TransactionParameter(TransactionViewHolder transactionViewHolder, String hash, int position) {
            this.transactionViewHolder = transactionViewHolder;
            this.hash = hash;
            this.position = position;
        }

        public FullTransaction getTransaction() {
            return transaction;
        }

        public void setTransaction(FullTransaction transaction) {
            this.transaction = transaction;
        }

        public TransactionViewHolder getTransactionViewHolder() {
            return transactionViewHolder;
        }

        public String getHash() {
            return hash;
        }

        public int getPosition() {
            return position;
        }
    }

    private class TransactionTask extends AsyncTask<TransactionParameter, Integer, TransactionParameter> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TransactionParameter doInBackground(TransactionParameter... parameters) {
            parameters[0].getTransactionViewHolder().confirmations.setText("loading...");
            try {
                parameters[0].setTransaction(TransactionViewModel.getSyncTransaction(parameters[0].getHash()));
                return parameters[0];
            } catch (Exception ex) {
                parameters[0].getTransactionViewHolder().confirmations.setText("0");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(TransactionParameter transactionResult) {
            super.onPostExecute(transactionResult);
            try {
                transactionResult.getTransactionViewHolder().confirmations.setText(String.valueOf(transactionResult.getTransaction().getConfirmations()));
            } catch (Exception ex) {
                transactionResult.getTransactionViewHolder().confirmations.setText("0");
            }
        }
    }

    public void setDirectionColors(TransactionViewHolder transactionViewHolder, int i) {
        String direction = String.valueOf(this.transactions.get(i).getDirection());

        if (direction.equals("Sent")) {
            transactionViewHolder.direction.setBackgroundResource(R.drawable.bg_paid);
            Drawable wrappedDrawable = changeIconColor(R.drawable.ic_arrow_up, R.color.paidColor);
            transactionViewHolder.icon.setBackground(wrappedDrawable);
        } else if (direction.equals("Received")) {
            transactionViewHolder.direction.setBackgroundResource(R.drawable.bg_receive);
            Drawable wrappedDrawable = changeIconColor(R.drawable.ic_arrow_down, R.color.receiveColor);
            transactionViewHolder.icon.setBackground(wrappedDrawable);
        } else if (direction.equals("Awaiting")) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DrawableCompat.setTint(wrappedDrawable, context.getColor(color));
        }
        return wrappedDrawable;
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
