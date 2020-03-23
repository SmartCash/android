package cc.smartcash.smarthub.ViewModels

import android.content.Context
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cc.smartcash.smarthub.Models.*
import cc.smartcash.smarthub.Services.SAPIConfig
import cc.smartcash.smarthub.Services.WebWalletAPIConfig
import cc.smartcash.smarthub.Utils.KEYS
import cc.smartcash.smarthub.Utils.NetworkUtil
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.Utils.Util
import cc.smartcash.smarthub.tasks.LoginTask
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserViewModel : ViewModel() {

    private var token: MutableLiveData<String>? = null

    private var user: MutableLiveData<User>? = null

    private var userRecoveryKey: MutableLiveData<UserRecoveryKey>? = null

    fun getToken(username: String, password: String, context: Context): LiveData<String> {
        token = MutableLiveData()

        try {
            loadToken(username, password, context)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return token as MutableLiveData<String>
    }

    fun getUser(token: String, context: Context): LiveData<User> {
        user = MutableLiveData()
        loadUser(token, context)
        return user as MutableLiveData<User>
    }

    fun setUser(newUser: UserRegisterRequest, context: Context): LiveData<User> {

        user = MutableLiveData()

        createUser(newUser, context)

        return user as MutableLiveData<User>
    }

    private fun loadUser(token: String, context: Context) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())

        val isInternetOn = NetworkUtil.getInternetStatus(context)

        if (isInternetOn) {
            val call = WebWalletAPIConfig().webWalletAPIService.getUser("Bearer $token")

            call.enqueue(object : Callback<WebWalletRootResponse<User>> {
                override fun onResponse(call: Call<WebWalletRootResponse<User>>, response: Response<WebWalletRootResponse<User>>) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            var _user = apiResponse!!.data as User

                            _user.wallet!!.forEach {
                                var call = SAPIConfig().sapiService.getAddressBalance(it.address!!)
                                var addressResponse = call.execute().body()

                                it.balance = addressResponse?.balance
                                it.totalReceived = addressResponse?.received
                                it.totalSent = addressResponse?.sent!!.toDouble()

                                //Get Transactions from a new API
                                var transactionsAddres = TransactionViewModel().getTransactions(it.address!!, context)
                                it.transactions = transactionsAddres!!.txs
                            }

                            user!!.setValue(_user)
                        } else {
                            try {
                                user!!.value = null
                                setResponseError(context, response, "message")
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    } else {
                        Log.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<WebWalletRootResponse<User>>, t: Throwable) {
                    Log.e("WebWalletAPIService", "Erro ao buscar o usuário:" + t.message)
                    user!!.value = null
                }
            })
        }
    }

    private fun loadBalance(user: User, context: Context){
        user.wallet!!.forEach {
            val call = SAPIConfig().sapiService.getAddressBalance(it.address!!)

            call.enqueue(object : Callback<SapiAddressBalance> {
                override fun onResponse(call: Call<SapiAddressBalance>, response: Response<SapiAddressBalance>) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()

                            it.balance = apiResponse!!.balance
                            it.totalReceived = apiResponse!!.received
                            it.totalSent = apiResponse!!.sent.toDouble()
                        } else {
                            try {
                                setResponseError(context, response, "message")
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<SapiAddressBalance>, t: Throwable) {
                    Log.e("WebWalletAPIService", "Erro ao buscar o usuário:" + t.message)
                }
            })
        }
    }


    fun saveUser(newUser: UserRegisterRequest, userRecoveryKey: UserRecoveryKey?, context: Context) {

        val isInternetOn = NetworkUtil.getInternetStatus(context)

        if (isInternetOn) {

            newUser.recoveryKey = userRecoveryKey!!.recoveryKey

            val call = WebWalletAPIConfig().webWalletAPIService.setUser(newUser)

            call.enqueue(object : Callback<WebWalletRootResponse<User>> {
                override fun onResponse(call: Call<WebWalletRootResponse<User>>, response: Response<WebWalletRootResponse<User>>) {

                    if (response != null) {
                        if (response.isSuccessful) {

                            val apiResponse = response.body()

                            val userResponse = apiResponse!!.data

                            userResponse!!.recoveryKey = userRecoveryKey.recoveryKey
                            userResponse.password = newUser.password

                            user!!.value = userResponse

                            Log.i(TAG, userResponse.username)

                            Toast.makeText(context, apiResponse.data!!.username, Toast.LENGTH_LONG).show()

                        } else {
                            try {
                                user!!.value = null
                                setResponseError(context, response, "message")
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    } else {
                        Log.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<WebWalletRootResponse<User>>, t: Throwable) {

                    Toast.makeText(context, "Error to try to retreive the user: " + t.message, Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Error to try to retreive the user" + t.message)
                    user!!.value = null
                }
            })
        }
    }

    private fun loadToken(username: String, password: String, context: Context) {

        val isInternetOn = NetworkUtil.getInternetStatus(context)

        if (isInternetOn) {

            var localIP: String? = null
            try {
                localIP = SmartCashApplication.getIPAddress(true)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            if (localIP == null || localIP.isEmpty()) localIP = "100.003.102.100"

            val call = WebWalletAPIConfig().webWalletAPIService.getToken(
                    Util.getProperty(KEYS.CONFIG_TEST_USER, context)!!,
                    Util.getProperty(KEYS.CONFIG_TEST_PASS, context)!!,
                    "password",
                    Util.getProperty(KEYS.CONFIG_CLIENT_ID, context)!!,

                    "",
                    "mobile",
                    localIP,
                    Util.getProperty(KEYS.CONFIG_CLIENT_SECRET, context)!!
            )

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Log.e("Access token", "" + apiResponse!!.accessToken!!)
                        token!!.setValue(apiResponse.accessToken)
                    } else {
                        try {
                            token!!.value = null
                            setResponseError(context, response, "error_description")
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        }

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    Toast.makeText(context, "Error to try to retreive the TOKEN: " + t.message, Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Error to try to retreive the TOKEN:" + t.message)
                    token!!.value = null
                }
            })
        }
    }

    private fun createUser(newUser: UserRegisterRequest, context: Context) {

        try {

            userRecoveryKey = MutableLiveData()

            val callUserRecoveryKey = WebWalletAPIConfig().webWalletAPIService.newMasterSecurityKey

            callUserRecoveryKey.enqueue(object : Callback<WebWalletRootResponse<UserRecoveryKey>> {
                override fun onResponse(call: Call<WebWalletRootResponse<UserRecoveryKey>>, response: Response<WebWalletRootResponse<UserRecoveryKey>>) {

                    if (response != null) {

                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            userRecoveryKey!!.value = apiResponse!!.data


                            saveUser(newUser, apiResponse.data, context)


                            Toast.makeText(context, apiResponse.data!!.recoveryKey, Toast.LENGTH_LONG).show()
                        } else {
                            try {
                                userRecoveryKey!!.value = null
                                setResponseError(context, response, "message")
                            } catch (e: Exception) {
                                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    } else {
                        Log.e(TAG, "Response is null")
                    }
                }

                override fun onFailure(call: Call<WebWalletRootResponse<UserRecoveryKey>>, t: Throwable) {
                    Log.e("WebWalletAPIService", "Erreor while getting the recovery key" + t.message)
                    user!!.value = null
                }
            })


        } catch (e: Exception) {
            this.user!!.value = null
            e.printStackTrace()
        }

    }

    @Throws(JSONException::class, IOException::class)
    private fun setResponseError(context: Context, response: Response<*>?, message: String) {
        if (response?.errorBody() != null && response.errorBody()!!.toString() != null && response.errorBody()!!.toString().isNotEmpty() && !response.errorBody()!!.toString().toLowerCase().contains("okhttp3")) {
            val jObjError = JSONObject(response.errorBody()!!.string())
            Toast.makeText(context, jObjError.getString(message), Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        val TAG: String? = UserViewModel::class.java.simpleName

        val syncUserRecoveryKey: UserRecoveryKey?
            get() {
                try {

                    val callUserRecoveryKey = WebWalletAPIConfig().webWalletAPIService.newMasterSecurityKey

                    val response = callUserRecoveryKey.execute()

                    if (response != null && response.isSuccessful && response.body() != null && response.body()!!.data != null) {

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

                val call = WebWalletAPIConfig().webWalletAPIService.setUser(newUser)

                try {
                    val response = call.execute()

                    if (response != null && response.isSuccessful && response.body() != null && response.body()!!.data != null) {

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

        fun loadSyncUser(token: String, context: Context) : User? {
            var userResponse: User? = null
            val isInternetOn = NetworkUtil.getInternetStatus(context)

            if (isInternetOn) {
                val call = WebWalletAPIConfig().webWalletAPIService.getUser("Bearer $token")
                val response = call.execute()

                if (response != null && response.isSuccessful && response.body() != null && response.body()!!.data != null) {

                    userResponse = response.body()!!.data as User

                    userResponse.wallet!!.forEach {
                        var call = SAPIConfig().sapiService.getAddressBalance(it.address!!)
                        var addressResponse = call.execute().body()

                        it.balance = addressResponse?.balance
                        it.totalReceived = addressResponse?.received
                        it.totalSent = addressResponse?.sent!!.toDouble()

                        //Get Transactions from a new API
                        var transactionsAddres = TransactionViewModel().getTransactions(it.address!!, context)
                        it.transactions = transactionsAddres!!.txs
                    }
                }
            }

            return userResponse
        }

    }

}