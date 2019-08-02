package com.example.walletsmart;

import com.example.walletsmart.Models.ExplorerApiAddressBalance;
import com.example.walletsmart.Models.ExplorerApiAddressBalanceWithTxs;
import com.example.walletsmart.Models.ExplorerApiBlock;
import com.example.walletsmart.Models.ExplorerApiTransactionDetail;
import com.example.walletsmart.Services.ExplorerAPIConfig;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExplorerApiUnitTest {

    @Test
    public void getAddressBalance(){

        String address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq";

        Call<ExplorerApiAddressBalance[]> call = new ExplorerAPIConfig().getExplorerService().GetAddressBalance(address);

        assertNotNull("Call OK", call);

        try {

            Response<ExplorerApiAddressBalance[]> r = call.execute();

            assertTrue(r.isSuccessful());

            ExplorerApiAddressBalance[] body = r.body();

            assertNotNull(body);

            assertTrue(body.length > 0);

            assertNotNull(body[0]);

            assertTrue(body[0].getBalance() >= 0);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void GetAddressBalanceWithTransactions(){

        String address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq";

        Call<ExplorerApiAddressBalanceWithTxs> call = new ExplorerAPIConfig(false).getExplorerService().GetAddressBalanceWithTransactions(address);

        assertNotNull("Call OK", call);

        try {

            Response<ExplorerApiAddressBalanceWithTxs> r = call.execute();

            assertTrue(r.isSuccessful());

            assertNotNull(r.body());

            assertNotNull(r.body().getTxs());

            assertNotNull(r.body().getAddressbalance());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void GetLatestBlockHeight(){

        String address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq";

        Call<String> call = new ExplorerAPIConfig(true).getExplorerService().GetLatestBlockHeight();

        assertNotNull("Call OK", call);

        try {

            Response<String> r = call.execute();

            assertTrue(r.isSuccessful());

            assertNotNull(r.body());

            assertTrue(Integer.parseInt(r.body()) > 0);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void GetTransactionDetail(){

        String txid = "ddf00e20666b40c42017788b076b88f01fac8b5b3990f1df0537d187429377d1";

        Call<ExplorerApiTransactionDetail[]> call = new ExplorerAPIConfig(false).getExplorerService().GetTransactionDetail(txid);

        assertNotNull("Call OK", call);

        try {

            Response<ExplorerApiTransactionDetail[]> r = call.execute();

            assertTrue(r.isSuccessful());

            assertNotNull(r.body());

            assertNotNull(r.body()[0]);

            assertNotNull(r.body()[0].getInputs());

            assertNotNull(r.body()[0].getOutputs());

            assertNotNull(r.body()[0].getTransaction());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void GetBlockByHash(){

        String hash = "00000000000018b6ac47b40024b960913f454cb3044111eb7435deae92422e54";

        Call<ExplorerApiBlock> call = new ExplorerAPIConfig(false).getExplorerService().GetBlockByHash(hash);

        assertNotNull("Call OK", call);

        try {

            Response<ExplorerApiBlock> r = call.execute();

            assertTrue(r.isSuccessful());

            assertNotNull(r.body());

            assertNotNull(r.body().getHash());

            assertNotNull(r.body().getConfirmations());

            assertTrue(r.body().getHash().equals(hash));

            assertTrue(Integer.parseInt(r.body().getConfirmations()) > 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void GetBlockById(){

        String height = "1072000";

        Call<ExplorerApiBlock> call = new ExplorerAPIConfig(false).getExplorerService().GetBlockById(height);

        assertNotNull("Call OK", call);

        try {

            Response<ExplorerApiBlock> r = call.execute();

            assertTrue(r.isSuccessful());

            assertNotNull(r.body());

            assertNotNull(r.body().getHash());

            assertNotNull(r.body().getConfirmations());

            assertTrue(r.body().getHeight().equals(height));

            assertTrue(Integer.parseInt(r.body().getConfirmations()) > 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }}