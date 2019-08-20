package cc.smartcash.wallet.Services;


import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {

    @GET("user/my")
    Call<WebWalletRootResponse<User>> getUser(@Header("Authorization") String auth);
}
