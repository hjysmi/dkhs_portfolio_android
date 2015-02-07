/**
 * @Title UserFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 下午1:01:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.SettingActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @ClassName UserFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-5 下午1:01:32
 * @version 1.0
 */
public class UserFragment extends BaseTitleFragment {

    @ViewInject(R.id.ll_login_layout)
    private View loginLayout;

    @Override
    public int setContentLayoutId() {
        return R.layout.setting_layout;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param view
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        // if (null != loginLayout) {
        // loginLayout.setVisibility(View.GONE);
        // }
    }

    private void initView(View view) {
        //
        Button addButton = getRightButton();
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.menu_icon_setting),
                null, null, null);
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);

            }
        });
    }

}
