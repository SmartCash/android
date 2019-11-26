package cc.smartcash.smarthub.utils

import cc.smartcash.smarthub.services.*

object ApiUtil {

    val userService: UserService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEB_WALLET).create(UserService::class.java)

    val userLoginService: UserLoginService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEB_WALLET).create(UserLoginService::class.java)

    val walletService: WalletService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEB_WALLET).create(WalletService::class.java)

    val currentPricesService: CurrentPricesService
        get() = RetrofitConfig.getClient(URLS.URL_CURRENT_PRICE_API).create(CurrentPricesService::class.java)

    val transactionService: TransactionService
        get() = RetrofitConfig.getClient(URLS.URL_INSIGHT_EXPLORER_API).create(TransactionService::class.java)

    val transactionDetailsService: TransactionDetailsService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEB_WALLET).create(TransactionDetailsService::class.java)

    val smartTextService: SmartTextService
        get() = RetrofitConfig.getClient(URLS.URL_API_SMART_TEXT).create(SmartTextService::class.java)

    val webWalletAPIService: WebWalletAPIService
        get() = RetrofitConfig.getClient(URLS.URL_API_WEB_WALLET).create(WebWalletAPIService::class.java)

    val businessAPIService: BusinessAPIService
        get() = RetrofitConfig.getClient(URLS.URL_API_BUSINESS).create(BusinessAPIService::class.java)

    val smartApiService: SmartAPIService
        get() = RetrofitConfig.getClient(URLS.URL_API_PRICE).create(SmartAPIService::class.java)

}
