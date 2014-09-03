/**
 * @Title CombinationDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentCompare;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentNews;
import com.dkhs.portfolio.ui.fragment.FragmentPositionDetail;
import com.dkhs.portfolio.ui.fragment.FragmentSelectCombinStock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @ClassName CombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version 1.0
 */
public class CombinationDetailActivity extends ModelAcitivity implements OnClickListener {

    private Button btnMore;
    private View btnPreBottom;

    private FragmentNetValueTrend mFragmentTrend;
    private FragmentCompare mFragmentCompare;
    private FragmentPositionDetail mFragmentDetail;
    private FragmentNews mFragmentNews;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_combination_detail);
        setTitle(R.string.netvalue_trend);
        // mFragmentTrend = new FragmentNetValueTrend();
        // replaceContentView(mFragmentTrend);
        showFragmentByButtonId(R.id.btn_trend);

        initView();
    }

    private void initView() {
        btnMore = getRightButton();
        btnMore.setBackgroundResource(R.drawable.nav_more_selector);

        btnPreBottom = findViewById(R.id.btn_trend);
        btnPreBottom.setEnabled(false);
        btnPreBottom.setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_comparison).setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_detail).setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_news).setOnClickListener(bottomClickListner);

    }

    private void replaceContentView(Fragment fragment, String tag) {

        getSupportFragmentManager().beginTransaction().replace(R.id.combination_contentview, fragment, tag);
    }

    OnClickListener bottomClickListner = new OnClickListener() {

        @Override
        public void onClick(View v) {

            btnPreBottom.setEnabled(true);
            v.setEnabled(false);
            btnPreBottom = v;
            showFragmentByButtonId(v.getId());
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    Fragment mPreFragment = null;

    protected void showFragmentByButtonId(int id) {
        Fragment mFragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.btn_trend: {
                // String mTag = R.id.btn_trend "";
                mFragment = getSupportFragmentManager().findFragmentByTag("trend");
                if (mFragment == null) {
                    mFragment = new FragmentNetValueTrend();
                    // replaceContentView(mFragment, R.id.btn_trend + "");
                    ft.add(R.id.combination_contentview, mFragment, "trend");
                } else {
                    if (mFragment.isDetached()) {
                        ft.attach(mFragment);
                    }
                }

                // if (null == mFragmentTrend) {
                // mFragmentTrend = new FragmentNetValueTrend();
                // }
                // mFragment = mFragmentTrend;
            }

                break;
            case R.id.btn_comparison: {

                mFragment = getSupportFragmentManager().findFragmentByTag(R.id.btn_comparison + "");

                if (mFragment == null) {
                    mFragment = new FragmentCompare();
                    // replaceContentView(mFragment, R.id.btn_comparison + "");
                    ft.add(R.id.combination_contentview, mFragment, R.id.btn_comparison + "");

                } else {
                    if (mFragment.isDetached()) {
                        ft.attach(mFragment);
                    }
                }
                // if (null == mFragmentCompare) {
                // mFragmentCompare = new FragmentCompare();
                // }
                // mFragment = mFragmentCompare;
            }

                break;
            case R.id.btn_detail: {

                mFragment = getSupportFragmentManager().findFragmentByTag(R.id.btn_detail + "");

                if (mFragment == null) {
                    mFragment = new FragmentPositionDetail();
                    // replaceContentView(mFragment, R.id.btn_detail + "");
                    ft.add(R.id.combination_contentview, mFragment, R.id.btn_detail + "");

                } else {
                    if (mFragment.isDetached()) {
                        ft.attach(mFragment);
                    }
                }

                // if (null == mFragmentDetail) {
                // mFragmentDetail = new FragmentPositionDetail();
                // }
                // mFragment = mFragmentDetail;
            }

                break;
            case R.id.btn_news: {

                mFragment = getSupportFragmentManager().findFragmentByTag(R.id.btn_news + "");

                if (mFragment == null) {
                    mFragment = new FragmentNews();
                    ft.add(R.id.combination_contentview, mFragment, R.id.btn_news + "");

                } else {
                    if (mFragment.isDetached()) {
                        ft.attach(mFragment);
                    }
                }
            }

                break;

            default:
                break;
        }

        if (mPreFragment != null) {
            ft.detach(mPreFragment);
        }
        mPreFragment = mFragment;
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();

    }
}
