package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Models.ApiResponse;
import cc.smartcash.wallet.Models.ContactResponse;
import cc.smartcash.wallet.Models.LoginResponse;
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
    Call<ApiResponse> getUser(@Header("Authorization") String auth);

    @GET("contact/my")
    Call<ContactResponse> getUserContacts(@Header("Authorization") String auth);

}
