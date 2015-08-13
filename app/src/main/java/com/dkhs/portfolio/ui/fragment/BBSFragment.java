package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.MyDraftActivity;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
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
    private FragmentSelectAdapter mAdpter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
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
        mAdpter = new FragmentSelectAdapter(mActivity, getResources().getStringArray(R.array.bbs_title_list), fragments, mll, getChildFragmentManager());
        super.onViewCreated(view, savedInstanceState);

        viewSendFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSendFail.setVisibility(View.GONE);
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), MyDraftActivity.class));
            }
        });
        handIntent(getActivity().getIntent());
    }


    private void handIntent(Intent intent) {

        if (intent.hasExtra("bbs_index")) {
            int index = intent.getIntExtra("bbs_index", 0);
            mAdpter.setCurrentItem(index);
        }
    }

    @Subscribe
    public void newIntent(NewIntent newIntent){
        handIntent(newIntent.bundle);
    }
    private void handIntent(Bundle bundle) {
        if (bundle.containsKey("bbs_index")) {
            int index = bundle.getInt("bbs_index", 0);
            mAdpter.setCurrentItem(index);
        }
    }
}
