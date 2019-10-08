package cc.smartcash.wallet.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.smartcash.wallet.Models.FullTransaction;
import cc.smartcash.wallet.Models.TransactionDetails;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.KEYS;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.URLS;
import cc.smartcash.wallet.ViewModels.TransactionViewModel;

public class TransactionActivity extends AppCompatActivity {

    public static final String TAG = TransactionActivity.class.getSimpleName();

    private String hash;
    private String token;
    private SmartCashApplication smartCashApplication;
    private TransactionDetails details;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(this.getColor(R.color.colorAccent)));
        }

        smartCashApplication = new SmartCashApplication(getApplicationContext());

        token = smartCashApplication.getToken(this);

        details = new TransactionDetails();

        hash = getIntent().getStringExtra(KEYS.KEY_TRANSACTION_HASH);
        details.setAmount(getIntent().getDoubleExtra(KEYS.KEY_TRANSACTION_AMOUNT, 0.0));
        details.setFromAddress(smartCashApplication.getWallet(this).getAddress());
        details.setToAddress(getIntent().getStringExtra(KEYS.KEY_TRANSACTION_TO_ADDRESS));

        if (hash != null)
            getTransaction(hash);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.btn_details)
    public void onViewClicked() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLS.URL_INSIGHT_EXPLORER + this.hash));
        this.startActivity(browserIntent);
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

    public void setData(FullTransaction transaction) {

        transactionHash.setText(transaction.getTxid());
        txtSize.setText(String.valueOf(transaction.getSize()));
        txtFeeRate.setText(String.valueOf(transaction.getFees()));
        txtReceivedTime.setText(getDate(transaction.getTime()));
        txtMinedTime.setText(getDate(transaction.getBlocktime()));
        txtIncludedInBlock.setText(transaction.getBlockhash());

    }

    private String getDate(long epoch) {
        Date date = new Date(epoch * 1000L);
        DateFormat format = new SimpleDateFormat(KEYS.KEY_DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(KEYS.KEY_DATE_TIMEZONE));
        return format.format(date);
    }

    public void getTransaction(String hash) {
        TransactionViewModel model = ViewModelProviders.of(this).get(TransactionViewModel.class);

        model.getTransaction(hash, TransactionActivity.this).observe(TransactionActivity.this, transaction -> {
            if (transaction != null) {
                getDetails(transaction);
                Log.e(TAG, transaction.getTxid());
            } else {
                Log.e(TAG, getString(R.string.transaction_error_message));
            }
        });
    }

    public void getDetails(FullTransaction transaction) {
        TransactionViewModel model = ViewModelProviders.of(this).get(TransactionViewModel.class);

        model.getTransactionDetails(token, this.details, TransactionActivity.this).observe(TransactionActivity.this, transactionDetails -> {
            if (transactionDetails != null) {
                setVisibility(false);
                setData(transaction);
                Log.e(TAG, transaction.getTxid());
            } else {
                setVisibility(false);
                setData(transaction);
                Log.e(TAG, getString(R.string.transaction_detail_error_message));
            }
        });
    }

}
