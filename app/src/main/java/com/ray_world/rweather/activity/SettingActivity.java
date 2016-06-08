package com.ray_world.rweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ray_world.rweather.R;
import com.ray_world.rweather.service.AutoRefreshService;
import com.ray_world.rweather.util.MyApplication;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout primaryColorSetting;
    private LinearLayout refreshRateSetting;
    private LinearLayout aboutSetting;
    private LinearLayout feedbackSetting;
    private TextView refreshRateText;
    private TextView colorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_setting);
        MyApplication.getInstance().addActivity(this);
        initToolbar();
        primaryColorSetting = (LinearLayout) findViewById(R.id.setting_primary_color);
        refreshRateSetting = (LinearLayout) findViewById(R.id.setting_refresh_rate);
        aboutSetting = (LinearLayout) findViewById(R.id.setting_about);
        feedbackSetting = (LinearLayout) findViewById(R.id.setting_feedback);
        primaryColorSetting.setOnClickListener(this);
        refreshRateSetting.setOnClickListener(this);
        aboutSetting.setOnClickListener(this);
        feedbackSetting.setOnClickListener(this);
        refreshRateText = (TextView) findViewById(R.id.refresh_rate_text);
        colorText = (TextView) findViewById(R.id.color_text);
        updateRate();
        updateColor();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_primary_color:
                final LinearLayout layoutColor = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.choose_color_layout, null);
                AlertDialog builderColor = new AlertDialog.Builder(this)
                        .setView(layoutColor)
                        .setTitle("选择主题色")
                        .create();
                RadioGroup radioGroupColor = (RadioGroup) layoutColor.findViewById(R.id.choose_color);
                RadioButton green = (RadioButton) layoutColor.findViewById(R.id.choose_color_green);
                RadioButton blue = (RadioButton) layoutColor.findViewById(R.id.choose_color_blue);
                RadioButton yellow = (RadioButton) layoutColor.findViewById(R.id.choose_color_yellow);
                RadioButton greenDark = (RadioButton) layoutColor.findViewById(R.id.choose_color_green_dark);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String currentColor = prefs.getString("current_color", "青春绿");
                if (currentColor.equals("青春绿")) {
                    green.setChecked(true);
                } else if (currentColor.equals("天空蓝")) {
                    blue.setChecked(true);
                } else if (currentColor.equals("活力黄")) {
                    yellow.setChecked(true);
                } else if (currentColor.equals("碧波翠")) {
                    greenDark.setChecked(true);
                }
                radioGroupColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        RadioButton radioButton = (RadioButton) layoutColor.findViewById(checkedId);
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(SettingActivity.this).edit();
                        editor.putString("current_color", radioButton.getText().toString());
                        editor.commit();
                        updateColor();
                        Toast.makeText(SettingActivity.this, "请重启应用以应用更改", Toast.LENGTH_SHORT).show();
                    }
                });
                builderColor.show();
                break;
            case R.id.setting_refresh_rate:
                final LinearLayout layout = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.choose_rate_layout, null);
                AlertDialog builder = new AlertDialog.Builder(this)
                        .setView(layout)
                        .setTitle("选择刷新频率")
                        .create();
                RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.choose_rate);
                RadioButton half = (RadioButton) layout.findViewById(R.id.choose_rate_half);
                RadioButton one = (RadioButton) layout.findViewById(R.id.choose_rate_one);
                RadioButton two = (RadioButton) layout.findViewById(R.id.choose_rate_two);
                RadioButton three = (RadioButton) layout.findViewById(R.id.choose_rate_three);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String currentRate = preferences.getString("refresh_rate", "1小时");
                if (currentRate.equals("30分钟")) {
                    half.setChecked(true);
                } else if (currentRate.equals("1小时")) {
                    one.setChecked(true);
                } else if (currentRate.equals("2小时")) {
                    two.setChecked(true);
                } else if (currentRate.equals("3小时")) {
                    three.setChecked(true);
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        RadioButton radioButton = (RadioButton) layout.findViewById(checkedId);
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(SettingActivity.this).edit();
                        editor.putString("refresh_rate", radioButton.getText().toString());
                        editor.commit();
                        updateRate();
                        Intent intent = new Intent(SettingActivity.this, AutoRefreshService.class);
                        startService(intent);
                    }
                });
                builder.show();
                break;
            case R.id.setting_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_feedback:
                Intent intentMail = new Intent(Intent.ACTION_SENDTO);
                intentMail.setData(Uri.parse("mailto:zhaorui007@qq.com"));
                intentMail.putExtra(Intent.EXTRA_SUBJECT, "Ray天气意见反馈");
                startActivity(intentMail);
                break;
        }
    }

    private void updateColor() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(SettingActivity.this);
        colorText.setText(preferences.getString("current_color", "青春绿"));
    }

    private void updateRate() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(SettingActivity.this);
        refreshRateText.setText(preferences.getString("refresh_rate", "1小时"));
    }
}
