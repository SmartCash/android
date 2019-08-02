package com.example.walletsmart;

import com.example.walletsmart.Models.SapiAddressBalance;
import com.example.walletsmart.Services.SAPIConfig;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SapiUnitTest {

    @Test
    public void getAddressBalance() {

        String address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq";

        Call<SapiAddressBalance> call = new SAPIConfig().getSapiService().GetAddressBalance(address);

        assertNotNull("Call OK", call);

        try {

            Response<SapiAddressBalance> r = call.execute();

            assertTrue(r.isSuccessful());

            SapiAddressBalance body = r.body();

            assertNotNull(body);

            assertTrue(body.getBalance() >= 0);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}