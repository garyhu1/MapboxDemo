package com.se.map.semapsdk.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mapbox.mapboxsdk.Mapbox;
import com.se.map.semapsdk.R;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Mapbox.getInstance(this, getString(R.string.access_token));
    }
}
