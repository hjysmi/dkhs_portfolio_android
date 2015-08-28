package com.dkhs.portfolio.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.dkhs.portfolio.base.widget.listener.SingleClickListener;

/**
 * @author zwm
 * @version 2.0
 * @ClassName Button
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class Button  extends AppCompatButton {


    public Button(Context context) {
        super(context);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new SingleClickListener(l));
    }

}
