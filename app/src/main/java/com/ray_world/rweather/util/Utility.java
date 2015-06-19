package com.ray_world.rweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.ray_world.rweather.model.City;
import com.ray_world.rweather.model.County;
import com.ray_world.rweather.model.Province;
import com.ray_world.rweather.model.RWeatherDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by ray on 15-5-3.
 */
public class Utility {

    public synchronized static boolean handleProvincesResponse(RWeatherDB rWeatherDB
            , String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    rWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCityResponse(RWeatherDB rWeatherDB
            , String responsse, int provinceId) {
        if (!TextUtils.isEmpty(responsse)) {
            String[] allCities = responsse.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    rWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(RWeatherDB rWeatherDB
            , String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    rWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {

        try {
            Log.i("test", "handleWeatherResponse");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("data");
            String cityName = weatherInfo.getString("district");
            String weatherCode = weatherInfo.getString("areaid");
            String temp = weatherInfo.getString("temp");
            String weatherDesp = weatherInfo.getString("weather");
            String windDirection = weatherInfo.getString("windDirection");
            String windForce = weatherInfo.getString("windForce");
            String humidity = weatherInfo.getString("humidity");
            String img1 = weatherInfo.getString("img_1");
            String refreshTime = weatherInfo.getString("refreshTime");
            saveWeatherInfo(context, cityName, weatherCode, temp,  weatherDesp
                    , windDirection, windForce, humidity, img1, refreshTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void handlePreWeatherResponse(Context context, String response) {
        try {
            Log.i("test", "handlePreWeatherResponse");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject preWeatherInfo = jsonObject.getJSONObject("data");
            String cityName = preWeatherInfo.getString("district");
            String weatherCode = preWeatherInfo.getString("areaid");
            String todayWeather = preWeatherInfo.getString("temp_1");
            String preWeather1 = preWeatherInfo.getString("temp_2");
            String preWeather2 = preWeatherInfo.getString("temp_3");
            String preWeather3 = preWeatherInfo.getString("temp_4");
            String todayImage = preWeatherInfo.getString("img_1");
            String preImage1 = preWeatherInfo.getString("img_3");
            String preImage2 = preWeatherInfo.getString("img_5");
            String preImage3 = preWeatherInfo.getString("img_7");
            String suggestion = preWeatherInfo.getString("index_d");
            savePreWeatherInfo(context, cityName, weatherCode, todayWeather, preWeather1
                    , preWeather2, preWeather3, todayImage, preImage1, preImage2
                    , preImage3, suggestion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void handleSuggestionResponse(Context context, String response) {
        try {
            Log.i("test", "handleSuggestionResponse");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject suggestionInfo = jsonObject.getJSONObject("data");
            String cyName = suggestionInfo.getString("ct_name");
            String cyDes = suggestionInfo.getString("ct_des");
            String gjName = suggestionInfo.getString("gj_name");
            String gjHint = suggestionInfo.getString("gj_hint");
            String gmName = suggestionInfo.getString("gm_name");
            String gmHint = suggestionInfo.getString("gm_hint");
            String lsName = suggestionInfo.getString("ls_name");
            String lsHint = suggestionInfo.getString("ls_hint");
            String xcName = suggestionInfo.getString("xc_name");
            String xcHint = suggestionInfo.getString("xc_hint");
            String ydName = suggestionInfo.getString("yd_name");
            String ydHint = suggestionInfo.getString("yd_hint");
            String ysName = suggestionInfo.getString("ys_name");
            String ysHint = suggestionInfo.getString("ys_hint");
            saveSuggestionInfo(context, cyName, cyDes, gjName, gjHint, gmName, gmHint, lsName
                    , lsHint, xcName, xcHint, ydName, ydHint, ysName, ysHint);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveSuggestionInfo(Context context, String cyName, String cyDes
            , String gjName, String gjHint, String gmName, String gmHint, String lsName
            , String lsHint, String xcName, String xcHint, String ydName, String ydHint
            , String ysName, String ysHint) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString("cyName", cyName);
        editor.putString("cyDes", cyDes);
        editor.putString("gjName", gjName);
        editor.putString("gjHint", gjHint);
        editor.putString("gmName", gmName);
        editor.putString("gmHint", gmHint);
        editor.putString("lsName", lsName);
        editor.putString("lsHint", lsHint);
        editor.putString("xcName", xcName);
        editor.putString("xcHint", xcHint);
        editor.putString("ydName", ydName);
        editor.putString("ydHint", ydHint);
        editor.putString("ysName", ysName);
        editor.putString("ysHint", ysHint);
        editor.commit();

    }

    private static void savePreWeatherInfo(Context context, String cityName, String weatherCode
            , String todayWeather, String preWeather1, String preWeather2, String preWeather3
            , String todayImage, String preImage1, String preImage2, String preImage3
            , String suggestion) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("cityName", cityName);
        editor.putString("weatherCode", weatherCode);
        editor.putString("todayWeather", todayWeather);
        editor.putString("preWeather1", preWeather1);
        editor.putString("preWeather2", preWeather2);
        editor.putString("preWeather3", preWeather3);
        editor.putString("todayImage", todayImage);
        editor.putString("preImage1", preImage1);
        editor.putString("preImage2", preImage2);
        editor.putString("preImage3", preImage3);
        editor.putString("suggestion", suggestion);
        editor.commit();
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode
            , String temp,  String weatherDesp, String windDirection
            , String windForce, String humidity, String img1, String refreshTime) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("cityName", cityName);
        editor.putString("weatherCode", weatherCode);
        editor.putString("temp", temp);
        editor.putString("weatherDesp", weatherDesp);
        editor.putString("windDirection", windDirection);
        editor.putString("windForce", windForce);
        editor.putString("humidity", humidity);
        editor.putString("img1", img1);
        editor.putString("refreshTime", refreshTime);
        editor.commit();
    }
}
