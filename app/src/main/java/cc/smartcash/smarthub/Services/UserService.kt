package cc.smartcash.smarthub.Services


import cc.smartcash.smarthub.Models.User
import cc.smartcash.smarthub.Models.WebWalletRootResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {

    //@GET("user/my")
    //fun getUser(@Header("Authorization") auth: String): Call<WebWalletRootResponse<User?>>

    @GET("user/info")
    fun getUser(@Header("Authorization") auth: String): Call<WebWalletRootResponse<User?>>
}
