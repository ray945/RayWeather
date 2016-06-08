package com.ray_world.rweather.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ray_world.rweather.R;
import com.ray_world.rweather.model.RWeatherDB;
import com.ray_world.rweather.model.SelectedCity;
import com.ray_world.rweather.service.AutoRefreshService;
import com.ray_world.rweather.util.MyApplication;
import com.ray_world.rweather.util.Utility;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rui on 2015/5/30 0030.
 */
public class WeatherActivity extends AppCompatActivity {

    public TextView publishText;
    private TextView weatherDespText;
    private TextView cityName;
    private TextView tempText;
    private TextView weatherText;
    private TextView preDayText2;
    private TextView preDayText3;
    private TextView preDayText4;
    private ImageView preImage1;
    private ImageView preImage2;
    private ImageView preImage3;
    private ImageView preImage4;
    private TextView preWeatherMinText1;
    private TextView preWeatherMaxText1;
    private TextView preWeatherMinText2;
    private TextView preWeatherMaxText2;
    private TextView preWeatherMinText3;
    private TextView preWeatherMaxText3;
    private TextView preWeatherMinText4;
    private TextView preWeatherMaxText4;
    private TextView windText;
    private TextView humidityText;
    private TextView dressingText;
    private TextView uvText;
    private TextView washText;
    private TextView exerciseText;
    private TextView quelityText;
    private TextView aqiText;
    private TextView pmText;
    private ImageView image;
    private TextView timeAreaText1;
    private TextView timeAreaText2;
    private TextView timeAreaText3;
    private TextView timeAreaText4;
    private TextView timeWeatherText1;
    private TextView timeWeatherText2;
    private TextView timeWeatherText3;
    private TextView timeWeatherText4;
    private TextView timeTempText1;
    private TextView timeTempText2;
    private TextView timeTempText3;
    private TextView timeTempText4;
    private ImageView timeImage1;
    private ImageView timeImage2;
    private ImageView timeImage3;
    private ImageView timeImage4;
    private NavigationView mNavigationView;
    private FrameLayout header;

    private Toolbar toolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;

