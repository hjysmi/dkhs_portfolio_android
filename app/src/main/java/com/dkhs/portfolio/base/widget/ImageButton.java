package com.dkhs.portfolio.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.dkhs.portfolio.base.widget.listener.SingleClickListener;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ImageButton
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class ImageButton  extends android.widget.ImageButton{
    public ImageButton(Context context) {
        super(context);
    }

    public ImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new SingleClickListener(l));
    }
}
