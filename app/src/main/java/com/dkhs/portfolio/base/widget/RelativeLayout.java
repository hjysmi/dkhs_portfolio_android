package com.dkhs.portfolio.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;

import com.dkhs.portfolio.base.widget.listener.SingleClickListener;

/**
 * @author zwm
 * @version 2.0
 * @ClassName RelativeLayout
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class RelativeLayout  extends android.widget.RelativeLayout {
    public RelativeLayout(Context context) {
        super(context);
    }

    public RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new SingleClickListener(l));
    }


}
