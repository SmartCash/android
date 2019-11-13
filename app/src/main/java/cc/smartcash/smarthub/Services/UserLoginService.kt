package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserLoginService {

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
}