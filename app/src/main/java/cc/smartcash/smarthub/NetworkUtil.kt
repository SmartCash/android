package cc.smartcash.smarthub

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    var TYPE_WIFI = 1
    var TYPE_MOBILE = 2
    var TYPE_NOT_CONNECTED = 0

    var TYPE_WIFI_TEXT = "Wifi enabled"
    var TYPE_MOBILE_TEXT = "Mobile data enabled"
    var TYPE_NOT_CONNECTED_TEXT = "Not connected to Internet"

    private fun getConnectivityStatus(context: Context): Int {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        when (conn) {
            TYPE_WIFI -> status = TYPE_WIFI_TEXT
            TYPE_MOBILE -> status = TYPE_MOBILE_TEXT
            TYPE_NOT_CONNECTED -> status = TYPE_NOT_CONNECTED_TEXT
        }
        return status
    }
}