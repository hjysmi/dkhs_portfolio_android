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
import android.view.View;
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
import com.lidroid.xutils.util.LogUtils;

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
        return R.layout.fragment_main_info;
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
        toolBar.setClickable(true);
        setTitle(R.string.title_bbs);
    }

    @Override
    public void requestData() {

    }

    private void initView(View view) {

        //// FIXME: 2015/7/23    这里缺少ic_new_post 的点击效果的图片
        getRightButton().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new_post, 0, 0, 0);

        getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.container,new BBSFragment()).commitAllowingStateLoss();

    }


    @Override
    public void onViewHide() {
        LogUtils.e("onViewHide");
        super.onViewHide();
    }

    @Override
    public void onViewShow() {
        LogUtils.e("onViewShow");
        super.onViewShow();
    }








}
