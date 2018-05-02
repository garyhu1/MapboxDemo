package com.se.map.semapsdk.areacode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.se.map.semapsdk.R;
import com.se.map.semapsdk.model.CityCodeEntity;
import com.se.map.semapsdk.model.Point;
import com.se.map.semapsdk.net.CityCodeRequest;
import com.se.map.semapsdk.net.GenerateSign;
import com.se.map.semapsdk.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Author: Administrator;
 * Since 2018/4/17;
 * Description:
 */

public class AreaCodeActivity extends AppCompatActivity {

    private TextView areaCodeText;
    private CityCodeRequest cityCodeRequest;
    private Set<String> codes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_code);

        areaCodeText = (TextView) findViewById(R.id.my_area_code);
        cityCodeRequest = new CityCodeRequest();

        codes = new HashSet<>();

        Point[] points = DistanceUtil.obtainAreaCode(113.65701,113.70988,34.75529,34.73722,13);
        for (int i = 0; i < points.length; i++) {
            HashMap<String,String> map = new HashMap<>();
            map.put("lat",points[i].getY()+"");
            map.put("lon",points[i].getX()+"");
            String sign = GenerateSign.getSign(map);

            map.put("sign",sign);
            map.put("key","99999999");
            cityCodeRequest.request(map);
        }

        cityCodeRequest.setCallback(new CityCodeRequest.CityCodeCallback() {
            @Override
            public void showContent(CityCodeEntity data) {
                codes.add(data.getAreacode());
            }
        });

        findViewById(R.id.show_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("garyhu","size----------------> "+codes.size());
                Iterator<String> iterator = codes.iterator();
                while(iterator.hasNext()){
                    String value = iterator.next();
                    Log.d("garyhu","value----------------> "+value);
                }
            }
        });


    }
}
