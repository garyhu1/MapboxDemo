package com.se.map.semapsdk.net;

import com.se.map.semapsdk.PoiEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Administrator;
 * Since 2018/4/12;
 * Description:
 */

public class PoiRequest {

    private final static String TAG = PoiRequest.class.getSimpleName();

    private PoiCallback poiCallback;

    public void addPoiListener(PoiCallback poiCallback){
        this.poiCallback = poiCallback;
    }

    public void request(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.150")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetRequest getRequest = retrofit.create(GetRequest.class);

        Call<PoiEntity> call = getRequest.getPoiContent(2);

        call.enqueue(new Callback<PoiEntity>() {
            @Override
            public void onResponse(Call<PoiEntity> call, Response<PoiEntity> response) {
                if(poiCallback!=null){
                    poiCallback.showPoiContent(response.body());
                }
            }

            @Override
            public void onFailure(Call<PoiEntity> call, Throwable t) {

            }
        });
    }

    public interface PoiCallback {
        void showPoiContent(PoiEntity poiEntity);
    }
}
