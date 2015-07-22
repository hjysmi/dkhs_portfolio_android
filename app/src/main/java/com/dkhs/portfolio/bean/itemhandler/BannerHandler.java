package com.dkhs.portfolio.bean.itemhandler;


import android.content.Context;
import android.os.Bundle;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.AdActivity;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

/**
 * @author zwm
 * @version 2.0
 * @ClassName BannerTopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class BannerHandler implements ItemHandler<BannerTopicsBean> {


    public Context mContext;


    private  OnSliderClickListenerImp mOnSliderClickListenerImp=new OnSliderClickListenerImp();

    public BannerHandler(Context mContext) {
        this.mContext = mContext;
    }



    @Override
    public int getLayoutResId() {
        return R.layout.layout_banner_topics;
    }

    @Override
    public void onBindView(ViewHolder vh, BannerTopicsBean data, int position) {
        AdBean adBean= data.adBean;
        int duration=1;
        SliderLayout  slider=vh.get(R.id.slider);
        for (AdBean.AdsEntity item : adBean.getAds()){
            TextSliderView textSliderView = new TextSliderView(vh.getConvertView().getContext());
            textSliderView
                    .description(item.getTitle())
                    .image(item.getImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
            ;
            duration=item.getDisplay_time();
            Bundle bundle=new Bundle();
            bundle.putString("redirect_url",item.getRedirect_url());
            textSliderView.bundle(bundle);
            textSliderView.setOnSliderClickListener(mOnSliderClickListenerImp);
            slider.addSlider(textSliderView);
        }
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(duration*1000);
        slider.startAutoCycle();



    }


    @Override
    public Class<?> getDataClass() {
        return BannerTopicsBean.class;
    }

    class OnSliderClickListenerImp implements  BaseSliderView.OnSliderClickListener{

        @Override
        public void onSliderClick(BaseSliderView slider) {

            Bundle bundle=slider.getBundle();
            String    redirectUrl=  bundle.getString("redirect_url");
            mContext.startActivity(AdActivity.getIntent(mContext, redirectUrl));
        }
    }
}
