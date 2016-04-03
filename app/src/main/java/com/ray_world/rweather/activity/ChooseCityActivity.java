package com.ray_world.rweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ray_world.rweather.R;
import com.ray_world.rweather.model.City;
import com.ray_world.rweather.util.MyApplication;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseCityActivity extends AppCompatActivity {

    private EditText cityText;
    private ListView listView;
    private TextView cityError;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private boolean isFromWeatherActivity;
    private boolean isFromManageCityActivity;
    private String cityNameInput;

    Handler myHandler = new Handler();

    MyTextWatcher myTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        initToolbar();

        MyApplication.getInstance().addActivity(this);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        isFromManageCityActivity = getIntent().getBooleanExtra("from_manage_city_activity", false);

        cityText = (EditText) findViewById(R.id.city_text);

        //如果标记城市已选择，则直接进入天气信息界面
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity && !isFromManageCityActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        listView = (ListView) findViewById(R.id.list_view_city);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        Log.d("RayTest", "listView = " + listView + " adapter = " + adapter);
        listView.setAdapter(adapter);

        myTextWatcher = new MyTextWatcher();
        cityText.addTextChangedListener(myTextWatcher);
        cityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                queryCity(cityText.getText().toString());
                return true;
            }
        });

        cityError = (TextView) findViewById(R.id.city_error);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityNameString = dataList.get(position);
                String[] cityName = cityNameString.split("   ");
                Intent intent = new Intent(ChooseCityActivity.this, WeatherActivity.class);
                intent.putExtra("district_name", cityName[0]);
                intent.putExtra("city_name", cityName[1]);
                startActivity(intent);
                finish();
            }
        });
    }

    private class MyTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            myHandler.post(change);
        }
    }

    Runnable change = new Runnable() {
        @Override
        public void run() {
            cityNameInput = cityText.getText().toString();
            Log.d("RayTest", "cityNameInput = " + cityNameInput);
            dataList.clear();
            queryCity(cityNameInput);
        }
    };

    private void queryCity(final String cityName) {
        final City city = new City();

        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/citys", JuheData.GET
                , null, new DataCallBack() {
            @Override
            public void onSuccess(int i, String response) {
                Log.d("RayTest", "onSuccess");
                try {
                    if (!TextUtils.isEmpty(response)) {
                        JSONObject jsonObject = new JSONObject(response);
                        String resultCode = jsonObject.getString("resultcode");
                        JSONArray results = jsonObject.getJSONArray("result");
                        for (int j = 0; j < results.length(); j++) {
                            JSONObject cityObj = results.getJSONObject(j);
                            String result = cityObj.getString("district");
                            if (result.equals(cityName) || result.contains(cityName)) {
                                cityError.setVisibility(View.GONE);
                                Log.d("RayTest", "cityError gone");
                                city.setId(cityObj.getInt("id"));
                                city.setProvince(cityObj.getString("province"));
                                city.setCity(cityObj.getString("city"));
                                city.setDistrict(cityObj.getString("district"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataList.add(city.getDistrict() + "   " + city.getCity()
                                                + "   " + city.getProvince());
                                    }
                                });
                            }
                        }
                        if (dataList.size() == 0) {
                            cityError.setText("城市好像不正确哦→_→");
                            cityError.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                        Toast.makeText(ChooseCityActivity.this, "网络好像出了点问题_(:з」∠)_", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("error", throwable.getMessage());
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        } else if (isFromManageCityActivity) {
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(this).edit();
            editor.putBoolean("city_selected", false);
            editor.commit();
            MyApplication.getInstance().exit();
        } else {
            finish();
        }
    }
}
