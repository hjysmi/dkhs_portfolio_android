package com.dkhs.portfolio.utils;

import android.view.View;
import android.widget.ImageView;

import com.dkhs.portfolio.R;

/**
 * Created by wuyongsen on 2015/12/9.
 */
public class WaterMarkUtil {
    public static final int TYPE_BLUE = 0;
    public static final int TYPE_RED = 1;
    public static void calWaterMarkImage(ImageView iv,boolean isShow,int type){
        if(isShow){
            iv.setVisibility(View.VISIBLE);
        }else{
            iv.setVisibility(View.GONE);
            switch (type){
                case TYPE_BLUE:
                    iv.setImageResource(R.drawable.water_mark_blue);
                    break;
                case TYPE_RED:
                    iv.setImageResource(R.drawable.water_mark_red);
                    break;
            }
        }
    }
}
