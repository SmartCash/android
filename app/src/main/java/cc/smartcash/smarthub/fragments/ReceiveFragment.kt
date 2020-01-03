package cc.smartcash.smarthub.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.smarthub.R
import cc.smartcash.smarthub.R.id.fragment_receive_qrcode_image
import cc.smartcash.smarthub.adapters.WalletSpinnerAdapter
import cc.smartcash.smarthub.utils.NetworkUtil
import cc.smartcash.smarthub.utils.SmartCashApplication
import cc.smartcash.smarthub.utils.Util
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.facebook.drawee.view.SimpleDraweeView
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import net.glxn.qrgen.android.QRCode
import pub.devrel.easypermissions.EasyPermissions
import java.math.BigDecimal
import java.util.*

class ReceiveFragment : Fragment(), QRCodeReaderView.OnQRCodeReadListener, PinDialogFragment.OnAddPinListener {

    private var smartCashApplication: SmartCashApplication? = null
    private var isOnline: Boolean = false
    private var mainFee: BigDecimal? = null
    private var perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.CAMERA)
    private var dialog: AlertDialog? = null
    private var parts: Array<String> = emptyArray()

    @BindView(fragment_receive_qrcode_image)
    internal lateinit var qrCodeImage: SimpleDraweeView

    @BindView(R.id.fragment_receive_wallet_address)
    lateinit var walletAddress: TextView

    @BindView(R.id.fragment_receive_wallet_spinner)
    lateinit var walletSpinner: Spinner

    @BindView(R.id.fragment_receive_txt_amount_converted)
    lateinit var txtAmountCrypto: EditText

    @BindView(R.id.fragment_receive_txt_amount_fiat)
    lateinit var txtAmountFiat: EditText

    @BindView(R.id.fragment_receive_amount_label_fiat)
    lateinit var amountLabel: TextView

    @BindView(R.id.fragment_receive_amount_crypto_label)
    lateinit var amountWithLabel: TextView

    @BindView(R.id.receive_button_fragment)
    lateinit var receiveButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.mainFee = BigDecimal.valueOf(0.0)

        this.isOnline = NetworkUtil.getInternetStatus(getContext()!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)

        val view = inflater.inflate(R.layout.fragment_receive, container, false)

        qrCodeImage = view.findViewById(fragment_receive_qrcode_image)

        ButterKnife.bind(this, view)

        if (!isOnline) {
            receiveButton.text = getString(R.string.send_save_button_network_error_message)
            receiveButton.isActivated = false
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EasyPermissions.requestPermissions(this, getString(R.string.send_camera_permission_label), ReceiveFragment.RC_CAMERA_PERM, *perms)

        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)

        val displayMetrics = this.resources.displayMetrics

        walletSpinner.dropDownWidth = displayMetrics.widthPixels

        val walletSpinner = getView()!!.findViewById<Spinner>(R.id.fragment_receive_wallet_spinner)

        val walletAdapter = WalletSpinnerAdapter(context!!, smartCashApplication?.appPreferences?.wallet!!)

        walletSpinner.adapter = walletAdapter

        walletSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                walletAddress.text = smartCashApplication?.appPreferences!!.wallet!![position].address
                smartCashApplication!!.saveWallet(smartCashApplication?.appPreferences!!.wallet!![position])

                setQRCodeByAmount()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
