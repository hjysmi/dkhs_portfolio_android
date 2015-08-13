/**
 * @Title: MenuItemFragment.java
 * @Package com.yulebaby
 * @Description: TODO(用一句话描述该文件做什么)
 * @author think4
 * @date 2014-5-7 下午1:18:45
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.LockMenuEvent;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;

import com.dkhs.portfolio.ui.eventbus.UnLockMenuEvent;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author zhoujunzhou
 * @version 1.0
 * @ClassName: MenuItemFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2014-5-7 下午1:18:45
 */
public class MenuItemFragment extends BaseFragment implements OnClickListener {

    public static final int TABINDEX_1 = R.id.tab_1;
    public static final int TABINDEX_2 = R.id.tab_2;
    public static final int TABINDEX_3 = R.id.tab_3;
    public static final int TABINDEX_4 = R.id.tab_4;
    public static final int TABINDEX_5 = R.id.tab_5;

    @ViewInject(R.id.btn_tab1)
    private View btnTab1;
    @ViewInject(R.id.tv_tab1)
    private TextView tvTab1;

    @ViewInject(R.id.btn_tab2)
    private View btnTab2;
    @ViewInject(R.id.tv_tab2)
    private TextView tvTab2;

    @ViewInject(R.id.btn_tab3)
    private View btnTab3;
    @ViewInject(R.id.tv_tab3)
    private TextView tvTab3;

    @ViewInject(R.id.btn_tab4)
    private View btnTab4;
    @ViewInject(R.id.tv_tab4)
    private TextView tvTab4;
    @ViewInject(R.id.btn_tab5)
    private View btnTab5;
    @ViewInject(R.id.tv_tab5)
    private TextView tvTab5;

    @ViewInject(R.id.tab_1)
    private View tabLayout1;
    @ViewInject(R.id.tab_2)
    private View tabLayout2;
    @ViewInject(R.id.tab_3)
    private View tabLayout3;
    @ViewInject(R.id.tab_4)
    private View tabLayout4;
    @ViewInject(R.id.tab_5)
    private View tabLayout5;
    @ViewInject(R.id.tv_new_count)
    private TextView newCountTV;

    private int mIndex = TABINDEX_1;

    public static final String KEY_TABINDEX = "key_tabindex";

    public static MenuItemFragment newInstance(int value) {
        MenuItemFragment fragment = new MenuItemFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(KEY_TABINDEX, value);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        setRetainInstance(true); // 需要设置为true，否则savadInstanceState会返回null
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("curChoice");
        }

    }


    @Override
    public int setContentLayoutId() {

        return R.layout.layout_bottom;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        BusProvider.getInstance().register(this);
            clickTab(mIndex);
    }

    public void clickTabIndex(int index) {

        int id = MenuItemFragment.TABINDEX_1;

        switch (index) {
            case 0: {
                id = MenuItemFragment.TABINDEX_1;

            }
            break;
            case 1: {
                id = MenuItemFragment.TABINDEX_2;
            }
            break;
            case 2: {
                id = MenuItemFragment.TABINDEX_3;

            }
            break;
            case 3 :{
                id = MenuItemFragment.TABINDEX_4;
            }
            break;
            case 4: {
                id = MenuItemFragment.TABINDEX_5;
            }
            break;

            default:
                break;
        }
        clickTab(id);
    }

    public void clickTab(int clickId) {
        mIndex = clickId;
        if (btnTab1 == null) {
            return;
        }

        setupView();
        ((MainActivity) getActivity()).showContentIndex(clickId);

    }

    private void setupView() {
        setViewDefStatus();
        if (mIndex == TABINDEX_1) {
            setSelectView(btnTab1);
            setSelectView(tabLayout1);
            setSelectText(tvTab1);

        } else if (mIndex == TABINDEX_2) {
            setSelectText(tvTab2);
            setSelectView(btnTab2);
            setSelectView(tabLayout2);
        } else if (mIndex == TABINDEX_3) {
            setSelectText(tvTab3);
            setSelectView(btnTab3);
            setSelectView(tabLayout3);
        } else if (mIndex == TABINDEX_4) {
            setSelectText(tvTab4);
            setSelectView(btnTab4);
            setSelectView(tabLayout4);
        } else if (mIndex == TABINDEX_5) {
            setSelectText(tvTab5);
            setSelectView(btnTab5);
            setSelectView(tabLayout5);
            MessageManager.getInstance().setHasNewUnread(false);
            AnimationHelper.dismissScale(newCountTV);
        }

    }

    private void setViewDefStatus() {
        btnTab1.setEnabled(true);
        btnTab2.setEnabled(true);
        btnTab3.setEnabled(true);
        btnTab4.setEnabled(true);
        btnTab5.setEnabled(true);
        tabLayout1.setEnabled(true);
        tabLayout2.setEnabled(true);
        tabLayout3.setEnabled(true);
        tabLayout4.setEnabled(true);
        tabLayout5.setEnabled(true);
        ColorStateList cls = getResources().getColorStateList(R.color.compare_select_gray);
        tvTab1.setTextColor(cls);
        tvTab2.setTextColor(cls);
        tvTab3.setTextColor(cls);
        tvTab4.setTextColor(cls);
        tvTab5.setTextColor(cls);
    }

    private void setSelectView(View rButton) {
        rButton.setEnabled(false);
    }

    @Subscribe
    public void lockMenu(LockMenuEvent lockMenuEvent) {
        tabLayout1.setClickable(false);
        tabLayout2.setClickable(false);
        tabLayout3.setClickable(false);
        tabLayout4.setClickable(false);
        tabLayout5.setClickable(false);
    }

    @Subscribe
    public void unLockMenu(UnLockMenuEvent unLockMenuEvent) {
        tabLayout1.setClickable(true);
        tabLayout2.setClickable(true);
        tabLayout3.setClickable(true);

        tabLayout4.setClickable(true);
        tabLayout5.setClickable(true);
//        mRootView.findViewById(mIndex).setClickable(false);

    }


    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void setSelectText(TextView tvSelect) {
        tvSelect.setTextColor(getResources().getColorStateList(R.color.title_color));
    }

    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4, R.id.tab_5})
    public void onClick(View v) {
        int id = v.getId();
        clickTab(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PortfolioApplication.hasUserLogin() && MessageManager.getInstance().isHasNewUnread()
                && MessageManager.getInstance().getTotalUnreadCount() > 0) {
            updateNewMessageView(true);
        } else {
            updateNewMessageView(false);
        }

    }

    @Subscribe
    public void updateMessageCenter(NewMessageEvent newMessageEvent) {

        if (null != newMessageEvent) {
            updateNewMessageView(newMessageEvent.hasUnread);
        }

    }

    private void updateNewMessageView(boolean showNewIcon) {
        if (showNewIcon) {
            AnimationHelper.showScale(newCountTV);
        } else {
            AnimationHelper.dismissScale(newCountTV);
        }
    }

    /**
     * @param outState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mIndex);
    }

}
