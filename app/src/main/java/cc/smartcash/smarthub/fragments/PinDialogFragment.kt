package cc.smartcash.smarthub.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import cc.smartcash.smarthub.R


class PinDialogFragment : DialogFragment() {
    @BindView(R.id.txtPIN)
    lateinit var pinText: EditText

    private lateinit var callback: OnAddPinListener
    var button: Button? = null

    interface OnAddPinListener {
        fun onResultPin(pin: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            callback = targetFragment as OnAddPinListener
        } catch (e: Exception) {
            throw ClassCastException("Calling Fragment must implement OnAddFriendListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var _view: View = getActivity()!!.getLayoutInflater().inflate(R.layout.fragment_pin_dialog, null)
        this.pinText = _view.findViewById(R.id.txtPIN)
        this.button = _view.findViewById(R.id.wallet_dialog_bkp_btnGoToCheckPIN)

        val alert = AlertDialog.Builder(activity!!)
        alert.setView(_view)

        this.button!!.setOnClickListener({
            callback.onResultPin("1234");
            dismiss()
        })

        return alert.create()
    }
}
