package cc.smartcash.wallet.Services;


import cc.smartcash.wallet.Models.ExplorerApiAddressBalance;
import cc.smartcash.wallet.Models.ExplorerApiAddressBalanceWithTxs;
import cc.smartcash.wallet.Models.ExplorerApiBlock;
import cc.smartcash.wallet.Models.ExplorerApiTransactionDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExplorerAPIService {

    @GET("GetLatestBlockHeight")
    Call<String> GetLatestBlockHeight();

    @GET("GetAddressBalance/{address}")
    Call<ExplorerApiAddressBalance[]> GetAddressBalance(@Path("address") String address);

    @GET("GetAddressBalanceWithTransactions/{address}")
    Call<ExplorerApiAddressBalanceWithTxs> GetAddressBalanceWithTransactions(@Path("address") String address);

    @GET("GetBlockById/{height}")
    Call<ExplorerApiBlock> GetBlockById(@Path("height") String height);

    @GET("GetBlockByHash/{hash}")
    Call<ExplorerApiBlock> GetBlockByHash(@Path("hash") String hash);

    @GET("GetTransactionDetail/{txid}")
    Call<ExplorerApiTransactionDetail[]> GetTransactionDetail(@Path("txid") String txid);

}