package cc.smartcash.wallet.Fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cc.smartcash.wallet.Adapters.WalletSpinnerAdapter
import cc.smartcash.wallet.R
import cc.smartcash.wallet.utils.SmartCashApplication
import cc.smartcash.wallet.utils.Util
import com.facebook.drawee.view.SimpleDraweeView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.math.BigDecimal
import java.util.*

class ReceiveFragment : Fragment() {

    private var smartCashApplication: SmartCashApplication? = null

    @BindView(R.id.fragment_receive_qrcode_image)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)

        val view = inflater.inflate(R.layout.fragment_receive, container, false)

        qrCodeImage = view.findViewById(R.id.fragment_receive_qrcode_image)

        ButterKnife.bind(this, view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(context!!)

        val displayMetrics = this.resources.displayMetrics

        walletSpinner.dropDownWidth = displayMetrics.widthPixels

        val walletSpinner = getView()!!.findViewById<Spinner>(R.id.fragment_receive_wallet_spinner)

        val walletAdapter = WalletSpinnerAdapter(context!!, smartCashApplication?.AppPreferences?.wallet!!)

        walletSpinner.adapter = walletAdapter

        walletSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                walletAddress.text = smartCashApplication?.AppPreferences?.wallet!![position].address
                smartCashApplication!!.saveWallet(smartCashApplication?.AppPreferences?.wallet!![position])

                setQRCodeByAmount()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val savedWallet = smartCashApplication!!.getWallet()

        if (savedWallet != null) {
            for (i in smartCashApplication?.AppPreferences?.wallet!!.indices) {
                if (savedWallet.walletId == smartCashApplication?.AppPreferences?.wallet!![i].walletId) {
                    walletSpinner.setSelection(i)
                }
            }
        }
        Util.setAmountListener(
                context!!,
                smartCashApplication!!,
                amountLabel,
                txtAmountFiat,
                txtAmountCrypto,
                BigDecimal(0.0),
                null,
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

    private fun createQRCode(qrCodeData: String, charset: String, hintMap: Map<*, *>, qrCodeheight: Int, qrCodewidth: Int) {


        try {
            //generating qr code.
            val matrix = MultiFormatWriter().encode(
                    qrCodeData.toByteArray(charset(charset)).toString(),
                    BarcodeFormat.QR_CODE,
                    qrCodewidth,
                    qrCodeheight,
                    hintMap as MutableMap<EncodeHintType, *>?
            )

            //converting bitmatrix to bitmap
            val width = matrix.width
            val height = matrix.height
            val pixels = IntArray(width * height)

            // All are 0, or black, by default
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    //for black and white
                    pixels[offset + x] = if (matrix.get(x, y)) BLACK else WHITE
                    //for custom color
                    //pixels[offset + x] = matrix.get(x, y) ?
                    //        ResourcesCompat.getColor(getResources(),R.color.colorB,null) :WHITE;
                }
            }
            //creating bitmap
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            //getting the logo
            val logo = BitmapFactory.decodeResource(resources, R.drawable.logo_qrcode)

            //setting bitmap to image view
            qrCodeImage.setImageBitmap(mergeBitmaps(logo, bitmap))

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

            createQRCode(qrCodeText, Util.UTF_8, hintMap, smallestDimension, smallestDimension)

        } catch (ex: Exception) {
            Log.e(TAG, ex.message)
        }

    }

    companion object {

        val TAG: String? = ReceiveFragment::class.java.simpleName

        fun newInstance(): ReceiveFragment {
            return ReceiveFragment()
        }
    }

}
