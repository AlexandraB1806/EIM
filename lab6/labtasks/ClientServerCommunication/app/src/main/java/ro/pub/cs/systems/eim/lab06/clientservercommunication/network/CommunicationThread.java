package ro.pub.cs.systems.eim.lab06.clientservercommunication.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Utilities;

public class CommunicationThread extends Thread {

    private final Socket socket;
    private final EditText serverTextEditText;

    public CommunicationThread(Socket socket, EditText serverTextEditText) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());

            // Obiectul printWriter este folosit ca sa putem scrie catre fluxul de iesire asociat socket-ului.
            // Printam (scriem) o linie care sa contina textul din editText-ul serverTextEditText
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(serverTextEditText.getText().toString());

            socket.close();

            Log.v(Constants.TAG, "Connection closed (communication thread)");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }
    }
}
