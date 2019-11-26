package cc.smartcash.smarthub.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.utils.KEYS
import cc.smartcash.smarthub.utils.NetworkUtil
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.Util
import com.google.crypto.tink.Aead
import java.nio.charset.StandardCharsets

class PinActivity : AppCompatActivity() {

    private var smartCashApplication: SmartCashApplication? = null
    private var currentPassword: String? = null
    private var encryptedPassword: ByteArray? = null
    private var internetAvailable: Boolean = false
    private var isPasswordVisible = false

    @BindView(R.id.pin_activity_txt_pin)
    lateinit var txtPin: EditText

    @BindView(R.id.pin_activity_txt_pin_confirmation)
    lateinit var txtConfirmPin: EditText

    @BindView(R.id.pin_activity_confirm_pin_label)
    lateinit var confirmPinLabel: TextView

    @BindView(R.id.pin_activity_forgot_pin_btn)
    lateinit var forgotPinBtn: TextView

    @BindView(R.id.pin_activity_continue_without_pin)
    lateinit var continueWithoutToken: TextView

    @BindView(R.id.pin_activity_btn_eye)
    lateinit var btnEye: ImageView

    override fun onBackPressed() {
        //Toast.makeText(applicationContext, "Back press disabled!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pin_activity)
        ButterKnife.bind(this)

        setSmartCashApplication()

        getPassword()

        setConfirmationTextsInvisible()

        setNavigationWithoutPin()
    }

    private fun createAlertDialog(context: Context, title: String, message: String): AlertDialog.Builder {
        return AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
    }

