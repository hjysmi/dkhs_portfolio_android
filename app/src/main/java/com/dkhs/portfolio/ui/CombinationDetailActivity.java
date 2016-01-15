/**
 * @Title NewCombinationDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-24 上午10:49:28
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.FloatingActionMenu.OnMenuItemSelectedListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateComDescEvent;
import com.dkhs.portfolio.ui.eventbus.UpdateCombinationEvent;
import com.dkhs.portfolio.ui.fragment.CompareIndexFragment;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentPositionBottom;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.melnykov.fab.ObservableScrollView;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

/**
 * @author zjz
 * @version 1.0
 * @ClassName NewCombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-4-24 上午10:49:28
 */
public class CombinationDetailActivity extends ModelAcitivity {
    public static final String EXTRA_COMBINATION = "extra_combination";
    private CombinationBean mCombinationBean;
    private boolean isMyCombination = false;
    private final int MENU_FOLLOW = 1;
    private final int MENU_DELFOLLOW = 2;
    private final int MENU_REMIND = 3;
    private final int MENU_MORE = 4;
    private final int MENU_ADJUST = 5;
    private final int MENU_SHARE = 6;
    private final int MENU_EDIT = 7;
    private final int MENU_PRIVACY = 8;
    private final int MENU_HISTORY_VALUE = 12;
    private final int MENU_ABOUT = 9;

    private final int REQUESTCODE_MODIFY_COMBINATION = 902;
    private ChangeFollowCombView mChangeFollowCombView;

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, CombinationDetailActivity.class);

        intent.putExtra(EXTRA_COMBINATION, Parcels.wrap(combinationBean));

        return intent;
    }

    @ViewInject(R.id.floating_action_view)
    FloatingActionMenu localFloatingActionMenu;

    @ViewInject(R.id.sv_combinations)
    private ObservableScrollView mScrollView;

    @ViewInject(R.id.tv_position_tip)
    private TextView tvBottomTip;

    @ViewInject(R.id.yanbao_view)
    private View yanbaoView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideBottomLine();
        hadFragment();
        setContentView(R.layout.activity_new_combinationdetail);
        ViewUtils.inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        updateTitleBackgroud(R.color.theme_blue);
        updataTitle();
        initView();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // TRIM_MEMORY_BACKGROUND TRIM_MEMORY_UI_HIDDEN
        LogUtils.d(this.getClass().getSimpleName(), "onTrimMemory level:" + level);
        if (level == TRIM_MEMORY_COMPLETE) {
            LogUtils.d(this.getClass().getSimpleName(), "TRIM_MEMORY_COMPLETE App will be kill");
        }
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void updataTitle() {
        setBackButtonDrawRes(R.drawable.btn_white_back_selector);
        ((TextView) findViewById(R.id.tv_title)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) findViewById(R.id.tv_title_info)).setTextColor(getResources().getColor(R.color.white));
        if (null != mCombinationBean) {
            setTitle(mCombinationBean.getName());
            setTitleTipString(getString(R.string.format_create_time,
                    TimeUtils.getSimpleDay(mCombinationBean.getCreateTime())));
        }
