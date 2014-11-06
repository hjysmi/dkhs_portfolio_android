/**
 * @Title StockUitls.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 上午10:15:50
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

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
        return symbol.startsWith("SH9");
    }
}
