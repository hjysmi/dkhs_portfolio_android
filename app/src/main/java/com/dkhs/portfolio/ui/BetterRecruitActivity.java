package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.FrameLayout;
import com.dkhs.portfolio.bean.QualificationToPersonalEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.PersonalFragment;
import com.dkhs.portfolio.ui.fragment.QualificationFragment;
import com.dkhs.portfolio.ui.fragment.SubmitFragment;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

/**
 * 牛人招募
 * Created by xuetong on 2015/12/7.
 */
public class BetterRecruitActivity extends ModelAcitivity implements View.OnClickListener {
    private FrameLayout fm_main;
    private FragmentManager fm;
    private TextView tv_qualification;
    private ImageView iv_right;
    private TextView tv_personal;
    private TextView tv_submit;
    int width;
    QualificationFragment qualificationFragment;
    SubmitFragment submitFragment;
    PersonalFragment personalFragment;
    private static final int index1 = 1;
    private static final int index2 = 2;
    private static final int index3 = 3;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_betterrecruit);
        BusProvider.getInstance().register(this);
        setTitle("牛人招募");
        initViews();
        initValues();
        initEvents();
    }

    private void initViews() {
        fm_main = (FrameLayout) findViewById(R.id.fm_main);
        tv_qualification = (TextView) findViewById(R.id.tv_qualification);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_personal = (TextView) findViewById(R.id.tv_personal);
        fm = getSupportFragmentManager();
        width = UIUtils.getDisplayMetrics().widthPixels;
    }

    private void initValues() {
        tv_qualification.setTextColor(getResources().getColor(R.color.white));
        showFragment(index1);
    }

    private void initEvents() {
        tv_personal.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        tv_qualification.setOnClickListener(this);
    }

    private void showFragment(int i) {
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (i) {
            case index1:
                if (qualificationFragment == null) {
                    qualificationFragment = new QualificationFragment();
                    ft.add(R.id.fm_main, qualificationFragment);
                } else {
                    ft.show(qualificationFragment);
                }

                break;
            case index2:
                if (personalFragment == null) {
                    personalFragment = new PersonalFragment();
                    ft.add(R.id.fm_main, personalFragment);
                } else {
                    ft.show(personalFragment);
                }

                break;
            case index3:
                submitFragment = new SubmitFragment();
                ft.add(R.id.fm_main, submitFragment);
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (qualificationFragment != null) {
            ft.hide(qualificationFragment);
        }
        if (personalFragment != null) {
            ft.hide(personalFragment);
        }
        if (submitFragment != null) {
            ft.hide(submitFragment);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qualification:
                showFragment(index1);
                break;
            case R.id.tv_personal:
                showFragment(index2);
                break;
            case R.id.tv_submit:
                showFragment(index3);
                break;
        }
    }

    @Subscribe
    public void toPersonalFragment(QualificationToPersonalEvent event) {
        showFragment(index2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}
