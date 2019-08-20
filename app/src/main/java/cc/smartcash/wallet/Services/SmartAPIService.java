package cc.smartcash.wallet.Services;


import cc.smartcash.wallet.Models.SmartApiDefaultPrice;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SmartAPIService {

    @GET("exchange/currencies")
    Call<SmartApiDefaultPrice> GetDefaultPrices();
}