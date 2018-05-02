package com.se.map.semapsdk.utils;

import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Author: Administrator;
 * Since 2018/4/29;
 * Description:
 */

public class MyLocationUtils {
    public static double[] standardToChina(Double x, Double y) {

        try {
            double[] haha = new double[2];
            PointDouble pointDouble = new PointDouble(x, y);
            PointDouble s2c = ModifyOffset.getInstance(
                    ModifyOffset.class.getResourceAsStream("axisoffset.dat"))
                    .s2c(pointDouble);
            haha[0] = s2c.x;
            haha[1] = s2c.y;
            return haha;
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return null;

    }

    private static double pi = 3.14159265358979324;
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }


    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 地球坐标转换为火星坐标 World Geodetic System ==> Mars Geodetic System
     *
     * @param wgLat
     *            地球坐标
     * @param wgLon
     *
     *            mglat,mglon 火星坐标
     */
    public static Gps transform2Mars(double wgLat, double wgLon) {
        double mgLat, mgLon;
        if (outOfChina(wgLat, wgLon)) {
            mgLat = wgLat;
            mgLon = wgLon;
            return new Gps(mgLat, mgLon);
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        mgLat = wgLat + dLat;
        mgLon = wgLon + dLon;
        return new Gps(mgLat, mgLon);
    }

    static class ModifyOffset {
        private static ModifyOffset modifyOffset;
        static double[] X = new double[660 * 450];
        static double[] Y = new double[660 * 450];

        private ModifyOffset(InputStream inputStream) throws Exception {
            init(inputStream);
        }

        public synchronized static ModifyOffset getInstance(InputStream is)
                throws Exception {
            if ( modifyOffset == null ) {
                modifyOffset = new ModifyOffset(is);
            }
            return modifyOffset;
        }

        public void init(InputStream inputStream) throws Exception {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            try {
                int i = 0;
                while ( in.available() > 0 ) {
                    if ( (i & 1) == 1 ) {
                        Y[(i - 1) >> 1] = in.readInt() / 100000.0d;
                        ;
                    } else {
                        X[i >> 1] = in.readInt() / 100000.0d;
                        ;
                    }
                    i++;
                }
            } finally {
                if ( in != null )
                    in.close();
            }
        }

        // standard -> china
        public static PointDouble s2c(PointDouble pt) {
            int cnt = 10;
            double x = pt.x, y = pt.y;
            while ( cnt-- > 0 ) {
                if ( x < 71.9989d || x > 137.8998d || y < 9.9997d || y > 54.8996d )
                    return pt;
                int ix = (int) (10.0d * (x - 72.0d));
                int iy = (int) (10.0d * (y - 10.0d));
                double dx = (x - 72.0d - 0.1d * ix) * 10.0d;
                double dy = (y - 10.0d - 0.1d * iy) * 10.0d;
                x = (x + pt.x + (1.0d - dx) * (1.0d - dy) * X[ix + 660 * iy] + dx
                        * (1.0d - dy) * X[ix + 660 * iy + 1] + dx * dy
                        * X[ix + 660 * iy + 661] + (1.0d - dx) * dy
                        * X[ix + 660 * iy + 660] - x) / 2.0d;
                y = (y + pt.y + (1.0d - dx) * (1.0d - dy) * Y[ix + 660 * iy] + dx
                        * (1.0d - dy) * Y[ix + 660 * iy + 1] + dx * dy
                        * Y[ix + 660 * iy + 661] + (1.0d - dx) * dy
                        * Y[ix + 660 * iy + 660] - y) / 2.0d;
            }
            return new PointDouble(x, y);
        }

        // china -> standard
        public PointDouble c2s(PointDouble pt) {
            int cnt = 10;
            double x = pt.x, y = pt.y;
            while ( cnt-- > 0 ) {
                if ( x < 71.9989d || x > 137.8998d || y < 9.9997d || y > 54.8996d )
                    return pt;
                int ix = (int) (10.0d * (x - 72.0d));
                int iy = (int) (10.0d * (y - 10.0d));
                double dx = (x - 72.0d - 0.1d * ix) * 10.0d;
                double dy = (y - 10.0d - 0.1d * iy) * 10.0d;
                x = (x + pt.x - (1.0d - dx) * (1.0d - dy) * X[ix + 660 * iy] - dx
                        * (1.0d - dy) * X[ix + 660 * iy + 1] - dx * dy
                        * X[ix + 660 * iy + 661] - (1.0d - dx) * dy
                        * X[ix + 660 * iy + 660] + x) / 2.0d;
                y = (y + pt.y - (1.0d - dx) * (1.0d - dy) * Y[ix + 660 * iy] - dx
                        * (1.0d - dy) * Y[ix + 660 * iy + 1] - dx * dy
                        * Y[ix + 660 * iy + 661] - (1.0d - dx) * dy
                        * Y[ix + 660 * iy + 660] + y) / 2.0d;
            }
            return new PointDouble(x, y);
        }


    }

    public static class PointDouble {
        double x, y;

        public PointDouble(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return "x=" + x + ", y=" + y;
        }
    }
}
