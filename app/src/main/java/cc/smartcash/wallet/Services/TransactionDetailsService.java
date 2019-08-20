package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Models.TransactionDetails;
import cc.smartcash.wallet.Models.TransactionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TransactionDetailsService {

    @POST("wallet/paymentfee")
    Call<TransactionResponse> getDetails(@Header("Authorization") String auth, @Body TransactionDetails transactionDetails);
}
