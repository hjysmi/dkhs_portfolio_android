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

import com.dkhs.portfolio.bean.StockQuotesBean;

/**
 * @ClassName StockUitls
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 上午10:15:50
 * @version 1.0
 */
public class StockUitls {

    public static boolean isShangZhengB(String symbol) {
        if (TextUtils.isEmpty(symbol)) {
            return false;
        }
        return symbol.startsWith("SH9");
    }
    
    
    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    public static final String SYMBOLTYPE_OTHER="0";
    public static final String SYMBOLTYPE_STOCK="1";
    public static final String SYMBOLTYPE_BOND="2";
    public static final String SYMBOLTYPE_FUND="3";
    public static final String SYMBOLTYPE_WARRANT ="4";
    public static final String SYMBOLTYPE_INDEX="5";
    public static final String SYMBOLTYPE_JHLC="6";
    public static final String SYMBOLTYPE_FUTURES="9";
    public static final String SYMBOLTYPE_OPTION ="10";
    
    
}