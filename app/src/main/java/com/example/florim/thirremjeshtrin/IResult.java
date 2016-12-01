package com.example.florim.thirremjeshtrin;

import com.android.volley.VolleyError;

/**
 * Created by vigan on 12/1/2016.
 */

public interface IResult {
    public void notifySuccess(String response);
    public void notifyError(VolleyError error);
}
