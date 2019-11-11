package cc.smartcash.wallet.Services

import cc.smartcash.wallet.Models.business.BusinessListRootResponse
import retrofit2.Call
import retrofit2.http.GET

interface BusinessAPIService {

    @get:GET("businesses")
    val getListOfBusiness: Call<BusinessListRootResponse>

}
