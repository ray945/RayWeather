package com.ray_world.rweather.util;

/**
 * Created by Rui on 2015/6/3 0003.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
