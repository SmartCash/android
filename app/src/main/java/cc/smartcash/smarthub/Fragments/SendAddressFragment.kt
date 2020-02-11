package cc.smartcash.smarthub.Fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.InputFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.smarthub.Activities.MainActivity
import cc.smartcash.smarthub.Adapters.WalletDialogAdapter
import cc.smartcash.smarthub.Models.SendPayment
import cc.smartcash.smarthub.Models.SmartTextRoot
import cc.smartcash.smarthub.Models.WebWalletRootResponse
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.Utils.NetworkUtil
import cc.smartcash.smarthub.Utils.SmartCashApplication
import cc.smartcash.smarthub.Utils.Util
import cc.smartcash.smarthub.ViewModels.TransactionViewModel
import cc.smartcash.smarthub.ViewModels.UserViewModel
import cc.smartcash.smarthub.ViewModels.WalletViewModel
import cc.smartcash.smarthub.tasks.*
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.android.material.bottomnavigation.BottomNavigationView
import pub.devrel.easypermissions.EasyPermissions
import java.math.BigDecimal
import java.util.*

class SendAddressFragment : Fragment(), QRCodeReaderView.OnQRCodeReadListener {

    //region var declarations

    private var perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.CAMERA)
    private var dialog: AlertDialog? = null
    private val amountConverted = BigDecimal.valueOf(0.0)
    private var password: String = ""
    private var isPasswordVisible = false
    private var smartCashApplication: SmartCashApplication? = null
    private var isTwoFaVisible = false
    private var mainFee: BigDecimal? = null
    private var isOnline: Boolean = false

    @BindView(R.id.login_main_txt_password)
    lateinit var txtPassword: EditText

    @BindView(R.id.fragment_send_txt_amount_converted)
    lateinit var txtAmountCrypto: EditText

    @BindView(R.id.send_txt_to_address)
    lateinit var txtToAddress: EditText

    @BindView(R.id.fragment_send_amount_label_fiat)
    lateinit var amountLabel: TextView

    @BindView(R.id.fragment_send_txt_amount_fiat)
    lateinit var txtAmountFiat: EditText

    @BindView(R.id.login_main_loader)
    lateinit var loader: ProgressBar

    @BindView(R.id.btn_eye2)
    lateinit var btnEye2: ImageView

    @BindView(R.id.send_button_fragment)
    lateinit var sendButton: Button

    @BindView(R.id.pin_label)
    lateinit var pinLabel: TextView

    @BindView(R.id.login_main_btn_eye)
    lateinit var btnEye: ImageView

    @BindView(R.id.txt_two_fa)
    lateinit var txtTwoFa: EditText

    private val sendPayment: SendPayment
        get() {

            var amount: Double? = 0.0
            if (Util.isNullOrEmpty(txtAmountCrypto).not())
                amount = java.lang.Double.parseDouble(Util.getString(txtAmountCrypto))

            val selectedWallet = smartCashApplication!!.getWallet()
            val sendPayment = SendPayment()
            sendPayment.fromAddress = selectedWallet?.address
            sendPayment.toAddress = txtToAddress.text.toString()
            sendPayment.amount = amount
            sendPayment.email = this.smartCashApplication!!.AppPreferences.Email
            sendPayment.userKey = password

            if (!Util.isNullOrEmpty(txtTwoFa))
                sendPayment.code = Util.getString(txtTwoFa)

            return sendPayment
        }
    //endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.smartCashApplication = SmartCashApplication(getContext()!!)

        this.mainFee = BigDecimal.valueOf(0.0)

        this.isOnline = NetworkUtil.getInternetStatus(getContext()!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_send_address, container, false)
        ButterKnife.bind(this, view)

        if (!isOnline) {
            sendButton.text = getString(R.string.send_save_button_network_error_message)
            sendButton.isActivated = false
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EasyPermissions.requestPermissions(this, getString(R.string.send_camera_permission_label), RC_CAMERA_PERM, *perms)

        if (this.smartCashApplication!!.AppPreferences.withoutPin) {
            pinLabel.text = resources.getString(R.string.send_password_label)
            txtPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            txtPassword.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_LENGTH_PASSWORD))
        }
        setFeeOnButton()

        Util.setAmountListener(
                context!!,
                smartCashApplication!!,
                amountLabel,
                txtAmountFiat,
                txtAmountCrypto,
                mainFee,
                sendButton,
                null
        )

        hideConfirmationFields()
    }

    override fun onStart() {
        super.onStart()
        getCurrentFeeAndPrices()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onQRCodeRead(text: String?, points: Array<PointF>) {

        if (text == null || text.isEmpty()) return

        val parsedQr = Util.parseQrCode(text)

        if (parsedQr.isEmpty()) return

        if (parsedQr.indexOf(QR_SEPARATOR) == -1) {
            txtToAddress.setText(text)
            txtAmountFiat.setText(ZERO)
            txtAmountCrypto.setText(ZERO)
        } else {
            val parts = parsedQr.split(QR_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            txtToAddress.setText(parts[0])
            txtAmountCrypto.setText(parts[1])
        }
        Util.calculateFromSmartToFiat(
                context!!,
                smartCashApplication!!,
                amountLabel,
                txtAmountFiat,
                txtAmountCrypto,
                mainFee,
                sendButton
        )

        dialog!!.hide()
    }

    @OnClick(R.id.btn_qr_code)
    fun onBtnQrCodeClicked() =
            if (EasyPermissions.hasPermissions(Objects.requireNonNull<FragmentActivity>(activity), *perms)) {
                val scanQrCodeDialog = AlertDialog.Builder(context)
                val scanQrCodeView = LayoutInflater.from(context).inflate(R.layout.scan_qrcode_dialog, null)

                val btnCancel = scanQrCodeView.findViewById<Button>(R.id.show_wallets_dialog_scan_qrcode_dialog_cancel_button)
                btnCancel.setOnClickListener { v1 ->
                    dialog!!.dismiss()
                    dialog!!.hide()
                }

                val qrCodeLayout = scanQrCodeView.findViewById<QRCodeReaderView>(R.id.scan_qrcode_dialog_qr_code_scan)
                qrCodeLayout.setBackCamera()
                qrCodeLayout.startCamera()
                qrCodeLayout.setOnQRCodeReadListener(this)

                scanQrCodeDialog.setView(scanQrCodeView)

                dialog = scanQrCodeDialog.create()

                Objects.requireNonNull<Window>(dialog!!.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialog!!.show()

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.send_camera_permission_label), RC_CAMERA_PERM, *perms)
            }

    @OnClick(R.id.btn_wallet)
    fun onBtnWalletClicked() {
        val walletListDialog = AlertDialog.Builder(context)
        val walletListView = LayoutInflater.from(context).inflate(R.layout.show_wallets_dialog, null)

        val btnCancel = walletListView.findViewById<Button>(R.id.show_wallets_dialog_scan_qrcode_dialog_cancel_button)
        val recycler = walletListView.findViewById<RecyclerView>(R.id.show_wallets_dialog_wallet_list)
        val title = walletListView.findViewById<TextView>(R.id.show_wallets_dialog_title)

        title.text = getString(R.string.send_wallet_dialog_title_label).replace("%s", this.smartCashApplication!!.AppPreferences.wallet!!.size.toString())

        walletListDialog.setView(walletListView)
        val dialog = walletListDialog.create()
        Objects.requireNonNull<Window>(dialog.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupRecyclerViewWallets(recycler, dialog)

        btnCancel.setOnClickListener { v1 -> dialog.hide() }

        dialog.show()
    }

    @OnClick(R.id.send_button_fragment)
    fun onViewClicked() {

        if (!isOnline) {
            Toast.makeText(activity, getString(R.string.send_save_button_network_error_message), Toast.LENGTH_SHORT).show()
            return
        }
        if (Util.isNullOrEmpty(txtToAddress)) {
            txtToAddress.error = getString(R.string.send_address_error_message)
            Toast.makeText(context, getString(R.string.send_address_error_message), Toast.LENGTH_LONG).show()
            return
        } else if (Util.isNullOrEmpty(txtAmountFiat) || Util.getString(txtAmountFiat) == ZERO) {
            txtAmountFiat.error = getString(R.string.send_amount_fiat_error_message)
            Toast.makeText(context, getString(R.string.send_amount_fiat_error_message), Toast.LENGTH_LONG).show()
            return
        } else if (Util.isNullOrEmpty(txtAmountCrypto) || Util.getString(txtAmountCrypto) == ZERO) {
            txtAmountCrypto.error = getString(R.string.send_amount_crypto_error_message)
            Toast.makeText(context, getString(R.string.send_amount_crypto_error_message), Toast.LENGTH_LONG).show()
            return
        }

        val amount: Double = java.lang.Double.parseDouble(Util.getString(txtAmountCrypto))
        val selectedWallet = smartCashApplication!!.getWallet()

        if (selectedWallet?.balance!! < amount) {
            Toast.makeText(context, getString(R.string.send_insufficient_balance_error_message), Toast.LENGTH_LONG).show()
            return
        }
        if (amount.equals(0.0)) {
            Toast.makeText(context, getString(R.string.send_min_amount_to_send_error_message), Toast.LENGTH_LONG).show()
            return
        }

        if (Util.isNullOrEmpty(txtPassword)) {

            showConfirmationFields()

            if (this.smartCashApplication!!.AppPreferences.withoutPin.not()) {
                txtPassword.error = getString(R.string.send_pin_error_message)
                Toast.makeText(context, getString(R.string.send_pin_error_message), Toast.LENGTH_LONG).show()
            } else {
                txtPassword.error = getString(R.string.login_password_error_message)
                Toast.makeText(context, getString(R.string.login_password_error_message), Toast.LENGTH_LONG).show()
            }
            return
        }

        password = if (this.smartCashApplication!!.AppPreferences.withoutPin.not()) {
            verifyPin()
        } else {
            Util.getString(txtPassword)
        }

        if (password.isNotEmpty()) {

            if (selectedWallet.address!!.equals(Util.getString(txtToAddress), ignoreCase = true)) {
                txtToAddress.error = getString(R.string.send_different_address_to_send_error_message)
                Toast.makeText(context, getString(R.string.send_different_address_to_send_error_message), Toast.LENGTH_LONG).show()
                return
            }

            CheckIfUserExistsOnWebWalletTask(context!!, ::lockSendButton, ::afterCheckIfUserExistsOnWebWalletTask).execute(sendPayment)

        } else {
            //ERROR
            AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.send_pin_verification_dialog_title_error_message))
                    .setMessage(getString(R.string.send_pin_verification_dialog_message_error_message))
                    .setPositiveButton(getString(R.string.send_pin_verification_dialog_ok_button)) { dialog, id -> dialog.cancel() }.show()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @OnClick(R.id.login_main_btn_eye)
    fun onBtnEyeClicked() {
        if (isPasswordVisible) {
            txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            isPasswordVisible = false
            btnEye.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
        } else {
            txtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            isPasswordVisible = true
            btnEye.setImageDrawable(resources.getDrawable(R.drawable.eye_off))
        }

        txtPassword.setSelection(txtPassword.text.length)
    }

    @OnClick(R.id.btn_eye2)
    fun onBtnEye2Clicked() {
        if (isPasswordVisible) {
            txtTwoFa.transformationMethod = PasswordTransformationMethod.getInstance()
            isTwoFaVisible = false
            btnEye2.setImageDrawable(resources.getDrawable(R.drawable.ic_eye))
        } else {
            txtTwoFa.transformationMethod = HideReturnsTransformationMethod.getInstance()
            isTwoFaVisible = true
            btnEye2.setImageDrawable(resources.getDrawable(R.drawable.eye_off))
        }
        txtTwoFa.setSelection(txtTwoFa.text.length)
    }

    private fun hideConfirmationFields() {

        view!!.findViewById<View>(R.id.first_card).visibility = View.VISIBLE

        view!!.findViewById<View>(R.id.second_card).visibility = View.VISIBLE

        view!!.findViewById<View>(R.id.pin_label).visibility = View.GONE

        view!!.findViewById<View>(R.id.relativelayout).visibility = View.GONE

        view!!.findViewById<View>(R.id.relativelayout2).visibility = View.GONE
    }

    private fun showConfirmationFields() {

        view!!.findViewById<View>(R.id.first_card).visibility = View.GONE

        view!!.findViewById<View>(R.id.second_card).visibility = View.GONE

        view!!.findViewById<View>(R.id.pin_label).visibility = View.VISIBLE

        view!!.findViewById<View>(R.id.relativelayout).visibility = View.VISIBLE

        view!!.findViewById<View>(R.id.relativelayout2).visibility = View.VISIBLE


    }

    private fun setupRecyclerViewWallets(recyclerView: RecyclerView, dialog: AlertDialog) {

        val linearLayoutManagerTransactions = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        val walletAdapter = WalletDialogAdapter(context!!, ArrayList(), this.txtToAddress, dialog)

        recyclerView.layoutManager = linearLayoutManagerTransactions

        recyclerView.adapter = walletAdapter

        walletAdapter.setItems(this.smartCashApplication!!.AppPreferences.wallet!!)

    }

    private fun clearInputs() {
        txtToAddress.setText("")
        txtAmountFiat.setText("")
        txtAmountCrypto.setText(0.0.toString())
        txtPassword.setText("")
        setFeeOnButton(amountConverted.toString())
    }

    //TODO: Make it an async task
    private fun updateData() {
        ViewModelProviders.of(this).get<UserViewModel>(UserViewModel::class.java).also {

            it.getUser(smartCashApplication!!.getToken()!!, activity!!).observe(this, androidx.lifecycle.Observer { response ->
                if (response != null) {
                    smartCashApplication!!.saveUser(activity!!, response)
                    (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).setWalletValue()

                    //unlockSendButton()
                    Toast.makeText(activity, getString(R.string.send_message_success_return), Toast.LENGTH_LONG).show()
                    navigateToTransaction()
                    clearInputs()
                } else {
                    Log.e(TAG, getString(R.string.send_message_error_return))
                }
            })
        }
    }

    private fun navigateToTransaction() {
        val nav = activity!!.findViewById<BottomNavigationView>(R.id.navigationView)
        nav.selectedItemId = R.id.nav_trans
        openFragment(TransactionFragment.newInstance())
    }

    private fun verifyPin(): String {

        return if (this.smartCashApplication!!.AppPreferences.withoutPin) {
            txtPassword.text.toString()
        } else this.smartCashApplication!!.getDecryptedPassword(activity!!, txtPassword.text.toString())

    }

    private fun lockSendButton() {
        sendButton.isEnabled = false
        sendButton.text = getString(R.string.send_save_button_loading_status)
        loader.visibility = View.VISIBLE
    }

    private fun unlockSendButton() {
        sendButton.isEnabled = true
        setFeeOnButton()
        loader.visibility = View.GONE
    }

    private fun beforeSendByWebWallet() {
        lockSendButton()
    }

    private fun afterSendByWebWallet(result: WebWalletRootResponse<String>?) {

        val hasError = Util.showWebWalletException(result, context!!)

        if (hasError.not()) {
            updateData()
            Log.d(TAG, result?.data)
        }
    }

    private fun afterSendSmartByTextTask(smartTextRoot: SmartTextRoot?) {
        if (smartTextRoot?.data != null) {
            val sendPaymentResultFromSendByText = Util.fillSendSendSmartByWebWalletRequestBySmartTextReponse(smartTextRoot)?.apply {
                this.email = email
                this.userKey = password
                if (!Util.isNullOrEmpty(txtTwoFa)) this.code = Util.getString(txtTwoFa)
            }

            SendSmartByWebWalletTask(context!!, ::beforeSendByWebWallet, ::afterSendByWebWallet).execute(sendPaymentResultFromSendByText)

            val order = StringBuilder()
            order.append("https://smartext.me/Order/" + smartTextRoot.data!!.orderID!!)
            Log.d(TAG, order.toString())

        } else {
            Toast.makeText(context, getString(R.string.send_web_wallet_error_message), Toast.LENGTH_LONG).show()
        }
        unlockSendButton()
    }

    private fun afterCheckIfUserExistsOnWebWalletTask(booleanWebWalletRootResponse: WebWalletRootResponse<Boolean>?) {
        if (booleanWebWalletRootResponse != null && booleanWebWalletRootResponse.data!!) {
            //SEND BY EMAIL/SMS/LINK
            sendBySmartTextOrder()
        } else {
            //SEND TO WEB WALLET
            sendByWebWallet()
        }
    }

    private fun afterCalculateFeeTask(fee: WebWalletRootResponse<Double>?) {
        val hasError = Util.showWebWalletException(fee, context!!)
        if (hasError.not()) {
            mainFee = BigDecimal.valueOf(fee?.data!!)
            setFeeOnButton()
            Log.d(TAG, fee.data.toString())
        }
        unlockSendButton()
    }

    private fun setFeeOnButton(fee: String = mainFee!!.toString()) {
        sendButton.text = getString(R.string.send_button_label).replace("%f", smartCashApplication!!.formatNumberByDefaultCrypto(java.lang.Double.parseDouble(fee)))
    }

    private fun sendByWebWallet() {
        SendSmartByWebWalletTask(context!!, ::beforeSendByWebWallet, ::afterSendByWebWallet).execute(sendPayment)
    }

    private fun sendSmartByText() {
        SendSmartByTextTask(context!!, sendPayment, ::beforeSendByWebWallet, ::afterSendSmartByTextTask).execute(Util.fillSmartTextRequest(sendPayment))
    }

    private fun getCurrentFeeAndPrices() {
        if (isOnline) {
            val sendPaymetToCalculateFee = SendPayment()
            sendPaymetToCalculateFee.fromAddress = smartCashApplication?.getWallet()!!.address
            sendPaymetToCalculateFee.toAddress = Util.getString(txtToAddress)
            CalculateFeeTask(context!!, ::beforeSendByWebWallet, ::afterCalculateFeeTask).execute(sendPaymetToCalculateFee)
        }
    }

    private fun sendBySmartTextOrder() {
        if (Util.isValidEmail(Util.getString(txtToAddress)) || PhoneNumberUtils.isGlobalPhoneNumber(Util.getString(txtToAddress))) {
            sendSmartByText()
        } else {
            sendByWebWallet()
        }
    }

    companion object {

        //region declarations
        val TAG: String? = SendAddressFragment::class.java.simpleName
        const val ZERO = "0"
        const val QR_SEPARATOR = "-"
        private const val RC_CAMERA_PERM = 123
        private const val MAX_LENGTH_PASSWORD = 123

        fun newInstance(): SendAddressFragment {
            return SendAddressFragment()
        }
    }
}