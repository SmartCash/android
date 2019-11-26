package cc.smartcash.smarthub.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.models.User
import cc.smartcash.smarthub.models.UserRecoveryKey
import cc.smartcash.smarthub.models.UserRegisterRequest
import cc.smartcash.smarthub.utils.ApiUtil
import cc.smartcash.smarthub.utils.NetworkUtil

class UserViewModel : ViewModel() {

    companion object {

        val TAG: String? = UserViewModel::class.java.simpleName

        val syncUserRecoveryKey: UserRecoveryKey?
            get() {
                try {

                    val callUserRecoveryKey = ApiUtil.webWalletAPIService.newMasterSecurityKey

                    val response = callUserRecoveryKey.execute()

                    if (response.isSuccessful && response.body() != null && response.body()!!.data != null) {

                        return response.body()!!.data
                    }

                } catch (ex: Exception) {
                    Log.e(TAG, ex.message)
                }

                return null
            }

        fun setSyncUser(newUser: UserRegisterRequest, userRecoveryKey: UserRecoveryKey, context: Context): User? {

            val isInternetOn = NetworkUtil.getInternetStatus(context)

            if (isInternetOn) {

                newUser.recoveryKey = userRecoveryKey.recoveryKey

                val call = ApiUtil.webWalletAPIService.setUser(newUser)

                try {
                    val response = call.execute()

                    if (response.isSuccessful && response.body() != null && response.body()!!.data != null) {

                        val userResponse = response.body()!!.data
                        userResponse!!.recoveryKey = userRecoveryKey.recoveryKey
                        userResponse.password = newUser.password

                        return userResponse
                    }

                } catch (ex: Exception) {
                    Log.e(TAG, ex.message)
                }

            }
            return null
        }
    }

}