package cc.smartcash.wallet.Utils

import cc.smartcash.wallet.Services.*

object ApiUtil {

    val userService: UserService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(UserService::class.java)

    val userLoginService: UserLoginService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(UserLoginService::class.java)

    val walletService: WalletService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(WalletService::class.java)

    val currentPricesService: CurrentPricesService
        get() = RetrofitConfig.getClient(URLS.URL_CURRENT_PRICE_API).create(CurrentPricesService::class.java)

    val transactionService: TransactionService
        get() = RetrofitConfig.getClient(URLS.URL_INSIGHT_EXPLORER_API).create(TransactionService::class.java)

    val transactionDetailsService: TransactionDetailsService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(TransactionDetailsService::class.java)

    val smartTextService: SmartTextService
        get() = RetrofitConfig.getClient(URLS.URL_API_SMARTTEXT).create(SmartTextService::class.java)

    val webWalletAPIService: WebWalletAPIService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEBWALLET).create(WebWalletAPIService::class.java)

}
