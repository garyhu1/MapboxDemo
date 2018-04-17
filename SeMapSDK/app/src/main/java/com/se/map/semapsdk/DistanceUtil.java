package com.se.map.semapsdk;

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

    public static void filterPoiData(List<PoiEntity.DataBean> data,List<List<PoiEntity.DataBean>> container){
        if(data==null){
            return;
        }

        if(data.size()<2){
            container.add(data);
            return ;
        }
        double standard = 0.005;
        List<PoiEntity.DataBean> list1 = new ArrayList<>();
        List<PoiEntity.DataBean> list2 = new ArrayList<>();
        PoiEntity.DataBean base = data.get(0);
        double a = distance(base.getX(),base.getY());
        for (int i = 0; i < data.size(); i++) {
            if(i==0){
                list1.add(data.get(0));
            }else {
                PoiEntity.DataBean dataBean = data.get(i);
                double b = distance(dataBean.getX(),dataBean.getY());
                if(Math.abs(a-b)<=standard){
                    list1.add(dataBean);
                }else {
                    list2.add(data.get(i));
                }
            }
        }

        container.add(list1);

        if(list2.size()==1){
            container.add(list2);
            return ;
        }else if(list2.size()==0){
            return ;
        }else {
            filterPoiData(list2,container);
        }
    }

    public static double distance(double lat,double lon){
        return Math.sqrt(lat*lat+lon*lon);
    }


}
