package cc.smartcash.wallet.business


import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import cc.smartcash.smarthub.utils.ApiUtil
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Integer.parseInt

@LargeTest
@RunWith(AndroidJUnit4::class)
class BusinessTest {

    @Test
    fun getBusiness() {

        try {

            val businessListRootResponse = ApiUtil.businessAPIService.getListOfBusiness.execute()

            if (businessListRootResponse.isSuccessful) {

                println("OK: Test to get list of business was successful")

                println("Message: " + businessListRootResponse.message())

                println("CODE: " + businessListRootResponse.code())

                println("IsError: " + businessListRootResponse.body().run { this?.isError })

                println("Count: " + businessListRootResponse.body().run { this?.items?.count() })

                println("Item[0]: " + businessListRootResponse.body().run { this?.items?.firstOrNull()?.stores?.first() })

                println("Items: " + businessListRootResponse.body().run { this?.items?.firstOrNull()?.stores })

            } else {

                println("NOK: Test to get list of business was unsuccessful")

                println("Message: " + businessListRootResponse.message())

                println("CODE: " + businessListRootResponse.code())

                println("IsError: " + businessListRootResponse.body().run { this?.isError })

                println("Count: " + businessListRootResponse.body().run { this?.items?.count() })

            }

            Assert.assertEquals(businessListRootResponse.code(), 200)

            Assert.assertTrue(parseInt(businessListRootResponse.body().run { this?.items?.count() }.toString()) >= 1)

        } catch (e: Exception) {
            println("Exception: " + e.message)
        }


    }

}