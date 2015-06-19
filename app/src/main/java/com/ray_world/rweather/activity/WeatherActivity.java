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
import com.ray_world.rweather.util.HttpCallBackListener;
import com.ray_world.rweather.util.HttpUtil;
import com.ray_world.rweather.util.MyApplication;
import com.ray_world.rweather.util.ParseUrl;
import com.ray_world.rweather.util.Utility;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rui on 2015/5/30 0030.
 */
public class WeatherActivity extends ActionBarActivity {

    public TextView publishText;
    private TextView weatherDespText;
    private TextView cityName;
    private TextView tempText;
    private TextView preDayText1;
    private TextView preDayText2;
    private TextView preDayText3;
    private ImageView todayImage;
    private ImageView preImage1;
    private ImageView preImage2;
    private ImageView preImage3;
    private TextView todayWeatherText;
    private TextView preWeatherText1;
    private TextView preWeatherText2;
    private TextView preWeatherText3;
    private TextView windText;
    private TextView humidityText;
    private TextView cyDesText;
    private TextView ydHintText;
    private TextView lsHintText;
    private TextView gmHintText;
    private TextView gjHintText;
    private TextView ysHintText;
    private TextView xcHintText;
    private ImageView image;
    private NavigationView mNavigationView;
    private FrameLayout header;

    private Toolbar toolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout mDrawerLayout;

    private boolean isWeatherHandled = false;
    private boolean isPreWeatherHandled = false;
    private boolean isSuggestionHandled = false;
    private String[] week = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private RWeatherDB rWeatherDB;

