package cc.smartcash.wallet.business


import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import cc.smartcash.wallet.utils.ApiUtil
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

                System.out.println("OK: Test to get list of business was successful")

                System.out.println("Message: " + businessListRootResponse.message())

                System.out.println("CODE: " + businessListRootResponse.code())

                System.out.println("IsError: " + businessListRootResponse.body().run { this?.isError })

                System.out.println("Count: " + businessListRootResponse.body().run { this?.items?.count() })

                System.out.println("Item[0]: " + businessListRootResponse.body().run { this?.items?.firstOrNull()?.stores?.first() })

                System.out.println("Items: " + businessListRootResponse.body().run { this?.items?.firstOrNull()?.stores })

            } else {

                System.out.println("NOK: Test to get list of business was unsuccessful")

                System.out.println("Message: " + businessListRootResponse.message())

                System.out.println("CODE: " + businessListRootResponse.code())

                System.out.println("IsError: " + businessListRootResponse.body().run { this?.isError })

                System.out.println("Count: " + businessListRootResponse.body().run { this?.items?.count() })

            }

            Assert.assertEquals(businessListRootResponse.code(), 200)

            Assert.assertTrue(parseInt(businessListRootResponse.body().run { this?.items?.count() }.toString()) >= 1)

        } catch (e: Exception) {
            System.out.println("Exception: " + e.message)
        }


    }

}