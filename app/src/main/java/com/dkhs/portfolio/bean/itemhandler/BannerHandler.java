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

    public BannerHandler(Context mContext) {
        this.mContext = mContext;
    }

    String s=" {\"id\": 2, \"code\": \"news_banner\", \"title\": \"\\u8d44\\u8baf\\u9875\\u5e7f\\u544a\", \"description\": \"\\u8d44\\u8baf\\u9875\\u5e7f\\u544a\", \"ads\": [{\"id\": 3, \"title\": \"\\u8d44\\u8baf\\u754c\\u9762\\u5e7f\\u544a\\u754c\\u97622\", \"description\": \"\\u8d44\\u8baf\\u754c\\u9762\\u5e7f\\u544a\\u754c\\u97622\", \"display_time\": 3, \"image\": \"http://com-dkhs-media-test.oss.aliyuncs.com/a/2015/06/25/11/4751/banner1.png\", \"redirect_url\": \"http://121.41.25.170:8030/portfolio/portfoliogame/\"}, {\"id\": 4, \"title\": \"\\u8d44\\u8baf\\u754c\\u9762\\u5e7f\\u544a\\u754c\\u97622\", \"description\": \"\\u8d44\\u8baf\\u754c\\u9762\\u5e7f\\u544a\\u754c\\u97622\", \"display_time\": 2, \"image\": \"http://com-dkhs-media-test.oss.aliyuncs.com/a/2015/06/25/11/4820/banner2.png\", \"redirect_url\": \"http://121.41.25.170:8030/portfolio/portfoliogame/\"}]}";


    @Override
    public int getLayoutResId() {
        return R.layout.layout_banner_topics;
    }

    @Override
    public void onBindView(ViewHolder vh, BannerTopicsBean data, int position) {
        AdBean adBean= DataParse.parseObjectJson(AdBean.class,s);
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
            textSliderView.setOnSliderClickListener(new OnSliderClickListenerImp());
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
