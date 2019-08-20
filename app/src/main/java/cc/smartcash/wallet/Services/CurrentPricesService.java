package cc.smartcash.wallet.Services;

import com.fasterxml.jackson.databind.JsonNode;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrentPricesService {

    @GET("wallet/getcurrentpricewithcoin")
    Call<JsonNode> getCurrentPrices();
}