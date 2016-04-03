package com.ray_world.rweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ray_world.rweather.db.RWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15-5-3.
 */
public class RWeatherDB {

    public static final String DB_NAME = "r_weather";

    public static final int VERSION = 1;

    private static RWeatherDB rWeatherDB;

    private SQLiteDatabase db;

    //构造方法私有化
    private RWeatherDB(Context context) {
        RWeatherOpenHelper dbHelper = new RWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取RWeatherDB实例
    public static RWeatherDB getInstance(Context context) {
        if (rWeatherDB == null) {
            rWeatherDB = new RWeatherDB(context);
        }
        return rWeatherDB;
    }

    //将已选城市存储到数据库
    public void saveSelectedCity(SelectedCity selectedCity) {
        if (selectedCity != null) {
            ContentValues values = new ContentValues();
            values.put("selected_district_name", selectedCity.getDistrictName());
            Log.d("RayTest", "save cityName = " + selectedCity.getCityName());
            values.put("selected_city_name", selectedCity.getCityName());
            values.put("selected_city_temp", selectedCity.getTemp() + "°C");
            db.insert("SelectedCity", null, values);
        }
    }

    //更新已选城市的数据
    public void updateSelectedCity(SelectedCity selectedCity) {
        if (selectedCity != null) {
            ContentValues values = new ContentValues();
            values.put("selected_city_temp", selectedCity.getTemp() + "°C");
            values.put("selected_district_name", selectedCity.getDistrictName());
            db.update("SelectedCity", values, "selected_district_name = ?",
                    new String[]{selectedCity.getDistrictName()});
        }
    }

    //删除已选城市
    public boolean deleteSelectedCity(String selectedCityName) {
        if (selectedCityName != null) {
            db.delete("SelectedCity", "selected_district_name = ?", new String[]{selectedCityName});
        }
        return true;
    }

    //从数据库读取已选城市
    public List<SelectedCity> loadSelectedCity() {
        List<SelectedCity> list = new ArrayList<>();
        Cursor cursor = db.query("SelectedCity", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SelectedCity selectedCity = new SelectedCity();
                selectedCity.setCityName(cursor.getString(cursor.getColumnIndex("selected_city_name")));
                selectedCity.setDistrictName(cursor.getString(cursor.getColumnIndex("selected_district_name")));
                selectedCity.setTemp(cursor.getString(cursor.getColumnIndex("selected_city_temp")));
                list.add(selectedCity);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //查询已选城市是否存在
    public boolean checkSelectedCity(String cityName) {
        Cursor cursor = db.query("SelectedCity", null, "selected_district_name = ?",
                new String[]{cityName}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
}
