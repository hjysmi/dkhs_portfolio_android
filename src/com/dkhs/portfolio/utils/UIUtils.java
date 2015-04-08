package com.dkhs.portfolio.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dkhs.portfolio.BuildConfig;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.LoginActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

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

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /** 从给定的路径加载图片，并指定是否自动旋转方向 */
    public static Bitmap loadBitmap(Bitmap bm, String imgpath) {
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            double oris = exif.getAttributeDouble(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            String oriss = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        if (digree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(digree);
            Bitmap bms = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            if (null != bms) {
                bm = bms;
                bms = null;
            }
        }
        return bm;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }

            Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            drawCircleBorder(canvas, roundPx - 2,
                    PortfolioApplication.getInstance().getResources().getColor(R.color.round), roundPx, roundPx);
            return output;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("ResourceAsColor")
    private static void drawCircleBorder(Canvas canvas, float radius, int color, float hei, float wid) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(6);
        canvas.drawCircle(wid, hei, radius - 1, paint);
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length * 8 / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            if (options < 30) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static DisplayMetrics getDisplayMetrics() {

        // width = metric.widthPixels; // 屏幕宽度（像素）
        // height = metric.heightPixels; // 屏幕高度（像素）
        // density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        // densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

        return PortfolioApplication.getInstance().getResources().getDisplayMetrics();
    }

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 320f;// 这里设置高度为800f
        float ww = 320f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static boolean roundAble(StockQuotesBean stockQuotesBean) {
        if (!TextUtils.isEmpty(stockQuotesBean.getTrade_status())
                && !(stockQuotesBean.getTrade_status().equals("0") || stockQuotesBean.getTrade_status().equals("3"))) {
            return true;
        }
        return false;
    }

    public static boolean roundAble(int statu) {
        if ((statu == 0) || (statu == 3)) {
            return true;
        }
        return false;
    }

    public static String getValue(double volume) {
        String text = null;
        volume = volume / 100;
        try {
            if (volume < 10000) {
                text = new DecimalFormat("0.00").format(volume);
            } else if (volume > 10000 && volume < 100000000) {
                volume = volume / 10000;
                text = new DecimalFormat("0.00").format(volume) + "万";
            } else {
                volume = volume / 100000000;
                text = new DecimalFormat("0.00").format(volume) + "亿";
            }
            if (volume == 0) {
                text = "--";
            }
        } catch (Exception e) {

        }
        return text;
    }

    /**
     * 匹配K线图无网络时经线标题值
     * 
     * @param value
     * @return
     */
    public static String nongNet(String value) {
        if (value.equals("-1.00") || value.equals("-1")) {
            value = "—";
        } else if (Double.valueOf(value) < 0) {
            value = "0.00";
        }
        return value;
    }

    public static String getshou(double volume) {
        String text = null;
        volume = volume / 100;
        if (volume < 10000) {
            text = new DecimalFormat("0.00").format(volume) + "手";
        } else if (volume > 10000 && volume < 100000000) {
            volume = volume / 10000;
            text = new DecimalFormat("0.00").format(volume) + "万手";
        } else {
            volume = volume / 100000000;
            text = new DecimalFormat("0.00").format(volume) + "亿手";
        }
        return text;
    }

    public static int getTextWidth(String text, int textSize) {
        Paint p = new Paint();
        Rect rect = new Rect();
        p.setTextSize(textSize);
        p.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    public static boolean iStartLoginActivity(Context context) {
        // try {
        // UserEntity user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
        // if (user == null) {
        // // PortfolioApplication.getInstance().exitApp();
        // // Intent intent = new Intent(context, LoginActivity.class);
        //
        // context.startActivity(LoginActivity.loginActivityByAnnoy(context));
        // return true;
        // } else {
        // return false;
        // }
        // } catch (DbException e) {
        // e.printStackTrace();
        // // PortfolioApplication.getInstance().exitApp();
        // // Intent intent = new Intent(context, LoginActivity.class);
        // context.startActivity(LoginActivity.loginActivityByAnnoy(context));
        // return true;
        // }
        if (!PortfolioApplication.hasUserLogin()) {
            context.startActivity(LoginActivity.loginActivityByAnnoy(context));
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasSmartBar() {
        /*
         * try {
         * Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
         * 
         * return ((Boolean) method.invoke(null)).booleanValue();
         * } catch (Exception e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         */
        if (Build.DEVICE.equals("mx2") || Build.DEVICE.equals("mx3") || Build.DEVICE.equals("mx4pro")) {
            return true;
        }
        return false;
    }

    public static boolean isSymbleIndex(String type) {
        if (type.equals("5")) {
            return true;
        }
        return false;
    }

    public static void startAminationActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        setOverridePendingAmin(context);
    }

    public static void setOverridePendingAmin(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
    }

    public static void outAminationActivity(Activity context) {
        context.overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_to_right);

    }

    public static void isShow() {
        // if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (Build.VERSION.SDK_INT >= 19) {
        } // 透明状态栏

    }

    public static float dip2px(Context paramContext, float paramFloat) {
        return 0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density;
    }

    public static float px2dip(Context paramContext, float paramFloat) {
        return 0.5F + paramFloat / paramContext.getResources().getDisplayMetrics().density;
    }

}
