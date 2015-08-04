package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.MyDraftActivity;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.SendTopicEvent;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BBSFragment extends VisiableLoadFragment {


    @ViewInject(R.id.ll)
    LinearLayout mll;
    @ViewInject(R.id.rl_send_fail_tip)
    View viewSendFail;

    public BBSFragment() {
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_bb;
    }


    @Override
    public void requestData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void topicSendFail(SendTopicEvent event) {
        if (null != event && null != viewSendFail) {
            viewSendFail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<>();
        fragments.add(new HotTopicsFragment());
        fragments.add(new LatestTopicsFragment());

        //超级夸张
        new FragmentSelectAdapter(mActivity, getResources().getStringArray(R.array.bbs_title_list), fragments, mll, getChildFragmentManager());
        super.onViewCreated(view, savedInstanceState);

        viewSendFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSendFail.setVisibility(View.GONE);
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), MyDraftActivity.class));
            }
        });
    }


}
