/**
 * @Title StringFromatUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午1:55:14
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

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

    public static String get4Point(float value) {
        return String.format("%.4f", value);
    }

    public static String get2Point(float value) {
        return String.format("%.2f", value);
    }

}