//Nothing to do
            }
        }

        val savedWallet = smartCashApplication!!.getWallet()

        if (savedWallet != null) {
            for (i in smartCashApplication?.appPreferences!!.wallet!!.indices) {
                if (savedWallet.walletId == smartCashApplication?.appPreferences!!.wallet!![i].walletId) {
                    walletSpinner.setSelection(i)
                }
            }
        }

        setFeeOnButton()

        Util.setAmountListener(
                context!!,
                smartCashApplication!!,
                amountLabel,
                txtAmountFiat,
                txtAmountCrypto,
                mainFee,
                receiveButton,
                ::setQRCodeByAmount
        )
    }

    @OnClick(R.id.fragment_receive_btn_copy)
    fun onViewClicked() {
        SmartCashApplication.copyToClipboard(context!!, walletAddress.text.toString())
    }

    fun setQRCodeByAmount() {

        var amountInSmartCash = "0.0"

        when {
            Util.isNullOrEmpty(txtAmountCrypto).not() && Util.getString(txtAmountCrypto) != "." -> {
                amountInSmartCash = txtAmountCrypto.text.toString()
            }
        }

        when {
            Util.isNullOrEmpty(amountInSmartCash).not() && java.lang.Float.parseFloat(amountInSmartCash) > 0 -> generateQrCode("smartcash:" + walletAddress.text.toString() + "?amount=" + amountInSmartCash)
            else -> generateQrCode("smartcash:" + walletAddress.text.toString())
        }

    }


    private fun qR(qrCodeData: String, qrCodeHeight: Int, qrCodeWidth: Int): Bitmap? {
        return QRCode.from(qrCodeData)
                .withSize(qrCodeWidth, qrCodeHeight)
                .bitmap()
    }

    private fun createQRCode(qrCodeData: String, qrCodeHeight: Int, qrCodeWidth: Int) {
        try {
            //getting the logo
            val logo = BitmapFactory.decodeResource(resources, R.drawable.logo_qrcode)
            //setting bitmap to image view
            qrCodeImage.setImageBitmap(mergeBitmaps(logo, qR(qrCodeData, qrCodeHeight, qrCodeWidth)!!))
        } catch (er: Exception) {
            Log.e(TAG, er.message)
        }
    }

    private fun mergeBitmaps(overlay: Bitmap, bitmap: Bitmap): Bitmap {

        val height = bitmap.height
        val width = bitmap.width

        val combined = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(combined)
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height

        canvas.drawBitmap(bitmap, Matrix(), null)

        val centreX = (canvasWidth - overlay.width) / 2
        val centreY = (canvasHeight - overlay.height) / 2
        canvas.drawBitmap(overlay, centreX.toFloat(), centreY.toFloat(), null)

        return combined
    }

    private fun generateQrCode(qrCodeText: String) {
        try {

            //setting size of qr code
            val width = qrCodeImage.width
            val height = qrCodeImage.height
            val smallestDimension = if (width < height) width else height

            val hintMap = HashMap<EncodeHintType, ErrorCorrectionLevel>()

            hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

            createQRCode(qrCodeText, smallestDimension, smallestDimension)

        } catch (ex: Exception) {
            Log.e(TAG, ex.message)
        }

    }


    private fun setFeeOnButton(fee: String = mainFee!!.toString()) {
        receiveButton.text = getString(R.string.receive_button_label).replace("%f", smartCashApplication!!.formatNumberByDefaultCrypto(java.lang.Double.parseDouble(fee)))
    }


    @OnClick(R.id.receive_button_fragment)
    fun onBtnQrCodeClicked() =
        if (EasyPermissions.hasPermissions(Objects.requireNonNull<FragmentActivity>(activity), *perms)) {
            val scanQrCodeDialog = AlertDialog.Builder(context)
            val scanQrCodeView = LayoutInflater.from(context).inflate(R.layout.scan_qrcode_dialog, null)

            val btnCancel = scanQrCodeView.findViewById<Button>(R.id.show_wallets_dialog_scan_qrcode_dialog_cancel_button)
            btnCancel.setOnClickListener {
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

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        if (text == null || text.isEmpty()) return

        val parsedQr = Util.parseQrCode(text)

        if (parsedQr.isEmpty()) return

        if (parsedQr.indexOf(QR_SEPARATOR) == -1) {
            Toast.makeText(activity, "Erro on read card...", Toast.LENGTH_SHORT).show()
        } else {
            parts = parsedQr.split(QR_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            Toast.makeText(activity, parts[1], Toast.LENGTH_SHORT).show()
            //txtToAddress.setText(parts[0])
            //txtAmountCrypto.setText(parts[1])

            var dialogPin : PinDialogFragment = PinDialogFragment()
            dialogPin.show(childFragmentManager,"PinDialogFragment")
        }

        /*
        Util.calculateFromSmartToFiat(
                context!!,
                smartCashApplication!!,
                amountLabel,
                txtAmountFiat,
                txtAmountCrypto,
                mainFee,
                sendButton
        )
        */
        dialog!!.hide()
    }

    override fun onResultPin(pin: String) {
        Toast.makeText(activity, pin, Toast.LENGTH_LONG).show()
    }

    companion object {

        val TAG: String? = ReceiveFragment::class.java.simpleName
        private const val RC_CAMERA_PERM = 123
        const val QR_SEPARATOR = "-"
        const val ZERO = "0"

        fun newInstance(): ReceiveFragment {
            return ReceiveFragment()
        }
    }
}