    private RWeatherDB rWeatherDB;
    boolean isWeatherHandled = false;
    boolean isPMHandled = false;
    boolean isTimeWeatherHandled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentColor = preferences.getString("current_color", "青春绿");
        if (currentColor.equals("青春绿")) {
            setTheme(R.style.ColorGreen);
        } else if (currentColor.equals("天空蓝")) {
            setTheme(R.style.ColorBlue);
        } else if (currentColor.equals("活力黄")) {
            setTheme(R.style.ColorYellow);
        } else if (currentColor.equals("碧波翠")) {
            setTheme(R.style.ColorGreenDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        MyApplication.getInstance().addActivity(this);
        initToolbar();

        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        tempText = (TextView) findViewById(R.id.temp_text);
        weatherText = (TextView) findViewById(R.id.weather_text);
        cityName = (TextView) findViewById(R.id.city_name_text);
        image = (ImageView) findViewById(R.id.img);
        preDayText2 = (TextView) findViewById(R.id.pre_day2);
        preDayText3 = (TextView) findViewById(R.id.pre_day3);
        preDayText4 = (TextView) findViewById(R.id.pre_day4);
        preImage1 = (ImageView) findViewById(R.id.pre_image1);
        preImage2 = (ImageView) findViewById(R.id.pre_image2);
        preImage3 = (ImageView) findViewById(R.id.pre_image3);
        preImage4 = (ImageView) findViewById(R.id.pre_image4);
        preWeatherMinText1 = (TextView) findViewById(R.id.pre_weather1_min);
        preWeatherMaxText1 = (TextView) findViewById(R.id.pre_weather1_max);
        preWeatherMinText2 = (TextView) findViewById(R.id.pre_weather2_min);
        preWeatherMaxText2 = (TextView) findViewById(R.id.pre_weather2_max);
        preWeatherMinText3 = (TextView) findViewById(R.id.pre_weather3_min);
        preWeatherMaxText3 = (TextView) findViewById(R.id.pre_weather3_max);
        preWeatherMinText4 = (TextView) findViewById(R.id.pre_weather4_min);
        preWeatherMaxText4 = (TextView) findViewById(R.id.pre_weather4_max);
        windText = (TextView) findViewById(R.id.wind_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        dressingText = (TextView) findViewById(R.id.cy_des_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        washText = (TextView) findViewById(R.id.wash_text);
        exerciseText = (TextView) findViewById(R.id.exercise_text);
        quelityText = (TextView) findViewById(R.id.quality_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pmText = (TextView) findViewById(R.id.pm_text);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        header = (FrameLayout) findViewById(R.id.header);
        timeAreaText1 = (TextView) findViewById(R.id.time_area);
        timeAreaText2 = (TextView) findViewById(R.id.time_area2);
        timeAreaText3 = (TextView) findViewById(R.id.time_area3);
        timeAreaText4 = (TextView) findViewById(R.id.time_area4);
        timeWeatherText1 = (TextView) findViewById(R.id.time_weather);
        timeWeatherText2 = (TextView) findViewById(R.id.time_weather2);
        timeWeatherText3 = (TextView) findViewById(R.id.time_weather3);
        timeWeatherText4 = (TextView) findViewById(R.id.time_weather4);
        timeImage1  = (ImageView) findViewById(R.id.time_image);
        timeImage2  = (ImageView) findViewById(R.id.time_image2);
        timeImage3  = (ImageView) findViewById(R.id.time_image3);
        timeImage4  = (ImageView) findViewById(R.id.time_image4);
        timeTempText1 = (TextView) findViewById(R.id.time_temp);
        timeTempText2 = (TextView) findViewById(R.id.time_temp2);
        timeTempText3 = (TextView) findViewById(R.id.time_temp3);
        timeTempText4 = (TextView) findViewById(R.id.time_temp4);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        rWeatherDB = RWeatherDB.getInstance(this);

        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour; // 0-23
        if (hour >= 6 && hour < 18) {
            header.setBackgroundResource(R.drawable.day);
        } else {
            header.setBackgroundResource(R.drawable.night);
        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                if (menuItem.getItemId() == R.id.item_add_city) {
                    Intent intent = new Intent(WeatherActivity.this, ChooseCityActivity.class);
                    intent.putExtra("from_weather_activity", true);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.item_manage_city) {
                    Intent intent = new Intent(WeatherActivity.this, ManageCityActivity.class);
                    intent.putExtra("currentCity", cityName.getText().toString());
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.item_settings) {
                    Intent intent = new Intent(WeatherActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        String districtName = getIntent().getStringExtra("district_name");
        String cityName = getIntent().getStringExtra("city_name");
        if (!TextUtils.isEmpty(districtName) || !TextUtils.isEmpty(cityName)) {
            toolbar.setTitle("同步中……");
            queryWeatherInfo(districtName, cityName);
        } else {
            showWeather();
            reFreshWeather();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFreshWeather();
            }
        });

        View hourWeather = findViewById(R.id.hour_weather);
        hourWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout diag = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.hour_weather_layout, null);
                AlertDialog builder = new AlertDialog.Builder(WeatherActivity.this)
                        .setView(diag)
                        .create();
                TextView timeAreaText1 = (TextView) diag.findViewById(R.id.time_area);
                TextView timeAreaText2 = (TextView) diag.findViewById(R.id.time_area2);
                TextView timeAreaText3 = (TextView) diag.findViewById(R.id.time_area3);
                TextView timeAreaText4 = (TextView) diag.findViewById(R.id.time_area4);
                TextView timeAreaText5 = (TextView) diag.findViewById(R.id.time_area5);
                TextView timeAreaText6 = (TextView) diag.findViewById(R.id.time_area6);
                TextView timeAreaText7 = (TextView) diag.findViewById(R.id.time_area7);
                TextView timeAreaText8 = (TextView) diag.findViewById(R.id.time_area8);
                TextView timeWeatherText1 = (TextView) diag.findViewById(R.id.time_weather);
                TextView timeWeatherText2 = (TextView) diag.findViewById(R.id.time_weather2);
                TextView timeWeatherText3 = (TextView) diag.findViewById(R.id.time_weather3);
                TextView timeWeatherText4 = (TextView) diag.findViewById(R.id.time_weather4);
                TextView timeWeatherText5 = (TextView) diag.findViewById(R.id.time_weather5);
                TextView timeWeatherText6 = (TextView) diag.findViewById(R.id.time_weather6);
                TextView timeWeatherText7 = (TextView) diag.findViewById(R.id.time_weather7);
                TextView timeWeatherText8 = (TextView) diag.findViewById(R.id.time_weather8);
                TextView timeTempText1 = (TextView) diag.findViewById(R.id.time_temp);
                TextView timeTempText2 = (TextView) diag.findViewById(R.id.time_temp2);
                TextView timeTempText3 = (TextView) diag.findViewById(R.id.time_temp3);
                TextView timeTempText4 = (TextView) diag.findViewById(R.id.time_temp4);
                TextView timeTempText5 = (TextView) diag.findViewById(R.id.time_temp5);
                TextView timeTempText6 = (TextView) diag.findViewById(R.id.time_temp6);
                TextView timeTempText7 = (TextView) diag.findViewById(R.id.time_temp7);
                TextView timeTempText8 = (TextView) diag.findViewById(R.id.time_temp8);
                ImageView timeImage1 = (ImageView) diag.findViewById(R.id.time_image);
                ImageView timeImage2 = (ImageView) diag.findViewById(R.id.time_image2);
                ImageView timeImage3 = (ImageView) diag.findViewById(R.id.time_image3);
                ImageView timeImage4 = (ImageView) diag.findViewById(R.id.time_image4);
                ImageView timeImage5 = (ImageView) diag.findViewById(R.id.time_image5);
                ImageView timeImage6 = (ImageView) diag.findViewById(R.id.time_image6);
                ImageView timeImage7 = (ImageView) diag.findViewById(R.id.time_image7);
                ImageView timeImage8 = (ImageView) diag.findViewById(R.id.time_image8);
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this);
                timeAreaText1.setText(prefs.getString("sh1", "") + "时-" + prefs.getString("eh1", "")
                        + "时");
                timeAreaText2.setText(prefs.getString("sh2", "") + "时-" + prefs.getString("eh2", "")
                        + "时");
                timeAreaText3.setText(prefs.getString("sh3", "") + "时-" + prefs.getString("eh3", "")
                        + "时");
                timeAreaText4.setText(prefs.getString("sh4", "") + "时-" + prefs.getString("eh4", "")
                        + "时");
                timeAreaText5.setText(prefs.getString("sh5", "") + "时-" + prefs.getString("eh5", "")
                        + "时");
                timeAreaText6.setText(prefs.getString("sh6", "") + "时-" + prefs.getString("eh6", "")
                        + "时");
                timeAreaText7.setText(prefs.getString("sh7", "") + "时-" + prefs.getString("eh7", "")
                        + "时");
                timeAreaText8.setText(prefs.getString("sh8", "") + "时-" + prefs.getString("eh8", "")
                        + "时");
                timeWeatherText1.setText(prefs.getString("weather1", ""));
                timeWeatherText2.setText(prefs.getString("weather2", ""));
                timeWeatherText3.setText(prefs.getString("weather3", ""));
                timeWeatherText4.setText(prefs.getString("weather4", ""));
                timeWeatherText5.setText(prefs.getString("weather5", ""));
                timeWeatherText6.setText(prefs.getString("weather6", ""));
                timeWeatherText7.setText(prefs.getString("weather7", ""));
                timeWeatherText8.setText(prefs.getString("weather8", ""));
                setImage(timeImage1, "weatherId1");
                setImage(timeImage2, "weatherId2");
                setImage(timeImage3, "weatherId3");
                setImage(timeImage4, "weatherId4");
                setImage(timeImage5, "weatherId5");
                setImage(timeImage6, "weatherId6");
                setImage(timeImage7, "weatherId7");
                setImage(timeImage8, "weatherId8");

                setText(timeTempText1, prefs.getString("tempMin1", ""), prefs
                        .getString("tempMax1", ""));
                setText(timeTempText2, prefs.getString("tempMin2", ""), prefs
                        .getString("tempMax2", ""));
                setText(timeTempText3, prefs.getString("tempMin3", ""), prefs
                        .getString("tempMax3", ""));
                setText(timeTempText4, prefs.getString("tempMin4", ""), prefs
                        .getString("tempMax4", ""));
                setText(timeTempText5, prefs.getString("tempMin5", ""), prefs
                        .getString("tempMax5", ""));
                setText(timeTempText6, prefs.getString("tempMin6", ""), prefs
                        .getString("tempMax6", ""));
                setText(timeTempText7, prefs.getString("tempMin7", ""), prefs
                        .getString("tempMax7", ""));
                setText(timeTempText8, prefs.getString("tempMin8", ""), prefs
                        .getString("tempMax8", ""));
                builder.show();
            }
        });
    }

