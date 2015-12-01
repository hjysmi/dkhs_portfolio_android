package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AutoScaleImageView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/6.
 */
public class AutoScaleImageView extends ImageView {
    public AutoScaleImageView(Context context) {
        super(context);
    }

    public AutoScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoScaleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private int mWidth;
    private int mHeight;

    @Override
    public void setImageBitmap(final Bitmap bm) {

        this.post(new Runnable() {
            @Override
            public void run() {
                if (mWidth == bm.getWidth() && mHeight == bm.getHeight()) {
                    setImageBitmap1(bm);
                } else {
                    mWidth = bm.getWidth();
                    mHeight = bm.getHeight();
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    ViewGroup.LayoutParams layoutParams = AutoScaleImageView.this.getLayoutParams();
                    int w = (int) (mWidth * metrics.density);
                    int h = (int) (mHeight * metrics.density);
                    int divisor = 1;
                    while(w/divisor > metrics.widthPixels){
                        divisor ++;
                    }
                    layoutParams.height = h/divisor;
                    layoutParams.width = w/divisor;
//                    layoutParams.height = (getWidth() - getPaddingLeft() - getPaddingRight()) * bm.getHeight() / bm.getWidth() + getPaddingTop() + getPaddingBottom();
                    setScaleType(ScaleType.FIT_CENTER);
                    AutoScaleImageView.this.requestFocus();

                    AutoScaleImageView.this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setImageBitmap1(bm);
                        }
                    }, 600);
                }
            }
        });






    }

    public void setImageBitmap1(Bitmap bm) {
        super.setImageBitmap(bm);
    }
}