    @OnClick(R.id.pin_activity_btn_confirm)
    fun onViewClicked() {
        if (encryptedPassword != null) {
            var right = false
            try {

                if (Util.isNullOrEmpty(txtPin)) {
                    txtPin.error = getString(R.string.pin_pin_error_message)

                    createAlertDialog(
                            this,
                            getString(R.string.pin_wrong_pin_dialog_title),
                            getString(R.string.pin_wrong_pin_dialog_message)
                    ).setPositiveButton("OK") { _, _ ->
                        finish()
                    }
                    return
                }
                smartCashApplication!!.aead = smartCashApplication!!.getOrGenerateNewKeysetHandle(applicationContext).getPrimitive(Aead::class.java)
                smartCashApplication!!.aead.decrypt(encryptedPassword, txtPin.text.toString().toByteArray(StandardCharsets.UTF_8))
                right = true
                navigateToMain()
            } catch (ex: Exception) {
                right = false
                Log.e(TAG, ex.message)
            } finally {
                if (!right)
                    AlertDialog.Builder(this)
                            .setTitle(getString(R.string.pin_wrong_pin_dialog_title))
                            .setMessage(getString(R.string.pin_wrong_pin_dialog_message))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes) { dialog, _ -> dialog.cancel() }
                            .setNegativeButton(android.R.string.no, null).show()
            }
        } else if (Util.compareString(txtPin, txtConfirmPin)) {

            if (Util.isNullOrEmpty(txtPin) || Util.isNullOrEmpty(txtConfirmPin)) {

                txtPin.error = getString(R.string.pin_pin_error_message)
                txtConfirmPin.error = getString(R.string.pin_pin_error_message)

                alertDialog(getString(R.string.pin_pin_error_message), getString(R.string.pin_wrong_pin_dialog_message2))

                createAlertDialog(
                        this,
                        getString(R.string.pin_wrong_pin_dialog_title),
                        getString(R.string.pin_wrong_pin_dialog_message)
                ).setPositiveButton("OK") { _, _ ->
                    finish()
                }

                return
            }

            //ENCRYPT THE PASSWORD
            try {
                setSmartCashApplication()
                val plainTextToEncrypt = this.currentPassword!!.toByteArray(StandardCharsets.UTF_8)
                val pin = this.txtConfirmPin.text.toString().toByteArray(StandardCharsets.UTF_8)
                val cipherTextToEncrypt = smartCashApplication!!.aead.encrypt(plainTextToEncrypt, pin)
                smartCashApplication!!.saveByte(cipherTextToEncrypt, KEYS.KEY_PASSWORD)
                navigateToMain()
            } catch (ex: Exception) {
                Log.e(TAG, ex.message)
            }

        } else if (!Util.compareString(txtPin, txtConfirmPin)) {
            txtPin.error = getString(R.string.pin_pin_confirmation_error_message)
            txtConfirmPin.error = getString(R.string.pin_pin_confirmation_error_message)
            alertDialog(getString(R.string.pin_pin_confirmation_error_message), getString(R.string.pin_wrong_pin_dialog_message3))
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.pin_wrong_pin_dialog_title))
                    .setMessage(getString(R.string.pin_wrong_pin_dialog_message))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK") { dialog, id ->
                        finish()
                    }
            return
        }
    }

    @OnClick(R.id.pin_activity_forgot_pin_btn)
    fun onForgotPinClicked() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.main_dialog_forgot_pin_title))
                .setMessage(getString(R.string.main_dialog_forgot_pin_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    Toast.makeText(this@PinActivity, getString(R.string.main_redirect_to_login_toast), Toast.LENGTH_SHORT).show()
                    smartCashApplication!!.deleteSharedPreferences()
                    startActivity(Intent(this@PinActivity, LoginActivity::class.java))
                }
                .setNegativeButton(android.R.string.no, null).show()
    }

    @OnClick(R.id.pin_activity_continue_without_pin)
    fun onContinueWithoutPinClicked() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.pin_continue_without_pin_dialog_title))
                .setMessage(getString(R.string.pin_continue_without_pin_dialog_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    Toast.makeText(this@PinActivity, getString(R.string.pin_continue_without_pin_dialog_redirect), Toast.LENGTH_SHORT).show()
                    smartCashApplication!!.saveWithoutPIN(true)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(android.R.string.no, null).show()
    }

    @OnClick(R.id.pin_activity_btn_eye)
    fun onBtnEyeClicked() {
        if (isPasswordVisible) {
            txtPin.transformationMethod = PasswordTransformationMethod.getInstance()
            txtConfirmPin.transformationMethod = PasswordTransformationMethod.getInstance()
            isPasswordVisible = false
            btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
        } else {
            txtPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
            txtConfirmPin.transformationMethod = HideReturnsTransformationMethod.getInstance()
            isPasswordVisible = true
            btnEye.setImageDrawable(resources.getDrawable(R.drawable.eye_off))
        }

        txtPin.setSelection(txtPin.text.length)
        txtConfirmPin.setSelection(txtConfirmPin.text.length)
    }

    private fun setSmartCashApplication() {
        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(applicationContext)

        smartCashApplication!!.allValues
    }

    private fun getPassword() {
        val intent = intent
        val extraPASSWORD = intent.getStringExtra(KEYS.KEY_PASSWORD)
        if (extraPASSWORD != null && extraPASSWORD.isNotEmpty()) {
            this.currentPassword = extraPASSWORD
        } else {
            encryptedPassword = this.smartCashApplication!!.getByte(KEYS.KEY_PASSWORD)
        }
    }

    private fun setConfirmationTextsInvisible() {
        if (encryptedPassword != null) {
            txtConfirmPin.visibility = View.GONE
            confirmPinLabel.visibility = View.GONE
            forgotPinBtn.visibility = View.VISIBLE
            continueWithoutToken.visibility = View.GONE
        }
    }

    private fun setNavigationWithoutPin() {
        val withoutPin = smartCashApplication?.appPreferences?.withoutPin ?: return
        if (withoutPin) {
            internetAvailable = NetworkUtil.getInternetStatus(this)
            if (!internetAvailable) navigateToLogin()
            if (isOnLocalDB(withoutPin)) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                navigateToLogin()
            }
        }
    }

    private fun isOnLocalDB(withoutPin: Boolean): Boolean {

        val token = smartCashApplication!!.getToken()
        val user = smartCashApplication!!.getUser()
        return if (!withoutPin) {
            val pin = smartCashApplication!!.getByte(KEYS.KEY_PASSWORD)
            token != null && token.isNotEmpty() && user != null && pin != null
        } else {
            token != null && token.isNotEmpty() && user != null
        }
    }

    private fun navigateToLogin() {
        Toast.makeText(this@PinActivity, getString(R.string.login_network_status_change), Toast.LENGTH_SHORT).show()
        smartCashApplication!!.deleteSharedPreferences()
        startActivity(Intent(this@PinActivity, LoginActivity::class.java))
    }

    private fun navigateToMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(KEYS.KEY_PIN, this.txtPin.text.toString())
        startActivity(intent)

    }

    private fun alertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    dialog.cancel()
                }.show()
    }

    companion object {

        val TAG: String? = PinActivity::class.java.simpleName
    }

}
