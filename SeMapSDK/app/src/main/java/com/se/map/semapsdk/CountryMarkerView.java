package com.se.map.semapsdk;

import android.graphics.Bitmap;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class CountryMarkerView extends MarkerView {

    private boolean isSkew;
    private List<String> resIds;
    private int locationMarker;

    public CountryMarkerView(BaseMarkerViewOptions baseMarkerViewOptions, boolean isSkew, List<String> resIds,int locationMarker) {
        super(baseMarkerViewOptions);
        this.isSkew = isSkew;
        this.resIds = resIds;
        this.locationMarker = locationMarker;
    }

    public boolean isSkew() {
        return isSkew;
    }

    public List<String> getFlagRes() {
        return resIds;
    }

    public int getLocationMarker(){
        return locationMarker;
    }
}
