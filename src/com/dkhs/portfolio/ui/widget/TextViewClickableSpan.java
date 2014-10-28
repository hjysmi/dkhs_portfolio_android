/**
 * @Title TextViewClickableSpan.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-28 下午1:45:07
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @ClassName TextViewClickableSpan
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-28 下午1:45:07
 * @version 1.0
 */
public class TextViewClickableSpan extends ClickableSpan {

    int color = -1;
    private Context context;
    private Intent intent;

    public TextViewClickableSpan(int color, Context context, Intent intent) {
        this.color = color;
        this.context = context;
        this.intent = intent;
    }

    public void onClick(View widget) {
        context.startActivity(intent);
    };

    @Override
    public void updateDrawState(TextPaint ds) {
        if (color == -1) {
            ds.setColor(ds.linkColor);
        } else {
            ds.setColor(color);
        }
        ds.setUnderlineText(false);
    }

}
