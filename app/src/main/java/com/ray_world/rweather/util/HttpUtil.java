package com.ray_world.rweather.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ray_world.rweather.activity.ChooseAreaActivity;
import com.ray_world.rweather.model.City;
import com.ray_world.rweather.model.Province;
import com.ray_world.rweather.model.RWeatherDB;


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
}
