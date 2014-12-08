/**
 * @Title TimeUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-10 下午2:06:56
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;

/**
 * @ClassName TimeUtils
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-10 下午2:06:56
 * @version 1.0
 */
public class TimeUtils {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String getTimeAgo(long time, Context ctx) {

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = UIUtils.getCurrentTime(ctx);
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE) {
            return "刚刚";
        } else if (diff < 2 * MINUTE) {
            return "一分钟前";
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + "分钟前";
        } else if (diff < 90 * MINUTE) {
            return "一小时前";
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + "小时前";
        } else if (diff < 48 * HOUR) {
            return "昨天";
        } else {
            return diff / DAY + "天前";
        }
    }

    private static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.CHINA),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.CHINA),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.CHINA),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CHINA),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.CHINA),
            new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA) };

    private static final SimpleDateFormat VALID_IFMODIFIEDSINCE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss Z", Locale.CHINA);
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.CHINA);

    public static Date parseISOTime(String iso8601str) {
        try {
            return DEFAULT_DATE_FORMAT.parse(iso8601str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeString(String iso8601Time) {
        return new SimpleDateFormat("HH:mm", Locale.CHINA).format(toDate(iso8601Time));
    }

    public static String getMDTimeString(String iso8601Time) {
        return new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(toDate(iso8601Time));
    }

    public static String getHourString(String iso8601Time) {
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(toDate(iso8601Time));

    }

    public static boolean compareTime(Calendar old) {
        Calendar c = Calendar.getInstance();
        if (old.get(Calendar.YEAR) == c.get(Calendar.YEAR) && old.get(Calendar.MONTH) == c.get(Calendar.MONTH)
                && old.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public static String getTimeString(Calendar calendar) {
        return ACCEPTED_TIMESTAMP_FORMATS[7].format(calendar.getTime());
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

    public static String getTimeByMSecond(float second) {
        String time = new SimpleDateFormat("HH:mm").format(float2Date(second));
        return time;

    }

    public static java.util.Date float2Date(float second) {
        java.util.Date date_origine = new Date((long) (second * 1000));
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.setTime(date_origine);
        date.add(java.util.Calendar.HOUR, -8);
        return date.getTime();
    }

    public static String getSimpleFormatTime(String iso8601str) {
        return ACCEPTED_TIMESTAMP_FORMATS[2].format(toDate(iso8601str));
    }

    public static String getSimpleDay(String iso8601str) {
        return ACCEPTED_TIMESTAMP_FORMATS[7].format(toDate(iso8601str));
    }

    public static Date parseTimestamp(String timestamp) {
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(timestamp);
            } catch (ParseException ex) {
                continue;
            }
        }

        return null;
    }

    public static boolean isValidFormatForIfModifiedSinceHeader(String timestamp) {
        try {
            return VALID_IFMODIFIEDSINCE_FORMAT.parse(timestamp) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    public static long timestampToMillis(String timestamp, long defaultValue) {
        if (TextUtils.isEmpty(timestamp)) {
            return defaultValue;
        }
        Date d = parseTimestamp(timestamp);
        return d == null ? defaultValue : d.getTime();
    }

    /** Transform ISO 8601 string to Calendar. */
    public static Calendar toCalendar(final String iso8601string) {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");

            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA).parse(s);
            calendar.setTime(date);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return calendar;
    }
    public static Calendar toCalendarAddHour(final String iso8601string) {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");

            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA).parse(s);
            calendar.setTime(date);
            int k = calendar.get(Calendar.HOUR_OF_DAY) + 8;
            calendar.set(Calendar.HOUR_OF_DAY,k);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return calendar;
    }
    public static String addHour(final String iso8601string) {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(iso8601string);
            calendar.setTime(date);
           /* int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
            int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
            calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));*/
            int k = calendar.get(Calendar.HOUR_OF_DAY) + 8;
            calendar.set(Calendar.HOUR_OF_DAY,k);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            s = sdf.format(calendar.getTime());
        } catch (Exception e) {

            e.printStackTrace();
        }
        return s;
    }
    public static Calendar simpleDateToCalendar(final String simpleDate) {
        Calendar calendar = GregorianCalendar.getInstance();

        try {

            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(simpleDate);
            calendar.setTime(date);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return calendar;
    }

    public static Date toDate(final String iso8601string) {

        String s = iso8601string.replace("Z", "+00:00");
        Date date = null;
        try {
            s = s.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA).parse(s);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return date;
    }

    // public static Calendar simpleStringToCalend(String dateString) {
    // Calendar calendar = GregorianCalendar.getInstance();
    // Date date;
    // try {
    // date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(dateString);
    // calendar.setTime(date);
    // } catch (ParseException e) {
    //
    // e.printStackTrace();
    // }
    // return calendar;
    // }

    // public static String formatShortDate(Context context, Date date) {
    // StringBuilder sb = new StringBuilder();
    // Formatter formatter = new Formatter(sb);
    // return DateUtils.formatDateRange(context, formatter, date.getTime(), date.getTime(),
    // DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR, PrefUtils.getDisplayTimeZone(context).getID())
    // .toString();
    // }
    //
    // public static String formatShortTime(Context context, Date time) {
    // DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
    // TimeZone tz = PrefUtils.getDisplayTimeZone(context);
    // if (tz != null) {
    // format.setTimeZone(tz);
    // }
    // return format.format(time);
    // }
    //
    // public static boolean hasConferenceStarted(final Context context) {
    // long now = UIUtils.getCurrentTime(context);
    // return now >= Config.CONFERENCE_START_MILLIS;
    // }
    //
    // public static boolean hasConferenceEnded(final Context context) {
    // long now = UIUtils.getCurrentTime(context);
    // return now > Config.CONFERENCE_END_MILLIS;
    // }
    //
    // public static boolean isConferenceInProgress(final Context context) {
    // long now = UIUtils.getCurrentTime(context);
    // return now >= Config.CONFERENCE_START_MILLIS && now <= Config.CONFERENCE_END_MILLIS;
    // }
    //
    // /**
    // * Returns "Today", "Tomorrow", "Yesterday", or a short date format.
    // */
    // public static String formatHumanFriendlyShortDate(final Context context, long timestamp) {
    // long localTimestamp, localTime;
    // long now = UIUtils.getCurrentTime(context);
    //
    // TimeZone tz = PrefUtils.getDisplayTimeZone(context);
    // localTimestamp = timestamp + tz.getOffset(timestamp);
    // localTime = now + tz.getOffset(now);
    //
    // long dayOrd = localTimestamp / 86400000L;
    // long nowOrd = localTime / 86400000L;
    //
    // if (dayOrd == nowOrd) {
    // return context.getString(R.string.day_title_today);
    // } else if (dayOrd == nowOrd - 1) {
    // return context.getString(R.string.day_title_yesterday);
    // } else if (dayOrd == nowOrd + 1) {
    // return context.getString(R.string.day_title_tomorrow);
    // } else {
    // return formatShortDate(context, new Date(timestamp));
    // }
    // }
}
