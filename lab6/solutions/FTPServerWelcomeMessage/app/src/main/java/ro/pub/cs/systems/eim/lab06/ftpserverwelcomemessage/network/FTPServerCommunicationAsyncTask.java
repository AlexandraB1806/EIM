package ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Constants;
import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Utilities;

public class FTPServerCommunicationAsyncTask extends AsyncTask<String, String, Void> {

    private final TextView welcomeMessageTextView;

    public FTPServerCommunicationAsyncTask(TextView welcomeMessageTextView) {
        this.welcomeMessageTextView = welcomeMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        try {
            socket = new Socket(params[0], Integer.parseInt(params[1]));
            Log.v(Constants.TAG, "Connected to: " + socket.getInetAddress() + ":" + socket.getLocalPort());
            bufferedReader = Utilities.getReader(socket);
            String line = bufferedReader.readLine();
            if (line != null) {
                Log.v(Constants.TAG, "A line has been received from the FTP server: " + line);
                publishProgress(line);
                while ((line = bufferedReader.readLine()) != null) {
                    Log.v(Constants.TAG, "A line has been received from the FTP server: " + line);
                    publishProgress(line);
                }
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
        return null;
    }

    @Override
    protected void onPreExecute() {
        welcomeMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        if (progress != null && progress.length > 0 && progress[0] != null) {
            welcomeMessageTextView.append(progress[0] + "\n");
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        // You can perform any final operations here, such as updating UI to show task completion.
    }
}
