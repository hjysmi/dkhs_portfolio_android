package com.dkhs.portfolio.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AdapterView;

import com.dkhs.portfolio.base.widget.listener.SingleItemClickListener;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ListView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class ListView extends android.widget.ListView {
    public ListView(Context context) {
        super(context);
    }

    public ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(new SingleItemClickListener(listener));
    }
}
