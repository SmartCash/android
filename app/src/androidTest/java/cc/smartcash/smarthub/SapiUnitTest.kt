package cc.smartcash.smarthub

import cc.smartcash.smarthub.Services.SAPIConfig
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class SapiUnitTest {

    @Test
    fun getAddressBalance() {

        val address = "SScgRNAofPxYkCxVz3JbsJYX9XbZnvjHhq"

        val call = SAPIConfig().sapiService.getAddressBalance(address)

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            val body = r.body()

            assertNotNull(body)

            assertTrue(body!!.balance >= 0)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}