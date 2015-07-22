package com.dkhs.portfolio.ui.widget;


import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dkhs.portfolio.utils.TextModifyUtil;
import com.rockerhieu.emojicon.EmojiconEditText;

/**
 * @author zhangcm
 *
 */
public class DKHSEditText extends EmojiconEditText {

	private static String FACE_IMG_PATTERN = "\\[[\u4E00-\u9FA5]+\\]";

	public DKHSEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DKHSEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DKHSEditText(Context context) {
		super(context);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!TextUtils.isEmpty(text)) {
			SpannableString span = new SpannableString(text);
			TextModifyUtil.setImgText(span, FACE_IMG_PATTERN, getContext());
			super.setText(span, type);
		} else {
			super.setText(text, type);
		}
	}

}
