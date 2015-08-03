package com.dkhs.portfolio.ui.widget;


import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dkhs.portfolio.R;
import com.rockerhieu.emojicon.EmojiconTextView;

/**
 * @author zhangcm
 */
public class DKHSTextView extends EmojiconTextView {

    private static String FACE_IMG_PATTERN = "\\[[\u4E00-\u9FA5]+\\]";
    private static String AT_PATTERN1 = "@{1}\\S+\\s+";
    private static String AT_PATTERN2 = "@{1}\\S+:";
    private static String AT_HREF_PATTERN = "<a\\shref='http.{20,30}'>@.{4,20}</a>";
    private static String STOCK_HREF_PATTERN = "@{1}\\S+:";
    private static String STOCK_PATTERN = "\\$\\S+\\([A-Z]+\\)\\$";
    private ClickableSpan[] spans;

    public DKHSTextView(Context context) {
        super(context);
    }

    public DKHSTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DKHSTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence charSequence = getText();
        if (event.getAction() == MotionEvent.ACTION_UP && charSequence instanceof Spanned) {
            Spanned span = (Spanned) charSequence;
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
                int i = (int) event.getX();
                int j = (int) event.getY();
                int k = i - getTotalPaddingLeft();
                int l = j - getTotalPaddingTop();
                int m = k + getScrollX();
                int n = l + getScrollY();
                Layout layout = getLayout();
                int g = layout.getOffsetForHorizontal(layout.getLineForVertical(n), m);
                spans = span.getSpans(g, g, ClickableSpan.class);
                if (spans.length != 0) {
                    spans[0].onClick(this);
                    return false;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            text = Html.fromHtml(text.toString());
            if (text instanceof Spannable) {
                int end = text.length();
                Spannable sp = (Spannable) text;
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                builder.clearSpans();
                for (URLSpan url : urls) {
                    MyClickableSpan mySpan = new MyClickableSpan(getResources().getColor(R.color.blue), getContext());
                    mySpan.url = url.getURL();
                    builder.setSpan(mySpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                setMovementMethod(LinkMovementMethod.getInstance());
                super.setText(builder, type);
            } else {
                super.setText(text, type);
            }
        } else {
            super.setText(text, type);
        }
    }

}
