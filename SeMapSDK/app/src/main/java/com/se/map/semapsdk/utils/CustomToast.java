package com.se.map.semapsdk.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/12.
 * 处理Toast的复用性
 */
public class CustomToast {

    private static long firstTime;//第一次点击的时间
    private static long secondTime;//第二次点击的时间
    private static String oldMsg = "";//显示后的文本信息
    private static Toast toast;

    /**
     * 短时间显示
     * @param context 传入的上下文
     * @param msg 显示的内容
     */
    public static void showShortToast(Context context, String msg){
        if(toast==null){
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            firstTime = System.currentTimeMillis();
        }else{
            secondTime = System.currentTimeMillis();
            if(oldMsg.equals(msg)){
                if(secondTime-firstTime > 2000){
                    toast.setText(msg);
                    toast.show();
                    firstTime = secondTime;
                }
            }else {
                toast.setText(msg);
                toast.show();
                firstTime = secondTime;
            }
        }
        oldMsg = msg;
    }


    /**
     * 长时间显示
     * @param context 传入的上下文
     * @param msg 显示的内容
     */
    public static void showLongToast(Context context, String msg){
        if(toast==null){
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();
            firstTime = System.currentTimeMillis();
        }else{
            secondTime = System.currentTimeMillis();
            if(oldMsg.equals(msg)){
                if(secondTime-firstTime > 3500){
                    toast.setText(msg);
                    toast.show();
                    firstTime = secondTime;
                }
            }else {
                toast.setText(msg);
                toast.show();
                firstTime = secondTime;
            }
        }
        oldMsg = msg;
    }

    /**
     * 长时间显示
     * @param context 传入的上下文
     * @param res 显示的内容的Id
     */
    public static void showLongToast(Context context, int res){
        showLongToast(context,context.getString(res));
    }

    /**
     * 长时间显示
     * @param context 传入的上下文
     * @param res 显示的内容的id
     */
    public static void showShortToast(Context context, int res){
        showShortToast(context,context.getString(res));
    }
}
