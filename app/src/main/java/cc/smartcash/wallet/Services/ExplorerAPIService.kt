package cc.smartcash.wallet.Services


import cc.smartcash.wallet.Models.ExplorerApiAddressBalance
import cc.smartcash.wallet.Models.ExplorerApiAddressBalanceWithTxs
import cc.smartcash.wallet.Models.ExplorerApiBlock
import cc.smartcash.wallet.Models.ExplorerApiTransactionDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExplorerAPIService {

    @GET("getLatestBlockHeight")
    fun getLatestBlockHeight(): Call<String>

    @GET("getAddressBalance/{address}")
    fun getAddressBalance(@Path("address") address: String): Call<Array<ExplorerApiAddressBalance>>

    @GET("getAddressBalanceWithTransactions/{address}")
    fun getAddressBalanceWithTransactions(@Path("address") address: String): Call<ExplorerApiAddressBalanceWithTxs>

    @GET("getBlockById/{height}")
    fun getBlockById(@Path("height") height: String): Call<ExplorerApiBlock>

    @GET("getBlockByHash/{hash}")
    fun getBlockByHash(@Path("hash") hash: String): Call<ExplorerApiBlock>

    @GET("getTransactionDetail/{txid}")
    fun getTransactionDetail(@Path("txid") txid: String): Call<Array<ExplorerApiTransactionDetail>>

}