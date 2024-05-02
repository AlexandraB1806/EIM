package ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Constants;
import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Utilities;

public class FTPServerCommunicationAsyncTask extends AsyncTask<String, String, String> {

    private final TextView welcomeMessageTextView;

    public FTPServerCommunicationAsyncTask(TextView welcomeMessageTextView) {
        this.welcomeMessageTextView = welcomeMessageTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        String line = null;

        try {
            if (params != null && params.length > 0 && params[0] != null) {
                // Deschidere socket cu cei 2 parametrii, hostname + port:
                // - params[0]: FTPServerAddressEditText.getText().toString()
                // - params[1]: Constants.FTP_PORT = 21
                socket = new Socket(params[0], Integer.parseInt(params[1]));

                Log.v(Constants.TAG, "Conectat la: " + socket.getInetAddress() + ":" + socket.getLocalPort());

                // Obtinerea fluxului de intrare atasat socket-ului, prin
                // apelul metodei aferente din clasa ajutatoare
                bufferedReader = Utilities.getReader(socket);

                // Citirea liniei de pe fluxul de intrare
                line = bufferedReader.readLine();

                if (line != null) {
                    Log.v(Constants.TAG, "Linia citita de la serverul FTP este analizata: " + line);

                    // Caz 1. Mesajul este analizat daca linia citita incepe cu: Constants.FTP_MULTILINE_START_CODE = "220-"
                    if (line.startsWith(Constants.FTP_MULTILINE_START_CODE)) {
                        // Valoarea primita de la serverul FTP (line) este inclusa in continutul
                        // textView-ului welcomeMessageTextView pe THREAD-UL DE UI cu ajutorul
                        // metodei "publishProgress()" -> invoca automat metoda de callback "onProgressUpdate()"
                        publishProgress(line);

                        // Se citesc in continuare linii de pe fluxul de intrare atasat socket-ului cat timp valoarea:
                        // - este diferita de FTP_MULTILINE_END_CODE1 = "220"
                        // - nu incepe cu FTP_MULTILINE_END_CODE2 = "220 "
                        while ((line = bufferedReader.readLine()) !=  null) {
                            Log.v(Constants.TAG, "Linia citita de la serverul FTP in continuare: " + line);

                            if (!(!line.equals(Constants.FTP_MULTILINE_END_CODE1) && !line.startsWith(Constants.FTP_MULTILINE_END_CODE2))) {
                                break;
                            }

                            publishProgress(line);
                        }
                    }
                    // Altfel, Caz 2. Mesajul este ignorat
                }
            } else {
                Log.d(Constants.TAG, "Parametrii lipsesc sau sunt incorecti");
            }
        } catch (IOException ioException) {
            String errorMessage = ioException.getMessage() == null ? "Unknown IOException" : ioException.getMessage();
            Log.d(Constants.TAG, errorMessage);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
                String errorMessage = ioException.getMessage() == null ? "Unknown IOException" : ioException.getMessage();
                Log.d(Constants.TAG, errorMessage);
            }
        }

        return line;
    }


    @Override
    protected void onPreExecute() {
        // Reseteaza continutul din textView-ul welcomeMessageTextView.
        // Acest lucru este realizat inainte de a rula firul de executie dedicat
        welcomeMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // Facem append continutului la textView-ul welcomeMessageTextView cu progress[0]
        if (progress != null && progress.length > 0 && progress[0] != null) {
            welcomeMessageTextView.append(progress[0] + "\n");
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Actualizam UI-ul pentru a demonstra rezolvarea task-ului
        welcomeMessageTextView.setText(result);
    }
}
