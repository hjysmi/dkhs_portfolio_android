package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BBSFragment extends VisiableLoadFragment {



    @ViewInject(R.id.ll)
    LinearLayout mll;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        ArrayList<android.support.v4.app.Fragment> fragments=new ArrayList<>();
        fragments.add(new HotTopicsFragment());
        fragments.add(new LatestTopicsFragment());

        //超级夸张
        new FragmentSelectAdapter(mActivity,   getResources().getStringArray(R.array.bbs_title_list), fragments, mll, getChildFragmentManager());
        super.onViewCreated(view, savedInstanceState);
    }


}
