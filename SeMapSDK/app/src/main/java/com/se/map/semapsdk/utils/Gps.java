package com.se.map.semapsdk.utils;

/**
 * Author: Administrator;
 * Since 2018/4/29;
 * Description:
 */

public class Gps {

    public double wgLat;
    public double wgLon;


    public Gps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }


    public double getWgLat() {
        return wgLat;
    }


    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }


    public double getWgLon() {
        return wgLon;
    }


    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }


    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }
}
