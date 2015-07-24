package com.dkhs.portfolio.bean.itemhandler;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

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
        if(adBean != null) {
            vh.get(R.id.sliderSL).setVisibility(View.VISIBLE);
            slider.removeAllSliders();
            for (AdBean.AdsEntity item : adBean.getAds()) {
                TextSliderView textSliderView = new TextSliderView(vh.getConvertView().getContext());
                textSliderView
                        .description(item.getTitle())
                        .image(item.getImage())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                ;
                duration = item.getDisplay_time();
                Bundle bundle = new Bundle();
                bundle.putString("redirect_url", item.getRedirect_url());
                textSliderView.bundle(bundle);
                textSliderView.setOnSliderClickListener(mOnSliderClickListenerImp);
                slider.addSlider(textSliderView);
            }
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setPresetTransformer(SliderLayout.Transformer.Default);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(duration * 1000);
            slider.startAutoCycle();
        }else{
            vh.get(R.id.sliderSL).setVisibility(View.GONE);
        }

        if(data.hotTopicsBeans != null ) {
            int size = data.hotTopicsBeans.size();
            switch (size) {
                case 5:
                    vh.get(R.id.stick_ll5).setVisibility(View.VISIBLE);
                    vh.get(R.id.line4).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics5, data.hotTopicsBeans.get(4).title);

                case 4:

                    vh.get(R.id.stick_ll4).setVisibility(View.VISIBLE);
                    vh.get(R.id.line3).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics4, data.hotTopicsBeans.get(3).title);
                case 3:
                    vh.get(R.id.stick_ll3).setVisibility(View.VISIBLE);
                    vh.get(R.id.line2).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics3, data.hotTopicsBeans.get(2).title);
                case 2:
                    vh.get(R.id.stick_ll2).setVisibility(View.VISIBLE);
                    vh.get(R.id.line1).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics2, data.hotTopicsBeans.get(1).title);
                case 1:

                    vh.get(R.id.stick_ll1).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics1, data.hotTopicsBeans.get(0).title);
            }
        }





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
