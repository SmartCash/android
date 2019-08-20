package cc.smartcash.wallet.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.ViewHolders.WalletDialogViewHolder;

public class WalletDialogAdapter extends RecyclerView.Adapter<WalletDialogViewHolder> {
    private Context context;
    private ArrayList<Wallet> wallets;
    private EditText txtAddress;
    private AlertDialog dialog;

    public WalletDialogAdapter(Context context, ArrayList<Wallet> wallets, EditText txtAddress, AlertDialog dialog) {
        this.txtAddress = txtAddress;
        this.context = context;
        this.wallets = wallets;
        this.dialog = dialog;
    }

    public void setItems(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WalletDialogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallet_dialog_item, viewGroup, false);
        return new WalletDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletDialogViewHolder walletDialogViewHolder, int i) {
        walletDialogViewHolder.name.setText(String.valueOf(this.wallets.get(i).getDisplayName()));
        walletDialogViewHolder.btnSelect.setOnClickListener(v -> {
            this.txtAddress.setText(this.wallets.get(i).getAddress());
            dialog.hide();
        });
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }
}
