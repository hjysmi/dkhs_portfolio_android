package com.dkhs.portfolio.utils;

import android.view.View;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;

/**
 * Created by wuyongsen on 2015/12/9.
 */
public class WaterMarkUtil {
    public static void calWaterMarkImage(ImageView iv,boolean isShow,int type){
        if(isShow){
            iv.setVisibility(View.VISIBLE);
            switch (UserEntity.VERIFIEDTYPE.getEnumType(type)){
                case ADVISER:
                case ANALYST:
                case FUND_CERTIFICATE:
                case FUTURES_CERTIFICATE:
                    iv.setImageResource(R.drawable.water_mark_blue);
                    break;
                case EXPERT:
                    iv.setImageResource(R.drawable.water_mark_red);
                    break;
                default:
                    break;
            }
        }else{
            iv.setVisibility(View.GONE);
        }
    }
}
