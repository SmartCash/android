package cc.smartcash.wallet.ViewModels

import android.content.Context
import android.util.Log
import cc.smartcash.wallet.Models.User
import cc.smartcash.wallet.Models.UserLogin
import cc.smartcash.wallet.Utils.*
import java.io.IOException

object LoginViewModel {

    val TAG: String = LoginViewModel::class.java.simpleName

    fun getSyncToken(userLogin: UserLogin, context: Context): String? {

        val localIP = SmartCashApplication.getIPAddress(true)

        val call = ApiUtil.userLoginService.getToken(
                userLogin.username!!,
                userLogin.password!!,
                "password",
                Util.getProperty(KEYS.CONFIG_CLIENT_ID, context)!!,
                userLogin.twoFactorAuthentication!!,
                "mobile",
                localIP,
                Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context)!!
        )

        try {
            val r = call.execute()
            val body = r.body()

            if (r.errorBody() != null) {

                return "error:" + r.errorBody()!!.string()
            }
            return body!!.accessToken

        } catch (e: IOException) {
            Log.e(TAG, e.message)
        }

        return null
    }

    fun getSyncUser(token: String, context: Context): User? {

        val isInternetOn = NetworkUtil.getInternetStatus(context)

        if (isInternetOn) {
            try {
                val callUser = ApiUtil.userService.getUser("Bearer $token")

                val apiResponse = callUser.execute()

                return apiResponse.body()!!.data

            } catch (e: IOException) {
                e.printStackTrace()
            }


        }

        return null
    }

}