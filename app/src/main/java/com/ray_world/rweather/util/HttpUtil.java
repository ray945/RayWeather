package com.ray_world.rweather.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ray_world.rweather.activity.ChooseAreaActivity;
import com.ray_world.rweather.activity.WeatherActivity;
import com.ray_world.rweather.model.City;
import com.ray_world.rweather.model.Province;
import com.ray_world.rweather.model.RWeatherDB;

import org.apache.http.HttpConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.util.AbstractCollection;


/**
 * Created by ray on 15-5-3.
 */
public class HttpUtil {

    public static ChooseAreaActivity activity;

    public static void sendHttpRequest(final Context context, final String address
            , final String type, final Province selectedProvince
            , final City selectedCity) {

        final RWeatherDB rWeatherDB = RWeatherDB.getInstance(context);
        activity = (ChooseAreaActivity) context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue mQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(address,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //响应成功
                                boolean result = false;
                                if ("province".equals(type)) {
                                    result = Utility.handleProvincesResponse(rWeatherDB, s);
                                } else if ("city".equals(type)) {
                                    result = Utility.handleCityResponse(rWeatherDB,
                                            s, selectedProvince.getId());
                                } else if ("county".equals(type)) {
                                    result = Utility.handleCountiesResponse(rWeatherDB,
                                            s, selectedCity.getId());
                                }
                                if (result) {
                                    activity.closeProgressDialog();
                                    Log.i("1", "更新界面");
                                    if ("province".equals(type)) {
                                        activity.queryProvinces();
                                    } else if ("city".equals(type)) {
                                        activity.queryCities();
                                    } else if ("county".equals(type)) {
                                        activity.queryCounties();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //响应失败
                        activity.closeProgressDialog();
                        Toast.makeText(context, "加载失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(stringRequest);
            }
        }).start();
    }

    public static  void sendHttpRequest(final String address
            , final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        }).start();
    }
}
