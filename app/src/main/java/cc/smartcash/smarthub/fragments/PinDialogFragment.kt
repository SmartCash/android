package cc.smartcash.smarthub.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import cc.smartcash.smarthub.R


class PinDialogFragment : DialogFragment() {
    var pinText: EditText? = null
    var button: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var _view: View = getActivity()!!.getLayoutInflater().inflate(R.layout.fragment_pin_dialog, null)
        this.pinText = _view.findViewById(R.id.txtPIN)
        this.button = _view.findViewById(R.id.wallet_dialog_bkp_btnGoToCheckPIN)

        val alert = AlertDialog.Builder(activity!!)
        alert.setView(_view)

        this.button!!.setOnClickListener({
            this.dismiss()
        })

        return alert.create()
    }
}
