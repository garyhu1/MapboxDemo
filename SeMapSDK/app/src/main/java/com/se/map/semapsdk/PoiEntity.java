package com.se.map.semapsdk;

import java.util.List;

/**
 * Author: Administrator;
 * Since 2018/4/11;
 * Description:
 */

public class PoiEntity {

    /**
     * msg :
     * code : 1
     * cose : 100
     * data : [{"mid":"D1WESZ245695RR","img":"http://botbrain.ai/adad.png","x":68.9873423,"y":123.9873423,"position_id":"2123dwwse2323"},{"mid":"DW3ESZ245695RR","img":"http://botbrain.ai/1adad.png","x":69.9873423,"y":133.9873423,"position_id":"3123dwwse2323"},{"mid":"DWE4SZ245695RR","img":"http://botbrain.ai/2adad.png","x":72.9873423,"y":163.9873423,"position_id":"4123dwwse2323"},{"mid":"DWES5Z245695RR","img":"http://botbrain.ai/3adad.png","x":73.9873423,"y":137.9873423,"position_id":"5123dwwse2323"},{"mid":"DWESZ2465695RR","img":"http://botbrain.ai/4adad.png","x":63.9873423,"y":135.9873423,"position_id":"6123dwwse2323"},{"mid":"DWESZ2457695RR","img":"http://botbrain.ai/a5dad.png","x":69.9873423,"y":139.9873423,"position_id":"123dwwse2323"}]
     */

    private String msg;
    private int code;
    private int cose;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCose() {
        return cose;
    }

    public void setCose(int cose) {
        this.cose = cose;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mid : D1WESZ245695RR
         * img : http://botbrain.ai/adad.png
         * x : 68.9873423
         * y : 123.9873423
         * position_id : 2123dwwse2323
         */

        private String mid;
        private String img;
        private double x;
        private double y;
        private String position_id;

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

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

        public String getPosition_id() {
            return position_id;
        }

        public void setPosition_id(String position_id) {
            this.position_id = position_id;
        }
    }
}
