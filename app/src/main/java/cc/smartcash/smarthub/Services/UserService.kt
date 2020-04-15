package cc.smartcash.smarthub.Services


import cc.smartcash.smarthub.Models.InfoRequest
import cc.smartcash.smarthub.Models.User
import cc.smartcash.smarthub.Models.UserLogin
import cc.smartcash.smarthub.Models.WebWalletRootResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("user/info")
    fun getUser(@Header("Authorization") auth: String, @Body req: InfoRequest?): Call<WebWalletRootResponse<User?>>
}
