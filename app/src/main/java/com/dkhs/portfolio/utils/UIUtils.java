package com.dkhs.portfolio.utils;

import android.annotation.SuppressLint;
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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dkhs.portfolio.BuildConfig;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.LoginActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * @author zjz
 * @version 1.0
 * @ClassName UiUtils
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-25 下午3:43:24
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
        listView.requestLayout();
    }


    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            //利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            //利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
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

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
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
        Matrix m = new Matrix();
        if (digree != 0) {
            // 旋转图片
            m.postRotate(digree);
            Bitmap bms = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            bm.recycle();

            return bms;
        }
        return bm;
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
        return BitmapFactory.decodeStream(isBm, null, null);
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
        return !TextUtils.isEmpty(stockQuotesBean.getTrade_status())
                && !(stockQuotesBean.getTrade_status().equals("0") || stockQuotesBean.getTrade_status().equals("3"));
    }

    public static boolean roundAble(int statu) {
        return (statu == 0) || (statu == 3);
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
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
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
        // // Intent intent = new Intent(mContext, LoginActivity.class);
        //
        // mContext.startActivity(LoginActivity.loginActivityByAnnoy(mContext));
        // return true;
        // } else {
        // return false;
        // }
        // } catch (DbException e) {
        // e.printStackTrace();
        // // PortfolioApplication.getInstance().exitApp();
        // // Intent intent = new Intent(mContext, LoginActivity.class);
        // mContext.startActivity(LoginActivity.loginActivityByAnnoy(mContext));
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
        return Build.DEVICE.equals("mx2") || Build.DEVICE.equals("mx3") || Build.DEVICE.equals("mx4pro");
    }

    public static void startAnimationActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        // mContext.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        setOverridePendingAnin(context);
    }

    public static void startAnimationActivityForResult(Activity context, Intent intent, int requestCode) {
        context.startActivityForResult(intent, requestCode);
        // mContext.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
        setOverridePendingAnin(context);
    }

    public static void setOverridePendingAnin(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_in_from_right, R.anim.activity_out_to_left);
    }

    public static void setOverridePendingSlideFormBottomAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_in_from_bottom, R.anim.activity_out_to_left);
    }

    public static void outAnimationActivity(Activity context) {
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

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, PortfolioApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static float px2dip(Context paramContext, float paramFloat) {
        return 0.5F + paramFloat / paramContext.getResources().getDisplayMetrics().density;
    }

    public static Bitmap getLocaleimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int originWidth = newOpts.outWidth;
        int originHeight = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float maxHeight = 1600f;// 这里设置高度为800f
        float maxWidth = 800f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
//        if (originWidth > originHeight && originWidth > maxWidth) {// 如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / maxWidth);
//        } else if (originWidth < originHeight && originHeight > maxHeight) {// 如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / maxHeight);
//        }
        if (originWidth > maxWidth) {
            be = (int) (newOpts.outWidth / maxWidth);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap == null?null : compress2mImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compress2mImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int options = 90;
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int streamSize = baos.size(); //byte size
        int maxSize = 500 * 128;  //bit size convert to byte size 1024/8
        while (streamSize > maxSize) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩

            options -= 10;// 每次都减少10
            if (options < 30) {
                break;
            }
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            streamSize = baos.size();
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        image.recycle();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            baos = null;
        }
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    public static String getResString(Context context,int resStr){
        return context.getResources().getString(resStr);
    }

    public static int getResColor(Context context,int resColor){
        return context.getResources().getColor(resColor);
    }
    public static float getDimen(Context context,int dimen){
        return context.getResources().getDimension(dimen);
    }

}
