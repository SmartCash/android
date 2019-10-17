package cc.smartcash.wallet.Utils;

import cc.smartcash.wallet.Services.CurrentPricesService;
import cc.smartcash.wallet.Services.RetrofitConfig;
import cc.smartcash.wallet.Services.SmartTextService;
import cc.smartcash.wallet.Services.TransactionDetailsService;
import cc.smartcash.wallet.Services.TransactionService;
import cc.smartcash.wallet.Services.UserLoginService;
import cc.smartcash.wallet.Services.UserService;
import cc.smartcash.wallet.Services.WalletService;
import cc.smartcash.wallet.Services.WebWalletAPIService;

public class ApiUtil {

    public static UserService getUserService() {
        return RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(UserService.class);
    }

    public static UserLoginService getUserLoginService() {
        return RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(UserLoginService.class);
    }

    public static WalletService getWalletService() {
        return RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(WalletService.class);
    }

    public static CurrentPricesService getCurrentPricesService() {
        return RetrofitConfig.getClient(URLS.URL_CURRENT_PRICE_API).create(CurrentPricesService.class);
    }

    public static TransactionService getTransactionService() {
        return RetrofitConfig.getClient(URLS.URL_INSIGHT_EXPLORER_API).create(TransactionService.class);
    }

    public static TransactionDetailsService getTransactionDetailsService() {
        return RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(TransactionDetailsService.class);
    }

    public static SmartTextService getSmartTextService() {
        return RetrofitConfig.getClient(URLS.URL_API_SMARTTEXT).create(SmartTextService.class);
    }

    public static WebWalletAPIService getWebWalletAPIService() {
        return RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(WebWalletAPIService.class);
    }

}
