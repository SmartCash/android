package cc.smartcash.wallet.Tasks;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class InternetCheckTask extends Service {
    public InternetCheckTask() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
