package cc.smartcash.wallet

import android.content.Context
import android.util.Log
import cc.smartcash.wallet.Models.LoginResponse
import cc.smartcash.wallet.Models.UserRegisterRequest
import cc.smartcash.wallet.Models.WalletPaymentFeeRequest
import cc.smartcash.wallet.Models.WebWalletUserAvailableRequest
import cc.smartcash.wallet.Services.WebWalletAPIConfig
import cc.smartcash.wallet.Utils.ApiUtil
import cc.smartcash.wallet.Utils.KEYS
import cc.smartcash.wallet.Utils.SmartCashApplication
import cc.smartcash.wallet.Utils.Util
import cc.smartcash.wallet.ViewModels.WalletViewModel
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.util.*

class WebWalletApiUnitTest {

    private val context: Context?
        get() = null

    @Test
    fun getToken() {

        val localIP = SmartCashApplication.getIPAddress(true)

        val call = WebWalletAPIConfig().webWalletAPIService.getToken(
                Util.getProperty(KEYS.CONFIG_TEST_USER, context!!)!!,
                Util.getProperty(KEYS.CONFIG_TEST_PASS, context!!)!!,
                "password",
                Util.getProperty(KEYS.CONFIG_CLIENT_ID, context!!)!!,

                "",
                "mobile",
                localIP,
                Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context!!)!!
        )

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            val body = r.body()

            assertNotNull(body)


            assertNotNull(body!!.accessToken)

            assertFalse(body.accessToken!!.isEmpty())


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun getUserInfo() {

        val localIP = SmartCashApplication.getIPAddress(true)

        var body: LoginResponse? = null

        var token: String? = ""

        val call = WebWalletAPIConfig().webWalletAPIService.getToken(
                Util.getProperty(KEYS.CONFIG_TEST_USER, context!!)!!,
                Util.getProperty(KEYS.CONFIG_TEST_PASS, context!!)!!,
                "password",
                Util.getProperty(KEYS.CONFIG_CLIENT_ID, context!!)!!,

                "",
                "mobile",
                localIP,
                Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context!!)!!
        )

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            body = r.body()

            assertNotNull(body)


            assertNotNull(body!!.accessToken)

            assertFalse(body.accessToken!!.isEmpty())

