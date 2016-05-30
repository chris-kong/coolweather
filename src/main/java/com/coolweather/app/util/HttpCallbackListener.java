package com.coolweather.app.util;

/**
 * Created by christopher on 2016/5/30.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
