package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ScaleRelativeLayout
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */
public class ScaleRelativeLayout  extends RelativeLayout {


    public ScaleRelativeLayout(Context context) {
        super(context);
    }

    public ScaleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScaleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
