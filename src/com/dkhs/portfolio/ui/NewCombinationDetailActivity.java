/**
 * @Title NewCombinationDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-24 上午10:49:28
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.FloatingActionMenu.OnMenuItemSelectedListener;
import com.dkhs.portfolio.ui.fragment.CompareIndexFragment;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentPositionBottom;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.melnykov.fab.ObservableScrollView;

/**
 * @ClassName NewCombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-24 上午10:49:28
 * @version 1.0
 */
public class NewCombinationDetailActivity extends ModelAcitivity {
    public static final String EXTRA_COMBINATION = "extra_combination";
    private CombinationBean mCombinationBean;
    private boolean isMyCombination = true;

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, NewCombinationDetailActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationBean);

        return intent;
    }

    @ViewInject(R.id.floating_action_view)
    FloatingActionMenu localFloatingActionMenu;

    @ViewInject(R.id.sv_combinations)
    private ObservableScrollView mScrollView;

    @ViewInject(R.id.tv_position_tip)
    private TextView tvBottomTip;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_new_combinationdetail);
        ViewUtils.inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        updataTitle();
        initView();
    }

    private void updataTitle() {
        if (null != mCombinationBean) {
            setTitle(mCombinationBean.getName());
            setTitleTipString(getString(R.string.format_create_time,
                    TimeUtils.getSimpleDay(mCombinationBean.getCreateTime())));
        }
        if (null != mCombinationBean) {
            updateTitleBackgroudByValue(mCombinationBean.getNetvalue() - 1);
        }
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }

    private void initView() {
        initFloatingActionMenu();
        localFloatingActionMenu.attachToScrollView(mScrollView);

        replaceTrendView();
        replacePostionView();
        replaceCompareView();

    }

    private void initFloatingActionMenu() {
        if (isMyCombination) {

            localFloatingActionMenu.addItem(1, "自选", R.drawable.ic_agree);
            localFloatingActionMenu.addItem(2, "调仓", R.drawable.ic_discuss);
            localFloatingActionMenu.addItem(3, "分享", R.drawable.ic_discuss);
            localFloatingActionMenu.addItem(4, "更多", R.drawable.ic_discuss);
            localFloatingActionMenu.setOnMenuItemSelectedListener(new OnMenuItemSelectedListener() {

                @Override
                public boolean onMenuItemSelected(int paramInt) {
                    if (paramInt == 1) {
                        PromptManager.showToast("Menu 1 on click");
                    } else {
                        PromptManager.showToast("Menu 2 on click");

                    }
                    return false;
                }
            });
        } else {

            localFloatingActionMenu.addItem(1, "自选", R.drawable.ic_agree);
            localFloatingActionMenu.addItem(2, "分享", R.drawable.ic_discuss);

        }
        // localFloatingActionMenu.addItem(1, "测试", 11);
    }

    private void replaceTrendView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_trend_view, FragmentNetValueTrend.newInstance(true, null)).commit();

    }

    private void replacePostionView() {

        if (null != mCombinationBean) {
            if (mCombinationBean.isPubilc()) {

                tvBottomTip.setVisibility(View.GONE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.combination_position,
                                FragmentPositionBottom.newInstance(mCombinationBean.getId())).commit();

            }
        }

    }

    private void replaceCompareView() {

        getSupportFragmentManager().beginTransaction().replace(R.id.compare_index, new CompareIndexFragment()).commit();

    }

}
