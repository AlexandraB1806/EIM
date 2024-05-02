package ro.pub.cs.systems.eim.lab06.singlethreadedserver.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Constants;
import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Utilities;

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    private EditText serverTextEditText;

    public ServerThread(EditText serverEditText) {
        this.serverTextEditText = serverEditText;
    }

    public void startServer() {
        isRunning = true;
        start();

        Log.v(Constants.TAG, "startServer() method invoked " + serverSocket);
    }

    // Repornirea serverului, astfel incat sa accepte din nou invocari
    // de la clienti, presupune crearea unui nou fir de executie
    // (cel vechi nu poate fi refolosit)
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
            // In momentul in care porneste serverul, se deschide un obiect serverSocket
            serverSocket = new ServerSocket(Constants.SERVER_PORT);

            // Ulterior, in cadrul unui ciclu, se asteapta invocari de la clienti,
            // prin intermediul metodei accept()
            while (isRunning) {
                // Metoda accept() deschide un alt obiect Socket care este blocant
                Socket socket = serverSocket.accept();

                Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());

                // Simulam faptul ca operatia de comunicare dintre client si server dureaza 3 secunde
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException interruptedException) {
                    Log.e(Constants.TAG, "An exception has occurred: " + interruptedException.getMessage());
                }

                // Dupa ce se obtine fluxul de iesire atasat, textul stocat se trimite
                // in controlul grafic corespunzator
                PrintWriter printWriter = Utilities.getWriter(socket);
                printWriter.println(serverTextEditText.getText().toString());

                // Se impune inchiderea canalului de comunicatie dupa ce acesta
                // nu mai este necesar => se inchide si serverSocket => exceptie in cadrul
                // metodei blocante accept() => invalidare conditie care asigura executia ciclului
                socket.close();

                Log.v(Constants.TAG, "Connection closed");
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }
    }
}
