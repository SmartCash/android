package cc.smartcash.wallet.Utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    private var TYPE_WIFI = 1
    private var TYPE_MOBILE = 2
    private var TYPE_NOT_CONNECTED = 0

    private val isInternetAvailable: Boolean
        get() = true

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

        val internet = " | " + if (isInternetAvailable) "Internet enabled" else "Internet disabled"

        when (conn) {
            TYPE_WIFI -> status = "Wifi enabled | $internet"
            TYPE_MOBILE -> status = "Mobile data enabled$internet"
            TYPE_NOT_CONNECTED -> status = "Not connected to Internet$internet"
        }
        return status
    }

    fun getInternetStatus(context: Context): Boolean {
        val conn = getConnectivityStatus(context)
        var status = false
        val isInternetAvailable = isInternetAvailable

        if (!isInternetAvailable) return false

        if (conn == TYPE_WIFI && isInternetAvailable) {
            status = true
        } else if (conn == TYPE_MOBILE && isInternetAvailable) {
            status = true
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = false
        }
        return status
    }
}
