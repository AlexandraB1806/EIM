package ro.pub.cs.systems.eim.modelcolocviu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.Date;
import java.util.Random;

public class ModelColocviuService extends Service {
    private int numberLeft, numberRight, sumNumbers = 0;
    private boolean isServiceRunning = false;

    // constructor default
    public ModelColocviuService() {

    }

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            numberLeft = intent.getIntExtra(Constants.NUMBER_LEFT, 0);
            numberRight = intent.getIntExtra(Constants.NUMBER_RIGHT, 0);

            sumNumbers = numberLeft + numberRight;
        }

        if (isServiceRunning) {
            onDestroy();
        }

        startService();

        activityNotification();

        return START_REDELIVER_INTENT;
    }

    void startService() {
        isServiceRunning = true;

        new Thread(() -> {
            while (isServiceRunning) {
                sendBroadcastMessage();
                SystemClock.sleep(Constants.SLEEP_TIME);
            }
        }).start();
    }

    private void sendBroadcastMessage() {
        Intent intent = new Intent();

        Random random = new Random();
        int randomAction = random.nextInt(3);

        switch(randomAction) {
            case 0:
                intent.setAction(Constants.ACTION_1);
                intent.putExtra(Constants.BROADCAST_ACTION, String.valueOf(new Date(System.currentTimeMillis())));
                break;

            case 1:
                intent.setAction(Constants.ACTION_2);
                intent.putExtra(Constants.BROADCAST_ACTION, String.valueOf(arithmeticMean(numberLeft, numberRight)));
                break;

            case 2:
                intent.setAction(Constants.ACTION_3);
                intent.putExtra(Constants.BROADCAST_ACTION, String.valueOf(geometricMean(numberLeft, numberRight)));
                break;

            default:
                break;
        }

        sendBroadcast(intent);

    }

    private double arithmeticMean(int numberLeft, int numberRight) {
        return (double) (numberLeft + numberRight) / 2;
    }

    private double geometricMean(int numberLeft, int numberRight) {
        return Math.sqrt(numberLeft * numberRight);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
    }
}
