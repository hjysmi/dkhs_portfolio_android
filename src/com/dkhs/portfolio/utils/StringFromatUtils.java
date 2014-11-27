/**
 * @Title StringFromatUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午1:55:14
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import android.R.integer;

/**
 * @ClassName StringFromatUtils
 * @Description 字符格式化工具类
 * @author zjz
 * @date 2014-8-26 下午1:55:14
 * @version 1.0
 */
public class StringFromatUtils {

    public final static String regexUsername = "[^\\-_A-Za-z0-9\\u4e00-\\u9fa5]";

    /**
     * 判断字体有多少长度，中文占2个字符
     */
    public static int getStringRealLength(String str) {
        String strTemp = "";
        try {
            strTemp = new String(str.getBytes("GBK"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTemp.length();
    }

    /**
     * 判断是否是中文字符
     */
    public static boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (bytes.length == str.length()) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

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

    public static String get3PointPercent(float value) {
        return String.format("%.3f", value) + "%";
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

    public static String get3Point(float value) {
        return String.format("%.3f", value);
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

    public static String get3PointPlus(float value) {
        if (value > 0) {
            return String.format("+%.3f", value);

        } else {
            return String.format("%.3f", value);
        }
    }

    public static String convertToWan(float value) {
//        value = value / 100;
        float wan = 10000f;
        float yi = wan * wan;
        if (value < wan) {
            return String.valueOf((int) value);
        } else if (value < 100 * wan) {
            return new DecimalFormat("0.00万").format(value / (wan));
        } else if (value < 1000 * wan) {
            return new DecimalFormat("0.0万").format(value / (wan));
        } else if (value < 1 * yi) {
            return new DecimalFormat("0万").format(value / (wan));
        } else if (value < 100 * yi) {
            return new DecimalFormat("0.00亿").format(value / (1 * yi));

        } else if (value < 1000 * yi) {
            return new DecimalFormat("0.0亿").format(value / (1 * yi));

        } else if (value < wan * yi) {
            return new DecimalFormat("0亿").format(value / (1 * yi));

        } else if (value < 100 * wan * yi) {
            return new DecimalFormat("0.00万亿").format(value / (wan * yi));
        }
        return "$$";

    }

    public static String convertToWanHand(int value) {
        // if (value < 100000) {
        // return String.valueOf(value) + "";
        // } else if (value < 10000000) {
        //
        // return String.format("%.2f万", value / 10000f);
        // } else {
        // return String.format("%.2f千万", value / 10000000f);
        // }
        return convertToWan(value/100);
    }

}