            token = body.accessToken

        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {

            val callUser = WebWalletAPIConfig().webWalletAPIService.getUser("Bearer " + token!!)

            val apiResponse = callUser.execute()

            assertNull(apiResponse.body()!!.error)

            assertNotNull(apiResponse.body())

            assertNotNull(apiResponse.body()!!.data)

            val user = apiResponse.body()!!.data

            assertNotNull(user!!.username)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun getUserContact() {

        val localIP = SmartCashApplication.getIPAddress(true)

        var body: LoginResponse? = null

        var token: String? = ""

        val call = WebWalletAPIConfig().webWalletAPIService.getToken(
                Util.getProperty(KEYS.CONFIG_TEST_USER, context!!)!!,
                Util.getProperty(KEYS.CONFIG_TEST_PASS, context!!)!!,
                "password",
                Util.getProperty(KEYS.CONFIG_CLIENT_ID, context!!)!!,

                "",
                "mobile",
                localIP,
                Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context!!)!!
        )

        assertNotNull("Call OK", call)

        try {

            val r = call.execute()

            assertTrue(r.isSuccessful)

            body = r.body()

            assertNotNull(body)


            assertNotNull(body!!.accessToken)

            assertFalse(body.accessToken!!.isEmpty())

            token = body.accessToken

        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {

            //token = "DBBCE3vsTIL-CM8XAVkCd2XNVdaL5QGwn3GdV1pg-GPVrxHGsPKL6K6LDcec6WQvFYlg0jAUuLyaTRSYSJ-JXK1Z29G6x2ktzcLgq9RrCm049s6gVLZLuq1pathUbINPmmOPSVKrtDEBcyc8ypW_j0zERXnkGOHYIFJBAbjfbRxv963DLvv0NZIE9ZyfFFB0CoWH_eCPuBYqmdoWrX7b5-kR9hM3XiSaQeqvtqXSI1CLMebyAZuogMSlKtHHWxmTVMNuV5Ye4rDu6J6Q8KaegXg0ypO63rZ61OWxi9hofG1Ig0Sf98IgsLXSqjIp7pL789qrG2FqtEZXv6qFCDR9RZdbGFk7ZYaAOerIX6EREkG62rxIdWVOfXuqLsQniOwmnHvqwwOmkyC3pA7GUr3Vvw";

            val callUser = WebWalletAPIConfig().webWalletAPIService.getUserContacts("Bearer " + token!!)

            val apiResponse = callUser.execute()

            println("Get contacts")

            assertNull(apiResponse.body()!!.error)

            assertNotNull(apiResponse.body())

            assertNotNull(apiResponse.body()!!.data)

            val contacts = apiResponse.body()!!.data

            println("Get contacts")

            for (contact in contacts!!) {
                println("Contact: ")
                println(contact.name)
                println(contact.email)
                println(contact.address)
            }

            assertNotNull(contacts)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun getNewMasterSecurityKey() {

        try {

            val callUser = WebWalletAPIConfig().webWalletAPIService.newMasterSecurityKey

            val apiResponse = callUser.execute()

            assertNull(apiResponse.body()!!.error)

            assertNotNull(apiResponse.body())

            assertNotNull(apiResponse.body()!!.data)

            val user = apiResponse.body()!!.data

            assertNotNull(user!!.recoveryKey)

            Log.d(TAG, user.recoveryKey)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun createNewUser() {

        try {

            val callUserRecoveryKey = WebWalletAPIConfig().webWalletAPIService.newMasterSecurityKey

            val apiResponseUserRecoveryKey = callUserRecoveryKey.execute()

            val userRecoveryKey = apiResponseUserRecoveryKey.body()!!.data

            Log.d(TAG, userRecoveryKey!!.recoveryKey)

            val useruuid = UUID.randomUUID().toString()

            val newUser = UserRegisterRequest()

            newUser.email = "$useruuid@testeandroidmobile.com"
            newUser.username = "$useruuid@testeandroidmobile.com"
            newUser.password = "123456"
            newUser.recoveryKey = userRecoveryKey.recoveryKey

            Log.d(TAG, useruuid)

            val callUser = WebWalletAPIConfig().webWalletAPIService.setUser(newUser)

            val apiResponse = callUser.execute()

            assertNull(apiResponse.body()!!.error)

            assertNotNull(apiResponse.body())

            assertNotNull(apiResponse.body()!!.data)

            val user = apiResponse.body()!!.data

            assertNotNull(user!!.username)

            Log.d(TAG, user.username)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun isUserAvailable() {

        try {

            val callUserRecoveryKey = WebWalletAPIConfig().webWalletAPIService.isUserAvailable(WebWalletUserAvailableRequest("fcb016e9-0408-4e4f-8a01-d86151fc605e@testeandroidmobile.com"))

            val response = callUserRecoveryKey.execute()

            println("Is user available: " + response.body()!!.data!!)


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Test
    fun calculateFee() {

        val feeRequest = WalletPaymentFeeRequest()
        feeRequest.fromAddress = "SQkTQxZ6KCTHJydEWE6sAyJm2Me4ryy8fF"
        feeRequest.recurrenceType = 3

        val callFee = ApiUtil.walletService.getFee("Bearer ${getTokenByLogin()}", feeRequest)

        try {
            val r = callFee.execute()


            System.out.println(r.body()?.data.toString())

        } catch (e: IOException) {
            Log.e(WalletViewModel.TAG, e.message)
        }

    }

    private fun getTokenByLogin(): String? {

        val localIP = SmartCashApplication.getIPAddress(true)

        val call = WebWalletAPIConfig().webWalletAPIService.getToken(
                Util.getProperty(KEYS.CONFIG_TEST_USER, context!!)!!,
                Util.getProperty(KEYS.CONFIG_TEST_PASS, context!!)!!,
                "password",
                Util.getProperty(KEYS.CONFIG_CLIENT_ID, context!!)!!,

                "",
                "mobile",
                localIP,
                Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context!!)!!
        )

        try {

            return call.execute().body()?.accessToken

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {

        val TAG = WebWalletApiUnitTest::class.java.simpleName
    }


}