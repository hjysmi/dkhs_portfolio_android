/**
 * @Title TimeUtils.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-10 下午2:06:56
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.dkhs.portfolio.R;

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
        // TODO: use DateUtils methods instead
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
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.CHINA) };

    private static final SimpleDateFormat VALID_IFMODIFIEDSINCE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss Z", Locale.CHINA);
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.CHINA);

    public static Date parseISOTime(String iso8601str) {
        try {
            return DEFAULT_DATE_FORMAT.parse(iso8601str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getSimpleFormatTime(String iso8601str) {
        return ACCEPTED_TIMESTAMP_FORMATS[2].format(parseISOTime(iso8601str));
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

        // All attempts to parse have failed
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
