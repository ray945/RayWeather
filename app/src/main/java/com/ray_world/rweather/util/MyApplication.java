package com.ray_world.rweather.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.thinkland.sdk.android.JuheSDKInitializer;

/**
 * Created by ray on 15-6-13.
 */
public class MyApplication extends Application {
    private List<Activity> activitys = null;
    private static MyApplication instance;

    private MyApplication() {
        activitys = new LinkedList<Activity>();
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if(!activitys.contains(activity)){
                activitys.add(activity);
            }
        }else{
            activitys.add(activity);
        }

    }

    // 遍历所有Activity并finish
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
    }
}
