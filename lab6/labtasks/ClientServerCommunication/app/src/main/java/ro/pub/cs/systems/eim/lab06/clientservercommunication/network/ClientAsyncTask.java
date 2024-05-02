package ro.pub.cs.systems.eim.lab06.clientservercommunication.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Constants;
import ro.pub.cs.systems.eim.lab06.clientservercommunication.general.Utilities;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private TextView serverMessageTextView;

    public ClientAsyncTask(TextView serverMessageTextView) {
        this.serverMessageTextView = serverMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        BufferedReader bufferedReader = null;

        try {
            // Obtinere parametrii de conexiune la server
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);

            // Deschidere socket catre server cu cei 2 parametrii obtinuti
            socket = new Socket(serverAddress, serverPort);

            // Obtinere flux de intrare atasat socket-ului pentru citire
            bufferedReader = Utilities.getReader(socket);

            String line;

            // Citeste pana cand linia nu este null (adica EOF nu s-a trimis inca)
            // Facem append continutului la serverMessageTextView prin
            // publicarea progresului: publishProgress() -> UI Thread
            while ((line = bufferedReader.readLine()) !=  null) {
                publishProgress(line);
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

                if (socket != null) {
                    // Inchidere socket catre server
                    socket.close();
                }

                Log.v(Constants.TAG, "Connection closed (client)");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // Reseteaza continutul din textView-ul serverMessageTextView.
        // Acest lucru este realizat inainte de a rula firul de executie dedicat
        serverMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // Facem append continutului la textView-ul serverMessageTextView cu progress[0]
        // serverMessageTextView.append(progress[0] + "\n");

        if (progress != null && progress.length > 0 && progress[0] != null) {
            serverMessageTextView.append(progress[0] + "\n");
        }
    }

    @Override
    protected void onPostExecute(Void result) {}

}