    private void setText(TextView tv, String temp1, String temp2) {
        int change = 0;
        int min = Integer.valueOf(temp1);
        int max = Integer.valueOf(temp2);
        if (min > max) {
            change = min;
            min = max;
            max = change;
            tv.setText(min + "℃~" + max + "℃");
        } else {
            tv.setText(min + "℃~" + max + "℃");
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    private void reFreshWeather() {
        toolbar.setTitle("同步中...");
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this);
        String districtName = prefs.getString("cityName", "");
        String cityName = prefs.getString("city", "");
        if (!TextUtils.isEmpty(districtName)) {
            Log.i("test", "getCityName");
            queryWeatherInfo(districtName, cityName);
        }
    }

    public void queryWeatherInfo(String districtName, String cityName) {
        Log.d("RayTest", "district = " + districtName + " city = " + cityName);

        //基础天气信息
        Parameters parameters = new Parameters();
        parameters.add("cityname", districtName);
        parameters.add("format", 2);
        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/index", JuheData.GET
                , parameters, new DataCallBack() {
            @Override
            public void onSuccess(int i, String response) {
                if (!TextUtils.isEmpty(response)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    isWeatherHandled = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isPMHandled && isTimeWeatherHandled) {
                                showWeather();
                                saveCity();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle("同步失败");
                    }
                });
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
                Utility.handlePMResponse(WeatherActivity.this, response);
                isPMHandled = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isWeatherHandled && isTimeWeatherHandled) {
                            showWeather();
                            saveCity();
                        }
                    }
                });
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle("同步失败");
                    }
                });
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
                Utility.handleTimeWeatherResponse(WeatherActivity.this, response);
                isTimeWeatherHandled = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isWeatherHandled && isPMHandled) {
                            showWeather();
                            saveCity();
                        }
                    }
                });
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle("同步失败");
                    }
                });
                Log.e("error", throwable.getMessage());
            }
        });
    }

    public void saveCity() {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this).edit();
        editor.putBoolean("city_selected", true);
        editor.commit();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this);
        String districtName = prefs.getString("cityName", "");
        String cityName = prefs.getString("city", "");
        String cityTemp = prefs.getString("temp", "");
        Log.d("RayTest", "prefs cityName = " + cityName);
        SelectedCity selectedCity = new SelectedCity(districtName,cityName, cityTemp);
        if (!rWeatherDB.checkSelectedCity(selectedCity.getDistrictName())) {
            rWeatherDB.saveSelectedCity(selectedCity);
        } else {
            rWeatherDB.updateSelectedCity(selectedCity);
        }
    }

    private void showWeather() {
        Log.i("test", "showWeather");
        isPMHandled = false;
        isTimeWeatherHandled = false;
        isWeatherHandled = false;
        mSwipeRefreshLayout.setRefreshing(false);
        toolbar.setTitle("Ray天气");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(prefs.getString("cityName", ""));
        tempText.setText(prefs.getString("temp", "") + "°");
        weatherText.setText(prefs.getString("temperature", ""));
        String string = prefs.getString("temperature", "");
        Log.d("RayTest", "string = " + string);
        String[] array = string.split("~");
        weatherText.setText(array[0] + " ~ " + array[1]);
        if (prefs.getString("weatherDesp", "").contains("转")) {
            String[] info = prefs.getString("weatherDesp", "").split("转");
            weatherDespText.setText(info[0] + "\n转" +info[1]);
        } else {
            weatherDespText.setText(prefs.getString("weatherDesp", ""));
        }
        publishText.setText(prefs.getString("refreshTime", "") + "更新");
        windText.setText(prefs.getString("windDirection", "") + prefs.getString("windStrength", ""));
        humidityText.setText("湿度" + prefs.getString("humidity", ""));
        dressingText.setText(prefs.getString("dressing", ""));
        uvText.setText(prefs.getString("uv", ""));
        washText.setText(prefs.getString("wash", ""));
        exerciseText.setText(prefs.getString("exercise", ""));
        //设置Image
        setImage(image, "img1");
        setTemp(preWeatherMinText1, preWeatherMaxText1, "preTemp1");
        setImage(preImage1, "preImage1");
        setTemp(preWeatherMinText2, preWeatherMaxText2, "preTemp2");
        setImage(preImage2, "preImage2");
        setTemp(preWeatherMinText3, preWeatherMaxText3, "preTemp3");
        setImage(preImage3, "preImage3");
        setTemp(preWeatherMinText4, preWeatherMaxText4, "preTemp4");
        setImage(preImage4, "preImage4");
        Calendar c = Calendar.getInstance();
        setWeek(preDayText2, "preWeek2");
        setWeek(preDayText3, "preWeek3");
        setWeek(preDayText4, "preWeek4");

        aqiText.setText(prefs.getString("aqi", ""));
        pmText.setText(prefs.getString("pm", ""));
        quelityText.setText(prefs.getString("quality", ""));
        int aqi = Integer.valueOf(prefs.getString("aqi", "0"));
        if (aqi <= 50) {
            quelityText.setBackgroundResource(R.color.you);
        } else if (aqi > 50 && aqi <=100) {
            quelityText.setBackgroundResource(R.color.liang);
        } else if (aqi > 100 && aqi <=150) {
            quelityText.setBackgroundResource(R.color.qingdu);
        } else if (aqi > 150 && aqi <=200) {
            quelityText.setBackgroundResource(R.color.zhongdu);
        } else if (aqi > 200 && aqi <=300) {
            quelityText.setBackgroundResource(R.color.yanzhong);
        } else {
            quelityText.setBackgroundResource(R.color.heavy);
        }

        timeAreaText1.setText(prefs.getString("sh1", "") + "时-" + prefs.getString("eh1", "")
                + "时");
        timeAreaText2.setText(prefs.getString("sh2", "") + "时-" + prefs.getString("eh2", "")
                + "时");
        timeAreaText3.setText(prefs.getString("sh3", "") + "时-" + prefs.getString("eh3", "")
                + "时");
        timeAreaText4.setText(prefs.getString("sh4", "") + "时-" + prefs.getString("eh4", "")
                + "时");
        timeWeatherText1.setText(prefs.getString("weather1", ""));
        timeWeatherText2.setText(prefs.getString("weather2", ""));
        timeWeatherText3.setText(prefs.getString("weather3", ""));
        timeWeatherText4.setText(prefs.getString("weather4", ""));
        setImage(timeImage1, "weatherId1");
        setImage(timeImage2, "weatherId2");
        setImage(timeImage3, "weatherId3");
        setImage(timeImage4, "weatherId4");
        setText(timeTempText1, prefs.getString("tempMin1", ""), prefs
                .getString("tempMax1", ""));
        setText(timeTempText2, prefs.getString("tempMin2", ""), prefs
                .getString("tempMax2", ""));
        setText(timeTempText3, prefs.getString("tempMin3", ""), prefs
                .getString("tempMax3", ""));
        setText(timeTempText4, prefs.getString("tempMin4", ""), prefs
                .getString("tempMax4", ""));

        Intent intent = new Intent(this, AutoRefreshService.class);
        startService(intent);
        Intent intentWhite = new Intent("android.appwidget.action.REFRESH");
        sendOrderedBroadcast(intentWhite, null);
        Intent intentTrans = new Intent("android.appwidget.action.REFRESH_TRANS");
        sendOrderedBroadcast(intentTrans, null);
    }

    public void setWeek(TextView tv, String str) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String week = prefs.getString(str, "");
        switch (week) {
            case "星期一":
                tv.setText("周一");
                break;
            case "星期二":
                tv.setText("周二");
                break;
            case "星期三":
                tv.setText("周三");
                break;
            case "星期四":
                tv.setText("周四");
                break;
            case "星期五":
                tv.setText("周五");
                break;
            case "星期六":
                tv.setText("周六");
                break;
            case "星期日":
                tv.setText("周日");
                break;
        }
    }

    public void setTemp(TextView tv1, TextView tv2, String str) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String string = prefs.getString(str, "");
        String[] array = string.split("~");
        tv1.setText(array[0]);
        tv2.setText(array[1]);
    }

    public void setImage(ImageView image, String str) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String imgNo = prefs.getString(str, "0");
        if (imageMapping == null)
            setImageMap();
        if (imageMapping.containsKey(imgNo))
            image.setImageResource(imageMapping.get(imgNo));

    }

    private Map<String ,Integer> imageMapping;
    private void setImageMap(){
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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            MyApplication.getInstance().exit();
        }
    }
}
