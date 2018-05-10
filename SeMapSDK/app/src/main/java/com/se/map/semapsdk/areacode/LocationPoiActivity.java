package com.se.map.semapsdk.areacode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.se.map.semapsdk.R;
import com.se.map.semapsdk.model.LocationEntity;
import com.se.map.semapsdk.net.GenerateSign;
import com.se.map.semapsdk.net.LocationPoiRequest;

import java.util.HashMap;

/**
 * Author: Administrator;
 * Since 2018/5/9;
 * Description:
 */

public class LocationPoiActivity extends AppCompatActivity {

    private TextView poiTxt;

    private LocationPoiRequest locationPoiRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_poi);

        poiTxt = (TextView) findViewById(R.id.poi_txt);

        loadData();
    }

    public void loadData(){
        locationPoiRequest = new LocationPoiRequest();

        locationPoiRequest.addPoiListener(new LocationPoiRequest.LocationPoiCallback() {
            @Override
            public void showPoi(LocationEntity locationEntity) {
                poiTxt.setText(locationEntity.getCount()+"");
            }
        });

        HashMap<String,String> map = new HashMap<>();
        map.put("size","1");
        map.put("range","50");
        map.put("sort","1");
        map.put("type","1");
        map.put("lat","32.038055");
        map.put("lon","118.775892");

        String sign = GenerateSign.getSign(map);

        map.put("sign",sign);
        map.put("key","99999999");

        locationPoiRequest.request(map);
    }
}
