package com.ray_world.rweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    //将Province实例存储到数据库
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    //从数据库读取Province
    public List<Province> loadProvince() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //将City实例存储到数据库
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    //从数据库中读取City
    public List<City> loadCity(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //将County实例存储到数据库
    public void saveCounty(County city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", city.getCountyName());
            values.put("county_code", city.getCountyCode());
            values.put("city_id", city.getCityId());
            db.insert("County", null, values);
        }
    }

    //从数据库读取County
    public List<County> loadCounty(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //将已选城市存储到数据库
    public void saveSelectedCity(SelectedCity selectedCity) {
        if (selectedCity != null) {
            ContentValues values = new ContentValues();
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
            db.update("SelectedCity", values, "selected_city_name = ?",
                    new String[]{selectedCity.getCityName()});
        }
    }

    //删除已选城市
    public boolean deleteSelectedCity(String selectedCityName) {
        if (selectedCityName != null) {
            db.delete("SelectedCity", "selected_city_name = ?", new String[]{selectedCityName});
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
                selectedCity.setTemp(cursor.getString(cursor.getColumnIndex("selected_city_temp")));
                list.add(selectedCity);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //查询已选城市是否存在
    public boolean checkSelectedCity(String cityName) {
        Cursor cursor = db.query("SelectedCity", null, "selected_city_name = ?",
                new String[]{cityName}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
}
