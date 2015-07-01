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

    public static void handlePMResponse(Context context, String response) {
        Log.i("test", "handlePMResponse");
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray("result");
            JSONObject result = results.getJSONObject(0);
            String aqi = result.getString("AQI");
            String pm = result.getString("PM2.5");
            String quality = result.getString("quality");
            savePMResponse(context, aqi, pm, quality);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void savePMResponse(Context context, String aqi, String pm, String quality) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString("aqi", aqi);
        editor.putString("pm", pm);
        editor.putString("quality", quality);
        editor.commit();
    }

    public static void handleTimeWeatherResponse(Context context, String response) {

        Log.i("test", "handleTimeWeatherResponse");
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray("result");

            JSONObject result1 = results.getJSONObject(0);
            String weatherId1 = result1.getString("weatherid");
            String weather1 = result1.getString("weather");
            String tempMin1 = result1.getString("temp1");
            String tempMax1 = result1.getString("temp2");
            String sh1 = result1.getString("sh");
            String eh1 = result1.getString("eh");

            JSONObject result2 = results.getJSONObject(1);
            String weatherId2 = result2.getString("weatherid");
            String weather2 = result2.getString("weather");
            String tempMin2 = result2.getString("temp1");
            String tempMax2 = result2.getString("temp2");
            String sh2 = result2.getString("sh");
            String eh2 = result2.getString("eh");

            JSONObject result3 = results.getJSONObject(2);
            String weatherId3 = result3.getString("weatherid");
            String weather3 = result3.getString("weather");
            String tempMin3 = result3.getString("temp1");
            String tempMax3 = result3.getString("temp2");
            String sh3 = result3.getString("sh");
            String eh3 = result3.getString("eh");

            JSONObject result4 = results.getJSONObject(3);
            String weatherId4 = result4.getString("weatherid");
            String weather4 = result4.getString("weather");
            String tempMin4 = result4.getString("temp1");
            String tempMax4 = result4.getString("temp2");
            String sh4 = result4.getString("sh");
            String eh4 = result4.getString("eh");

            JSONObject result5 = results.getJSONObject(4);
            String weatherId5 = result5.getString("weatherid");
            String weather5 = result5.getString("weather");
            String tempMin5 = result5.getString("temp1");
            String tempMax5 = result5.getString("temp2");
            String sh5 = result5.getString("sh");
            String eh5 = result5.getString("eh");

            JSONObject result6 = results.getJSONObject(5);
            String weatherId6 = result6.getString("weatherid");
            String weather6 = result6.getString("weather");
            String tempMin6 = result6.getString("temp1");
            String tempMax6 = result6.getString("temp2");
            String sh6 = result6.getString("sh");
            String eh6 = result6.getString("eh");

            JSONObject result7 = results.getJSONObject(6);
            String weatherId7 = result7.getString("weatherid");
            String weather7 = result7.getString("weather");
            String tempMin7 = result7.getString("temp1");
            String tempMax7 = result7.getString("temp2");
            String sh7 = result7.getString("sh");
            String eh7 = result7.getString("eh");

            JSONObject result8 = results.getJSONObject(7);
            String weatherId8 = result8.getString("weatherid");
            String weather8 = result8.getString("weather");
            String tempMin8 = result8.getString("temp1");
            String tempMax8 = result8.getString("temp2");
            String sh8 = result8.getString("sh");
            String eh8 = result8.getString("eh");

            saveTimeWeatherResponse(context, weatherId1, weather1,tempMax1, tempMin1, sh1, eh1
                    , weatherId2, weather2,tempMax2, tempMin2, sh2, eh2
                    , weatherId3, weather3,tempMax3, tempMin3, sh3, eh3
                    , weatherId4, weather4,tempMax4, tempMin4, sh4, eh4
                    , weatherId5, weather5,tempMax5, tempMin5, sh5, eh5
                    , weatherId6, weather6,tempMax6, tempMin6, sh6, eh6
                    , weatherId7, weather7,tempMax7, tempMin7, sh7, eh7
                    , weatherId8, weather8,tempMax8, tempMin8, sh8, eh8);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveTimeWeatherResponse(Context context
            , String weatherId1, String weather1, String tempMax1, String tempMin1, String sh1
            , String eh1
            , String weatherId2, String weather2, String tempMax2, String tempMin2, String sh2
            , String eh2
            , String weatherId3, String weather3, String tempMax3, String tempMin3, String sh3
            , String eh3
            , String weatherId4, String weather4, String tempMax4, String tempMin4, String sh4
            , String eh4
            , String weatherId5, String weather5, String tempMax5, String tempMin5, String sh5
            , String eh5
            , String weatherId6, String weather6, String tempMax6, String tempMin6, String sh6
            , String eh6
            , String weatherId7, String weather7, String tempMax7, String tempMin7, String sh7
            , String eh7
            , String weatherId8, String weather8, String tempMax8, String tempMin8, String sh8
            , String eh8) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putString("weatherId1", weatherId1);
        editor.putString("weather1", weather1);
        editor.putString("tempMax1", tempMax1);
        editor.putString("tempMin1", tempMin1);
        editor.putString("sh1", sh1);
        editor.putString("eh1", eh1);

        editor.putString("weatherId2", weatherId2);
        editor.putString("weather2", weather2);
        editor.putString("tempMax2", tempMax2);
        editor.putString("tempMin2", tempMin2);
        editor.putString("sh2", sh2);
        editor.putString("eh2", eh2);

        editor.putString("weatherId3", weatherId3);
        editor.putString("weather3", weather3);
        editor.putString("tempMax3", tempMax3);
        editor.putString("tempMin3", tempMin3);
        editor.putString("sh3", sh3);
        editor.putString("eh3", eh3);

        editor.putString("weatherId4", weatherId4);
        editor.putString("weather4", weather4);
        editor.putString("tempMax4", tempMax4);
        editor.putString("tempMin4", tempMin4);
        editor.putString("sh4", sh4);
        editor.putString("eh4", eh4);

        editor.putString("weatherId5", weatherId5);
        editor.putString("weather5", weather5);
        editor.putString("tempMax5", tempMax5);
        editor.putString("tempMin5", tempMin5);
        editor.putString("sh5", sh5);
        editor.putString("eh5", eh5);

        editor.putString("weatherId6", weatherId6);
        editor.putString("weather6", weather6);
        editor.putString("tempMax6", tempMax6);
        editor.putString("tempMin6", tempMin6);
        editor.putString("sh6", sh6);
        editor.putString("eh6", eh6);

        editor.putString("weatherId7", weatherId7);
        editor.putString("weather7", weather7);
        editor.putString("tempMax7", tempMax7);
        editor.putString("tempMin7", tempMin7);
        editor.putString("sh7", sh7);
        editor.putString("eh7", eh7);

        editor.putString("weatherId8", weatherId8);
        editor.putString("weather8", weather8);
        editor.putString("tempMax8", tempMax8);
        editor.putString("tempMin8", tempMin8);
        editor.putString("sh8", sh8);
        editor.putString("eh8", eh8);

        editor.commit();
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
