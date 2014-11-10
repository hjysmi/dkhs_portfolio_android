/**
 * @Title UiUtils.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:43:24
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dkhs.portfolio.BuildConfig;

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
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
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
			
			Bitmap output = Bitmap.createBitmap(width,
			                height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
			final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);
			
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);
			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
}
    public static Bitmap compressImage(Bitmap image) {  
  	  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length * 8 / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    } 
    public static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    } 
}
