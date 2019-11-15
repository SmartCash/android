package cc.smartcash.smarthub.services

import cc.smartcash.smarthub.models.SmartTextRequest
import cc.smartcash.smarthub.models.SmartTextRoot
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SmartTextService {

    @POST("apiorder/createorder")
    fun sentPayment(@Header("Authorization-Token") auth: String, @Body sendPayment: SmartTextRequest): Call<SmartTextRoot>

}