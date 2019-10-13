package cc.smartcash.wallet.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;

        String internet = " | " + (isInternetAvailable() ? "Internet enabled" : "Internet disabled");

        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled | " + internet;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled" + internet;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet" + internet;
        }
        return status;
    }

    public static boolean getInternetStatus(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        boolean status = false;
        boolean isInternetAvailable = isInternetAvailable();

        if (!isInternetAvailable) return false;

        if (conn == NetworkUtil.TYPE_WIFI && isInternetAvailable) {
            status = true;
        } else if (conn == NetworkUtil.TYPE_MOBILE && isInternetAvailable) {
            status = true;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = false;
        }
        return status;
    }

    public static boolean isInternetAvailable() {
        return true;
    }
}
