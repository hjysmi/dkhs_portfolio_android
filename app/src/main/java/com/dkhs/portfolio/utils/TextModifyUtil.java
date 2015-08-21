package com.dkhs.portfolio.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.MyClickableSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextModifyUtil {
    private static Map<String, String> faceMap;

    public static void setFaceMap(Context context) {
        if (faceMap == null) {
            // TODO 表情集合初始化
//    		String[] face_img_names=context.getString(R.string.face_img_names).split(",");
//    		String[] face_texts=context.getString(R.string.face_texts).split(",");
//    		if(face_img_names.length==face_texts.length){
//    			faceMap=new HashMap<String, String>();
//    			for(int i=0;i<face_img_names.length;i++){
//    				faceMap.put(face_texts[i], "face_"+face_img_names[i].replace("-", "_"));
//    			}
//    		}
        }
    }

    public static Map<String, String> getFaceMap() {
        return faceMap;
    }

    public static void setImgText(Spannable builder, String patternStr, Context context) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(builder.toString());
        String temptStr = builder.toString();
        int tempt = 0;
        while (matcher.find()) {
            String s = matcher.group();
            int index = temptStr.indexOf(s);
            temptStr = temptStr.substring(index + s.length());
            tempt += index + s.length();
            // 将s转化成bitmap图片
            String source = faceMap.get(s);
            if (!TextUtils.isEmpty(source)) {
                String sourceName = context.getPackageName()
                        + ":drawable/" + source;
                int id = context.getResources().getIdentifier(sourceName,
                        null, null);
                Bitmap bitmap = BitmapFactory.decodeResource(context
                        .getResources(), id);
                builder.setSpan(new ImageSpan(context, bitmap),
                        tempt - s.length(), tempt,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
    }

    public static void setStockText(Spannable builder, String patternStr, Context context) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(builder.toString());
        String temptStr = builder.toString();
        int tempt = 0;
        while (matcher.find()) {
            String s = matcher.group();
            int index = temptStr.indexOf(s);
            temptStr = temptStr.substring(index + s.length());
            tempt += index + s.length();
            //TODO 输入股票之后跳转
//			Intent intent = new Intent(mContext, WriteStatusActivity.class);
//			intent.putExtra("dollar", s);
            builder.setSpan(getMyClickableSpan(context, null),
                    tempt - s.length(), tempt, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

    }

    private static List<MyClickableSpan> spans = new ArrayList<MyClickableSpan>();

    public static void setAtText(SpannableStringBuilder builder, String patternStr, Context context) {
//        Pattern pattern = Pattern.compile(patternStr);
//        Matcher matcher = pattern.matcher(builder.toString());
//        String temptStr = builder.toString();
//        int tempt = 0;
//        while (matcher.find()) {
//            String s = matcher.group();
//            int index = temptStr.indexOf(s);
//            temptStr = temptStr.substring(index + s.length());
//            tempt += index + s.length();
//            //TODO 输入at之后跳转
////			Intent intent = new Intent(mContext, WriteStatusActivity.class);
////			intent.putExtra("at", s);
////			builder.setSpan(getClickableSpan(mContext, intent),
////					tempt - s.length(), tempt - 1,
////					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        }
        spans.clear();
        ;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(builder.toString());
        String temptStr = builder.toString();
        String start = "<a\\shref='http.+'>";
        String end = "</a>";
        Pattern startP = Pattern.compile(start);
        Pattern endP = Pattern.compile(end);
        int tempt = 0;
        SpannableStringBuilder tempBuilder = new SpannableStringBuilder();
        while (matcher.find()) {
            String s = matcher.group();
            int index = temptStr.indexOf(s);
            temptStr = temptStr.substring(index + s.length());
            tempBuilder.append(builder.subSequence(tempt, builder.length() - temptStr.length() - s.length()));
            //记录位置和要显示的可点击字体的长度，并放入集合
            //每次截取之后的下一次开始截取的位置
            tempt = builder.length() - temptStr.length();
            Matcher matcherS = startP.matcher(s);
            Matcher matcherE = endP.matcher(s);
            String startA = "";
            String endA = "";
            if (matcherS.find()) {
                startA = matcherS.group();
            }
            if (matcherE.find()) {
                endA = matcherE.group();
            }
//            Log.i("MATTERN", startA +"...starA.length..." +startA.length()+"..."+endA +"...endA.length..." +endA.length());
//            Log.i("MATTERN", s +"...s.length..." +s.length()+"...");
            String target = s.substring(startA.length(), s.length() - endA.length());
//            spans.add(getClickableSpan(mContext,new Intent(mContext, PostTopicActivity.class),tempBuilder.length(), target.length()));
            tempBuilder.append(target);
            //TODO 输入at之后跳转
//			Intent intent = new Intent(mContext, WriteStatusActivity.class);
//			intent.putExtra("at", s);
//			builder.setSpan(getClickableSpan(mContext, intent),
//					tempt - s.length(), tempt - 1,
//					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (MyClickableSpan span : spans) {
            tempBuilder.setSpan(span, span.startIndex, span.startIndex + span.sLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        builder = tempBuilder;
        builder.clear();
        Log.i("MATTERN", tempBuilder.toString());
        Log.i("MATTERN", builder.toString());
    }

    public static SpannableStringBuilder getAtBuilder(SpannableStringBuilder builder, String patternStr, Context context) {
        spans.clear();
        ;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(builder.toString());
        String temptStr = builder.toString();
        String start = "<a\\shref='http.+'>";
        String end = "</a>";
        Pattern startP = Pattern.compile(start);
        Pattern endP = Pattern.compile(end);
        int tempt = 0;
        SpannableStringBuilder tempBuilder = new SpannableStringBuilder();
        while (matcher.find()) {
            String s = matcher.group();
            Log.i("MATTERN", s);
            int index = temptStr.indexOf(s);
            temptStr = temptStr.substring(index + s.length());
            tempBuilder.append(builder.subSequence(tempt, builder.length() - temptStr.length() - s.length()));
            //记录位置和要显示的可点击字体的长度，并放入集合
            //每次截取之后的下一次开始截取的位置
            tempt = builder.length() - temptStr.length();
            Matcher matcherS = startP.matcher(s);
            Matcher matcherE = endP.matcher(s);
            String startA = "";
            String endA = "";
            if (matcherS.find()) {
                startA = matcherS.group();
            }
            if (matcherE.find()) {
                endA = matcherE.group();
            }
//            Log.i("MATTERN", startA +"...starA.length..." +startA.length()+"..."+endA +"...endA.length..." +endA.length());
//            Log.i("MATTERN", s +"...s.length..." +s.length()+"...");
            String target = s.substring(startA.length(), s.length() - endA.length());
//            spans.add(getClickableSpan(mContext,new Intent(mContext, PostTopicActivity.class),tempBuilder.length(), target.length()));
            tempBuilder.append(target);
            //TODO 输入at之后跳转
//			Intent intent = new Intent(mContext, WriteStatusActivity.class);
//			intent.putExtra("at", s);
//			builder.setSpan(getClickableSpan(mContext, intent),
//					tempt - s.length(), tempt - 1,
//					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (MyClickableSpan span : spans) {
            tempBuilder.setSpan(span, span.startIndex, span.startIndex + span.sLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return tempBuilder;
    }

//    private static MyClickableSpan getClickableSpan(Context mContext, Intent intent, int startIndex, int sLen) {
//        MyClickableSpan span = new MyClickableSpan(mContext);
//        span.startIndex = startIndex;
//        span.sLen = sLen;
//        return span;
//    }

    private static MyClickableSpan getMyClickableSpan(Context context, URLSpan urlSpan) {
        MyClickableSpan mySpan = new MyClickableSpan(context.getResources().getColor(R.color.blue), context);
        if (urlSpan != null) {
            String url = urlSpan.getURL();
            mySpan.url = url;
        }
        return mySpan;
    }


    public static void copyToClipboard(String text, Context context) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

}
