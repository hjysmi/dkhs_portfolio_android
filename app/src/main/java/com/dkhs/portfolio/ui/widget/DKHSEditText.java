package com.dkhs.portfolio.ui.widget;


import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

import com.dkhs.portfolio.utils.TextModifyUtil;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangcm
 *
 */
public class DKHSEditText extends EmojiconEditText {

	private static String FACE_IMG_PATTERN = "\\[[\u4E00-\u9FA5]+\\]";
    private static String STOCK_PATTERN = "\\$.+?\\$";

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
            TextModifyUtil.setStockText(span, STOCK_PATTERN, getContext());
			super.setText(span, type);
		} else {
			super.setText(text, type);
		}
	}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN){
            int index = getSelectionStart();
            if(!TextUtils.isEmpty(getText().toString())){
                String s =  getText().toString();
                String tempt;
                String end = "";
                tempt = s.substring(0,index);
                if(s.length() != index){
                    end = getText().toString().substring(index);
                }
                Log.i("TextView", tempt + "...."+end);
                Pattern pattern = Pattern.compile(STOCK_PATTERN);
                //判断光标是否在$股票里
                int tIndex = tempt.lastIndexOf("$");
                int eIndex = end.indexOf("$");
                if(tIndex >= 0 && eIndex >= 0){
                    try {
                        String inTempt = tempt.substring(tIndex,tempt.length() - tIndex) + end.substring(0,eIndex+1);
                        Log.i("TextView", inTempt);
                        Matcher matcher = pattern.matcher(inTempt);
                        while(matcher.find()){
                            String group = matcher.group();
                            setText(tempt.substring(0,tIndex)+end.substring(eIndex+1));
                            Log.i("TextView",tempt.substring(0,tIndex)+end.substring(eIndex+1));
                            setSelection(tIndex);
                            return true;
                        }
                    }catch (Exception e){

                    }
                }
                Matcher matcher = pattern.matcher(tempt);
                while (matcher.find()){
                    String group = matcher.group();
                    if(tempt.endsWith(group)){
                        int endIndex = tempt.length() - group.length();
                        setText(tempt.substring(0,tempt.length() - group.length())+ end);
                        setSelection(endIndex);
                        return false;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
