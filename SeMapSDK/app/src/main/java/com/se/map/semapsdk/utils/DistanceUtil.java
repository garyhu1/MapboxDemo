package com.se.map.semapsdk.utils;

import android.content.Context;
import android.graphics.PointF;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import com.se.map.semapsdk.PoiEntity;
import com.se.map.semapsdk.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public class DistanceUtil {

    private static double EARTH_RADIUS = 6378137;    //地球半径
    private static double RAD = Math.PI / 180.0;   //   π/180

    public static double getDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = lat1 * RAD;  // // RAD=π/180
        double radLat2 = lat2 * RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s; //返回 单位是m
    }

    public static double getValue(double x,double y){
        return Math.sqrt(x*x+y*y);
    }

    public static void filterPoiData(MapboxMap mapboxMap,List<PoiEntity.DataBean> data, List<List<PoiEntity.DataBean>> container, double standard){
        if(data==null){
            return;
        }

        if(data.size()<2){
            container.add(data);
            return ;
        }

        Projection projection = mapboxMap.getProjection();
        List<PoiEntity.DataBean> list1 = new ArrayList<>();
        List<PoiEntity.DataBean> list2 = new ArrayList<>();
        PoiEntity.DataBean base = data.get(0);
        PointF pointF = projection.toScreenLocation(new LatLng(base.getY(),base.getX()));
        double a = distance(pointF.x,pointF.y);
        for (int i = 0; i < data.size(); i++) {
            if(i==0){
                list1.add(data.get(0));
            }else {
                PoiEntity.DataBean dataBean = data.get(i);
                PointF pointF1 = projection.toScreenLocation(new LatLng(dataBean.getY(),dataBean.getX()));
                double b = distance(pointF1.x,pointF1.y);
                if(Math.abs(a-b)<=standard){
                    list1.add(dataBean);
                }else {
                    list2.add(data.get(i));
                }
            }
        }

        if(list1.size()>3){
            List<PoiEntity.DataBean> list3 = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                list3.add(list1.get(i));
            }
            container.add(list3);
        }else {
            container.add(list1);
        }

        if(list2.size()==1){
            container.add(list2);
            return ;
        }else if(list2.size()==0){
            return ;
        }else {
            filterPoiData(mapboxMap,list2,container,standard);
        }
    }

    public static float dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (dpValue*scale+0.5f);
    }

    public static double distance(double lat,double lon){
        return Math.sqrt(lat*lat+lon*lon);
    }


    /**
     * 获取区域编码
     */
    public static Point[] obtainAreaCode(double left, double right, double top, double bottom,double zoom){
        int n;
        if(zoom<4){// 省
            n = 4;
        }else if(zoom<=5){//省
            n= 3;
        }else if(zoom<= 6){//省
            n = 2;
        }else if(zoom <= 7){//市
            n = 4;
        }else if(zoom<=9){//市
            n = 2;
        }else if(zoom<=10){//区
            n = 4;
        }else if(zoom<12){//区
            n = 3;
        }else if(zoom<=14){//区
            n = 2;
        }else {// 区
            n = 1;
        }
        List<Point> codes = new ArrayList<>();
        double x = (right-left)/n;
        double y = (bottom-top)/n;
        //第一个点的位置
        double x1 = left+x/2;
        double y1 = top+y/2;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                codes.add(new Point(x1+x*j,y1+y*i));
            }
        }
        codes.add(new Point(left,top));
        codes.add(new Point(left,bottom));
        codes.add(new Point(right,top));
        codes.add(new Point(right,bottom));
        Point[] areaCodes = new Point[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            areaCodes[i] = codes.get(i);
        }
        return areaCodes;
    }


}
