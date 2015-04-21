package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;

/**
 * @author zwm
 * @version 1.0
 * @ClassName MessageTextView
 * @date 2015/4/20.16:53
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class MessageTextView  extends TextView {
    public MessageTextView(Context context) {
        super(context);
    }

    public MessageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MessageTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

            setAutoLinkMask(0);
            super.setText(text, type);
    }







}
