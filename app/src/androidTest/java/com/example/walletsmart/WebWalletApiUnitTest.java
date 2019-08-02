package com.example.walletsmart;

import android.util.Log;
import android.widget.Toast;

import com.example.walletsmart.Models.ApiResponse;
import com.example.walletsmart.Models.ExplorerApiAddressBalance;
import com.example.walletsmart.Models.ExplorerApiAddressBalanceWithTxs;
import com.example.walletsmart.Models.ExplorerApiBlock;
import com.example.walletsmart.Models.ExplorerApiTransactionDetail;
import com.example.walletsmart.Models.LoginResponse;
import com.example.walletsmart.Models.User;
import com.example.walletsmart.Services.ExplorerAPIConfig;
import com.example.walletsmart.Services.WebWalletAPIConfig;
import com.example.walletsmart.Utils.Utils;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WebWalletApiUnitTest {

    @Test
    public void getToken(){

        String localIP = Utils.getIPAddress(true);

        Call<LoginResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getToken(
                "enriquesouza6",
                "yfKN8v4$",
                "password",
                "81d46070-686b-4975-9c29-9ebc867a3c4e",
                "",
                "mobile",
                localIP,
                "B3EIldyQp5Hl2CXZdP8MeYmDl3gXb3tan4XCNg0ZK0"
        );

        assertNotNull("Call OK", call);

        try {

            Response<LoginResponse> r = call.execute();

            assertTrue(r.isSuccessful());

            LoginResponse body = r.body();

            assertNotNull(body);


            assertNotNull(body.getAccessToken());

            assertFalse(body.getAccessToken().isEmpty());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void getUserInfo(){

        String localIP = Utils.getIPAddress(true);

        LoginResponse body = null;

        String token = "";

        Call<LoginResponse> call = new WebWalletAPIConfig().getWebWalletAPIService().getToken(
                "enriquesouza6",
                "yfKN8v4$",
                "password",
                "81d46070-686b-4975-9c29-9ebc867a3c4e",
                "",
                "mobile",
                localIP,
                "B3EIldyQp5Hl2CXZdP8MeYmDl3gXb3tan4XCNg0ZK0"
        );

        assertNotNull("Call OK", call);

        try {

            Response<LoginResponse> r = call.execute();

            assertTrue(r.isSuccessful());

            body = r.body();

            assertNotNull(body);


            assertNotNull(body.getAccessToken());

            assertFalse(body.getAccessToken().isEmpty());

            token = body.getAccessToken();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            Call<ApiResponse> callUser = new WebWalletAPIConfig().getWebWalletAPIService().getUser("Bearer " + token);

            Response<ApiResponse> apiResponse = callUser.execute();

            assertNull(apiResponse.body().getError());

            assertNotNull(apiResponse.body());

            assertNotNull(apiResponse.body().getData());

            User user = apiResponse.body().getData();

            assertNotNull(user.getUsername());



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}