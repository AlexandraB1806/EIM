package ro.pub.cs.systems.eim.lab07.landmarklister.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.lab07.landmarklister.controller.LandmarkInformationAdapter;
import ro.pub.cs.systems.eim.lab07.landmarklister.general.Constants;
import ro.pub.cs.systems.eim.lab07.landmarklister.model.LandmarkInformation;

public class LandmarkListerAsyncTask extends AsyncTask<String, Void, List<LandmarkInformation>> {

    private ListView landmarkListView;

    public LandmarkListerAsyncTask(ListView landmarkListView) {
        this.landmarkListView = landmarkListView;
    }

    @Override
    protected List<LandmarkInformation> doInBackground(String... params) {
        // - create an instance of a HttpClient object
        // - create the URL to the web service, appending the bounding box coordinates and the username to the base Internet address
        // - create an instance of a HttGet object
        // - create an instance of a ReponseHandler object
        // - execute the request, thus obtaining the response
        // - get the JSON object representing the response
        // - get the JSON array (the value corresponding to the "geonames" attribute)
        // - iterate over the results list and create a LandmarkInformation for each element
        HttpClient httpClient = new DefaultHttpClient();

        // construire url, prin atasarea informatiilor preluate de la utilizator
        // (coordonatele punctelor cardinale) in cadrul unui obiect de tip HttpGet
        String url = Constants.LANDMARK_LISTER_WEB_SERVICE_INTERNET_ADDRESS
                + Constants.NORTH + params[Constants.NORTH_INDEX]
                + "&" + Constants.SOUTH + params[Constants.SOUTH_INDEX]
                + "&" + Constants.EAST + params[Constants.EAST_INDEX]
                + "&" + Constants.WEST + params[Constants.WEST_INDEX]
                + "&" + Constants.CREDENTIALS;

        Log.d(Constants.TAG, url);

        HttpGet httpGet = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try {
            // executie cerere HTTP => raspunsul "content"
            String content = httpClient.execute(httpGet, responseHandler);

            final ArrayList<LandmarkInformation> landmarkInformationArrayList = new ArrayList<>();

            // parsarea documentului JSON corespunzator raspunsului HTTP
            JSONObject result = new JSONObject(content);
            JSONArray jsonArray = result.getJSONArray(Constants.GEONAMES);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                landmarkInformationArrayList.add(new LandmarkInformation(
                    jsonObject.getDouble(Constants.LATITUDE),
                    jsonObject.getDouble(Constants.LONGITUDE),
                    jsonObject.getString(Constants.TOPONYM_NAME),
                    jsonObject.getLong(Constants.POPULATION),
                    jsonObject.getString(Constants.CODE_NAME),
                    jsonObject.getString(Constants.NAME),
                    jsonObject.getString(Constants.WIKIPEDIA_WEB_PAGE_ADDRESS),
                    jsonObject.getString(Constants.COUNTRY_CODE)
                ));
            }

            return landmarkInformationArrayList;
        } catch (JSONException | IOException jsonException) {
            Log.e(Constants.TAG, Objects.requireNonNull(jsonException.getMessage()));
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<LandmarkInformation> landmarkInformationList) {
        // create a LandmarkInformationAdapter with the array and attach it to the landmarksListView
        landmarkListView.setAdapter(new LandmarkInformationAdapter(landmarkListView.getContext(), landmarkInformationList));
    }
}
