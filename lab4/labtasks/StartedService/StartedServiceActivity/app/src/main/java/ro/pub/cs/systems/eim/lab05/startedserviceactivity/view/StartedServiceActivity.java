package ro.pub.cs.systems.eim.lab05.startedserviceactivity.view;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.startedserviceactivity.R;
import ro.pub.cs.systems.eim.lab05.startedserviceactivity.general.Constants;

public class StartedServiceActivity extends AppCompatActivity {

    private TextView messageTextView;
    private StartedServiceBroadcastReceiver startedServiceBroadcastReceiver;
    private IntentFilter startedServiceIntentFilter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_service);

        messageTextView = (TextView)findViewById(R.id.message_text_view);

        // ex 4
        // Creare intent explicit; indica exact ca dorim sa pornim
        // un anumit serviciu, si anume: StartedService
        intent = new Intent();

        // setComponent(): indică pachetul aplicatiei Android care contine serviciul + clasa corespunzatoare serviciului
        intent.setComponent(new ComponentName(Constants.SERVICE_PACKAGE, Constants.SERVICE_CLASS));

        // Pornirea serviciului depinde de versiunea de SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Pentru versiuni mai sus de Oreo
            this.startForegroundService(intent);
        } else {
            this.startService(intent);
        }

        // ex 6 - a
        // Creare instanta a ascultatorului cu intentie de difuzare
        // => apel constructor care primeste messageTextView
        startedServiceBroadcastReceiver = new StartedServiceBroadcastReceiver(messageTextView);

        // ex 6 - b
        // Creare instanta de IntentFilter cu cele 3 tipuri de actiuni cuprinse in cadrul
        // intent-urilor de broadcast ale serviciului
        startedServiceIntentFilter = new IntentFilter();

        startedServiceIntentFilter.addAction(Constants.ACTION_STRING);
        startedServiceIntentFilter.addAction(Constants.ACTION_INTEGER);
        startedServiceIntentFilter.addAction(Constants.ACTION_ARRAY_LIST);
    }

    // ex 6 - c
    // Activarea ascultatorului - registerReceiver()
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter);
    }

    // ex 6 - c
    // Dezactivarea ascultatorului - unregisterReceiver(),
    @Override
    protected void onPause() {
        unregisterReceiver(startedServiceBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // ex 6 - d
        // Oprire serviciu
        stopService(intent);

        super.onDestroy();
    }

    // ex 7 - implementarea callback-ului onNewIntent
    // Activitatea este invocata prin intermediul intentiei,
    // in conditiile in care este activa (se gaseste in memorie ->
    // in stiva de activitati, fara a fi vizibila)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Obtinere mesaj in campul extra al intentului
        String message = intent.getStringExtra(Constants.MESSAGE);

        if (message != null) {
            Log.d("DATA", message);

            // Pune mesajul in TextView
            messageTextView.setText(messageTextView.getText().toString() + "\n" + message);
        }
    }
}
