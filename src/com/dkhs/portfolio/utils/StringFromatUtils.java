/**
 * @Title StringFromatUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午1:55:14
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.R.integer;

/**
 * @ClassName StringFromatUtils
 * @Description 字符格式化工具类
 * @author zjz
 * @date 2014-8-26 下午1:55:14
 * @version 1.0
 */
public class StringFromatUtils {
    public static String getPercentValue(int value) {
        return value + "%";
    }

    public static String getPercentValue(float value) {
        return String.format("%.2f", value) + "%";
    }

    public static String get4PointPercent(float value) {
        return String.format("%.4f", value) + "%";
    }

    public static String get2PointPercent(float value) {
        return String.format("%.2f", value) + "%";
    }

    public static String get2PointPercentPlus(float value) {
        if (value > 0) {
            return String.format("+%.2f", value) + "%";

        }
        return String.format("%.2f", value) + "%";
    }

    public static String get4Point(float value) {
        return String.format("%.4f", value);
    }

    public static String get2Point(float value) {
        return String.format("%.2f", value);
    }
    public static String get4String(String text) {
        return String.format("%4s", text);
    }

    public static String get2PointPlus(float value) {
        if (value > 0) {
            return String.format("+%.2f", value);

        } else {
            return String.format("%.2f", value);
        }
    }

    public static String convertToWan(int value) {
        if (value < 100000) {
            return String.valueOf(value);
        }
        return String.format("%.2f万", value / 10000f);
    }

    public static String convertToWanHand(int value) {
        if (value < 100000) {
            return String.valueOf(value) + "手";
        }else if(value<10000000){
            
            return String.format("%.2f万手", value / 10000f);
        }else{
            return String.format("%.2f千万手", value / 10000000);
        }
    }

}
