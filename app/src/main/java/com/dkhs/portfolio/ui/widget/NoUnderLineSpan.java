package com.dkhs.portfolio.ui.widget;
import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.dkhs.portfolio.ui.messagecenter.MessageHandler;

/**
 * If an object of this type is attached to the text of a TextView with a
 * movement method of LinkMovementMethod, the affected spans of text can be
 * selected. If clicked, the {@link #onClick} method will be called.
 *
 * @author zcm
 */
public class NoUnderLineSpan extends ClickableSpan {

	int color = -1;
	private Context context;
	public String url;
	public int startIndex;
	public int sLen;

	public NoUnderLineSpan(Context context) {
		this(-1, context);
	}

	/**
	 * constructor
	 * @param color the link color
	 * @param context
	 */
	public NoUnderLineSpan(int color, Context context) {
		if (color!=-1) {
			this.color = color;
		}
		this.context = context;
	}

	/**
	 * Performs the click action associated with this span.
	 */
	public void onClick(View widget){
		if(!TextUtils.isEmpty(url)){
			new MessageHandler(context).handleURL(url);
		}
		widget.invalidate();
	}

	/**
	 * Makes the text without underline.
	 */
	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		if (color == -1) {
			ds.setColor(ds.linkColor);
		} else {
			ds.setColor(color);
		}
		ds.setUnderlineText(false);
	}
}
