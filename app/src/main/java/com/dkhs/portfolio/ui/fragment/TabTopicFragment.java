package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.MyTopicActivity;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabTopicFragment extends VisiableLoadFragment {

    @ViewInject(R.id.ll)
    LinearLayout mll;
    @ViewInject(R.id.rl_send_fail_tip)
    View viewSendFail;
    private FragmentSelectAdapter mAdpter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handIntent(extras);
        }
    }

    public TabTopicFragment() {
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_bb;
    }


    @Override
    public void requestData() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<>();


        fragments.add(UsersTopicsFragment.newIntent(userId, userName));
        fragments.add(ReplyFragment.getIntent( GlobalParams.LOGIN_USER.getId() + ""));

        mAdpter = new FragmentSelectAdapter(mActivity, getResources().getStringArray(R.array.tab_topic_title_list), fragments, mll, getChildFragmentManager());
        mAdpter.setCurrentItem(0);


    }


    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }

    private String userId;
    private String userName;

    private void handIntent(Bundle bundle) {
        userId = bundle.getString(MyTopicActivity.USER_NAME);
        userName = bundle.getString(MyTopicActivity.USER_ID);


    }
}
