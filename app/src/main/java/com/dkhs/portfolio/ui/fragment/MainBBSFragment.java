/**
 * @Title MainInfoFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-26 下午3:31:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MainInfoFragment
 * @Description 主界面资讯tab页
 * @date 2015-2-26 下午3:31:46
 */
public class MainBBSFragment extends BaseTitleFragment {


    public static final String TAG = "MainBBSFragment";

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_main_info;
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


    @Override
    public void onViewShow() {
        super.onViewShow();
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

    }

    @Override
    public void onViewHide() {
        super.onViewHide();
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    private void initView(View view) {
        getRightButton().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_edit_selector), null,
                null, null);
        getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UIUtils.iStartLoginActivity(getActivity())) {
                    return;
                }
                getActivity().startActivity(PostTopicActivity.getIntent(getActivity(), PostTopicActivity.TYPE_POST, "", ""));
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.container, new BBSFragment()).commitAllowingStateLoss();

    }


}
