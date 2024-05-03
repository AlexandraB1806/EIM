package ro.pub.cs.systems.eim.lab07.calculatorwebservice.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];

        // GET -> poz 0; POST -> poz 1
        // Dupa aceste coduri (0 / 1) se va face switch
        int method = Integer.parseInt(params[3]);

        // Daca unul dintre operatori nu este precizat, se va afisa un mesaj de eroare
        String errorMessage = null;

        if (operator1 == null || operator1.isEmpty()) {
            errorMessage = Constants.ERROR_MESSAGE_EMPTY;
        }

        if (operator2 == null || operator2.isEmpty()) {
            errorMessage = Constants.ERROR_MESSAGE_EMPTY;
        }

        if (errorMessage != null) {
            return errorMessage;
        }

        // Creez o instanta a unui obiect de tip HttpClient
        HttpClient httpClient = new DefaultHttpClient();

        // Din Spinner-ul de la Method, obtin metoda suportata pentru
        // transmiterea informatiilor: GET / POST
        switch (method) {
            case Constants.GET_OPERATION:
                // Construiesc URL-ul in cadrul unui obiect de tip HttpGet.
                // Ca sa transmit parametrii catre serverul web, trebuie sa le fac append la URL
                // - nu depasesc 2048 caractere
                // - doar caractere ASCII
                // - ex: HttpGet httpGet = new HttpGet("http:*www.server.com?attribute1=value1&...&attributen=valuen");
                HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                        + "?" + Constants.OPERATION_ATTRIBUTE + "=" + operation
                        + "&" + Constants.OPERATOR1_ATTRIBUTE + "=" + operator1
                        + "&" + Constants.OPERATOR2_ATTRIBUTE + "=" + operator2);

                // Creare obiect de tip ResponseHandler -> va fi trimis ca parametru
                // in metoda execute()
                ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();

                try {
                    // Executa cererea.
                    // Rezultatul metodei execute() este un sir de caractere
                    // ce contine resursa care se doreste a fi descarcata
                    return httpClient.execute(httpGet, responseHandlerGet);
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, Objects.requireNonNull(ioException.getMessage()));
                }

                break;

            case Constants.POST_OPERATION:
                // Construiesc URL-ul in cadrul unui obiect de tip HttpPost
                HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);

                // Creez o lista de perechi de tipul (atribut, valoare) care contine informatiile
                // transmise de client pe baza carora serverul va genera continutul
                List<NameValuePair> parameters = new ArrayList<>();
                parameters.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
                parameters.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1));
                parameters.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2));

                // Creez o instanta a obiectului UrlEncodedFormEntity folosind lista parameters
                // si UTF-8 encoding. Instanta se va atasa cererii POST
                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                    httpPost.setEntity(urlEncodedFormEntity);
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    Log.e(Constants.TAG, Objects.requireNonNull(unsupportedEncodingException.getMessage()));
                }

                // Creare obiect de tip ResponseHandler -> va fi trimis ca parametru
                // in metoda execute()
                ResponseHandler<String> responseHandlerPost = new BasicResponseHandler();

                try {
                    // Executa cererea pentru a se genera rezultatul
                    return httpClient.execute(httpPost, responseHandlerPost);
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, Objects.requireNonNull(ioException.getMessage()));
                }

                break;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // Afisare rezultat in resultTextView
        resultTextView.setText(result);
    }
}
