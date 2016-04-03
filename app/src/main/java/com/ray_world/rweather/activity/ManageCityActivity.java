package com.ray_world.rweather.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ray_world.rweather.R;
import com.ray_world.rweather.model.RWeatherDB;
import com.ray_world.rweather.model.SelectedCity;
import com.ray_world.rweather.util.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ray on 15-6-13.
 */
public class ManageCityActivity extends AppCompatActivity {

    private ListView manageCityist;
    private SimpleAdapter adapter;

    private RWeatherDB rWeatherDB;

    public static List<Map<String, String>> lists = new ArrayList<>();

    private String currentCity;
    private int flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_city);
        MyApplication.getInstance().addActivity(this);

        final Intent intent = getIntent();
        currentCity = intent.getStringExtra("currentCity");

        initToolbar();
        manageCityist = (ListView) findViewById(R.id.manage_city_list);

        adapter = new SimpleAdapter(this, lists, R.layout.list_item_layout
                , new String[] {"districtName", "temp"}
                , new int[] {R.id.city_name, R.id.temp});
        manageCityist.setAdapter(adapter);
        rWeatherDB = RWeatherDB.getInstance(this);
        querySelectedCity();
        manageCityist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Map<String, String> item = (Map<String, String>) adapterView
                        .getItemAtPosition(position);
                String districtName = item.get("districtName");
                String cityName = item.get("cityName");
                Log.d("RayTest", "manage district = " + districtName + " city = " + cityName);
                Intent intent = new Intent(ManageCityActivity.this, WeatherActivity.class);
                intent.putExtra("district_name", districtName);
                intent.putExtra("city_name", cityName);
                startActivity(intent);
                finish();
            }
        });
        manageCityist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view
                    , int position, long l) {
                Log.i("test", "长按");
                final Map<String, String> item = (Map<String, String>) adapterView
                        .getItemAtPosition(position);
                AlertDialog builder = new AlertDialog.Builder(ManageCityActivity.this)
                        .setTitle("删除提示")
                        .setMessage("您确定要删除 " + item.get("districtName") + " 吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lists.remove(item);
                                rWeatherDB.deleteSelectedCity(item.get("districtName"));
                                adapter.notifyDataSetChanged();

                                if (!rWeatherDB.checkSelectedCity(currentCity)) {
                                    flag = 1;
                                    if (adapterView.getCount() == 0) {
                                        Intent intent = new Intent(ManageCityActivity.this, ChooseCityActivity.class);
                                        intent.putExtra("from_manage_city_activity", true);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Map<String, String> item = (Map<String, String>) adapterView
                                                .getItemAtPosition(0);
                                        currentCity = item.get("districtName");
                                    }
                                } else {
                                    flag = 0;
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                builder.show();

                return true;
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

    public void querySelectedCity() {
        List<SelectedCity> selectedCityList = rWeatherDB.loadSelectedCity();
        if (selectedCityList.size() > 0) {
            lists.clear();
            for (SelectedCity selectedCity : selectedCityList) {
                Map<String, String> list = new HashMap<>();
                list.put("districtName", selectedCity.getDistrictName());
                list.put("cityName", selectedCity.getCityName());
                Log.d("RayTest", "query cityName = " + selectedCity.getCityName());
                list.put("temp", selectedCity.getTemp());
                lists.add(list);
            }
            adapter.notifyDataSetChanged();
            manageCityist.setSelection(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (flag == 1) {
            flag = 0;
            Intent intent = new Intent(ManageCityActivity.this, WeatherActivity.class);
            intent.putExtra("district_name", currentCity);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
