package com.se.map.semapsdk.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.se.map.semapsdk.R;
import com.se.map.semapsdk.model.CityCodeEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Administrator;
 * Since 2018/4/16;
 * Description:
 */

public class GenerateSignActivity extends AppCompatActivity {

    private TextView showText;
    private TextView responseText;

    private CityCodeRequest cityCodeRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        showText  = (TextView) findViewById(R.id.show_text);
        responseText = (TextView) findViewById(R.id.response_text);

        cityCodeRequest = new CityCodeRequest();

        HashMap<String,String> map = new HashMap<>();
        map.put("lat","31.26599274");
        map.put("lon","120.735399");
        String sign = GenerateSign.getSign(map);
        showText.setText(sign);

        map.put("sign",sign);
        map.put("key","99999999");
        cityCodeRequest.request(map);

        Log.d("garyhu","sign---------------------->"+sign);

        cityCodeRequest.setCallback(new CityCodeRequest.CityCodeCallback() {
            @Override
            public void showContent(CityCodeEntity data) {
                Log.d("garyhu","加载成功---------------------->"+data.getState());
                responseText.setText(data.getAreacode());
            }
        });
    }
}
