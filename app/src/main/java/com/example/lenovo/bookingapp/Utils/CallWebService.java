package com.example.lenovo.bookingapp.Utils;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lenovo.bookingapp.CustomDialog.CustomCardleDialog;
import com.example.lenovo.bookingapp.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Lenovo on 30-10-2015.
 */
public class CallWebService {

    private static Context context = null;

    private static CallWebService instance = null;

    private static CustomProgressDialog progressDialog = null;

    private static CustomCardleDialog customCardleDialog = null;


    public static CallWebService getInstance(Context context, boolean showProgressBar) {
        instance.context = context;
        if (context != null && showProgressBar) {
            customCardleDialog = new CustomCardleDialog(context);

        } else {

            customCardleDialog = null;
        }
        if (instance == null) {
            instance = new CallWebService();
        }

        return instance;
    }

    public void hitJSONObjectVolleyWebService(int requestType, String url, HashMap<String, String> json, final CallBackInterface callBackinerface) {

        if (customCardleDialog != null)
            customCardleDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(requestType, url, json == null ? null : (new JSONObject(json)), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                try {
                    if (response.getString(Constants.SUCCESS).equals("1")) {
                        if (customCardleDialog != null) {
                            customCardleDialog.Success();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    callBackinerface.onJsonObjectSuccess(response);
                                    customCardleDialog.dismiss();
                                }
                            }, 2000);
                        } else
                            callBackinerface.onJsonObjectSuccess(response);
                    } else {
                        if (customCardleDialog != null) {
                            customCardleDialog.Fail(response.optString(Constants.MESSAGE));
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    callBackinerface.onFailure(response.optString(Constants.MESSAGE));
                                    customCardleDialog.dismiss();
                                }
                            }, 2000);
                        } else
                            callBackinerface.onFailure(response.optString(Constants.MESSAGE));
                    }
                } catch (final JSONException e) {
                    if (customCardleDialog != null) {
                        customCardleDialog.Fail(e.getMessage());

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callBackinerface.onFailure(e.getMessage());
                                customCardleDialog.dismiss();
                            }
                        }, 2000);
                    } else
                        callBackinerface.onFailure(e.getMessage());

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                if (customCardleDialog != null) {
                    customCardleDialog.Fail(error.getMessage());

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callBackinerface.onFailure(error.getMessage());
                            customCardleDialog.dismiss();
                        }
                    }, 2000);
                } else
                    callBackinerface.onFailure(error.getMessage());


            }
        });

        MyApplication.getInstance(context).addToRequestQueue(request);
    }

}