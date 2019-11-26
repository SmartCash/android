package cc.smartcash.smarthub

import cc.smartcash.smarthub.utils.ApiUtil
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

class SmartApiUnitTest {

    @Test
    fun getDefaultPrices() {

        val call = ApiUtil.smartApiService.getDefaultPrices()

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            assertNotNull(r.body())

            assertTrue(r.body()!!.count > 0)

            assertEquals(r.body()!!.isError, false)

            assertEquals(r.body()!!.status.toLong(), 200)

            assertNotNull(r.body()!!.items)

            assertTrue(r.body()!!.items!!.isNotEmpty())

            assertNotNull(r.body()!!.items!![0])

            assertNotNull(r.body()!!.items!![0].currencies)

            assertTrue(r.body()!!.items!![0].currencies!!.brl > 0)

            assertTrue(r.body()!!.items!![0].currencies!!.usd > 0)

            assertTrue(r.body()!!.items!![0].currencies!!.eur > 0)

            assertTrue(r.body()!!.items!![0].currencies!!.gbp > 0)


        } catch (e: IOException) {
            e.printStackTrace()
        }


    }
}