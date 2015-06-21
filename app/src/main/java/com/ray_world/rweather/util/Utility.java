package com.ray_world.rweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.ray_world.rweather.R;
import com.ray_world.rweather.model.City;
import com.ray_world.rweather.model.County;
import com.ray_world.rweather.model.Province;
import com.ray_world.rweather.model.RWeatherDB;

import org.json.JSONArray;
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
            JSONObject result = jsonObject.getJSONObject("result");

            JSONObject sk = result.getJSONObject("sk");
            String temp = sk.getString("temp");
            String windDirection = sk.getString("wind_direction");
            String windStrength = sk.getString("wind_strength");
            String humidity = sk.getString("humidity");
            String time = sk.getString("time");

            JSONObject today = result.getJSONObject("today");
            String cityName = today.getString("city");
            String temperature = today.getString("temperature");
            String weatherDesp = today.getString("weather");
            JSONObject weatherId = today.getJSONObject("weather_id");
            String img1 = weatherId.getString("fa");
            String dressing = today.getString("dressing_advice");
            String uv = today.getString("uv_index");
            String wash = today.getString("wash_index");
            String exercise = today.getString("exercise_index");

            JSONArray future = result.getJSONArray("future");
            JSONObject pre1 = future.getJSONObject(1);
            String preWeek1 = pre1.getString("week");
            JSONObject preWeather1 = pre1.getJSONObject("weather_id");
            String preImage1 = preWeather1.getString("fa");
            String preTemp1 = pre1.getString("temperature");
            JSONObject pre2 = future.getJSONObject(2);
            String preWeek2 = pre2.getString("week");
            JSONObject preWeather2 = pre2.getJSONObject("weather_id");
            String preImage2 = preWeather2.getString("fa");
            String preTemp2 = pre2.getString("temperature");
            JSONObject pre3 = future.getJSONObject(3);
            String preWeek3 = pre3.getString("week");
            JSONObject preWeather3 = pre3.getJSONObject("weather_id");
            String preImage3 = preWeather3.getString("fa");
            String preTemp3 = pre3.getString("temperature");
            JSONObject pre4 = future.getJSONObject(4);
            String preWeek4 = pre4.getString("week");
            JSONObject preWeather4 = pre4.getJSONObject("weather_id");
            String preImage4 = preWeather4.getString("fa");
            String preTemp4 = pre4.getString("temperature");

            saveWeatherInfo(context, temp, windDirection, windStrength, humidity, time
                    , cityName, temperature, weatherDesp, img1, dressing, uv, wash, exercise
                    , preWeek1, preImage1, preTemp1, preWeek2, preImage2, preTemp2
                    , preWeek3, preImage3, preTemp3, preWeek4, preImage4, preTemp4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static void saveWeatherInfo(Context context, String temp, String windDirection
            , String windStrength, String humidity, String time, String cityName
            , String temperature, String weatherDesp, String img1, String dressing, String uv
            , String wash, String exercise, String preWeek1, String preImage1
            , String preTemp1, String preWeek2, String preImage2, String preTemp2, String preWeek3
            , String preImage3, String preTemp3, String preWeek4, String preImage4, String preTemp4) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("temp", temp);
        editor.putString("windDirection", windDirection);
        editor.putString("windStrength", windStrength);
        editor.putString("humidity", humidity);
        editor.putString("refreshTime", time);
        editor.putString("cityName", cityName);
        editor.putString("temperature", temperature);
        editor.putString("weatherDesp", weatherDesp);
        editor.putString("img1", img1);
        editor.putString("dressing", dressing);
        editor.putString("uv", uv);
        editor.putString("wash", wash);
        editor.putString("exercise", exercise);
        editor.putString("preWeek1", preWeek1);
        editor.putString("preImage1", preImage1);
        editor.putString("preTemp1", preTemp1);
        editor.putString("preWeek2", preWeek2);
        editor.putString("preImage2", preImage2);
        editor.putString("preTemp2", preTemp2);
        editor.putString("preWeek3", preWeek3);
        editor.putString("preImage3", preImage3);
        editor.putString("preTemp3", preTemp3);
        editor.putString("preWeek4", preWeek4);
        editor.putString("preImage4", preImage4);
        editor.putString("preTemp4", preTemp4);
        editor.commit();
    }
}
