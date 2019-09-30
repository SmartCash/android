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

    RetrofitConfig retrofitConfig;
    Context context;

    public ApiUtils(Context context) {
        this.context = context;
        retrofitConfig = new RetrofitConfig();
    }

    public UserService getUserService() {
        return RetrofitConfig.getClient(ConstantsURLS.URL_API_WEBWALLET).create(UserService.class);
    }

    public UserLoginService getUserLoginService() {
        return RetrofitConfig.getClient(ConstantsURLS.URL_API_WEBWALLET).create(UserLoginService.class);
    }

    public WalletService getWalletService() {
        return RetrofitConfig.getClient(ConstantsURLS.URL_API_WEBWALLET).create(WalletService.class);
    }

    public CurrentPricesService getCurrentPricesService() {
        return RetrofitConfig.getClient(ConstantsURLS.URL_CURRENT_PRICE_API).create(CurrentPricesService.class);
    }

    public TransactionService getTransactionService() {
        return RetrofitConfig.getClient(ConstantsURLS.URL_INSIGHT_EXPLORER).create(TransactionService.class);
    }

    public TransactionDetailsService getTransactionDetailsService() {
        return RetrofitConfig.getClient(ConstantsURLS.URL_API_WEBWALLET).create(TransactionDetailsService.class);
    }
}
