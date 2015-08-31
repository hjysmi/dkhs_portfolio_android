package com.dkhs.portfolio.ui.widget;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.dkhs.portfolio.R;
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

    //
//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        if (!TextUtils.isEmpty(text)) {
//            String soucreHtml = Html.toHtml(getText());
////            Log.e(this.getClass().getSimpleName(), " before setText soucreHtml :" + soucreHtml);
//            Log.e(this.getClass().getSimpleName(), " before setText text :" + text);
////            CharSequence s = Html.toHtml((Editable) text);
//            text = Html.fromHtml(text.toString());
////            text = getSpannable(text);
//            text = buildSpannChar(text);
//
//            mLastSourceText = Html.toHtml(getText());
//
//            Log.e(this.getClass().getSimpleName(), " after setText soucreHtml :" + soucreHtml);
//
//            super.setText(text, type);
//        } else {
//            super.setText(text, type);
//        }
//    }


    /**
     * when copy loop the html text
     */
    protected CharSequence buildSpannChar(CharSequence html) {

        CharSequence sequence = html;
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        return strBuilder;

    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        NoUnderLineSpan mySpan = new NoUnderLineSpan(getResources().getColor(R.color.blue), getContext());
        strBuilder.setSpan(mySpan, start, end, flags);
        strBuilder.removeSpan(span);
    }

    /**
     * 重新处理光标删除股票逻辑
     */
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
        stockname = String.format("<a href=\"portfolio:stock\">$%s$</a>", stockname);
        insertHtmlText(stockname);
    }


    public void inserUserText(String name) {

        name = String.format("<a href=\"portfolio:friend\">@%s</a> ", name);
        insertHtmlText(name);

    }


    private void insertHtmlText(String htmlText) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        CharSequence htmlSequence = buildSpannChar(Html.fromHtml(htmlText));
        if (start < 0) {
            append(htmlSequence);
        } else {
            getText().replace(Math.min(start, end), Math.max(start, end), htmlSequence, 0, htmlSequence.length());
        }

        setSelection(getText().length());
    }

    /**
     * 处理复制黏贴逻辑，对HTML的一些转义符的处理
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            CharSequence mText = getText();
            int min = 0;
            int max = mText.length();
            if (isFocused()) {
                final int selStart = getSelectionStart();
                final int selEnd = getSelectionEnd();

                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            paste(mText, min, max);
            return true;
        } else {
            return super.onTextContextMenuItem(id);
        }
    }

    private void paste(CharSequence mText, int min, int max) {
        int beforeLen = mText.length();
        ClipboardManager clipboard =
                (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            boolean didFirst = false;
            for (int i = 0; i < clip.getItemCount(); i++) {
                CharSequence paste = clip.getItemAt(i).coerceToText(getContext());
//                paste = getSpannable(paste);
                paste = Html.fromHtml(paste.toString());
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
