/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerSortMenuBean;
import com.dkhs.portfolio.bean.FundTypeMenuBean;
import com.dkhs.portfolio.bean.MenuBean;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.widget.MenuChooserRelativeLayout;
import com.dkhs.portfolio.ui.widget.MultiChooserRelativeLayout;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import java.util.LinkedList;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 */
public class MarketFundsFragment extends VisiableLoadFragment implements IDataUpdateListener, OnClickListener {

    @ViewInject(R.id.rl_menu)
    ViewGroup menuRL;
    private MultiChooserRelativeLayout fundTypeMenuChooserL;
    private MenuChooserRelativeLayout sortTypeMenuChooserL;
    @ViewInject(R.id.tv_current)
    private TextView tvCurrent;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;
    @ViewInject(R.id.tv_fund_type)
    private TextView fundTypeTV;

    @ViewInject(R.id.view_stock_title)
    private View titleView;
    @ViewInject(R.id.rootView)
    private ViewGroup mRootView;

    private LinkedList<MenuBean> sorts;
//    private SwitchThreeStateOnClickListener mSwitchThreeStateOnClickListener;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_funds;
    }

    private FundOrderFragment loadDataListFragment;
    private FundManagerRankingsFragment fundManagerRankingsFragment;

    public interface OnRefreshI {
        public void refresh(String type, String sort);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(getView());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void onViewShow() {


        super.onViewShow();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isVisible()) {
            if (getView() != null) {
                onViewShow();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    public void initView(View view) {
        fundTypeMenuChooserL = new MultiChooserRelativeLayout(getActivity());
        sortTypeMenuChooserL = new MenuChooserRelativeLayout(getActivity());
        sortTypeMenuChooserL.setParentView(menuRL);
        fundTypeMenuChooserL.setParentView(mRootView);

        LinkedList<MenuBean> types = MenuBean.fundTypeFromXml(getActivity());
        sorts = MenuBean.fundManagerSortFromXml(getActivity());

        fundTypeMenuChooserL.setData(types, MenuBean.fundManagerFromXml(getActivity()));
        String type = types.getFirst().getValue();
        String sort = sorts.getFirst().getValue();

        sortTypeMenuChooserL.setData(sorts);
        setDrawableDown(fundTypeTV);
        setDrawableDown(tvPercentgae);
        tvPercentgae.setText(R.string.win_rate_day);
        tvCurrent.setText(R.string.join_time);
        fundTypeTV.setText(R.string.fund_manager_order);
        sortKeyFormatStr = mActivity.getString(R.string.win_rate_format);

//         mSwitchThreeStateOnClickListener=     new SwitchThreeStateOnClickListener(tvCurrent, new Action1<SwitchThreeStateOnClickListener.Status>() {
//            @Override
//            public void call(SwitchThreeStateOnClickListener.Status status) {
//
//                switch (status) {
//                    case NORMAL:
//                        fundManagerRankingsFragment.refresh(fundTypeMenuChooserL.getSelectItem().getValue(), sortTypeMenuChooserL.getSelectItem().getValue());
//                        break;
//                    case UP:
//                        fundManagerRankingsFragment.refresh(fundTypeMenuChooserL.getSelectItem().getValue(), "-work_seniority");
//                        break;
//                    case DOWN:
//                        fundManagerRankingsFragment.refresh(fundTypeMenuChooserL.getSelectItem().getValue(), "work_seniority");
//                        break;
//                }
//            }
//        });
//        tvCurrent.setOnClickListener(mSwitchThreeStateOnClickListener);
        replaceFundManagerRankingsDataList(type, sort);
    }


    @OnClick({R.id.tv_current, R.id.tv_percentage, R.id.tv_increase, R.id.rl_fund_type})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_current:
                break;
            case R.id.tv_percentage: {
                fundTypeMenuChooserL.dismiss();
                boolean select = sortTypeMenuChooserL.getVisibility() == View.GONE;
                sortTypeMenuChooserL.toggle();
                setViewOrderIndicator(tvPercentgae, select);
            }
            break;
            case R.id.rl_fund_type: {
                sortTypeMenuChooserL.dismiss();
                boolean select = fundTypeMenuChooserL.getVisibility() == View.GONE;
                fundTypeMenuChooserL.toggle();
                setViewOrderIndicator(fundTypeTV, select);
            }
            break;
            case R.id.btn_refresh: {
                refresh();
            }
            break;
            case R.id.btn_search:
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), SelectAddOptionalActivity.class));
                break;
            default:
                break;
        }

    }

    String sortKeyFormatStr;

    @Subscribe
    public void update(MenuBean menuBean) {

        if (menuBean instanceof FundTypeMenuBean) {
            fundTypeTV.setText(menuBean.getKey());
            FundTypeMenuBean type = (FundTypeMenuBean) menuBean;
            sortKeyFormatStr = "%s";
            tvCurrent.setText(R.string.net_value);
            /**
             * (306, '货币型','hb'),
             (307, '理财型','lc'),
             */
//            mSwitchThreeStateOnClickListener.updateState(SwitchThreeStateOnClickListener.Status.NORMAL,false);
            tvCurrent.setClickable(false);
            if (StockUitls.isSepFund(type.getCode())) {
                sortTypeMenuChooserL.notifyDataSetChanged(MenuBean.sepFundSortFromXml(mActivity));
                tvCurrent.setText(R.string.tenthou_unit_incm);
                tvPercentgae.setText(R.string.year_yld);
            } else {
                sortTypeMenuChooserL.notifyDataSetChanged(MenuBean.fundSortFromXml(mActivity));
                tvPercentgae.setText(sortTypeMenuChooserL.getSelectItem().getKey());
            }
        } else if (menuBean instanceof FundManagerSortMenuBean) {
            tvCurrent.setText(R.string.join_time);
            fundTypeTV.setText(R.string.fund_manager_order);
//            mSwitchThreeStateOnClickListener.updateState(SwitchThreeStateOnClickListener.Status.NORMAL,false);
            tvCurrent.setClickable(true);
            sortKeyFormatStr = mActivity.getString(R.string.win_rate_format);
            tvPercentgae.setText(R.string.win_rate_day);
            sortTypeMenuChooserL.notifyDataSetChanged(MenuBean.fundManagerSortFromXml(mActivity));
        } else {
            tvPercentgae.setText(String.format(sortKeyFormatStr, menuBean.getKey()));
        }

        refresh();

    }

    private void refresh() {


        if (fundTypeMenuChooserL.getSelectItem() instanceof FundManagerSortMenuBean) {

            replaceFundManagerRankingsDataList(fundTypeMenuChooserL.getSelectItem().getValue(), sortTypeMenuChooserL.getSelectItem().getValue());

        } else {

            replaceFundDataList(fundTypeMenuChooserL.getSelectItem().getValue(), sortTypeMenuChooserL.getSelectItem().getValue());
        }

    }


    private void replaceFundManagerRankingsDataList(String type, String sort) {
        if (loadDataListFragment != null) {
            getChildFragmentManager().beginTransaction().detach(loadDataListFragment).commitAllowingStateLoss();
        }
        if (fundManagerRankingsFragment == null) {
            fundManagerRankingsFragment = FundManagerRankingsFragment.newInstant(type, sort);
            getChildFragmentManager().beginTransaction().add(R.id.view_datalist, fundManagerRankingsFragment, "fundManagerRankingsFragment").commitAllowingStateLoss();
        } else {
            getChildFragmentManager().beginTransaction().attach(fundManagerRankingsFragment).commitAllowingStateLoss();
            fundManagerRankingsFragment.refresh(type, sort);
        }
    }

    private void replaceFundDataList(String type, String sort) {

        if (fundManagerRankingsFragment != null) {
            getChildFragmentManager().beginTransaction().detach(fundManagerRankingsFragment).commitAllowingStateLoss();
        }
        if (loadDataListFragment == null) {
            loadDataListFragment = FundOrderFragment.newInstant(type, sort);
            getChildFragmentManager().beginTransaction().add(R.id.view_datalist, loadDataListFragment, "loadDataListFragment").commitAllowingStateLoss();

        } else {
            getChildFragmentManager().beginTransaction().attach(loadDataListFragment).commitAllowingStateLoss();
            loadDataListFragment.refresh(type, sort);
        }

    }


    @Subscribe
    public void menuRLdismiss(MenuChooserRelativeLayout menuChooserRelativeLayout) {
        if (menuChooserRelativeLayout == sortTypeMenuChooserL) {
            setDrawableDown(tvPercentgae);
        } else {
            setDrawableDown(fundTypeTV);
        }

    }

    @Subscribe
    public void menuRLdismiss(MultiChooserRelativeLayout menuChooserRelativeLayout) {

        setDrawableDown(fundTypeTV);
    }


    private void setDrawableUp(TextView view) {
        view.setSelected(true);
        Drawable drawable = getResources().getDrawable(R.drawable.menu_arrow_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setDrawableDown(TextView view) {
        // orderType = typeCurrentDown;
        view.setSelected(false);
        Drawable drawable = getResources().getDrawable(R.drawable.menu_arrow_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private TextView viewLastClick;

    private void setViewOrderIndicator(TextView currentSelectView, boolean select) {
        currentSelectView.setSelected(select);
        if (select) {
            setDrawableUp(currentSelectView);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        // MobclickAgent.onPause(this);

    }

    @Override
    public void dataUpdate(boolean isEmptyData) {
        if (isEmptyData) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
        }
    }


}
