package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Models.SmartTextRequest;
import cc.smartcash.wallet.Models.SmartTextRoot;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SmartTextService {

    @POST("apiorder/createorder")
    Call<SmartTextRoot> sentPayment(@Header("Authorization-Token") String auth, @Body SmartTextRequest sendPayment);

}