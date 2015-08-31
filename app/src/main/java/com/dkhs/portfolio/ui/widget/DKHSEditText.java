package com.dkhs.portfolio.ui.widget;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
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
        stockname = String.format("<a href=\"dkhs:stock\">$%s$</a>", stockname);
        insertHtmlText(stockname);
    }


    public void inserUserText(String name) {

        name = String.format("<a href=\"dkhs:friend\">@%s</a> ", name);
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

            pasteToResult();
            return true;
        } else {
            return super.onTextContextMenuItem(id);
        }
    }

    private ClipboardManager mClipboard = null;

    private void pasteToResult() {
        // Gets a handle to the clipboard service.
        if (null == mClipboard) {
            mClipboard = (ClipboardManager) getContext().
                    getSystemService(Context.CLIPBOARD_SERVICE);
        }

        String resultString = "";
        // 检查剪贴板是否有内容
        if (mClipboard.hasPrimaryClip()) {

            ClipData clipData = mClipboard.getPrimaryClip();
            int count = clipData.getItemCount();

            for (int i = 0; i < count; ++i) {

                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item
                        .coerceToText(getContext());
                Log.i("mengdd", "item : " + i + ": " + str);

                resultString += str;
            }
            insertHtmlText(resultString);
        }
    }


}
