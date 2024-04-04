package ro.pub.cs.systems.eim.modelcolocviu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ModelColocviuBroadcastReceiver extends BroadcastReceiver {

    // constructor default
    public ModelColocviuBroadcastReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String data = null;

        if (action != null) {
            switch(action) {
                case Constants.ACTION_1:
                case Constants.ACTION_2:
                case Constants.ACTION_3:
                    data = intent.getStringExtra(Constants.BROADCAST_ACTION);
                    break;
                default:
                    break;
            }
        }

        Log.d(Constants.TAG, data);
    }
}
