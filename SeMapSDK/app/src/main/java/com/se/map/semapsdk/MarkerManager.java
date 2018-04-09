package com.se.map.semapsdk;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class MarkerManager {

    public static void filterMarker(List<Point> points,List<List<Point>> pps){
        if(points==null||pps==null){
            return ;
        }
        if(points.size()<2){
            pps.add(points);
            return ;
        }
        List<Point> list1 = new ArrayList<>();
        List<Point> list2 = new ArrayList<>();
        Point p = points.get(0);
        for (int i = 0; i < points.size(); i++) {
            if(i == 0){
                list1.add(points.get(i));
            }else {
                if(p.x-points.get(i).x<0.001){
                    list1.add(points.get(i));
                }else {
                    list2.add(points.get(i));
                }
            }
        }
        pps.add(list1);
        if(list2.size()>0){
            filterMarker(list2,pps);
        }
    }
}
