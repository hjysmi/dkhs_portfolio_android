/**
 * @Title TimeUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-10 下午2:06:56
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TimeUtils
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-10 下午2:06:56
 */
public class TimeUtils {
//    private static final int SECOND = 1000;
//    private static final int MINUTE = 60 * SECOND;
//    private static final int HOUR = 60 * MINUTE;
//    private static final int DAY = 24 * HOUR;
//
//    public static String getTimeAgo(long time, Context ctx) {
//
//        if (time < 1000000000000L) {
//            // if timestamp given in seconds, convert to millis
//            time *= 1000;
//        }
//
//        long now = UIUtils.getCurrentTime(ctx);
//        if (time > now || time <= 0) {
//            return null;
//        }
//
//        final long diff = now - time;
//        if (diff < MINUTE) {
//            return "刚刚";
//        } else if (diff < 2 * MINUTE) {
//            return "一分钟前";
//        } else if (diff < 50 * MINUTE) {
//            return diff / MINUTE + "分钟前";
//        } else if (diff < 90 * MINUTE) {
//            return "一小时前";
//        } else if (diff < 24 * HOUR) {
//            return diff / HOUR + "小时前";
//        } else if (diff < 48 * HOUR) {
//            return "昨天";
//        } else {
//            return diff / DAY + "天前";
//        }
//    }


    public static String getBriefTimeString(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        return getBriefTimeString(new DateTime(time));
    }


    public static String getBriefTimeString(long second) {
        return getBriefTimeString(new DateTime(second * 1000));
    }

    public static String getBriefTimeString(DateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        if (Days.daysBetween(date, currentDate).getDays() == 0) {
            return dateTime.toString("HH:mm");
        } else {
            return dateTime.toString("MM-dd HH:mm");
        }
//        else if (date.getYear() == currentDate.getYear()) {
//            return dateTime.toString("MM-dd HH:mm");
//        } else {
//            return dateTime.toString(FORMAT_TEMPLATE_DAY_MM);
//        }
    }

    public static final String FORMAT_TEMPLATE_BASE = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TEMPLATE_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String FORMAT_TEMPLATE_DAY = "yyyy-MM-dd";
    public static final String FORMAT_TEMPLATE_DAY_MM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_TEMPLATE_DAY_MM_WITHOUT_YEAR = "MM-dd HH:mm";
    public static final String TAG = "TimeUtils";


    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(FORMAT_TEMPLATE_ISO8601,
            Locale.CHINA);

    public static Date parseISOTime(String iso8601str) {
        return new DateTime(iso8601str).toDate();
    }

    public static String getTimeString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("HH:mm", Locale.CHINA);
    }

    public static String getMMDDString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("MM-dd", Locale.CHINA);
    }

    public static String getMDTimeString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("MM-dd HH:mm:ss", Locale.CHINA);
    }

    public static String getDaySecondString(String iso8601Time) {
        return new DateTime(iso8601Time).toString(FORMAT_TEMPLATE_DAY_MM, Locale.CHINA);
    }

    public static String getDaySecondWITHOUTYEARString(String iso8601Time) {
        return new DateTime(iso8601Time).toString(FORMAT_TEMPLATE_DAY_MM_WITHOUT_YEAR, Locale.CHINA);
    }

    public static String getSimpleDay(String iso8601str) {
        return new DateTime(iso8601str).toString(FORMAT_TEMPLATE_DAY, Locale.CHINA);
    }


    public static String getHourString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("HH:mm:ss", Locale.CHINA);

    }

    //比较是否是同一天的时间
    public static boolean isSameDay(String iso8601Time) {
        return compareDateTime(new DateTime(iso8601Time), new DateTime()) == 0;
//        return DateTimeComparator.getDateOnlyInstance().compare(new DateTime(iso8601Time).toDate(), new DateTime().toDate()) == 0;
    }

    public static int compareDateTime(DateTime date1, DateTime date2) {
        return DateTimeComparator.getDateOnlyInstance().compare(date1.toDate(), date2.toDate());
    }


    public static String getTimeString(Calendar calendar) {
        return new SimpleDateFormat(FORMAT_TEMPLATE_DAY).format(calendar.getTime());
    }

    public static String getDateString(String iso8601Time) {
        String dateString = "";
        Date date = parseISOTime(iso8601Time);
        dateString = new SimpleDateFormat("yyyy年MM月dd日  ", Locale.CHINA).format(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String weekString = new SimpleDateFormat("EEEE").format(c.getTime());
        if ("1".equals(weekString)) {
            weekString = "  星期日";
        } else if ("2".equals(weekString)) {
            weekString = "  星期一";
        } else if ("3".equals(weekString)) {
            weekString = "  星期二";
        } else if ("4".equals(weekString)) {
            weekString = "  星期三";
        } else if ("5".equals(weekString)) {
            weekString = "  星期四";
        } else if ("6".equals(weekString)) {
            weekString = "  星期五";
        } else if ("7".equals(weekString)) {
            weekString = "  星期六";
        }
        dateString += weekString;
        return dateString;
    }


    public static Calendar getCalendar(final String simpleDate) {

        return new DateTime(simpleDate).toCalendar(Locale.CHINA);
    }


    public static String getUTCdatetimeAsString() {
        return new DateTime(DateTimeZone.UTC).toString(FORMAT_TEMPLATE_ISO8601, Locale.CHINA);
    }


    public static boolean isEnableImageTime(long utime) {
        LocalDate date = new DateTime(utime * 1000).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        if (Hours.hoursBetween(date, currentDate).getHours() > 8) {
            return false;
        }
        return true;
    }


}
