package com.dkhs.portfolio.bean.itemhandler.homepage;


import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.listener.OnSliderClickListenerImp;
import com.dkhs.portfolio.ui.widget.ScaleLayout;

/**
 * @author zwm
 * @version 2.0
 * @ClassName BannerTopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class SubBannerHandler extends SimpleItemHandler<AdBean> implements View.OnClickListener {


    public Context mContext;

    private HomePageBannerHandler.RefreshEnable mRefreshEnable;

    private int mSortType = 0;
    private OnSliderClickListenerImp mOnSliderClickListenerImp;


    public SubBannerHandler(Context context, HomePageBannerHandler.RefreshEnable refreshEnable) {
        mContext = context;
        mRefreshEnable = refreshEnable;
        mOnSliderClickListenerImp = new OnSliderClickListenerImp(mContext);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_sub_banner_home_page;
    }

    @Override
    public void onBindView(ViewHolder vh, AdBean data, int position) {
        AdBean adBean = data;
        int duration = 1;
        final SliderLayout slider = vh.get(R.id.slider);
        if (adBean != null && adBean.getAds() != null && adBean.getAds().size() > 0) {

            if (adBean != slider.getTag()) {


                ScaleLayout scaleLayout = vh.get(R.id.sliderSL);
                scaleLayout.setInterceptTouch(true);
                slider.stopAutoCycle();
                slider.removeAllSliders();
//                slider.setVisibility(View.INVISIBLE);
//                slider.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        slider.setVisibility(View.VISIBLE);
//                    }
//                }, 600);
                if (mRefreshEnable != null) {
                    slider.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            mRefreshEnable.disEnable();
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    mRefreshEnable.enable();
                                    break;
                            }
                            return false;
                        }
                    });
                }
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
                if (adBean.getAds().size() == 1)
                    slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
//                slider.setVisibility(View.VISIBLE);
                slider.setTag(adBean);
                slider.startAutoCycle();
            }

        } else {
            vh.get(R.id.sliderSL).setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        TopicsBean topicsBean = (TopicsBean) v.getTag();
        if (topicsBean != null) {
            TopicsDetailActivity.startActivity(mContext, topicsBean);
        }
    }


}