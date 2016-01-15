package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.MyDraftFragmnet;

/**
 * Created by zjz on 2015/7/24.
 */
public class MyDraftActivity extends ModelAcitivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
        setTitle(R.string.title_my_draft);

        initTitleView();
        replaceMyDraftFragment();
    }


    private void initTitleView() {

    }


    private void replaceMyDraftFragment() {
        replaceContentFragment(new MyDraftFragmnet());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_mydraft;
    }
}
