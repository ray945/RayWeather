package com.ray_world.rweather.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.ray_world.rweather.R;
import com.ray_world.rweather.widget.WhiteLineOneWidget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateWidgetService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("RayTest", "UpdateWidgetService create");
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("RayTest", "onStartCommand");
        Intent i = new Intent("android.appwidget.action.REFRESH");
        sendBroadcast(i);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WhiteLineOneWidget.updateUI();
        }
    };

    @Override
    public void onDestroy() {
        Log.d("RayTest", "UpdateWidgetService Destroy");
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
