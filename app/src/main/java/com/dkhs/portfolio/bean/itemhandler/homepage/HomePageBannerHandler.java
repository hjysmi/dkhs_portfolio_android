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
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.listener.OnSliderClickListenerImp;
import com.dkhs.portfolio.ui.widget.ScaleLayout;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName BannerTopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class HomePageBannerHandler extends SimpleItemHandler<BannerTopicsBean> implements View.OnClickListener {


    public Context mContext;

    private RefreshEnable mRefreshEnable;

    private int mSortType = 0;
    private OnSliderClickListenerImp mOnSliderClickListenerImp;


    public HomePageBannerHandler(Context context, RefreshEnable refreshEnable) {
        mContext = context;
        mRefreshEnable = refreshEnable;
        mOnSliderClickListenerImp = new OnSliderClickListenerImp(mContext);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_banner_home_page;
    }

    @Override
    public void onBindView(ViewHolder vh, BannerTopicsBean data, int position) {
        AdBean adBean = data.adBean;
        int duration = 1;
        final SliderLayout slider = vh.get(R.id.slider);
        if (adBean != null && adBean.getAds() != null && adBean.getAds().size() > 0) {

            if (adBean != slider.getTag()) {


                ScaleLayout scaleLayout = vh.get(R.id.sliderSL);
                scaleLayout.setInterceptTouch(true);
                slider.stopAutoCycle();
//                slider.removeAllSliders();
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
                List<BaseSliderView> allSliders = slider.getAllSliders();
                if (allSliders != null && allSliders.size() > 0) {
                    if (allSliders.size() <= adBean.getAds().size()) {
                        for (int j = 0; j < allSliders.size(); j++) {
                            BaseSliderView baseSliderView = allSliders.get(j);
                            if (baseSliderView instanceof TextSliderView) {
                                TextSliderView textSliderView = (TextSliderView) baseSliderView;
                                AdBean.AdsEntity item = adBean.getAds().get(j);
                                if(!textSliderView.getUrl().equals(item.getImage())){
                                    slider.removeSliderAt(j);
                                    textSliderView = new TextSliderView(vh.getConvertView().getContext());
                                    slider.addSliderAt(j,textSliderView);
                                }
                                textSliderView
                                        .description(item.getTitle())
                                        .image(item.getImage())
                                        .setScaleType(BaseSliderView.ScaleType.Fit).setHideLoadingImageBar(true);
                                ;
                                duration = item.getDisplay_time();
                                Bundle bundle = new Bundle();
                                bundle.putString("redirect_url", item.getRedirect_url());
                                textSliderView.bundle(bundle);
                                textSliderView.setOnSliderClickListener(mOnSliderClickListenerImp);
                            }
                        }
                        for (int k = allSliders.size(); k < adBean.getAds().size(); k++) {
                            TextSliderView textSliderView = new TextSliderView(vh.getConvertView().getContext());
                            AdBean.AdsEntity item = adBean.getAds().get(k);
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
                    } else {
                        for (int j = 0; j < adBean.getAds().size(); j++) {
                            BaseSliderView baseSliderView = allSliders.get(j);
                            if (baseSliderView instanceof TextSliderView) {
                                TextSliderView textSliderView = (TextSliderView) baseSliderView;
                                AdBean.AdsEntity item = adBean.getAds().get(j);
                                if(!textSliderView.getUrl().equals(item.getImage())){
                                    slider.removeSliderAt(j);
                                    textSliderView = new TextSliderView(vh.getConvertView().getContext());
                                    slider.addSliderAt(j,textSliderView);
                                }
                                textSliderView
                                        .description(item.getTitle())
                                        .image(item.getImage())
                                        .setScaleType(BaseSliderView.ScaleType.Fit).setHideLoadingImageBar(true)
                                ;
                                duration = item.getDisplay_time();
                                Bundle bundle = new Bundle();
                                bundle.putString("redirect_url", item.getRedirect_url());
                                textSliderView.bundle(bundle);
                                textSliderView.setOnSliderClickListener(mOnSliderClickListenerImp);
                            }
                        }
                        if (adBean.getAds().size() < allSliders.size()) {
                            int count = allSliders.size() - adBean.getAds().size();
                            for(int i = 0; i < count; i++){
                                slider.removeSliderAt(adBean.getAds().size());
                            }
                        }
                    }
                } else {
                    for (AdBean.AdsEntity item : adBean.getAds()) {
                        TextSliderView textSliderView = new TextSliderView(vh.getConvertView().getContext());
                        textSliderView
                                .description(item.getTitle())
                                .image(item.getImage())
                                .setScaleType(BaseSliderView.ScaleType.Fit).setHideLoadingImageBar(true)
                        ;
                        duration = item.getDisplay_time();
                        Bundle bundle = new Bundle();
                        bundle.putString("redirect_url", item.getRedirect_url());
                        textSliderView.bundle(bundle);
                        textSliderView.setOnSliderClickListener(mOnSliderClickListenerImp);
                        slider.addSlider(textSliderView);
                    }
                }
//                slider.setVisibility(View.INVISIBLE);
//                slider.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        slider.setVisibility(View.VISIBLE);
//                    }
//                }, 600);

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

    public interface RefreshEnable {
        void enable();

        void disEnable();
    }


}
