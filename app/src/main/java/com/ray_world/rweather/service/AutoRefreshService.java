package com.ray_world.rweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.ray_world.rweather.R;
import com.ray_world.rweather.util.Utility;
import com.ray_world.rweather.widget.WhiteLineOneWidget;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AutoRefreshService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("RayTest", "AutoRefreshService create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String districtName = prefs.getString("cityName", "");
        String cityName = prefs.getString("city", "");
        //基础天气信息
        Parameters parameters = new Parameters();
        parameters.add("cityname", districtName);
        parameters.add("format", 2);
        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/index", JuheData.GET
                , parameters, new DataCallBack() {
                    @Override
                    public void onSuccess(int i, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            Utility.handleWeatherResponse(AutoRefreshService.this, response);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        Log.e("error", throwable.getMessage());
                    }
                });

        //空气质量信息
        Parameters parameters2 = new Parameters();
        parameters2.add("city", cityName);
        Log.d("RayTest", "pm cityName = " + cityName);
        JuheData.executeWithAPI(this, 33, "http://web.juhe.cn:8080/environment/air/pm"
                , JuheData.GET, parameters2, new DataCallBack() {
                    @Override
                    public void onSuccess(int i, String response) {
                        Utility.handlePMResponse(AutoRefreshService.this, response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        Log.e("error", throwable.getMessage());
                    }
                });

        //每3小时预报信息
        Parameters parameters3 = new Parameters();
        parameters3.add("cityname", districtName);
        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/forecast3h"
                , JuheData.GET, parameters3, new DataCallBack() {
                    @Override
                    public void onSuccess(int i, String response) {
                        Log.i("text", response);
                        Utility.handleTimeWeatherResponse(AutoRefreshService.this, response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        Log.e("error", throwable.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
