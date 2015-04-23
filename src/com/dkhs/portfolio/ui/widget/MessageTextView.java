package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author zwm
 * @version 1.0
 * @ClassName MessageTextView
 * @date 2015/4/20.16:53
 * @Description TODO(去除消息中的AutoLink 属性)
 */
public class MessageTextView extends TextView {
    public MessageTextView(Context context) {
        super(context);
    }

    public MessageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // public MessageTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    // super(context, attrs, defStyleAttr, defStyleRes);
    // }

    @Override
    public void setText(CharSequence text, BufferType type) {

        setAutoLinkMask(0);
        super.setText(text, type);
    }

}
