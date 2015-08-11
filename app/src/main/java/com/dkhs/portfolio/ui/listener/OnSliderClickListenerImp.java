package com.dkhs.portfolio.ui.listener;

import android.content.Context;
import android.os.Bundle;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.dkhs.portfolio.ui.AdActivity;

/**
 * @author zwm
 * @version 2.0
 * @ClassName OnSliderClickListenerImp
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/5.
 */
public class OnSliderClickListenerImp implements  BaseSliderView.OnSliderClickListener{


    private Context mContext;

    public OnSliderClickListenerImp(Context context) {
        mContext = context;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Bundle bundle=slider.getBundle();
        String    redirectUrl=  bundle.getString("redirect_url");
        mContext.startActivity(AdActivity.getIntent(mContext, redirectUrl));
    }
}
