package com.example.florim.thirremjeshtrin;

import android.os.AsyncTask;
import android.util.JsonReader;




import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gresa on 28-Nov-16.
 */

/**
 * Class that provides the interface needed to create a connection to the network via HTTP, send requests
 * and get the data returned as a response
 */
public class ConnectToServer {
    /**
     * The url to initiate the HTTP connection to
     */
    public String url;
    /**
     * The parameters to be sent with the HTTP request by POST method
     */
    private String urlParameters;
    /**
     * The data returned from the HTTP responsed, parsed from JSON
     */
    public List<Map<String, String>> results;

    /**
     * The constructor for the class
     *
     * @param url           the url that the object will communicate via HTTP with
     * @param urlParameters the parameters that will be sent with the HTTP request
     */
    public ConnectToServer(String url, String urlParameters) {
        this.url = url;
        this.urlParameters = urlParameters;
        new NetworkTask().execute(this);

    }

    /**
     * A subclass of Asynctask that makes network communication possible in the background
     */
    private class NetworkTask extends AsyncTask<ConnectToServer, Void, Void> {

        @Override
        protected Void doInBackground(ConnectToServer... requestContents) {


            for (ConnectToServer request : requestContents) {

                getResponse(request);
            }
            return null;

        }

        /**
         * Method that creates a URL Connection via HTTP, sends a request by POST method and saves the response as a result variable of the
         * ConnectToServer object
         *
         * @param request The ConnectToServer object that is used to get the URL, the URL Parameters and then save the response data
         */
        private void getResponse(ConnectToServer request) {
            JsonReader jReader;
            try {
                URL url = new URL(request.url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");

                byte[] postData = request.urlParameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;

                urlConnection.connect();

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("charset", "utf-8");
                urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                urlConnection.setUseCaches(false);
                urlConnection.getOutputStream().write(postData);


                InputStream is = urlConnection.getInputStream();
                jReader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                urlConnection.disconnect();

                request.results = ParseJson(jReader);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        /**
         * Method that parses a JSON Array into a Map List
         *
         * @param jsonReader The object that contains unparsed JSON data
         * @return The JSON data, parsed
         */
        private List<Map<String, String>> ParseJson(JsonReader jsonReader) {
            try {
                List<Map<String, String>> results = new ArrayList<>();
                Map<String, String> row;
                if (jsonReader != null) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {

                        jsonReader.beginObject();
                        row = new HashMap<String, String>();
                        while (jsonReader.hasNext()) {
                            row.put(jsonReader.nextName(), jsonReader.nextString());
                        }
                        results.add(row);
                        jsonReader.endObject();

                    }
                    jsonReader.endArray();
                }
                return results;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

    }
}
