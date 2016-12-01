package com.example.florim.thirremjeshtrin;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by vigan on 12/1/2016.
 */

public class VolleyService {
    IResult mResultCallback = null;
    Context mContext;
    //private ProgressDialog pDialog;

    VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
        //pDialog = new ProgressDialog(context);
        //pDialog.setCancelable(false);
    }


    public void postDataVolley(String url, final Map<String, String> parameters){
        try {
            //pDialog.setMessage("Loading ...");
            //showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(response);
                    //hideDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(error);
                    //hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = parameters;

                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(strReq);

        }catch(Exception e){

        }
    }

    public void getDataVolley(String url, final Map<String, String> parameters){
        try {
            //pDialog.setMessage("Loading ...");
            //showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(error);
                    //hideDialog();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = parameters;

                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(strReq);

        }catch(Exception e){

        }
    }

}
