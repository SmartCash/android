package cc.smartcash.wallet.BO;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;

import cc.smartcash.wallet.Models.Coin;
import cc.smartcash.wallet.R;
import cc.smartcash.wallet.Utils.NetworkUtil;
import cc.smartcash.wallet.Utils.SmartCashApplication;
import cc.smartcash.wallet.Utils.ViewModelUtil;


public class PriceBO {

    Context context;

    private ArrayList<Coin> coins = new ArrayList<Coin>();

    private ViewModelUtil viewModelUtil = this.viewModelUtil == null ? new ViewModelUtil() : this.viewModelUtil;

    private SmartCashApplication smartCashApplication = this.smartCashApplication == null ? new SmartCashApplication(context) : this.smartCashApplication;

    public ArrayList<Coin> getCurrentPrice(Context context, Fragment owner, LifecycleOwner lifecycleOwner) {

        if (NetworkUtil.getInternetStatus(context)) {

            this.viewModelUtil.getCurrentPriceViewModel(owner).getCurrentPrices(context).observe(lifecycleOwner, currentPrices -> {

                if (currentPrices != null) {

                    coins = SmartCashApplication.convertToArrayList(currentPrices);

                } else {

                    Log.e(context.getString(R.string.tag_log_error), "Error to get current prices.");
                }

            });

        } else {

            coins = smartCashApplication.getCurrentPrice(context);

        }

        return coins;
    }


}

