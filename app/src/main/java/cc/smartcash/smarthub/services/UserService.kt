package cc.smartcash.smarthub.services


import cc.smartcash.smarthub.models.User
import cc.smartcash.smarthub.models.WebWalletRootResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {

    @GET("user/my")
    fun getUser(@Header("Authorization") auth: String): Call<WebWalletRootResponse<User?>>
}
