package com.dkhs.portfolio.ui.widget;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.TextModifyUtil;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangcm
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
//            CharSequence s = Html.toHtml((Editable) text);
//            text = Html.fromHtml(s.toString());
            text = getSpannable(text);
            super.setText(text, type);
        } else {
            super.setText(text, type);
        }
        mText = text;
    }

    private CharSequence getSpannable(CharSequence text) {
        text = Html.fromHtml(text.toString());
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//                SpannableStringBuilder builder = new SpannableStringBuilder(text);
            SpannableString span = new SpannableString(text);
//                builder.clearSpans();
            for (URLSpan url : urls) {
                MyClickableSpan mySpan = new MyClickableSpan(getResources().getColor(R.color.blue), getContext());
                mySpan.url = url.getURL();
                Log.i("DKHSTEXT", mySpan.url);
                span.setSpan(mySpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            TextModifyUtil.setImgText(span, FACE_IMG_PATTERN, getContext());
            TextModifyUtil.setStockText(span, STOCK_PATTERN, getContext());
            //加上下面方法会导致崩溃，所以去掉，并不影响，因为edittext不需要点击
//                setMovementMethod(LinkMovementMethod.getInstance());
            return span;
        } else {
        }
        return text;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            int index = getSelectionStart();
            if (!TextUtils.isEmpty(getText().toString())) {
                String s = getText().toString();
                String tempt;
                String end = "";
                tempt = s.substring(0, index);
                if (s.length() != index) {
                    end = getText().toString().substring(index);
                }
                Log.i("TextView", tempt + "...." + end);
                Pattern pattern = Pattern.compile(STOCK_PATTERN);
                //判断光标是否在$股票里
                int tIndex = tempt.lastIndexOf("$");
                int eIndex = end.indexOf("$");
                if (tIndex >= 0 && eIndex >= 0) {
                    try {
//                        Log.i("TextView", inTempt);
                        Log.i("TextView", tempt.substring(tIndex, tempt.length()));
                        Log.i("TextView", end.substring(0, eIndex + 1));
                        String inTempt = tempt.substring(tIndex, tempt.length()) + end.substring(0, eIndex + 1);
                        Matcher matcher = pattern.matcher(inTempt);
                        while (matcher.find()) {
                            String group = matcher.group();
                            setText(tempt.substring(0, tIndex) + end.substring(eIndex + 1));
                            Log.i("TextView", tempt.substring(0, tIndex) + end.substring(eIndex + 1));
                            setSelection(tIndex);
                            return true;
                        }
                    } catch (Exception e) {

                    }
                }
                Matcher matcher = pattern.matcher(tempt);
                while (matcher.find()) {
                    String group = matcher.group();
                    if (tempt.endsWith(group)) {
                        int endIndex = tempt.length() - group.length();
                        setText(tempt.substring(0, tempt.length() - group.length()) + end);
                        setSelection(endIndex);
                        return false;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }


    public void insesrStockText(String stockname) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        stockname = "$" + stockname + "$ ";
        if (start < 0) {
            append(stockname);
        } else {
            getText().replace(Math.min(start, end), Math.max(start, end), stockname, 0, stockname.length());
        }
        setText(getText().toString());
//        mText = getText();
//        mText = Html.toHtml((Editable) mText);
//        Editable.Factory mEditableFactory = Editable.Factory.getInstance();
//        Editable mEditable = mEditableFactory.newEditable(mText);
//        setText(mEditable);
        setSelection(start + stockname.length());
    }

    CharSequence mText = "";

    @Override
    public boolean onTextContextMenuItem(int id) {
        if(id == android.R.id.paste){
            mText = getText();
            int min = 0;
            int max = mText.length();
            if (isFocused()) {
                final int selStart = getSelectionStart();
                final int selEnd = getSelectionEnd();

                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            paste(min,max);
            return true;
        }else{
            return super.onTextContextMenuItem(id);
        }
    }

    private void paste(int min, int max) {
        int beforeLen = mText.length();
        ClipboardManager clipboard =
                (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            boolean didFirst = false;
            for (int i=0; i<clip.getItemCount(); i++) {
                CharSequence paste = clip.getItemAt(i).coerceToText(getContext());
//                paste = getSpannable(paste);
                paste=Html.fromHtml(paste.toString());
                if (paste != null) {
                    if (!didFirst) {
                        Selection.setSelection((Spannable) mText, max);
                        ((Editable) mText).replace(min, max, paste);
                        didFirst = true;
                    } else {
                        ((Editable) mText).insert(getSelectionEnd(), "\n");
                        ((Editable) mText).insert(getSelectionEnd(), paste);
                    }
                    //以下注释是为了让编辑框内超链接变蓝色，变态需求
//                    mText = Html.toHtml((Editable) mText);
//                    Editable.Factory mEditableFactory = Editable.Factory.getInstance();
//                    Editable mEditable = mEditableFactory.newEditable(mText);
//                    setText(mEditable);
                    setText(mText);
                    setSelection(min + mText.length() - beforeLen);

                }
            }
        }
    }
}
