package com.ray_world.rweather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.ray_world.rweather.R;
import com.ray_world.rweather.activity.WeatherActivity;
import com.ray_world.rweather.service.AutoRefreshService;
import com.ray_world.rweather.service.UpdateWidgetService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class WhiteLineOneWidget extends AppWidgetProvider {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    private static Context mContext;
    private static AppWidgetManager mAppWidgetManager;
    private static RemoteViews mRemoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/

        Log.d("RayTest", "onUpdate");
        mContext = context;
        mAppWidgetManager = appWidgetManager;
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.white_line_one_widget);
        Intent intent = new Intent(context, UpdateWidgetService.class);
        context.startService(intent);
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("isAddWidget", true);
        editor.commit();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.appwidget.action.REFRESH")) {
            Log.d("RayTest", "onReceive CREATE_REMOTE_VIEW");
            if (mRemoteViews == null) {
                mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.white_line_one_widget);
                mContext = context;
                mAppWidgetManager = AppWidgetManager.getInstance(context);
            }
            updateUI();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Intent intent = new Intent(context, UpdateWidgetService.class);
        context.stopService(intent);
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("isAddWidget", false);
        editor.commit();
    }

    public static void updateUI() {
        Log.d("RayTest", "updateUI");
        String timeString = dateFormat.format(new Date());
        mRemoteViews.setTextViewText(R.id.clock_desk, timeString);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        setImage(R.id.weather_desk);
        String temp = prefs.getString("temp", "--")+ "℃";
        String tempText = prefs.getString("temperature", "");
        String[] array = tempText.split("~");
        String tempDay = array[0] + " ~ " +array[1];
        String pmText = prefs.getString("pm", "--");
        String pm = "pm2.5\n" + pmText;
        mRemoteViews.setTextViewText(R.id.temp_desk, temp);
        mRemoteViews.setTextViewText(R.id.temp_day_desk, tempDay);
        mRemoteViews.setTextViewText(R.id.pm_desk, pm);
        Intent intent = new Intent(mContext, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
        ComponentName componentName = new ComponentName(mContext,
                WhiteLineOneWidget.class);
        mAppWidgetManager.updateAppWidget(componentName, mRemoteViews);
    }

    public static void setImage(int imageId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String imgNo = prefs.getString("img1", "0");
        if (imageMapping == null)
            setImageMap();
        if (imageMapping.containsKey(imgNo))
            mRemoteViews.setImageViewResource(imageId, imageMapping.get(imgNo));
    }

    private static Map<String ,Integer> imageMapping;
    private static void setImageMap(){
        imageMapping =new HashMap<String,Integer>();
        imageMapping.put("00", R.drawable.a00);
        imageMapping.put("01", R.drawable.a01);
        imageMapping.put("02", R.drawable.a02);
        imageMapping.put("03", R.drawable.a03);
        imageMapping.put("04", R.drawable.a04);
        imageMapping.put("05", R.drawable.a05);
        imageMapping.put("06", R.drawable.a06);
        imageMapping.put("07", R.drawable.a07);
        imageMapping.put("08", R.drawable.a08);
        imageMapping.put("09", R.drawable.a09);
        imageMapping.put("10", R.drawable.a10);
        imageMapping.put("11", R.drawable.a11);
        imageMapping.put("12", R.drawable.a12);
        imageMapping.put("13", R.drawable.a13);
        imageMapping.put("14", R.drawable.a14);
        imageMapping.put("15", R.drawable.a15);
        imageMapping.put("16", R.drawable.a16);
        imageMapping.put("17", R.drawable.a17);
        imageMapping.put("18", R.drawable.a18);
        imageMapping.put("19", R.drawable.a19);
        imageMapping.put("20", R.drawable.a20);
        imageMapping.put("21", R.drawable.a21);
        imageMapping.put("22", R.drawable.a22);
        imageMapping.put("23", R.drawable.a23);
        imageMapping.put("24", R.drawable.a24);
        imageMapping.put("25", R.drawable.a25);
        imageMapping.put("26", R.drawable.a26);
        imageMapping.put("27", R.drawable.a27);
        imageMapping.put("28", R.drawable.a28);
        imageMapping.put("29", R.drawable.a29);
        imageMapping.put("30", R.drawable.a30);
        imageMapping.put("31", R.drawable.a31);
    }

    public static boolean isRemoteViewNull() {
        if (mRemoteViews == null) {
            return true;
        } else {
            return false;
        }
    }
}

