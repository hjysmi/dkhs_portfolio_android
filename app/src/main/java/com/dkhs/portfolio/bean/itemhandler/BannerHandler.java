package com.dkhs.portfolio.bean.itemhandler;


import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.AdActivity;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.listener.OnSliderClickListenerImp;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.ui.widget.ScaleLayout;

/**
 * @author zwm
 * @version 2.0
 * @ClassName BannerTopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class BannerHandler implements ItemHandler<BannerTopicsBean>, View.OnClickListener {


    public Context mContext;

    private  RefreshEnable mRefreshEnable;

    public interface  RefreshEnable{
        void enable();
        void disEnable();
    }



    private OnSliderClickListenerImp mOnSliderClickListenerImp;


    public BannerHandler(Context context, RefreshEnable refreshEnable) {
        mContext = context;
        mRefreshEnable = refreshEnable;
        mOnSliderClickListenerImp = new OnSliderClickListenerImp(mContext);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_banner_topics;
    }

    @Override
    public void onBindView(ViewHolder vh, BannerTopicsBean data, int position) {
        AdBean adBean = data.adBean;
        int duration = 1;
        final SliderLayout slider = vh.get(R.id.slider);
        if (adBean != null && adBean.getAds() != null && adBean.getAds().size() > 0) {

            if (adBean != slider.getTag()) {


                ScaleLayout scaleLayout=vh.get(R.id.sliderSL);
                scaleLayout.setInterceptTouch(true);
                slider.stopAutoCycle();
                slider.removeAllSliders();
                slider.setVisibility(View.INVISIBLE);
                slider.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slider.setVisibility(View.VISIBLE);
                    }
                },600);
                if (mRefreshEnable != null){
                    slider .setOnTouchListener(new View.OnTouchListener() {
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

        if (data.hotTopicsBeans != null) {
            int size = data.hotTopicsBeans.size();
            switch (size) {
                case 5:
                    vh.get(R.id.stick_ll5).setVisibility(View.VISIBLE);
                    vh.get(R.id.line4).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics5, data.hotTopicsBeans.get(4).title);
                    vh.get(R.id.stick_ll5).setTag(data.hotTopicsBeans.get(4));
                    vh.get(R.id.recommend_topics5).setTag(data.hotTopicsBeans.get(4));
                case 4:
                    vh.get(R.id.stick_ll4).setVisibility(View.VISIBLE);
                    vh.get(R.id.line3).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics4, data.hotTopicsBeans.get(3).title);
                    vh.get(R.id.stick_ll4).setTag(data.hotTopicsBeans.get(3));
                    vh.get(R.id.recommend_topics4).setTag(data.hotTopicsBeans.get(3));
                case 3:
                    vh.get(R.id.stick_ll3).setVisibility(View.VISIBLE);
                    vh.get(R.id.line2).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics3, data.hotTopicsBeans.get(2).title);
                    vh.get(R.id.stick_ll3).setTag(data.hotTopicsBeans.get(2));
                    vh.get(R.id.recommend_topics3).setTag(data.hotTopicsBeans.get(2));
                case 2:
                    vh.get(R.id.stick_ll2).setVisibility(View.VISIBLE);
                    vh.get(R.id.line1).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics2, data.hotTopicsBeans.get(1).title);
                    vh.get(R.id.stick_ll2).setTag(data.hotTopicsBeans.get(1));
                    vh.get(R.id.recommend_topics2).setTag(data.hotTopicsBeans.get(1));
                case 1:

                    vh.get(R.id.stick_ll1).setVisibility(View.VISIBLE);
                    vh.setTextView(R.id.recommend_topics1, data.hotTopicsBeans.get(0).title);
                    vh.get(R.id.stick_ll1).setTag(data.hotTopicsBeans.get(0));
                    vh.get(R.id.recommend_topics1).setTag(data.hotTopicsBeans.get(0));
            }
        }
        vh.get(R.id.stick_ll1).setOnClickListener(this);
        vh.get(R.id.stick_ll2).setOnClickListener(this);
        vh.get(R.id.stick_ll3).setOnClickListener(this);
        vh.get(R.id.stick_ll4).setOnClickListener(this);
        vh.get(R.id.stick_ll5).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        TopicsBean topicsBean = (TopicsBean) v.getTag();
        if (topicsBean != null) {
            TopicsDetailActivity.startActivity(mContext, topicsBean);
        }
    }


}
