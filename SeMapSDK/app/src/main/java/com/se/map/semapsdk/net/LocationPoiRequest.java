package com.se.map.semapsdk.net;

import android.util.Log;
import android.widget.Toast;

import com.se.map.semapsdk.PoiEntity;
import com.se.map.semapsdk.model.LocationEntity;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Administrator;
 * Since 2018/5/9;
 * Description:
 */

public class LocationPoiRequest {

    private final static String TAG = LocationPoiRequest.class.getSimpleName();

    private LocationPoiCallback locationPoiCallback;

    public void addPoiListener(LocationPoiCallback locationPoiCallback){
        this.locationPoiCallback = locationPoiCallback;
    }

    public void request(HashMap<String,String> map){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lcs.emapgo.com.cn")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetRequest getRequest = retrofit.create(GetRequest.class);

        Call<LocationEntity> call = getRequest.getLocationInfo(map);

        call.enqueue(new Callback<LocationEntity>() {
            @Override
            public void onResponse(Call<LocationEntity> call, Response<LocationEntity> response) {
                if(locationPoiCallback!=null){
                    locationPoiCallback.showPoi(response.body());
                }
            }

            @Override
            public void onFailure(Call<LocationEntity> call, Throwable t) {
                Log.d(TAG,"请求失败=======》》》》》》》》"+t.getMessage());
            }
        });
    }

    public interface LocationPoiCallback {
        void showPoi(LocationEntity locationEntity);
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
