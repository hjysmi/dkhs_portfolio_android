/**
 * @Title UiUtils.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:43:24
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import java.util.Calendar;
import java.util.TimeZone;

import com.dkhs.portfolio.BuildConfig;
import com.dkhs.portfolio.app.PortfolioApplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @ClassName UiUtils
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:43:24
 * @version 1.0
 */
public class UIUtils {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // public static boolean isSameDayDisplay(long time1, long time2, Context context) {
    // TimeZone displayTimeZone = PrefUtils.getDisplayTimeZone(context);
    // Calendar cal1 = Calendar.getInstance(displayTimeZone);
    // Calendar cal2 = Calendar.getInstance(displayTimeZone);
    // cal1.setTimeInMillis(time1);
    // cal2.setTimeInMillis(time2);
    // return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    // && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    // }

    private static final long sAppLoadTime = System.currentTimeMillis();

    public static long getCurrentTime(final Context context) {
        if (BuildConfig.DEBUG) {
            return context.getSharedPreferences("mock_data", Context.MODE_PRIVATE).getLong("mock_current_time",
                    System.currentTimeMillis())
                    + System.currentTimeMillis() - sAppLoadTime;
            // return ParserUtils.parseTime("2012-06-27T09:44:45.000-07:00")
            // + System.currentTimeMillis() - sAppLoadTime;
        } else {
            return System.currentTimeMillis();
        }
    }

}
