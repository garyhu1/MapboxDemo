package com.se.map.semapsdk.model;

/**
 * Author: Administrator;
 * Since 2018/4/16;
 * Description:
 */

public class CityCodeEntity {


    /**
     * state : 1
     * provincename : 上海市
     * provincecode : 310000
     * cityname : 上海市
     * citycode : 310000
     * areaname : 普陀区
     * areacode : 310107
     */

    private String state;
    private String provincename;
    private String provincecode;
    private String cityname;
    private String citycode;
    private String areaname;
    private String areacode;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }
}
