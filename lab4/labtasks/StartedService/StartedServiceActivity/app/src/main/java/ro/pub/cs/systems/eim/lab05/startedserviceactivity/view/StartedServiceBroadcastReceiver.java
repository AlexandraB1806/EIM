package ro.pub.cs.systems.eim.lab05.startedserviceactivity.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.startedserviceactivity.general.Constants;

public class StartedServiceBroadcastReceiver extends BroadcastReceiver {

    private final TextView messageTextView;

    // default constructor
    public StartedServiceBroadcastReceiver() {
        this.messageTextView = null;
    }

    public StartedServiceBroadcastReceiver(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    // Datele extrase din intentie (cheia: Constants.DATA) vor fi afisate intr-un camp text (messageTextView)
    // din cadrul interfetei grafice

    // Se verifica:
    // - actiunea: getAction()
    // - informatiile transmise continute in campul extra (bazat pe tipul de date din cadrul intentului):
    //      --> getStringExtra() / getIntExtra() / getStringArrayListExtra()
    @SuppressLint("SetTextI18n")
    @Override
    public void onReceive(Context context, Intent intent) {
        // ex 5
        // Obtinere actiune + informatiile extra din intent
        String action = intent.getAction();
        String data = null;

        if (action != null) {
            switch(action) {
                case Constants.ACTION_STRING:
                    data = intent.getStringExtra(Constants.DATA);
                    break;
                case Constants.ACTION_INTEGER:
                    data = String.valueOf(intent.getIntExtra(Constants.DATA, 0));
                    break;
                case Constants.ACTION_ARRAY_LIST:
                    data = intent.getStringArrayListExtra(Constants.DATA).toString();
                    break;
                default:
                    break;
            }
        }

        // In functie de ce constructor s-a apelat:
        // cel default / cel care primeste ca arg un TextView
        if (messageTextView != null) {
            // Seteaza textul pe messageTextView
            messageTextView.setText(messageTextView.getText().toString() + "\n" + data);
        } else {
            // ex 7
            // Daca messageTextView nu e valabil (s-a apelat constructorul default),
            // da restart la activitate printr-un intent
            Intent startedServiceActivityIntent = new Intent(context, StartedServiceActivity.class);
            startedServiceActivityIntent.putExtra(Constants.MESSAGE, data);
            startedServiceActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            context.startActivity(startedServiceActivityIntent);
        }
    }
}
