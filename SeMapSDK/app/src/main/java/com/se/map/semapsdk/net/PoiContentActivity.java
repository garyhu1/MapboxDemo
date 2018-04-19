package com.se.map.semapsdk.net;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.se.map.semapsdk.utils.DistanceUtil;
import com.se.map.semapsdk.PoiEntity;
import com.se.map.semapsdk.R;

import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Administrator;
 * Since 2018/4/11;
 * Description:
 */

public class PoiContentActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_activity);
        txt = (TextView) findViewById(R.id.text);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("garyhu","one---->"+ DistanceUtil.getValue(0.00044,0.00379));
                Log.e("garyhu","two---->"+ DistanceUtil.getValue(0.00011,0.005));
                Log.e("garyhu","three---->"+ DistanceUtil.getValue(0.00322,0.0136));
                Log.e("garyhu","four---->"+ DistanceUtil.getValue(0.0041,0.03273));
                Log.e("garyhu","five---->"+ DistanceUtil.getValue(0.02731,0.0337));
            }
        });

        request();
    }

    private void request(){
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.158")
                .baseUrl("https://lkapi.botbrain.ai")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        long[] codes = new long[]{110100,500100,610100,120100,441900};


        HashMap<String,Object> map = new HashMap<>();
        map.put("x1",120.00000001);
        map.put("y1",40.00000001);
        map.put("x2",100.00000002);
        map.put("y2",20.00000007);
        map.put("area","[110100,500100,610100,120100,441900]");

        GetRequest getRequest = retrofit.create(GetRequest.class);

        Call<PoiEntity> call = getRequest.getPoiContent(map);

        call.enqueue(new Callback<PoiEntity>() {
            @Override
            public void onResponse(Call<PoiEntity> call, Response<PoiEntity> response) {
                txt.setText(response.body().getMsg());
                HttpUrl url = call.request().url();
                String s = url.encodedPath();
                Log.d("garyhu","url -----> "+s);

            }

            @Override
            public void onFailure(Call<PoiEntity> call, Throwable t) {

            }
        });
    }

    private OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("zcb","OkHttp====Message:"+message);
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }
}
