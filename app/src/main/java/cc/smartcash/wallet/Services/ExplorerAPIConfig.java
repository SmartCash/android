package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Utils.URLS;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ExplorerAPIConfig {

    private final Retrofit retrofit;

    public ExplorerAPIConfig() {
        this.retrofit = RetrofitConfig.getClient(URLS.URL_API_EXPLORER);
    }

    public ExplorerAPIConfig(boolean scalar) {
        if (scalar) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URLS.URL_API_EXPLORER)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        } else {
            this.retrofit = RetrofitConfig.getClient(URLS.URL_API_EXPLORER);
        }
    }

    public ExplorerAPIService getExplorerService() {
        return this.retrofit.create(ExplorerAPIService.class);
    }

}