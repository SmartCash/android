package cc.smartcash.wallet.ViewModels

import android.content.Context
import android.util.Log
import cc.smartcash.wallet.Models.User
import cc.smartcash.wallet.Models.UserLogin
import cc.smartcash.wallet.Models.WebWalletException
import cc.smartcash.wallet.Models.WebWalletRootResponse
import cc.smartcash.wallet.utils.*
import com.google.gson.Gson
import java.io.IOException

object LoginViewModel {

    val TAG: String = LoginViewModel::class.java.simpleName

    fun getSyncToken(userLogin: UserLogin, context: Context): WebWalletRootResponse<String?> {

        val responseWebWalletRootResponse: WebWalletRootResponse<String?> = WebWalletRootResponse()

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
            responseWebWalletRootResponse.valid = r.isSuccessful
            if (r.isSuccessful) {
                val body = r.body()
                if (body != null) {
                    responseWebWalletRootResponse.data = body.accessToken
                }
            } else {
                try {
                    val ex = Gson().fromJson<WebWalletException>(r.errorBody()?.string(), WebWalletException::class.java)
                    responseWebWalletRootResponse.errorDescription = ex.errorDescription
                    responseWebWalletRootResponse.error = ex.error

                } catch (e: Exception) {
                    responseWebWalletRootResponse.errorDescription = e.message
                    responseWebWalletRootResponse.error = e.toString()
                    Log.e(TAG, e.message)
                }
            }
        } catch (e: IOException) {
            responseWebWalletRootResponse.errorDescription = e.message
            responseWebWalletRootResponse.error = e.toString()
            Log.e(TAG, e.message)
        }

        return responseWebWalletRootResponse
    }

    fun getSyncUser(token: String, context: Context): WebWalletRootResponse<User?> {
        var responseWebWalletRootResponse: WebWalletRootResponse<User?> = WebWalletRootResponse()
        val isInternetOn = NetworkUtil.getInternetStatus(context)
        if (isInternetOn) {
            try {
                val callUser = ApiUtil.userService.getUser("Bearer $token")
                responseWebWalletRootResponse = Util.getWebWalletResponse(callUser)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return responseWebWalletRootResponse
    }

}