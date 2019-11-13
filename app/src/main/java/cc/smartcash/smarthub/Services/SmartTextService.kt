package cc.smartcash.smarthub.Services

import cc.smartcash.smarthub.Models.SmartTextRequest
import cc.smartcash.smarthub.Models.SmartTextRoot
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SmartTextService {

    @POST("apiorder/createorder")
    fun sentPayment(@Header("Authorization-Token") auth: String, @Body sendPayment: SmartTextRequest): Call<SmartTextRoot>

}