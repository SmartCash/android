package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Models.FullTransaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface TransactionService {

    @GET
    Call<FullTransaction> getTransaction(@Url String url);
}
