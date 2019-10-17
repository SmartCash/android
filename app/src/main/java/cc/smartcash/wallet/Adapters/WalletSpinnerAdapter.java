package cc.smartcash.wallet.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.SmartCashApplication;

public class WalletSpinnerAdapter extends ArrayAdapter<Wallet> {

    private Integer count;
    private Boolean check;
    private ArrayList<Wallet> wallets;
    private Context context;

    public WalletSpinnerAdapter(Context context, ArrayList<Wallet> walletList) {
        super(context, 0, walletList);
        this.count = 0;
        this.wallets = walletList;
        this.context = context;
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


            SmartCashApplication app = new SmartCashApplication(context);
            Coin actualSelectedCoin = app.getActualSelectedCoin(context);

            String fiatValue = "";

            if (actualSelectedCoin == null || actualSelectedCoin.getName().equals(context.getString(R.string.default_crypto))) {
                ArrayList<Coin> currentPrice = app.getCurrentPrice(context);
                fiatValue = (app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(currentItem.getBalance(), currentPrice.get(0).getValue())));
            } else {
                fiatValue = (app.formatNumberBySelectedCurrencyCode(app.getCurrentValueByRate(currentItem.getBalance(), actualSelectedCoin.getValue())));
            }


            textViewName.setText(currentItem.getDisplayName());
            textViewBalance.setText(app.formatNumberByDefaultCrypto(currentItem.getBalance()) + " (" + fiatValue + ")");
            textViewAddress.setText(currentItem.getAddress());
            textViewAddress.setText(currentItem.getAddress());
        }


        return convertView;
    }
}