package cc.smartcash.wallet.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.wallet.*
import cc.smartcash.wallet.Models.Coin
import cc.smartcash.wallet.Models.User
import cc.smartcash.wallet.Models.UserLogin
import cc.smartcash.wallet.Models.WebWalletRootResponse
import cc.smartcash.wallet.tasks.LoginTask
import cc.smartcash.wallet.tasks.PriceTask
import cc.smartcash.wallet.utils.KEYS
import cc.smartcash.wallet.utils.SmartCashApplication
import cc.smartcash.wallet.utils.URLS
import cc.smartcash.wallet.utils.Util
import java.util.*

class LoginActivity : AppCompatActivity(), INetworkChangeReceiver {

    //region var declarations

    private var smartCashApplication: SmartCashApplication? = null
    private var internetAvailable: Boolean = false
    private var isPasswordVisible = false

    private lateinit var networkReceiver: NetworkChangeReceiver

    @BindView(R.id.login_main_btn_login)
    lateinit var btnLogin: Button

    @BindView(R.id.login_main_btn_eye)
    lateinit var btnEye: ImageView

    @BindView(R.id.login_main_network_status)
    lateinit var networkSwitch: Switch

    @BindView(R.id.login_main_txt_user)
    lateinit var txtUser: EditText

    @BindView(R.id.login_main_txt_password)
    lateinit var txtPassword: EditText

    @BindView(R.id.login_main_txt_two_factor_auth)
    lateinit var txt2fa: EditText

    @BindView(R.id.login_main_loader)
    lateinit var loader: ProgressBar

    @BindView(R.id.login_main_login_content)
    lateinit var loginContent: ConstraintLayout

    //endregion

    private val isOnLocalDB: Boolean
        get() {
            val token = smartCashApplication!!.getToken()
            val user = smartCashApplication!!.getUser()
            val password = smartCashApplication!!.getByte(KEYS.KEY_PASSWORD)
            return token != null && token.isNotEmpty() && user != null && password != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
        ButterKnife.bind(this)

        this.networkReceiver = NetworkChangeReceiver(this)


        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(applicationContext)

        setDebugInfo()

        if (isOnLocalDB) {
            startActivity(Intent(applicationContext, PinActivity::class.java))
        }
    }

    override fun onNetworkChange(value: String?) {

        this.internetAvailable = value.equals(NetworkUtil.TYPE_NOT_CONNECTED_TEXT, true).not()

        networkSwitch.isChecked = internetAvailable

        networkSwitch.text = String.format(BuildConfig.VERSION_NAME, " ", value)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, networkReceiver.getIntentFilters())
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }

    override fun onStart() {
        super.onStart()
        getCurrentPrices()
    }

    @OnClick(R.id.login_main_btn_login)
    fun onViewClicked() {

        if (!internetAvailable) {
            Toast.makeText(applicationContext, resources.getString(R.string.login_network_error_message), Toast.LENGTH_SHORT).show()
            return
        }

        val userLogin = UserLogin()
        userLogin.username = Util.getString(txtUser)
        userLogin.password = Util.getString(txtPassword)
        userLogin.twoFactorAuthentication = Util.getString(txt2fa)

        if (Util.isNullOrEmpty(txtUser) || Util.isNullOrEmpty(txtPassword)) {
            txtUser.error = resources.getString(R.string.login_username_error_message)
            txtPassword.error = resources.getString(R.string.login_password_error_message)
            return
        }

        LoginTask(applicationContext, ::startLoadingProcess, ::afterLoginTaskExecuted).execute(userLogin)
    }

    @OnClick(R.id.login_main_btn_register)
    fun register() {

        if (!internetAvailable) {
            Toast.makeText(applicationContext, resources.getString(R.string.login_network_error_message), Toast.LENGTH_SHORT).show()
            return
        }
        this.startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    @OnClick(R.id.login_main_btn_forgot_password)
    fun onTxtForgetYourPasswordClicked() {

        if (!internetAvailable) {
            Toast.makeText(applicationContext, resources.getString(R.string.login_network_error_message), Toast.LENGTH_SHORT).show()
            return
        }
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URLS.URL_CHANGE_PASSWORD)))
    }

    @OnClick(R.id.login_main_btn_eye)
    fun onBtnEyeClicked() {
        if (isPasswordVisible) {
            txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            isPasswordVisible = false
            Util.changeImage(btnEye, R.drawable.ic_eye, this.applicationContext)
        } else {
            txtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            isPasswordVisible = true
            Util.changeImage(btnEye, R.drawable.eye_off, this.applicationContext)
        }

        txtPassword.setSelection(txtPassword.text.length)
    }

    override fun onBackPressed() {
        //Toast.makeText(applicationContext, "Back press disabled!", Toast.LENGTH_SHORT).show()
    }

    private fun setDebugInfo() {
        if (BuildConfig.DEBUG) {
            txtUser.setText(Util.getProperty(KEYS.CONFIG_TEST_USER, applicationContext))
            txtPassword.setText(Util.getProperty(KEYS.CONFIG_TEST_PASS, applicationContext))
        }
    }

    private fun getCurrentPrices() {
        PriceTask(this.applicationContext, ::startLoadingProcess, ::afterPriceTaskWasExecuted).execute()
    }

    private fun startLoadingProcess() {
        findViewById<View>(R.id.login_main_login_content).visibility = View.GONE
        findViewById<View>(R.id.login_main_login_content).visibility = View.INVISIBLE
        findViewById<View>(R.id.login_main_loader).visibility = View.VISIBLE

        btnLogin.isEnabled = false
        btnLogin.text = getString(R.string.login_enter_button_loading_status)
        findViewById<View>(R.id.login_main_btn_register).visibility = View.INVISIBLE
        findViewById<View>(R.id.login_main_btn_forgot_password).visibility = View.INVISIBLE
    }

    private fun endLoadingProcess() {
        findViewById<View>(R.id.login_main_loader).visibility = View.GONE
        findViewById<View>(R.id.login_main_loader).visibility = View.INVISIBLE
        findViewById<View>(R.id.login_main_login_content).visibility = View.VISIBLE

        btnLogin.isEnabled = true
        btnLogin.text = getString(R.string.login_enter_button)
        findViewById<View>(R.id.login_main_btn_register).visibility = View.VISIBLE
        findViewById<View>(R.id.login_main_btn_forgot_password).visibility = View.VISIBLE
    }

    private fun afterLoginTaskExecuted(user: WebWalletRootResponse<User?>) {

        if ((user.valid == null || !user.valid!! || user.data == null).not()) {
            navigateToPinActivity()
        }
        if (user.error != null || user.errorDescription != null)
            Toast.makeText(applicationContext, user.error + " : " + user.errorDescription, Toast.LENGTH_LONG).show()

        endLoadingProcess()
    }

    private fun afterPriceTaskWasExecuted(coins: ArrayList<Coin>?) {
        val size = coins?.size
        endLoadingProcess()
    }

    private fun navigateToPinActivity() {
        if (smartCashApplication!!.checkAllNecessaryKeys().not()) {
            endLoadingProcess()
            Toast.makeText(applicationContext, "We don't have all necessary keys to proceed" + smartCashApplication!!.getKeysThatDoesNotExists(), Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent(applicationContext, PinActivity::class.java)
        intent.putExtra(KEYS.KEY_PASSWORD, txtPassword.text.toString())
        startActivity(intent)
    }

    companion object {
        val TAG: String = LoginActivity::class.java.simpleName
    }

}
