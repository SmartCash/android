package cc.smartcash.wallet.Utils;

import android.content.Context;

import cc.smartcash.wallet.Services.CurrentPricesService;
import cc.smartcash.wallet.Services.RetrofitConfig;
import cc.smartcash.wallet.Services.TransactionDetailsService;
import cc.smartcash.wallet.Services.TransactionService;
import cc.smartcash.wallet.Services.UserLoginService;
import cc.smartcash.wallet.Services.UserService;
import cc.smartcash.wallet.Services.WalletService;

public class ApiUtils {
    public static final String MAIN_API = "https://smartcashapi.azurewebsites.net/api/";
    public static final String TRANSACTION_API = "https://insight.smartcash.cc/api/tx/";
    public static final String CURRENT_PRICE_API = "https://wallet.smartcash.cc/api/";
    RetrofitConfig retrofitConfig;
    Context context;
    public ApiUtils(Context context) {
        this.context = context;
        retrofitConfig = new RetrofitConfig();
    }

    public UserService getUserService() {
        return this.retrofitConfig.getClient(MAIN_API).create(UserService.class);
    }

    public UserLoginService getUserLoginService() {
        return this.retrofitConfig.getClient(MAIN_API).create(UserLoginService.class);
    }

    public WalletService getWalletService() {
        return this.retrofitConfig.getClient(MAIN_API).create(WalletService.class);
    }

    public CurrentPricesService getCurrentPricesService() {
        return this.retrofitConfig.getClient(CURRENT_PRICE_API).create(CurrentPricesService.class);
    }

    public TransactionService getTransactionService() {
        return this.retrofitConfig.getClient(TRANSACTION_API).create(TransactionService.class);
    }

    public TransactionDetailsService getTransactionDetailsService() {
        return this.retrofitConfig.getClient(MAIN_API).create(TransactionDetailsService.class);
    }
}
