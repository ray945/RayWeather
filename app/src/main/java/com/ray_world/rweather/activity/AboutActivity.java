package com.ray_world.rweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ray_world.rweather.R;
import com.ray_world.rweather.util.MyApplication;

/**
 * Created by ray on 15-6-21.
 */
public class AboutActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentColor = preferences.getString("current_color", "青春绿");
        if (currentColor.equals("青春绿")) {
            setTheme(R.style.ColorGreenTheme);
        } else if (currentColor.equals("天空蓝")) {
            setTheme(R.style.ColorBlueTheme);
        } else if (currentColor.equals("活力黄")) {
            setTheme(R.style.ColorYellowTheme);
        } else if (currentColor.equals("碧波翠")) {
            setTheme(R.style.ColorGreenDarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        MyApplication.getInstance().addActivity(this);
        initToolbar();
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
}
