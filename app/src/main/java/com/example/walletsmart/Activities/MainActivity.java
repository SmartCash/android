package com.example.walletsmart.Activities;

import com.example.walletsmart.Adapters.CoinSpinnerAdapter;
import com.example.walletsmart.Fragments.DashboardFragment;
import com.example.walletsmart.Fragments.ReceiveFragment;
import com.example.walletsmart.Fragments.TransactionFragment;
import com.example.walletsmart.Models.Coin;
import com.example.walletsmart.Models.Wallet;
import com.example.walletsmart.R;
import com.example.walletsmart.Fragments.SendFragment;
import com.example.walletsmart.Utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private BottomNavigationView mNavigationView;
    private ImageView btnExit;
    private ImageView btnSettings;
    private TextView walletTxt;
    private TextView walletConverted;
    private Utils utils;
    private CoinSpinnerAdapter adapter;
    private Wallet wallet;
    private Coin selectedCoin;
    private boolean withoutPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        utils = new Utils();
        withoutPin = utils.getBoolean(this, "WithoutPin");
        mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        Fragment dashFragment = DashboardFragment.newInstance();
        openFragment(dashFragment);

        walletTxt = findViewById(R.id.wallet_txt);
        walletConverted = findViewById(R.id.wallet_converted_txt);

        setWalletValue();

        btnExit = findViewById(R.id.button_exit);
        btnSettings = findViewById(R.id.button_settings);

        btnExit.setOnClickListener(v -> {
            utils.deleteSharedPreferences(this);
            startActivity(new Intent(this, LoginActivity.class));
        });

        btnSettings.setOnClickListener(v2 -> {
            AlertDialog.Builder settingsDialog = new AlertDialog.Builder(this);
            View settingsView = getLayoutInflater().inflate(R.layout.settings_modal, null);

            Button btnClose = settingsView.findViewById(R.id.button_close);
            Spinner currentPriceSpinner = settingsView.findViewById(R.id.current_price_spinner);
            TextView forgotPinBtn = settingsView.findViewById(R.id.forgot_pin_btn);
            TextView createPinBtn = settingsView.findViewById(R.id.create_pin);

            if (withoutPin) {
                forgotPinBtn.setVisibility(View.GONE);
            } else {
                createPinBtn.setVisibility(View.GONE);
            }

            createPinBtn.setOnClickListener(v4 -> {
                utils.saveBoolean(this, false, "WithoutPin");
                startActivity(new Intent(this, PinActivity.class));
            });

            forgotPinBtn.setOnClickListener(v3 -> {
                utils.deleteSharedPreferences(this);
                startActivity(new Intent(this, LoginActivity.class));
            });

            ArrayList<Coin> coins = utils.getCurrentPrice(this);

            adapter = new CoinSpinnerAdapter(this, android.R.layout.simple_spinner_item, coins);

            currentPriceSpinner.setAdapter(adapter);

            Coin selectedCoin = utils.getActualSelectedCoin(this);

            if (selectedCoin != null) {
                for(int i = 0; i < coins.size(); i++) {
                    if (selectedCoin.getValue().equals(coins.get(i).getValue()) && selectedCoin.getName().equals(coins.get(i).getName())) {
                        currentPriceSpinner.setSelection(i);
                    }
                }
            }

            currentPriceSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    Coin coin = adapter.getItem(position);
                    utils.saveActualSelectedCoin(MainActivity.this, coin);
                    setWalletValue();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

            settingsDialog.setView(settingsView);
            AlertDialog dialog = settingsDialog.create();

            btnClose.setOnClickListener(v3 -> {
                dialog.hide();
                setWalletValue();
            });

            dialog.show();
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dash: {
                Fragment dashFragment = DashboardFragment.newInstance();
                openFragment(dashFragment);
                setWalletValue();
                break;
            }
            case R.id.nav_receive: {
                Fragment receiveFragment = ReceiveFragment.newInstance();
                openFragment(receiveFragment);
                setWalletValue();
                break;
            }
            case R.id.nav_send: {
                Fragment sendFragment = SendFragment.newInstance();
                openFragment(sendFragment);
                setWalletValue();
                break;
            }
            case R.id.nav_trans: {
                Fragment transactionFragment = TransactionFragment.newInstance();
                openFragment(transactionFragment);
                setWalletValue();
                break;
            }
        }

        return true;
    }

    public void setWalletValue() {
        ArrayList<Wallet> wallets = utils.getUser(this).getWallet();
        Double amount = 0.0;

        for (Wallet wallet : wallets) {
            amount += wallet.getBalance();
        }

        selectedCoin = utils.getActualSelectedCoin(this);

        walletTxt.setText(getResources().getString(R.string.smartCash) + String.format("%.8f", amount));
        if (selectedCoin == null) {
            ArrayList<Coin> currentPrice = utils.getCurrentPrice(this);
        //    walletConverted.setText("$ " + String.format("%.3f", amount / currentPrice.get(0).getValue()));
               walletConverted.setText("$ "+  utils.converterValue(amount,currentPrice.get(1).getValue()));

        }
        else {
            //  walletConverted.setText("$ " + String.format("%.3f", amount / selectedCoin.getValue()));
            walletConverted.setText("$ " + utils.converterValue(amount,selectedCoin.getValue()));

        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}