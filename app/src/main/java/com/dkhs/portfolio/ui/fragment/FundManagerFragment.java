package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zjz on 2015/6/8.
 */
public class FundManagerFragment extends BaseFragment {

    public static FundManagerFragment newInstance() {
        FundManagerFragment fragment = new FundManagerFragment();


        return fragment;
    }

    @ViewInject(R.id.lv_manager)
    private ListViewEx lvManger;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_fund_manager;
    }
}
