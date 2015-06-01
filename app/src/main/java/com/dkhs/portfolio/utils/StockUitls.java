/**
 * @Title StockUitls.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 上午10:15:50
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.text.TextUtils;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StockUitls
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-6 上午10:15:50
 */
public class StockUitls {
    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    public static final String SYMBOLTYPE_OTHER = "0";
    public static final String SYMBOLTYPE_STOCK = "1";
    public static final String SYMBOLTYPE_BOND = "2";
    public static final String SYMBOLTYPE_FUND = "3";
    public static final String SYMBOLTYPE_WARRANT = "4";
    public static final String SYMBOLTYPE_INDEX = "5";
    public static final String SYMBOLTYPE_JHLC = "6";
    public static final String SYMBOLTYPE_FUTURES = "9";
    public static final String SYMBOLTYPE_OPTION = "10";

    //理财型
    public static final int STYPE_EF = 306;
    //债券型
    public static final int STYPE_MBS = 307;

    public static boolean isShangZhengB(String symbol) {
        if (TextUtils.isEmpty(symbol)) {
            return false;
        }
        return symbol.startsWith("SH9");
    }

    public static boolean isIndexStock(String symbolType) {
        if (TextUtils.isEmpty(symbolType)) {
            return false;
        }
        return symbolType.equalsIgnoreCase(SYMBOLTYPE_INDEX);
    }

    public static boolean isDelistStock(String status) {
        if (TextUtils.isEmpty(status)) {
            return false;
        }
        if (status.equalsIgnoreCase("2") || status.equalsIgnoreCase("3")) {
            return true;
        }
        return false;
    }

    public static boolean isNewStock(String status) {
        if (TextUtils.isEmpty(status)) {
            return false;
        }
        if (status.equalsIgnoreCase("7")) {
            return true;
        }
        return false;
    }


    public static boolean isSepFund(int stype) {
        if (stype == STYPE_EF || stype == STYPE_MBS) {
            return true;
        }
        return false;
    }

}
