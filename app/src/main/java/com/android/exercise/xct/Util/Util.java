package com.android.exercise.xct.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by xct on 2018/5/19.
 */

public class Util {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    //Todo  关于工具类的总结使用

    public Util(){
        //装换成字符串时间
        mFormatBuilder=new StringBuilder();
        mFormatter=new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * 把毫秒转换成 1:20:30形式
     */
    public  String stringForTime(int timeMs){
        int totalSeconds=timeMs / 1000;
        int seconds=totalSeconds % 60;

            int minutes=(totalSeconds/60)% 60;
            int hours= totalSeconds/3600;
            mFormatBuilder.setLength(0);
            if(hours>0){
                return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else{
                return mFormatter.format("%02d:%02d",minutes,seconds).toString();
            }
    }
    //得到系统时间
    public  String getSystemTime(){
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }


}
