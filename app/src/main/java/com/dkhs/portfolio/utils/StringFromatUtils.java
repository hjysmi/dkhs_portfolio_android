/**
 * @Title StringFromatUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午1:55:14
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.common.Spanny;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StringFromatUtils
 * @Description 字符格式化工具类
 * @date 2014-8-26 下午1:55:14
 */
public class StringFromatUtils {

    public final static String regexUsername = "[^\\-_A-Za-z0-9\\u4e00-\\u9fa5]";

    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    public static boolean isContainsEmoji(String text) {
        for (char curr : text.toCharArray()) {
            if (isEmojiCharacter(curr)) {
                return true;
            }
        }
        return false;
    }

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
            return bytes.length != str.length();
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

    public static String get4PointPlus(float value) {
        if (value > 0) {
            return String.format("+%.4f", value);

        } else {
            return String.format("%.4f", value);
        }
    }

    public static String convertToWan(float value) {
        // value = value / 100;
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

    /**
     * 处理 数字
     *
     * @param count
     * @return
     */
    public static String handleNumber(int count) {

        String countStr;

        if (count > 1000) {
            DecimalFormat df2 = new DecimalFormat("#.#");
            countStr = df2.format(count / 1000.0) + "k";
        } else {
            countStr = count + "";
        }
        return countStr;
    }

    public static String convertToWanHand(float value) {

        return convertToWan(value / 100);
    }

    public static String dateFormat(long dateLong) {

        String dateFormatStr = "";

        if (dateLong > 0) {
            Date date = new Date(dateLong);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormatStr = simpleDateFormat.format(date);
        }

        return dateFormatStr;
    }

    public static CharSequence getPercentSpan(String value) {
        if (!TextUtils.isEmpty(value)) {
            return new Spanny().append(StringFromatUtils.get2PointPercent(Float.valueOf(value)), new ForegroundColorSpan(ColorTemplate.getPercentColor(value)));
        } else {
            return new Spanny().append("--", new ForegroundColorSpan(ColorTemplate.DEF_GRAY));
        }

    }

    public static String getDiscount(float fareRatio,float discount,Context context){
        if(fareRatio == 0) {
            return context.getString(R.string.zero_rate);
        }else if(discount == 1){
            return String.format("%.2f", fareRatio) + "%";
        }else{
            String discountStr = new DecimalFormat("0.00").format(discount * 10);
            discountStr = removeZeroAfterDot(discountStr);
            return String.
                    format(context.getString(R.string.fund_discount_format),discountStr);
        }
    }

    /**
     * 删除小数点后的零
     * @param discountStr
     * @return
     */
    public static String removeZeroAfterDot(String discountStr){
        if(discountStr.indexOf(".") > 0){
            //正则表达
            discountStr = discountStr.replaceAll("0+?$", "");//去掉后面无用的零
            discountStr = discountStr.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return discountStr;
    }

    public static String convert2Wan(double value){
            double  wan = 10000d;
            if (value < wan) {
                return String.valueOf((int) value);
            } else if (value < 100 * wan) {
                return new DecimalFormat("0万").format(value / (wan));
            }
        return "--";
    }


}
