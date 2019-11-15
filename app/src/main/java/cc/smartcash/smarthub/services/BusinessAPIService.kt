package cc.smartcash.smarthub.services

import cc.smartcash.smarthub.models.business.BusinessListRootResponse
import retrofit2.Call
import retrofit2.http.GET

interface BusinessAPIService {

    @get:GET("businesses")
    val getListOfBusiness: Call<BusinessListRootResponse>

}
