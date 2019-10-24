package cc.smartcash.wallet

import cc.smartcash.wallet.Services.ExplorerAPIConfig
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ExplorerApiUnitTest {

    @Test
    fun getAddressBalance() {

        val address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq"

        val call = ExplorerAPIConfig().explorerService.getAddressBalance(address)

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            val body = r.body()

            assertNotNull(body)

            assertTrue(body!!.size > 0)

            assertNotNull(body[0])

            assertTrue(body[0].balance >= 0)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun GetAddressBalanceWithTransactions() {

        val address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq"

        val call = ExplorerAPIConfig(false).explorerService.getAddressBalanceWithTransactions(address)

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            assertNotNull(r.body())

            assertNotNull(r.body()!!.txs)

            assertNotNull(r.body()!!.addressbalance)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun GetLatestBlockHeight() {

        val address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq"

        val call = ExplorerAPIConfig(true).explorerService.getLatestBlockHeight()

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            assertNotNull(r.body())

            assertTrue(Integer.parseInt(r.body()!!) > 0)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun GetTransactionDetail() {

        val txid = "ddf00e20666b40c42017788b076b88f01fac8b5b3990f1df0537d187429377d1"

        val call = ExplorerAPIConfig(false).explorerService.getTransactionDetail(txid)

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            assertNotNull(r.body())

            assertNotNull(r.body()!![0])

            assertNotNull(r.body()!![0].inputs)

            assertNotNull(r.body()!![0].outputs)

            assertNotNull(r.body()!![0].transaction)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun GetBlockByHash() {

        val hash = "00000000000018b6ac47b40024b960913f454cb3044111eb7435deae92422e54"

        val call = ExplorerAPIConfig(false).explorerService.getBlockByHash(hash)

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            assertNotNull(r.body())

            assertNotNull(r.body()!!.hash)

            assertNotNull(r.body()!!.confirmations)

            assertTrue(r.body()!!.hash == hash)

            assertTrue(Integer.parseInt(r.body()!!.confirmations!!) > 0)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun GetBlockById() {

        val height = "1072000"

        val call = ExplorerAPIConfig(false).explorerService.getBlockById(height)

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            assertNotNull(r.body())

            assertNotNull(r.body()!!.hash)

            assertNotNull(r.body()!!.confirmations)

            assertTrue(r.body()!!.height == height)

            assertTrue(Integer.parseInt(r.body()!!.confirmations!!) > 0)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}