package com.ray_world.rweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.TextView;

import com.ray_world.rweather.R;
import com.ray_world.rweather.model.RWeatherDB;
import com.ray_world.rweather.model.SelectedCity;
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
    private TextView preDayText1;
    private TextView preDayText2;
    private TextView preDayText3;
    private TextView preDayText4;
    private ImageView preImage1;
    private ImageView preImage2;
    private ImageView preImage3;
    private ImageView preImage4;
    private TextView preWeatherText1;
    private TextView preWeatherText2;
    private TextView preWeatherText3;
    private TextView preWeatherText4;
    private TextView windText;
    private TextView humidityText;
    private TextView dressingText;
    private TextView uvText;
    private TextView washText;
    private TextView exerciseText;
    private ImageView image;
    private NavigationView mNavigationView;
    private FrameLayout header;

    private Toolbar toolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;

    private RWeatherDB rWeatherDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_weather_layout);
        MyApplication.getInstance().addActivity(this);

        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        tempText = (TextView) findViewById(R.id.temp_text);
        weatherText = (TextView) findViewById(R.id.weather_text);
        cityName = (TextView) findViewById(R.id.city_name);
        image = (ImageView) findViewById(R.id.img);
        preDayText1 = (TextView) findViewById(R.id.pre_day1);
        preDayText2 = (TextView) findViewById(R.id.pre_day2);
        preDayText3 = (TextView) findViewById(R.id.pre_day3);
        preDayText4 = (TextView) findViewById(R.id.pre_day4);
        preImage1 = (ImageView) findViewById(R.id.pre_image1);
        preImage2 = (ImageView) findViewById(R.id.pre_image2);
        preImage3 = (ImageView) findViewById(R.id.pre_image3);
        preImage4 = (ImageView) findViewById(R.id.pre_image4);
        preWeatherText1 = (TextView) findViewById(R.id.pre_weather1);
        preWeatherText2 = (TextView) findViewById(R.id.pre_weather2);
        preWeatherText3 = (TextView) findViewById(R.id.pre_weather3);
        preWeatherText4 = (TextView) findViewById(R.id.pre_weather4);
        windText = (TextView) findViewById(R.id.wind_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        dressingText = (TextView) findViewById(R.id.cy_des_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        washText = (TextView) findViewById(R.id.wash_text);
        exerciseText = (TextView) findViewById(R.id.exercise_text);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        header = (FrameLayout) findViewById(R.id.header);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        rWeatherDB = RWeatherDB.getInstance(this);

        initToolbar();

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
                    Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                    intent.putExtra("from_weather_activity", true);
                    startActivity(intent);
                    finish();
                } else if (menuItem.getItemId() == R.id.item_manage_city) {
                    Intent intent = new Intent(WeatherActivity.this, ManageCityActivity.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.item_about) {
                    Intent intent = new Intent(WeatherActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        String cityName = getIntent().getStringExtra("city_name");
        if (!TextUtils.isEmpty(cityName)) {
            toolbar.setTitle("同步中……");
            queryWeatherInfo(cityName);
        } else {
            showWeather();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFreshWeather();
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawer弹出响应
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void reFreshWeather() {
        toolbar.setTitle("同步中...");
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this);
        String cityName = prefs.getString("cityName", "");
        if (!TextUtils.isEmpty(cityName)) {
            Log.i("test", "getCityName");
            queryWeatherInfo(cityName);
        }
    }


    public void queryWeatherInfo(String cityName) {
        Parameters parameters = new Parameters();
        parameters.add("cityname", cityName);
        parameters.add("format", 2);
        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/index", JuheData.GET
                , parameters, new DataCallBack() {
            @Override
            public void onSuccess(int i, String response) {
                if (!TextUtils.isEmpty(response)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(WeatherActivity.this);
                    String cityName = prefs.getString("cityName", "");
                    String cityTemp = prefs.getString("temp", "");
                    SelectedCity selectedCity = new SelectedCity(cityName, cityTemp);
                    if (!rWeatherDB.checkSelectedCity(selectedCity)) {
                        rWeatherDB.saveSelectedCity(selectedCity);
                    } else {
                        rWeatherDB.updateSelectedCity(selectedCity);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
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
    }


    private void showWeather() {
        Log.i("test", "showPreWeather");
        mSwipeRefreshLayout.setRefreshing(false);
        toolbar.setTitle("Ray天气");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(prefs.getString("cityName", ""));
        tempText.setText(prefs.getString("temp", "") + "°");
        weatherText.setText(prefs.getString("temperature", ""));
        String string = prefs.getString("temperature", "");
        String[] array = string.split("~");
        weatherText.setText(array[0] + " ~ " + array[1]);
        weatherDespText.setText(prefs.getString("weatherDesp", ""));
        publishText.setText(prefs.getString("refreshTime", "") + "更新");
        windText.setText(prefs.getString("windDirection", "") + prefs.getString("windStrength", ""));
        humidityText.setText("湿度" + prefs.getString("humidity", ""));
        dressingText.setText(prefs.getString("dressing", ""));
        uvText.setText(prefs.getString("uv", ""));
        washText.setText(prefs.getString("wash", ""));
        exerciseText.setText(prefs.getString("exercise", ""));
        //设置Image
        setImage(image, "img1");
        setTemp(preWeatherText1, "preTemp1");
        setImage(preImage1, "preImage1");
        setTemp(preWeatherText2, "preTemp2");
        setImage(preImage2, "preImage2");
        setTemp(preWeatherText3, "preTemp3");
        setImage(preImage3, "preImage3");
        setTemp(preWeatherText4, "preTemp4");
        setImage(preImage4, "preImage4");
        Calendar c = Calendar.getInstance();
        setWeek(preDayText1, "preWeek1");
        setWeek(preDayText2, "preWeek2");
        setWeek(preDayText3, "preWeek3");
        setWeek(preDayText4, "preWeek4");
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

    public void setTemp(TextView tv, String str) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String string = prefs.getString(str, "");
        String[] array = string.split("~");
        tv.setText(array[0] + "/" + array[1]);
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