//        if (null != mCombinationBean) {
//            updateTitleBackgroudByValue(mCombinationBean.getNetvalue() - 1);
//        }
    }

    private void handleExtras(Bundle extras) {
//        mCombinationBean = (CombinationBean) extras.getParcelable(EXTRA_COMBINATION);
        mCombinationBean = Parcels.unwrap(extras.getParcelable(EXTRA_COMBINATION));
        if (null != mCombinationBean && null != mCombinationBean.getUser() && mCombinationBean.getUser().getId() > 0) {

            if (null != UserEngineImpl.getUserEntity() && !TextUtils.isEmpty(UserEngineImpl.getUserEntity().getId() + "")) {
                if (mCombinationBean.getUser().getId() == UserEngineImpl.getUserEntity().getId()) {
                    isMyCombination = true;
                }
            }
        }

    }

    private void initView() {
        localFloatingActionMenu.attachToScrollView(mScrollView);
        localFloatingActionMenu.setOnMenuItemSelectedListener(mFloatMenuSelectListner);
        if (isMyCombination) {
            yanbaoView.setVisibility(View.VISIBLE);
            yanbaoView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(CombinationNewsActivity
                            .newIntent(CombinationDetailActivity.this, mCombinationBean));
                }
            });
        } else {
            yanbaoView.setVisibility(View.GONE);
        }

        replaceTrendView();
        replacePostionView();
        replaceCompareView();

        mChangeFollowCombView = new ChangeFollowCombView(this);

    }

    OnMenuItemSelectedListener mFloatMenuSelectListner = new OnMenuItemSelectedListener() {

        @Override
        public boolean onMenuItemSelected(int selectIndex) {

            switch (selectIndex) {
                case MENU_FOLLOW:
                case MENU_DELFOLLOW: {

                    if (null != mCombinationBean && mChangeFollowCombView != null) {
                        mChangeFollowCombView.changeFollow(mCombinationBean);
                    }
                }
                break;
                case MENU_REMIND: {
                    if (!UIUtils.iStartLoginActivity(CombinationDetailActivity.this)) {

                        startActivity(StockRemindActivity.newCombinatIntent(CombinationDetailActivity.this,
                                mCombinationBean));
                    }
                }
                break;
                case MENU_EDIT: { // 组合编辑
                    startActivityForResult(ModifyComNameActivity.newIntent(CombinationDetailActivity.this,
                            mCombinationBean), REQUESTCODE_MODIFY_COMBINATION);
                }
                break;
                case MENU_ADJUST: { // 调整仓位
                    Intent intent = new Intent(CombinationDetailActivity.this, PositionAdjustActivity.class);
                    intent.putExtra(PositionAdjustActivity.EXTRA_COMBINATION_ID, mCombinationBean.getId());
                    intent.putExtra(PositionAdjustActivity.EXTRA_ISADJUSTCOMBINATION, true);
                    startActivity(intent);
                }
                break;
                case MENU_PRIVACY: {
                    // 隐私设置
                    startActivity(ModifyPrivacyActivity.newIntent(CombinationDetailActivity.this, mCombinationBean));

                }
                break;
                case MENU_HISTORY_VALUE: {
                    // 每日收益记录
                    startActivity(EveryDayValueActivity.newIntent(CombinationDetailActivity.this, mCombinationBean));

                }
                break;
                case MENU_ABOUT: {
                    // 谁牛FAQ
                    startActivity(new Intent(CombinationDetailActivity.this, FAQTextActivity.class));

                }
                break;
                case MENU_SHARE: {
                    Fragment mFragmentTrend = getSupportFragmentManager().findFragmentById(R.id.rl_trend_view);
                    if (null != mFragmentTrend && mFragmentTrend instanceof FragmentNetValueTrend) {
                        ((FragmentNetValueTrend) mFragmentTrend).showShareImage();

                    }
                }
                break;
                default:
                    break;
            }
            return false;
        }
    };

    @Subscribe
    public void updateCombination(UpdateCombinationEvent updateCombinationEvent) {
        if (null != updateCombinationEvent && null != updateCombinationEvent.mCombinationBean) {
            this.mCombinationBean = updateCombinationEvent.mCombinationBean;
            initFloatingActionMenu();
            updataTitle();
        }
    }

    private void initFloatingActionMenu() {
        localFloatingActionMenu.removeAllItems();
        if (isMyCombination) {

            if (mCombinationBean.isFollowed()) {

                localFloatingActionMenu.addItem(MENU_REMIND, R.string.float_menu_remind, R.drawable.ic_fm_remind);
                localFloatingActionMenu.addItem(MENU_ADJUST, R.string.float_menu_adjust, R.drawable.ic_fm_adjust);
                localFloatingActionMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);
                localFloatingActionMenu
                        .addMoreItem(MENU_MORE, getString(R.string.float_menu_more), R.drawable.ic_fm_more)
                        .addItem(MENU_EDIT, getString(R.string.float_menu_edit))
                        .addItem(MENU_PRIVACY, getString(R.string.float_menu_privacy))
                        .addItem(MENU_HISTORY_VALUE, getString(R.string.float_menu_history_value))
                        .addItem(MENU_ABOUT, getString(R.string.float_menu_combination))
                        .addItem(MENU_DELFOLLOW, getString(R.string.float_menu_delfollow));
            } else {
                localFloatingActionMenu.addItem(MENU_FOLLOW, R.string.float_menu_optional, R.drawable.ic_add);
                localFloatingActionMenu.addItem(MENU_ADJUST, R.string.float_menu_adjust, R.drawable.ic_fm_adjust);
                localFloatingActionMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);
                localFloatingActionMenu
                        .addMoreItem(MENU_MORE, getString(R.string.float_menu_more), R.drawable.ic_fm_more)
                        .addItem(MENU_EDIT, getString(R.string.float_menu_edit))
                        .addItem(MENU_PRIVACY, getString(R.string.float_menu_privacy))
                        .addItem(MENU_HISTORY_VALUE, getString(R.string.float_menu_history_value))
                        .addItem(MENU_ABOUT, getString(R.string.float_menu_combination));
            }

        } else {

            if (null != mCombinationBean) {
                if (mCombinationBean.isFollowed()) {
                    localFloatingActionMenu.addItem(MENU_DELFOLLOW, R.string.float_menu_delfollow,
                            R.drawable.btn_del_item_normal);
                    localFloatingActionMenu.addItem(MENU_REMIND, R.string.float_menu_remind, R.drawable.ic_fm_remind);
                    localFloatingActionMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);
                } else {
                    localFloatingActionMenu.addItem(MENU_FOLLOW, R.string.float_menu_follow, R.drawable.ic_add);
                    localFloatingActionMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);

                }

            }

        }
    }


//    private FragmentNetValueTrend mFragmentTrend;

    private void replaceTrendView() {
//        if (null == mFragmentTrend) {
//            mFragmentTrend = FragmentNetValueTrend.newInstance(true, null);
//        }
        Fragment mFragmentTrend = getSupportFragmentManager().findFragmentById(R.id.rl_trend_view);
        if (null == mFragmentTrend) {
            mFragmentTrend = FragmentNetValueTrend.newInstance(true, null);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_trend_view, mFragmentTrend).commit();

    }

    private void replacePostionView() {

        if (null != mCombinationBean) {
            if (isMyCombination || mCombinationBean.isPubilc()) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_MODIFY_COMBINATION:
                    CombinationBean cBean = Parcels.unwrap(data
                            .getParcelableExtra(ModifyComNameActivity.ARGUMENT_COMBINATION_BEAN));
                    if (null != cBean) {
                        mCombinationBean = cBean;
                        updataTitle();

                    }
                    break;
            }

        }
    }


    @Subscribe
    public void updateComName(UpdateComDescEvent event) {
        if (null != event) {
            if (!TextUtils.isEmpty(event.comName)) {
                mCombinationBean.setName(event.comName);
            }
            if (!TextUtils.isEmpty(event.comDesc)) {
                mCombinationBean.setDescription(event.comDesc);
            }

            updataTitle();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_combination_detail;
    }
}
