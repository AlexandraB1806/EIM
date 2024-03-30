package ro.pub.cs.systems.eim.bluetoothapp2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import ro.pub.cs.systems.eim.bluetoothapp2.Constants;

public class ChatUtils {
    private final Handler handler;
    private final BluetoothAdapter bluetoothAdapter;
    private final Context context;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private final UUID APP_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    private int state;

    public ChatUtils(Context context, Handler handler) {
        // Vom folosi handler pentru a realiza transmisia de date intre thread-uri
        // si MainActivity
        this.handler = handler;
        this.context = context;

        state = STATE_NONE;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public int getState() {
        return state;
    }

    public synchronized void setState(int state) {
        this.state = state;

        // Trimitem un mesaj catre MainActivity cu starea in care o conexiune se afla
        handler.obtainMessage(Constants.MESSAGE_STATE_CHANGED, state, -1).sendToTarget();
    }

    public synchronized void start() {
        //todo
    }

    public synchronized void stop() {
        //todo
    }

    public void connect(BluetoothDevice device) {
        //todo
    }

    public void write(byte[] buffer) {
        //todo
    }

    private void createAndStartAcceptThread() {
        //todo
    }

    private void createAndStartConnectThread(BluetoothDevice device) {
        //todo
    }

    private void cancelAcceptThread() {
        //todo
    }

    private void cancelConnectThread() {
        //todo
    }

    private void cancelConnectedThread() {
        //todo
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                String APP_NAME = "BluetoothChatApp";
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, APP_UUID);
            } catch (IOException e) {
                Log.e("Accept->Constructor", e.toString());
            }

            serverSocket = tmp;
        }

        public void run() {
            // TODO
        }

        public void cancel() {
            // todo
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private final BluetoothDevice device;

        public ConnectThread(BluetoothDevice device) {
            this.device = device;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            BluetoothSocket tmp = null;
            try {

                tmp = device.createRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e) {
                Log.e("Connect->Constructor", e.toString());
            }

            socket = tmp;
        }

        public void run() {
            // TODO
        }

        public void cancel() {
            // todo
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private class ConnectedThread extends Thread {
        private final BluetoothSocket socket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.d("Connected->Constructor", e.toString());
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            // TODO
        }

        public void write(byte[] buffer) {
            //TODO
        }

        public void cancel() {
            //TODO
        }
    }

    private void connectionLost() {
        Message message = handler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Connection Lost");
        message.setData(bundle);
        handler.sendMessage(message);

        ChatUtils.this.start();
    }

    private synchronized void connectionFailed() {
        Message message = handler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Cant connect to the device");
        message.setData(bundle);
        handler.sendMessage(message);

        ChatUtils.this.start();
    }

    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // todo
    }
}
