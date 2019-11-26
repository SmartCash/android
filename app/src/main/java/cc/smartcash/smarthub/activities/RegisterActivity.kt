package cc.smartcash.smarthub.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.smarthub.BuildConfig
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.models.*
import cc.smartcash.smarthub.tasks.LoginTask
import cc.smartcash.smarthub.tasks.PriceTask
import cc.smartcash.smarthub.utils.KEYS
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.Util
import cc.smartcash.smarthub.viewModels.LoginViewModel
import cc.smartcash.smarthub.viewModels.UserViewModel
import cc.smartcash.smarthub.viewModels.WalletViewModel
import java.nio.charset.StandardCharsets
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var smartCashApplication: SmartCashApplication? = null

    private var userRecoveryKeyMain: UserRecoveryKey? = null

    @BindView(R.id.register_main_activity_txt_user)
    lateinit var txtUser: EditText

    @BindView(R.id.register_main_activity_txt_password)
    lateinit var txtPassword: EditText

    @BindView(R.id.register_main_activity_txt_confirm_password)
    lateinit var txtConfirmPassword: EditText

    @BindView(R.id.register_main_activity_txt_pin)
    lateinit var txtPin: EditText

    @BindView(R.id.register_main_activity_txt_pin_confirmation)
    lateinit var txtConfirmPin: EditText

    @BindView(R.id.register_main_activity_loader)
    lateinit var loader: ProgressBar

    @BindView(R.id.register_main_activity_login_content)
    lateinit var loginContent: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_main)
        ButterKnife.bind(this)

        if (this.smartCashApplication == null)
            this.smartCashApplication = SmartCashApplication(applicationContext)

        setDebugInfo()

    }

    override fun onStart() {
        super.onStart()
        getUserRecoveryKey()
        getCurrentPrices()
    }

    @OnClick(R.id.register_main_activity_btn_save)
    fun onViewClicked() {

        if (hasErrorOnForm(Util.getString(txtUser), Util.getString(txtPassword), Util.getString(txtConfirmPassword), Util.getString(txtPin), Util.getString(txtConfirmPin)))
            return

        IsUserAvailableTask().execute()

    }

    private fun setDebugInfo() {
        if (BuildConfig.DEBUG) {
            // do something for a debug build
            val userUUID = UUID.randomUUID().toString()
            txtUser.setText("$userUUID@testeandroidmobile.com")
            txtPassword.setText("123456")
            txtConfirmPassword.setText("123456")
            txtPin.setText("1234")
            txtConfirmPin.setText("1234")
        }
    }

    private fun navigateToMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(KEYS.KEY_PIN, this.txtPin.text.toString())
        startActivity(intent)
    }

    private fun hasErrorOnForm(username: String, password: String, confirmPassword: String, pin: String, confirmPin: String): Boolean {

        var hasError = false

        if (username.isEmpty()) {
            txtUser.error = getString(R.string.register_username_error_message)
            hasError = true
        }
        if (password.isEmpty()) {
            txtPassword.error = getString(R.string.register_password_error_message)
            hasError = true
        }
        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.error = getString(R.string.register_password_confirmation_error_message)
            hasError = true
        }
        if (pin.isEmpty()) {
            txtPin.error = getString(R.string.register_pin_error_message)
            hasError = true
        }
        if (confirmPin.isEmpty()) {
            txtConfirmPin.error = getString(R.string.register_pin_confirmation_error_message)
            hasError = true
        }
        if (!pin.equals(confirmPin, ignoreCase = true)) {
            txtConfirmPin.error = getString(R.string.register_pin_match_error_message)
            hasError = true
        }
        if (!password.equals(confirmPassword, ignoreCase = true)) {
            txtPassword.error = getString(R.string.register_password_match_error_message)
            hasError = true
        }
        if (!Util.isValidEmail(username)) {
            txtUser.error = getString(R.string.register_username_pattern_error_message)
            hasError = true
        }
        return hasError
    }

    private fun createNewUser(username: String, password: String) {
        val newUser = UserRegisterRequest()
        newUser.username = username
        newUser.password = password
        newUser.email = username
        RegisterUserTask().execute(newUser)
    }

    private fun encryptAndSavePassword(newUser: UserRegisterRequest) {
        //ENCRYPT AND SAVE THE PASSWORD
        try {
            smartCashApplication!!.saveByte(encrypt(newUser.password), KEYS.KEY_PASSWORD)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message)
        }

    }

    private fun encryptAndSaveMSK(user: User, user1: User?) {
        //ENCRYPT AND SAVE THE MSK
        try {
            val cipherTextToEncrypt = encrypt(user.recoveryKey)
            user1!!.recoveryKey = Base64.encodeToString(cipherTextToEncrypt, Base64.DEFAULT)
            smartCashApplication!!.saveMSK(cipherTextToEncrypt)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message)
        }

    }

    private fun encrypt(plainTextToEncrypt: String?): ByteArray {
        val pinByte = this.txtPin.text.toString().toByteArray(StandardCharsets.UTF_8)
        return smartCashApplication!!.aead.encrypt(plainTextToEncrypt!!.toByteArray(StandardCharsets.UTF_8), pinByte)
    }

    private fun getCurrentPrices() {
        PriceTask(this.applicationContext, ::startLoadingProcess, ::afterPriceTaskWasExecuted).execute()
    }

    private fun afterPriceTaskWasExecuted(coins: ArrayList<Coin>?) {
        if (this.smartCashApplication?.appPreferences?.coins?.isNotEmpty()!!) {
            endLoadingProcess()
        }
    }

    private fun getUserRecoveryKey() {
        UserRecoveryKeyTask().execute()
    }

    private inner class RegisterUserTask : AsyncTask<UserRegisterRequest, Int, User>() {

        override fun onPreExecute() {
            super.onPreExecute()
            startLoadingProcess()
        }

        override fun doInBackground(vararg users: UserRegisterRequest): User? {

            if (userRecoveryKeyMain != null) {


                val newUserResponse = UserViewModel.setSyncUser(users[0], userRecoveryKeyMain!!, applicationContext)
                if (newUserResponse != null) {

                    val userLogin = UserLogin()
                    userLogin.username = newUserResponse.username
                    userLogin.password = newUserResponse.password
                    userLogin.twoFactorAuthentication = ""

                    val token = LoginViewModel.getSyncToken(userLogin, applicationContext)

                    if (token.valid!!) {
                        val userLoginResponse = LoginViewModel.getSyncUser(token.data!!, applicationContext)
                        encryptAndSaveMSK(newUserResponse, userLoginResponse.data)
                        encryptAndSavePassword(users[0])

                        //TODO: Save User
                        LoginTask.saveUser(token.data!!, userLoginResponse.data, applicationContext, smartCashApplication!!)

                        return userLoginResponse.data
                    }
                }
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if (!Util.isTaskComplete(values[0]!!))
                startLoadingProcess() //percentage of the progress
        }

        override fun onPostExecute(user: User?) {
            super.onPostExecute(user)

            if (user != null) {
                navigateToMain()
            } else {
                endLoadingProcess()
            }
        }
    }

    private inner class UserRecoveryKeyTask : AsyncTask<Void, Int, UserRecoveryKey>() {

        override fun doInBackground(vararg users: Void): UserRecoveryKey? {
            return UserViewModel.syncUserRecoveryKey
        }


        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if (!Util.isTaskComplete(values[0]!!))
                startLoadingProcess() //percentage of the progress
        }

        override fun onPostExecute(userRecoveryKey: UserRecoveryKey?) {
            super.onPostExecute(userRecoveryKey)
            if (userRecoveryKey != null) {
                userRecoveryKeyMain = userRecoveryKey
                Toast.makeText(applicationContext, userRecoveryKeyMain!!.recoveryKey, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class IsUserAvailableTask : AsyncTask<Void, Int, WebWalletRootResponse<Boolean>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            startLoadingProcess()
        }

        override fun doInBackground(vararg users: Void): WebWalletRootResponse<Boolean>? {
            return WalletViewModel.isUserAvailable(Util.getString(txtUser))
        }


        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if (!Util.isTaskComplete(values[0]!!))
                startLoadingProcess() //percentage of the progress
        }


        override fun onPostExecute(booleanWebWalletRootResponse: WebWalletRootResponse<Boolean>?) {
            super.onPostExecute(booleanWebWalletRootResponse)
            if (booleanWebWalletRootResponse != null && booleanWebWalletRootResponse.data!!) {
                createNewUser(Util.getString(txtUser), Util.getString(txtPassword))
            } else {
                txtUser.error = getString(R.string.register_username_not_available_error_message)
                endLoadingProcess()
            }
        }
    }

    private fun startLoadingProcess() {
        findViewById<View>(R.id.register_main_activity_login_content).visibility = View.GONE
        findViewById<View>(R.id.register_main_activity_login_content).visibility = View.INVISIBLE
        findViewById<View>(R.id.register_main_activity_loader).visibility = View.VISIBLE

        findViewById<View>(R.id.register_main_activity_btn_save).isEnabled = false
        (findViewById<View>(R.id.register_main_activity_btn_save) as Button).text = getString(R.string.register_save_button_loading_status)
    }

    private fun endLoadingProcess() {
        findViewById<View>(R.id.register_main_activity_loader).visibility = View.GONE
        findViewById<View>(R.id.register_main_activity_loader).visibility = View.INVISIBLE
        findViewById<View>(R.id.register_main_activity_login_content).visibility = View.VISIBLE

        findViewById<View>(R.id.register_main_activity_btn_save).isEnabled = true
        (findViewById<View>(R.id.register_main_activity_btn_save) as Button).text = getString(R.string.register_save_button)
    }

    companion object {
        val TAG: String? = RegisterActivity::class.java.simpleName
    }

}