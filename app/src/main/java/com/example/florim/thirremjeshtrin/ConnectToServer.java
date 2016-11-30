package com.example.florim.thirremjeshtrin;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.JsonReader;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.StringReader;
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
<<<<<<< HEAD
=======
    public static final String LOG_IN="http://200.6.254.247/thirremjeshtrin/login.php";
    public static final String REGISTER="http://200.6.254.247/thirremjeshtrin/register.php";
>>>>>>> 5b53b56d4d9b9221dfd186cac6d05322d63939d0
    /**
     * The url to initiate the HTTP connection to
     */
    private String url;
    /**
     * The parameters to be sent with the HTTP request by POST method
     */
    private Map<String,String> urlParameters;
    /**
     * The data returned from the HTTP responsed, parsed from JSON
     */
    private ProgressDialog pDialog;
    public List<Map<String, String>> results;

    /**
     * The constructor for the class
     *
     * @param url           the url that the object will communicate via HTTP with
     * @param urlParameters the parameters that will be sent with the HTTP request
     */
    public void sendRequest(Context context, String url, Map<String,String> urlParameters) {
        this.url = url;
        this.urlParameters=urlParameters;
        pDialog=new ProgressDialog(context);
        this.getResponse();
    }



        /**
         * Method that creates a URL Connection via HTTP, sends a request by POST method and saves the response as a result variable of the
         * ConnectToServer object
         */
        private void getResponse() {

            pDialog.setCancelable(false);
            pDialog.setMessage("Loading ...");
            showDialog();
            try {

                new StringRequest(Request.Method.POST,
                        this.url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonReader jReader = new JsonReader(new StringReader(response));
                            ConnectToServer.this.results = ConnectToServer.this.ParseJson(jReader);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params =urlParameters;

                        return params;
                    }

                };
            }
            catch(Exception e){
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
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}
