package com.se.map.semapsdk;

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
}
