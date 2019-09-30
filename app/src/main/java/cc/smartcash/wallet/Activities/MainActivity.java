package cc.smartcash.wallet.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import cc.smartcash.wallet.Adapters.CoinSpinnerAdapter;
import cc.smartcash.wallet.Fragments.DashboardFragment;
import cc.smartcash.wallet.Fragments.ReceiveFragment;
import cc.smartcash.wallet.Fragments.SendFragment;
import cc.smartcash.wallet.Fragments.TransactionFragment;
import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.Wallet;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.Keys;
import cc.smartcash.wallet.Utils.SmartCashApplication;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private BottomNavigationView mNavigationView;
    private ImageView btnExit;
    private ImageView btnSettings;
    private TextView walletTxt;
    private TextView walletConverted;
    private ArrayList<Coin> coins;
    private CoinSpinnerAdapter adapter;
    private Coin selectedCoin;
    private boolean withoutPin;
    private SmartCashApplication smartCashApplication;
    private LinearLayout linearLayoutBkp;
    private String txtPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        if (smartCashApplication == null)
            smartCashApplication = new SmartCashApplication(getApplicationContext());

        withoutPin = smartCashApplication.getBoolean(this, Keys.KEY_WITHOUT_PIN);

        this.txtPin = getPIN();

        getCoins();

        setUI();

        setBtnSettingsClick();

        setIfNeedToBackUpTheWallet();
    }

    private void setIfNeedToBackUpTheWallet() {

        if (smartCashApplication.getMSK() != null) {

            linearLayoutBkp.setVisibility(View.VISIBLE);
            return;

        }
        linearLayoutBkp.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setWalletValue();
    }

    private void setBtnSettingsClick() {
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
                smartCashApplication.saveBoolean(this, false, Keys.KEY_WITHOUT_PIN);
                startActivity(new Intent(this, PinActivity.class));
            });

            forgotPinBtn.setOnClickListener(v3 -> {
                new AlertDialog.Builder(this)
                        .setTitle("Forgot the PIN?")
                        .setMessage("Are you sure you want to redefine your PIN?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> navigateToLogin())
                        .setNegativeButton(android.R.string.no, null).show();
            });

            setSpinner(currentPriceSpinner);
            settingsDialog.setView(settingsView);
            AlertDialog dialog = settingsDialog.create();
            btnClose.setOnClickListener(v3 -> {
                dialog.hide();
                setWalletValue();
            });
            dialog.show();
        });
    }

    private void setUI() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        Fragment dashboardFragment = DashboardFragment.newInstance();
        openFragment(dashboardFragment);
        walletTxt = findViewById(R.id.wallet_txt);
        walletConverted = findViewById(R.id.wallet_converted_txt);
        btnExit = findViewById(R.id.button_exit);
        btnSettings = findViewById(R.id.button_settings);

        setBtnExitClick();

        linearLayoutBkp = findViewById(R.id.bkpwallet);

        setLinearLayoutBkpClick();
    }

    private void setLinearLayoutBkpClick() {
        linearLayoutBkp.setOnClickListener(v -> {

            String msk = smartCashApplication.getDecryptedMSK(getPIN());

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Backup your wallet");
            alert.setMessage("Write down in a paper the code:");

            final FrameLayout frameView = new FrameLayout(this);
            alert.setView(frameView);

            final AlertDialog alertDialog = alert.create();
            LayoutInflater inflater = alertDialog.getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.wallet_dialog_backup, frameView);

            EditText txtMSC = dialogLayout.findViewById(R.id.txtMSC);
            TextView lblMSC = dialogLayout.findViewById(R.id.labelMSC);
            lblMSC.setText(msk);

            Button btnGoToCheckMSC = dialogLayout.findViewById(R.id.btnGoToCheckMSC);
            Button btnCheckMSC = dialogLayout.findViewById(R.id.btnCheckMSC);

            btnGoToCheckMSC.setOnClickListener(v1 -> {
                lblMSC.setVisibility(View.GONE);
                btnGoToCheckMSC.setVisibility(View.GONE);

                btnCheckMSC.setVisibility(View.VISIBLE);
                txtMSC.setVisibility(View.VISIBLE);
            });

            btnCheckMSC.setOnClickListener(v2 -> {
                if (!msk.trim().equals(txtMSC.getText().toString().trim()))
                    txtMSC.setError("Not the same code yet.");
                else {
                    AlertDialog.Builder builderDeleteMSK = new AlertDialog.Builder(this);
                    builderDeleteMSK.setTitle("Your code is OK!");
                    builderDeleteMSK.setMessage("*We don't recommend to keep it in the device.");

                    builderDeleteMSK.setPositiveButton("OK, It is safe now. Delete from device.", (dialog, id) -> {

                        smartCashApplication.deleteMSK();
                        linearLayoutBkp.setVisibility(View.GONE);
                        alertDialog.cancel();

                    });
                    builderDeleteMSK.setNegativeButton("Not yet. Keep it there for a while.", (dialog, id) -> {

                    });

                    AlertDialog dialogDeleteMSK = builderDeleteMSK.create();
                    dialogDeleteMSK.show();
                }
            });

            txtMSC.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!msk.trim().equals(txtMSC.getText().toString().trim()))
                        txtMSC.setError("Not the same code yet.");

                }
            });


            alertDialog.show();
        });
    }

    private void setBtnExitClick() {
        btnExit.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout?")
                    .setMessage("Are you sure you want to logout?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> navigateToLogin())
                    .setNegativeButton(android.R.string.no, null).show();
        });
    }

    private String getPIN() {
        Intent intent = getIntent();
        String extraPIN = intent.getStringExtra(Keys.KEY_PIN);
        if (extraPIN != null && !extraPIN.isEmpty()) {
            return extraPIN;
        }
        return null;
    }

    private void navigateToLogin() {
        Toast.makeText(MainActivity.this, "Redirecting to login...", Toast.LENGTH_SHORT).show();
        smartCashApplication.deleteMSK();
        smartCashApplication.deleteSharedPreferences(MainActivity.this);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void setSpinner(Spinner currentPriceSpinner) {

        adapter = new CoinSpinnerAdapter(this, android.R.layout.simple_spinner_item, coins);
        currentPriceSpinner.setAdapter(adapter);

        setSelectedCoinOnSpinner(currentPriceSpinner, coins);

        currentPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                saveSelectedCoin(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void saveSelectedCoin(Coin coin) {

        smartCashApplication.saveActualSelectedCoin(MainActivity.this, coin);
        setWalletValue();
        reloadCurrentFragment();
    }

    private void setSelectedCoinOnSpinner(Spinner currentPriceSpinner, ArrayList<Coin> coins) {
        getCoins();
        Coin selectedCoin = smartCashApplication.getActualSelectedCoin(this);
        if (selectedCoin != null) {
            for (int i = 0; i < coins.size(); i++) {
                if (selectedCoin.getValue().equals(coins.get(i).getValue()) && selectedCoin.getName().equals(coins.get(i).getName())) {
                    currentPriceSpinner.setSelection(i);
                }
            }
        }
        setWalletValue();
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

    private void getCoins() {

        if (this.coins == null)
            this.coins = smartCashApplication.getCurrentPrice(this);

    }

    public void setWalletValue() {

        User user = smartCashApplication.getUser(this);

        if (user == null) navigateToLogin();

        ArrayList<Wallet> wallets = user.getWallet();
        Double amount = 0.0;

        for (Wallet wallet : wallets) {
            amount += wallet.getBalance();
        }

        selectedCoin = smartCashApplication.getActualSelectedCoin(this);

        getCoins();

        if (selectedCoin != null && selectedCoin.getName().equalsIgnoreCase("USD")) {
            for (Coin auxcoin : this.coins) {
                if (auxcoin.getName().equalsIgnoreCase(selectedCoin.getName())) {
                    selectedCoin.setValue(auxcoin.getValue());
                    smartCashApplication.saveActualSelectedCoin(this, auxcoin);
                    break;
                }
            }
        }

        walletTxt.setText(getResources().getString(R.string.smartCash) + String.format("%.8f", amount));
        if (selectedCoin == null || selectedCoin.getName().equals("SMART")) {
            ArrayList<Coin> currentPrice = smartCashApplication.getCurrentPrice(this);
            walletConverted.setText("$ " + smartCashApplication.converterValue(amount, currentPrice.get(0).getValue()) + " " + currentPrice.get(0).getName());
        } else {
            //  walletConverted.setText("$ " + String.format("%.3f", amount / selectedCoin.getValue()));
            walletConverted.setText("$ " + smartCashApplication.converterValue(amount, selectedCoin.getValue()) + " " + selectedCoin.getName());
        }

    }

    private void reloadCurrentFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment instanceof DashboardFragment) {

            openFragment(DashboardFragment.newInstance());

        } else if (fragment instanceof ReceiveFragment) {

            openFragment(ReceiveFragment.newInstance());

        } else if (fragment instanceof SendFragment) {

            openFragment(SendFragment.newInstance());

        } else if (fragment instanceof TransactionFragment) {

            openFragment(TransactionFragment.newInstance());

        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}