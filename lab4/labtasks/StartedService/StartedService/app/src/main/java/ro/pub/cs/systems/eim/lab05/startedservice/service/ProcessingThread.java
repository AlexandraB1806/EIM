package ro.pub.cs.systems.eim.lab05.startedservice.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ro.pub.cs.systems.eim.lab05.startedservice.general.Constants;

public class ProcessingThread extends Thread {

    private final Context context;

    public ProcessingThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.d(Constants.TAG, "Thread.run() was invoked, PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());

        // Temporizarea mesajelor se face cu sleep()
        while (true) {
            sendMessage(Constants.MESSAGE_STRING);
            sleep();

            sendMessage(Constants.MESSAGE_INTEGER);
            sleep();

            sendMessage(Constants.MESSAGE_ARRAY_LIST);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(Constants.SLEEP_TIME);
        } catch (InterruptedException interruptedException) {
            Log.e(Constants.TAG, interruptedException.getMessage());
            interruptedException.printStackTrace();
        }
    }

    // setAction() & putExtra()
    private void sendMessage(int messageType) {
        // Se propaga 3 intentii cu difuzare la nivelul SO Android
        Intent intent = new Intent();

        // Pentru fiecare intentie creata, se specifica:
        // - actiunea: setAction()
        // - informatiile transmise, specificate in campul extra: putExtra(cheie, valoare);
        //                           Constants.DATA -> STRING / INTEGER / ARRAY_LIST
        switch(messageType) {
            case Constants.MESSAGE_STRING:
                intent.setAction(Constants.ACTION_STRING);
                intent.putExtra(Constants.DATA, Constants.STRING_DATA);
                break;

            case Constants.MESSAGE_INTEGER:
                intent.setAction(Constants.ACTION_INTEGER);
                intent.putExtra(Constants.DATA, Constants.INTEGER_DATA);
                break;

            case Constants.MESSAGE_ARRAY_LIST:
                intent.setAction(Constants.ACTION_ARRAY_LIST);
                intent.putExtra(Constants.DATA, Constants.ARRAY_LIST_DATA);
                break;

            default:
                break;
        }

        // Transmiterea propriu-zisa a intentiei
        context.sendBroadcast(intent);
    }
}
