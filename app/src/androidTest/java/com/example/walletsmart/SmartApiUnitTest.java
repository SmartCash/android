package com.example.walletsmart;

import com.example.walletsmart.Models.SmartApiDefaultPrice;
import com.example.walletsmart.Services.SmartAPIConfig;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SmartApiUnitTest {

    @Test
    public void GetDefaultPrices() {

        Call<SmartApiDefaultPrice> call = new SmartAPIConfig().getSmartApiService().GetDefaultPrices();

        assertNotNull("Call OK", call);

        try {

            Response<SmartApiDefaultPrice> r = call.execute();

            assertTrue(r.isSuccessful());

            assertNotNull(r.body());

            assertTrue(r.body().getCount() > 0);

            assertEquals(r.body().getIsError(), false);

            assertEquals(r.body().getStatus(), 200);

            assertNotNull(r.body().getItems());

            assertTrue(r.body().getItems().size() > 0);

            assertNotNull(r.body().getItems().get(0));

            assertNotNull(r.body().getItems().get(0).getCurrencies());

            assertTrue(r.body().getItems().get(0).getCurrencies().getBrl() > 0);

            assertTrue(r.body().getItems().get(0).getCurrencies().getUsd() > 0);

            assertTrue(r.body().getItems().get(0).getCurrencies().getEur() > 0);

            assertTrue(r.body().getItems().get(0).getCurrencies().getGbp() > 0);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}