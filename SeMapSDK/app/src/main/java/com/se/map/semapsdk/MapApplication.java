package com.se.map.semapsdk;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, getString(R.string.access_token));
    }
}
