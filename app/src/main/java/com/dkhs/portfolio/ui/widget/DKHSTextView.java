package com.dkhs.portfolio.ui.widget;


import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.rockerhieu.emojicon.EmojiconTextView;

/**
 * @author zhangcm
 */
public class DKHSTextView extends EmojiconTextView implements View.OnTouchListener{

	private static String FACE_IMG_PATTERN = "\\[[\u4E00-\u9FA5]+\\]";
	private static String AT_PATTERN1 = "@{1}\\S+\\s+";
	private static String AT_PATTERN2 = "@{1}\\S+:";
	private static String AT_HREF_PATTERN = "<a\\shref='http.{20,30}'>@.{4,20}</a>";
	private static String STOCK_HREF_PATTERN = "@{1}\\S+:";
	private static String STOCK_PATTERN = "\\$\\S+\\([A-Z]+\\)\\$";


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

			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
				ClickableSpan[] spans= getClickSpans(event, (Spanned) charSequence);
				if (spans.length != 0) {
					spans[0].onClick(this);
					return false;
				}
			}
		}/*else if(event.getAction() == MotionEvent.ACTION_DOWN && charSequence instanceof Spanned){

			ClickableSpan[] spans= getClickSpans(event, (Spanned) charSequence);
			if (spans.length == 0) {
				return false;
			}
		}*/
		Log.d("wys","motionEvent"+event.getAction());

		return super.onTouchEvent(event);
	}

	private ClickableSpan[] getClickSpans(MotionEvent event, Spanned charSequence) {
		Spanned span = charSequence;
		int i = (int) event.getX();
		int j = (int) event.getY();
		int k = i - getTotalPaddingLeft();
		int l = j - getTotalPaddingTop();
		int m = k + getScrollX();
		int n = l + getScrollY();
		Layout layout = getLayout();
		int g = layout.getOffsetForHorizontal(layout.getLineForVertical(n), m);
		return  span.getSpans(g, g, ClickableSpan.class);
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
					NoUnderLineSpan mySpan = new NoUnderLineSpan(getResources().getColor(R.color.blue), getContext());
					mySpan.url = url.getURL();
					builder.setSpan(mySpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
//				setMovementMethod(LinkMovementMethod.getInstance());
				setOnTouchListener(this);
				super.setText(builder, type);
			} else {
				super.setText(text, type);
			}
		} else {
			super.setText(text, type);
		}

	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean ret = false;
		CharSequence text = ((TextView)v).getText();
		Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
		TextView widget = (TextView)v;
		int action = event.getAction();
		if(action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_DOWN){
			int x = (int)event.getX();
			int y = (int)event.getY();
			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();
			x += widget.getScrollX();
			y += widget.getScrollY();
			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line,x);
			ClickableSpan[] link = stext.getSpans(off,off,ClickableSpan.class);
			if(link.length != 0){
				if(action == MotionEvent.ACTION_UP){
					link[0].onClick(widget);
				}
				ret = true;
			}
		}
		return ret;
	}
}
