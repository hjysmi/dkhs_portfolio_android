/**
 * @Title MainInfoFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-26 下午3:31:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.AdActivity;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.widget.ScaleLayout;
import com.dkhs.portfolio.ui.widget.kline.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MainInfoFragment
 * @Description 主界面资讯tab页
 * @date 2015-2-26 下午3:31:46
 */
public class MainInfoFragment extends BaseTitleFragment {


    private SliderLayout slider;
    private Context mContext;

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_yanbao;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        initView(view);
        titleRL.setClickable(true);
        setTitle(R.string.title_info);
    }

    private void initView(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.yanbao_layout);
        String userId = null;
        UserEntity user = UserEngineImpl.getUserEntity();
        if (user != null) {
            userId = user.getId() + "";
        }

        String[] name = getResources().getStringArray(R.array.main_info_title);
        NewsforModel infoEngine;
        List<Fragment> fragmentList = new ArrayList<Fragment>();


        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("301");
        Fragment hongguanFragment = ReportListForAllFragment.getFragment(infoEngine,
                OpitionNewsEngineImple.NEWS_GROUP_TWO);
        fragmentList.add(hongguanFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("304");
        Fragment optionalInfoFragment = ReportListForAllFragment.getFragment(infoEngine,
                OpitionNewsEngineImple.NEWS_GROUP);
        fragmentList.add(optionalInfoFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("302");
        Fragment celueFragment = ReportListForAllFragment
                .getFragment(infoEngine, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        fragmentList.add(celueFragment);

        new FragmentSelectAdapter(getActivity(), name, fragmentList, layout, getChildFragmentManager());


        slider = (SliderLayout) view.findViewById(R.id.slider);
        AdEngineImpl.getNewsBannerAds(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return AdBean.class;
            }

            @Override
            protected void afterParseData(Object object) {

                if (object != null) {
                    AdBean adBean= (AdBean) object;
                    updateAdBanner(adBean);
                }
            }
        });

    }

    private void updateAdBanner(AdBean adBean) {


        int duration=1;
        for (AdBean.AdsEntity item : adBean.getAds()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
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
//        slider.startAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(slider!=null){
//            slider.startAutoCycle();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(slider!=null){
            slider.stopAutoCycle();
        }
    }

    class OnSliderClickListenerImp implements  BaseSliderView.OnSliderClickListener{

        @Override
        public void onSliderClick(BaseSliderView slider) {

            Bundle bundle=slider.getBundle();
            String    redirectUrl=  bundle.getString("redirect_url");
            getActivity().startActivity(AdActivity.getIntent(getActivity(),slider.getDescription(),redirectUrl));
        }
    }

}