    int flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_weather_layout);
        MyApplication.getInstance().addActivity(this);

        String url = ParseUrl.getInterfaceURL("101270301", "forecast_v");
        Log.i("Url", url);

        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        tempText = (TextView) findViewById(R.id.temp_text);
        cityName = (TextView) findViewById(R.id.city_name);
        image = (ImageView) findViewById(R.id.img);
        preDayText1 = (TextView) findViewById(R.id.pre_day1);
        preDayText2 = (TextView) findViewById(R.id.pre_day2);
        preDayText3 = (TextView) findViewById(R.id.pre_day3);
        todayImage = (ImageView) findViewById(R.id.today_image);
        preImage1 = (ImageView) findViewById(R.id.pre_image1);
        preImage2 = (ImageView) findViewById(R.id.pre_image2);
        preImage3 = (ImageView) findViewById(R.id.pre_image3);
        todayWeatherText = (TextView) findViewById(R.id.today_weather);
        preWeatherText1 = (TextView) findViewById(R.id.pre_weather1);
        preWeatherText2 = (TextView) findViewById(R.id.pre_weather2);
        preWeatherText3 = (TextView) findViewById(R.id.pre_weather3);
        windText = (TextView) findViewById(R.id.wind_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        cyDesText = (TextView) findViewById(R.id.cy_des_text);
        ydHintText = (TextView) findViewById(R.id.yd_hint_text);
        lsHintText = (TextView) findViewById(R.id.ls_hint_text);
        gmHintText = (TextView) findViewById(R.id.gm_hint_text);
        gjHintText = (TextView) findViewById(R.id.gj_hint_text);
        ysHintText = (TextView) findViewById(R.id.ys_hint_text);
        xcHintText = (TextView) findViewById(R.id.xc_hint_text);
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
                }
                return true;
            }
        });

        String countyCode = getIntent().getStringExtra("county_code");
        String weatherCode = getIntent().getStringExtra("weather_code");
        if (!TextUtils.isEmpty(countyCode)) {
            toolbar.setTitle("同步中……");
            queryWeatherCode(countyCode);
            flag = 1;
        } else if (!TextUtils.isEmpty(weatherCode)) {
            Log.i("test", "更换城市");
            toolbar.setTitle("同步中……");
            queryWeatherInfo(weatherCode);
            flag = 2;
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
        String weatherCode = prefs.getString("weatherCode", "");
        if (!TextUtils.isEmpty(weatherCode)) {
            Log.i("test", "getWeatherCode");
            queryWeatherInfo(weatherCode);
        }
    }

    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode
                + ".xml";
        queryFromServer(address, "countyCode");
    }

    public void queryWeatherInfo(String weatherCode) {
        Log.i("test", "queryWeatherInfo");
        String address1 = "http://api.36wu.com/Weather/GetWeather?district=" + weatherCode +
                "&format=json";
        String address2 = "http://api.36wu.com/Weather/GetMoreWeather?district=" + weatherCode +
                "&format=json";
        String address3 = "http://api.36wu.com/Weather/GetWeatherIndex?district=" + weatherCode +
                "&format=json";
        queryFromServer(address1, "weatherCode");
        queryFromServer(address2, "preWeatherCode");
        queryFromServer(address3, "suggestionCode");
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    isWeatherHandled = true;
                    SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(WeatherActivity.this);
                    String cityName = prefs.getString("cityName", "");
                    String cityCode = prefs.getString("weatherCode", "");
                    String cityTemp = prefs.getString("temp", "");
                    SelectedCity selectedCity = new SelectedCity(cityName, cityCode);
                    selectedCity.setTemp(cityTemp);
                    if (!rWeatherDB.checkSelectedCity(selectedCity)) {
                        rWeatherDB.saveSelectedCity(selectedCity);
                    } else {
                        rWeatherDB.updateSelectedCity(selectedCity);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isPreWeatherHandled && isSuggestionHandled)
                                showWeather();
                        }
                    });
                } else if ("preWeatherCode".equals(type)) {
                    Utility.handlePreWeatherResponse(WeatherActivity.this, response);
                    isPreWeatherHandled = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isWeatherHandled && isSuggestionHandled)
                                showWeather();
                        }
                    });
                } else if ("suggestionCode".equals(type)) {
                    Utility.handleSuggestionResponse(WeatherActivity.this, response);
                    isSuggestionHandled = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isWeatherHandled && isPreWeatherHandled)
                                showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle("同步失败");
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void showWeather() {
        Log.i("test", "showPreWeather");
        mSwipeRefreshLayout.setRefreshing(false);
        toolbar.setTitle("Ray天气");
        isWeatherHandled = false;
        isPreWeatherHandled = false;
        isSuggestionHandled = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(prefs.getString("cityName", ""));
        tempText.setText(prefs.getString("temp", ""));
        weatherDespText.setText(prefs.getString("weatherDesp", ""));
        publishText.setText(prefs.getString("refreshTime", "") + "更新");
        windText.setText(prefs.getString("windDirection", "") + prefs.getString("windForce", ""));
        humidityText.setText("湿度" + prefs.getString("humidity", ""));
        cyDesText.setText(prefs.getString("cyDes", ""));
        ydHintText.setText(prefs.getString("ydHint", ""));
        lsHintText.setText(prefs.getString("lsHint", ""));
        gmHintText.setText(prefs.getString("gmHint", ""));
        gjHintText.setText(prefs.getString("gjHint", ""));
        ysHintText.setText(prefs.getString("ysHint", ""));
        xcHintText.setText(prefs.getString("xcHint", ""));
        //设置Image
        setImage(image, "img1");
        todayWeatherText.setText(prefs.getString("todayWeather", ""));
        setImage(todayImage, "todayImage");
        preWeatherText1.setText(prefs.getString("preWeather1", ""));
        setImage(preImage1, "preImage1");
        preWeatherText2.setText(prefs.getString("preWeather2", ""));
        setImage(preImage2, "preImage2");
        preWeatherText3.setText(prefs.getString("preWeather3", ""));
        setImage(preImage3, "preImage3");
        Calendar c = Calendar.getInstance();
        int mWay = Integer.valueOf(String.valueOf(c.get(Calendar.DAY_OF_WEEK)));
        //设置未来几天的星期
        if (mWay <= 4) {
            preDayText1.setText(week[mWay]);
            preDayText2.setText(week[mWay+1]);
            preDayText3.setText(week[mWay+2]);
        } else if (mWay == 5) {
            preDayText1.setText(week[mWay]);
            preDayText2.setText(week[mWay+1]);
            preDayText3.setText(week[0]);
        } else if (mWay == 6) {
            preDayText1.setText(week[mWay]);
            preDayText2.setText(week[0]);
            preDayText3.setText(week[1]);
        } else if (mWay == 7) {
            preDayText1.setText(week[0]);
            preDayText2.setText(week[1]);
            preDayText3.setText(week[2]);
        }
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
        imageMapping.put("0", R.drawable.a00);
        imageMapping.put("1", R.drawable.a01);
        imageMapping.put("2", R.drawable.a02);
        imageMapping.put("3", R.drawable.a03);
        imageMapping.put("4", R.drawable.a04);
        imageMapping.put("5", R.drawable.a05);
        imageMapping.put("6", R.drawable.a06);
        imageMapping.put("7", R.drawable.a07);
        imageMapping.put("8", R.drawable.a08);
        imageMapping.put("9", R.drawable.a09);
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
