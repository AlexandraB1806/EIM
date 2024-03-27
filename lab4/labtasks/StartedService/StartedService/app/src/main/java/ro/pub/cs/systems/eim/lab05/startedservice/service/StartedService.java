package ro.pub.cs.systems.eim.lab05.startedservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import ro.pub.cs.systems.eim.lab05.startedservice.general.Constants;

public class StartedService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate() method was invoked");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.TAG, "onBind() method was invoked");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(Constants.TAG, "onUnbind() method was invoked");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(Constants.TAG, "onRebind() method was invoked");
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "onDestroy() method was invoked");
        super.onDestroy();
    }

    // ex 4
    private void activityNotification() {
        // Creeaza canalul de notificare: id + nume + grad de prioritate
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        }

        // Creare Notification Manager
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        // Creare Notificare: context + channel id
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(), Constants.CHANNEL_ID).build();
        }

        // versiuni >> Oreo
        startForeground(1, notification);
    }

    // Serviciul este pornit in urma apelului startService() din cadrul
    // altei componente: StartedServiceActivity (din metoda onCreate())
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "onStartCommand() method was invoked");

        // ex 3
        // Se porneste firul de executie ProcessingThread in cadrul caruia
        // sunt propagate cele 3 intentii cu difuzare: STRING, INTEGER, ARRAY_LIST
        ProcessingThread processingThread = new ProcessingThread(this);
        processingThread.start();

        // ex 4
        // Serviciul notifica activitatea ca a inceput
        activityNotification();

        return START_REDELIVER_INTENT;
    }
}
