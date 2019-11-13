package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.*
import retrofit2.Call
import retrofit2.http.*

interface WebWalletAPIService {

    @get:GET("user/newkey")
    val newMasterSecurityKey: Call<WebWalletRootResponse<UserRecoveryKey>>

    @POST("user/authenticate")
    @FormUrlEncoded
    fun getToken(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("grant_type") grantType: String,
                 @Field("client_id") clientId: String,
                 @Field("TwoFactorAuthentication") twoFactorAuthentication: String,
                 @Field("client_type") clientType: String,
                 @Field("client_ip") clientIp: String,
                 @Field("client_secret") clientSecret: String): Call<LoginResponse>

    @GET("user/my")
    fun getUser(@Header("Authorization") auth: String): Call<WebWalletRootResponse<User>>

    @GET("contact/my")
    fun getUserContacts(@Header("Authorization") auth: String): Call<WebWalletRootResponse<List<WebWalletContact>>>

    @POST("user")
    fun setUser(@Body auth: UserRegisterRequest): Call<WebWalletRootResponse<User>>

    @POST("user/available")
    fun isUserAvailable(@Body request: WebWalletUserAvailableRequest): Call<WebWalletRootResponse<Boolean>>
}
