package com.example.walletsmart.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.walletsmart.Models.FullTransaction;
import com.example.walletsmart.Models.Transaction;
import com.example.walletsmart.Models.TransactionDetails;
import com.example.walletsmart.Models.TransactionResponse;
import com.example.walletsmart.R;
import com.example.walletsmart.Utils.Utils;
import com.example.walletsmart.ViewModels.TransactionViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionActivity extends AppCompatActivity {
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.summary_title)
    TextView summaryTitle;
    @BindView(R.id.size_label)
    TextView sizeLabel;
    @BindView(R.id.fee_rate_label)
    TextView feeRateLabel;
    @BindView(R.id.received_time_label)
    TextView receivedTimeLabel;
    @BindView(R.id.mined_time_label)
    TextView minedTimeLabel;
    @BindView(R.id.inclued_in_block_label)
    TextView incluedInBlockLabel;
    @BindView(R.id.transaction_hash)
    TextView transactionHash;
    @BindView(R.id.txt_size)
    TextView txtSize;
    @BindView(R.id.txt_fee_rate)
    TextView txtFeeRate;
    @BindView(R.id.txt_received_time)
    TextView txtReceivedTime;
    @BindView(R.id.txt_mined_time)
    TextView txtMinedTime;
    @BindView(R.id.txt_included_in_block)
    TextView txtIncludedInBlock;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.btn_details)
    Button btnDetails;
    private String hash;
    private String token;
    private Utils utils;
    private TransactionDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getColor(R.color.colorAccent)));

        utils = new Utils();

        token = utils.getToken(this);

        details = new TransactionDetails();

        hash = getIntent().getStringExtra("TransactionHash");
        details.setAmount(getIntent().getDoubleExtra("Amount", 0.0));
        details.setFromAddress(utils.getWallet(this).getAddress());
        details.setToAddress(getIntent().getStringExtra("ToAddress"));

        if (hash != null)
            getTransaction(hash);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setVisibility(Boolean isLoading) {
        if (!isLoading) {
            activityTitle.setVisibility(View.VISIBLE);
            transactionHash.setVisibility(View.VISIBLE);
            summaryTitle.setVisibility(View.VISIBLE);
            sizeLabel.setVisibility(View.VISIBLE);
            txtSize.setVisibility(View.VISIBLE);
            feeRateLabel.setVisibility(View.VISIBLE);
            txtFeeRate.setVisibility(View.VISIBLE);
            receivedTimeLabel.setVisibility(View.VISIBLE);
            txtReceivedTime.setVisibility(View.VISIBLE);
            minedTimeLabel.setVisibility(View.VISIBLE);
            txtMinedTime.setVisibility(View.VISIBLE);
            incluedInBlockLabel.setVisibility(View.VISIBLE);
            txtIncludedInBlock.setVisibility(View.VISIBLE);
            btnDetails.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }

    public void setData(FullTransaction transaction, TransactionResponse transactionDetails) {
        transactionHash.setText(transaction.getBlockhash());
        txtSize.setText(String.valueOf(transaction.getSize()));
        txtFeeRate.setText(String.valueOf(transaction.getFees()));
        txtReceivedTime.setText("Jul 5, 2019 8:38:56 PM");
        txtMinedTime.setText("Jul 5, 2019 8:38:56 PM");
        txtIncludedInBlock.setText(transaction.getBlockhash());
    }

    public void getTransaction(String hash) {
        TransactionViewModel model = ViewModelProviders.of(this).get(TransactionViewModel.class);

        model.getTransaction(hash, TransactionActivity.this).observe(TransactionActivity.this, transaction -> {
            if (transaction != null) {
                getDetails(transaction);
                Log.e("Transaction", transaction.getTxid());
            } else {
                Log.e("Erro", "Não foi possível buscar a transaction!");
            }
        });
    }

    public void getDetails(FullTransaction transaction) {
        TransactionViewModel model = ViewModelProviders.of(this).get(TransactionViewModel.class);

        model.getTransactionDetails(token, this.details, TransactionActivity.this).observe(TransactionActivity.this, transactionDetails -> {
            if (transactionDetails != null) {
                setVisibility(false);
                setData(transaction, transactionDetails);
                Log.e("Transaction", transaction.getTxid());
            } else {
                setVisibility(false);
                setData(transaction, transactionDetails);
                Log.e("Erro", "Não foi possível buscar os detalhes da transaction!");
            }
        });
    }

    @OnClick(R.id.btn_details)
    public void onViewClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://insight.smartcash.cc/tx/" + this.hash));
        this.startActivity(browserIntent);
    }
}
