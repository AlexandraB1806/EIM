package ro.pub.cs.systems.eim.lab06.clientservercommunication.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;

    private final EditText serverTextEditText;

    public ServerThread(EditText serverTextEditText) {
        this.serverTextEditText = serverTextEditText;
    }

    public void startServer() {
        isRunning = true;
        start();

        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;

        new Thread(() -> {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }

                Log.v(Constants.TAG, "stopServer() method invoked" + serverSocket);
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            // Pornire server.
            // Serverul asculta la toate interfetele de retea disponibile (0.0.0.0).
            // Dimensiunea maxima a cozii de asteptare a conexiunilor este 50
            serverSocket = new ServerSocket(Constants.SERVER_PORT, 50, InetAddress.getByName("0.0.0.0"));

            // ciclu
            while (isRunning) {
                // accept() -> deschiderea altui socket
                Socket socket = serverSocket.accept();

                Log.v(Constants.TAG, "accept()-ed: " + socket.getInetAddress());

                // Se porneste un thread separat pentru comunicarea intre server si client
                CommunicationThread communicationThread = new CommunicationThread(socket, serverTextEditText);
                communicationThread.start();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }
    }
}
