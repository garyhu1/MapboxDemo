package com.se.map.semapsdk.net;

import com.se.map.semapsdk.PoiEntity;
import com.se.map.semapsdk.model.CityCodeEntity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Author: Administrator;
 * Since 2018/4/11;
 * Description:
 */

public interface GetRequest {

    @GET("/get_poi_content")
    Call<PoiEntity> getPoiContent(@Query("id") int id);

    @GET("/api/location")
    Call<CityCodeEntity> getLocationCode(@QueryMap HashMap<String,String> map);
}
