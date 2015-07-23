package com.dkhs.portfolio.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.dkhs.portfolio.ui.widget.MyClickableSpan;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextModifyUtil {
    private static Map<String, String> faceMap;

    public static void setFaceMap(Context context){
        if(faceMap==null){
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
    public static Map<String, String> getFaceMap(){
        return faceMap;
    }
    public static void setImgText(Spannable builder, String patternStr,Context context) {
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

    public static void setStockText(SpannableStringBuilder builder, String patternStr,Context context) {
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
//			Intent intent = new Intent(context, WriteStatusActivity.class);
//			intent.putExtra("dollar", s);
//			builder.setSpan(getClickableSpan(context, intent),
//					tempt - s.length(), tempt, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

    }

    public static void setAtText(SpannableStringBuilder builder, String patternStr,Context context) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(builder.toString());
        String temptStr = builder.toString();
        int tempt = 0;
        while (matcher.find()) {
            String s = matcher.group();
            int index = temptStr.indexOf(s);
            temptStr = temptStr.substring(index + s.length());
            tempt += index + s.length();
            //TODO 输入at之后跳转
//			Intent intent = new Intent(context, WriteStatusActivity.class);
//			intent.putExtra("at", s);
//			builder.setSpan(getClickableSpan(context, intent),
//					tempt - s.length(), tempt - 1,
//					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }

    private static MyClickableSpan getClickableSpan(Context context, Intent intent) {
        return new MyClickableSpan(context, intent);
    }

}
