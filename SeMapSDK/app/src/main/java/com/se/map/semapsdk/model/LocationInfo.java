package com.se.map.semapsdk.model;

/**
 * Author: Administrator;
 * Since 2018/5/9;
 * Description:
 */

public class LocationInfo {

    private double x; // 经度
    private double y; // 纬度
    private String name; // poi 名称
    private String code; // 区域编码
    private String poi_id; // poi id
    private String source_province;
    private String source_city;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(String poi_id) {
        this.poi_id = poi_id;
    }

    public String getSource_province() {
        return source_province;
    }

    public void setSource_province(String source_province) {
        this.source_province = source_province;
    }

    public String getSource_city() {
        return source_city;
    }

    public void setSource_city(String source_city) {
        this.source_city = source_city;
    }
}
