/**
 * @Title TimeUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-10 下午2:06:56
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
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

        DateTime dateTime = new DateTime(time);
        LocalDate date = dateTime.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        if (date == currentDate) {
            return dateTime.toString("HH:mm");
        } else if (date.getYear() == currentDate.getYear()) {
            return dateTime.toString("MM-dd HH:mm");
        } else {
            return dateTime.toString(FORMAT_TEMPLATE_DAY_MM);
        }
    }

    private static final String FORMAT_TEMPLATE_BASE = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_TEMPLATE_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String FORMAT_TEMPLATE_DAY = "yyyy-MM-dd";
    private static final String FORMAT_TEMPLATE_DAY_MM = "yyyy-MM-dd HH:mm";
    private static final String TAG = "TimeUtils";


    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(FORMAT_TEMPLATE_ISO8601,
            Locale.CHINA);

    public static Date parseISOTime(String iso8601str) {
        return new DateTime(iso8601str).toDate();
    }

    public static String getTimeString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("HH:mm", Locale.CHINA);
//        return new SimpleDateFormat("HH:mm", Locale.CHINA).format(toDate(iso8601Time));
    }

    public static String getMMDDString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("MM-dd", Locale.CHINA);
//        return new SimpleDateFormat("HH:mm", Locale.CHINA).format(toDate(iso8601Time));
    }

    public static String getMDTimeString(String iso8601Time) {
        return new DateTime(iso8601Time).toString("MM-dd HH:mm:ss", Locale.CHINA);
//        return new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(toDate(iso8601Time));
    }

    public static String getDaySecondString(String iso8601Time) {
        return new DateTime(iso8601Time).toString(FORMAT_TEMPLATE_DAY_MM, Locale.CHINA);
//        return new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(toDate(iso8601Time));
    }


    public static String getSimpleDay(String iso8601str) {
        return new DateTime(iso8601str).toString(FORMAT_TEMPLATE_DAY);
    }


    public static String getHourString(String iso8601Time) {
//        if (TextUtils.isEmpty(iso8601Time)) {
//            return "";
//        }
//        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(toDate(iso8601Time));

        return new DateTime(iso8601Time).toString("HH:mm:ss", Locale.CHINA);

    }


    //比较是否是同一天的时间
    public static boolean isSameDay(String iso8601Time) {
        return DateTimeComparator.getDateOnlyInstance().compare(new DateTime(iso8601Time).toDate(), new DateTime().toDate()) == 0;
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

    public static java.util.Date float2Date(float second) {
        java.util.Date date_origine = new Date((long) (second * 1000));
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.setTime(date_origine);
        date.add(java.util.Calendar.HOUR, -8);
        return date.getTime();
    }


//    /**
//     * Transform ISO 8601 string to Calendar.
//     */
//    public static Calendar toCalendar(final String iso8601string) {
//        Calendar calendar = GregorianCalendar.getInstance();
//        String s = iso8601string.replace("Z", "+00:00");
//        try {
//            s = s.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
//
//            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA).parse(s);
//            calendar.setTime(date);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//        return calendar;
//    }

    public static Calendar getCalendar(final String simpleDate) {

        return new DateTime(simpleDate).toCalendar(Locale.CHINA);
    }


    public static String getUTCdatetimeAsString() {
        return new DateTime(DateTimeZone.UTC).toString(FORMAT_TEMPLATE_ISO8601, Locale.CHINA);
    }

}
