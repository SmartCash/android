package cc.smartcash.wallet.Services;


import cc.smartcash.wallet.Models.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {

    @GET("user/my")
    Call<ApiResponse> getUser(@Header("Authorization") String auth);
}
