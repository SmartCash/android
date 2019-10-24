package cc.smartcash.wallet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast

class NetworkChangeReceiver() : BroadcastReceiver() {

    private var listener: INetworkChangeReceiver? = null

    constructor(listener: INetworkChangeReceiver) : this() {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {

        val status = NetworkUtil.getConnectivityStatusString(context)

        Toast.makeText(context, status, Toast.LENGTH_LONG).show()

        listener?.onNetworkChange(status)
    }

    fun getIntentFilters(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        intentFilter.addAction("android.net.wifi.STATE_CHANGE")
        return intentFilter
    }

}
