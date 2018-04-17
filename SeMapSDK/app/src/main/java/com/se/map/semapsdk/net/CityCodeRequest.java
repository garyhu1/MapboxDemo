package com.se.map.semapsdk.net;

import android.util.Log;

import com.se.map.semapsdk.model.CityCodeEntity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Administrator;
 * Since 2018/4/16;
 * Description:
 */

public class CityCodeRequest {

    private  CityCodeCallback callback;

    public void setCallback(CityCodeCallback callback){
        this.callback = callback;
    }

    public  void request(HashMap<String,String> map){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://lcs.emapgo.com.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetRequest getRequest = retrofit.create(GetRequest.class);

        Call<CityCodeEntity> call = getRequest.getLocationCode(map);

        call.enqueue(new Callback<CityCodeEntity>() {
            @Override
            public void onResponse(Call<CityCodeEntity> call, Response<CityCodeEntity> response) {
                if(callback != null){
                    callback.showContent(response.body());
                }
            }

            @Override
            public void onFailure(Call<CityCodeEntity> call, Throwable t) {
                Log.e("garyhu",t.getMessage());
            }
        });
    }

    public interface CityCodeCallback {
        void showContent(CityCodeEntity data);
    }
}
