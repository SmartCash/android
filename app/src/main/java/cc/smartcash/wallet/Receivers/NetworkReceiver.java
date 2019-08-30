package cc.smartcash.wallet.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cc.smartcash.wallet.Utils.NetworkUtil;

public class NetworkReceiver extends BroadcastReceiver {

    public static final String TAG = NetworkReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "The status of the network has changed");
        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context, "Receiver | " + intent.getAction() + " | " + status, Toast.LENGTH_LONG).show();

    }

}
