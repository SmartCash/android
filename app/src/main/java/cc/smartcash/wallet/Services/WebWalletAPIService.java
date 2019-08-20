package cc.smartcash.wallet.Services;

import java.util.List;

import cc.smartcash.wallet.Models.LoginResponse;
import cc.smartcash.wallet.Models.User;
import cc.smartcash.wallet.Models.WebWalletContact;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WebWalletAPIService {

    @POST("user/authenticate")
    @FormUrlEncoded
    Call<LoginResponse> getToken(@Field("username") String username,
                                 @Field("password") String password,
                                 @Field("grant_type") String grantType,
                                 @Field("client_id") String clientId,
                                 @Field("TwoFactorAuthentication") String twoFactorAuthentication,
                                 @Field("client_type") String clientType,
                                 @Field("client_ip") String clientIp,
                                 @Field("client_secret") String clientSecret);

    @GET("user/my")
    Call<WebWalletRootResponse<User>> getUser(@Header("Authorization") String auth);

    @GET("contact/my")
    Call<WebWalletRootResponse<List<WebWalletContact>>> getUserContacts(@Header("Authorization") String auth);

}
