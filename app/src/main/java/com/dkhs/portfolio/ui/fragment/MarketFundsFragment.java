/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundTypeMenuBean;
import com.dkhs.portfolio.bean.MenuBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.widget.MenuChooserRelativeLayout;
import com.dkhs.portfolio.utils.StockUitls;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 */
public class MarketFundsFragment extends BaseFragment implements IDataUpdateListener, OnClickListener {


    private LinkedList<MenuBean> sorts;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_funds;
    }

    private FundOrderFragment loadDataListFragment;

    @ViewInject(R.id.rl_menu)
    ViewGroup menuRL;
    private MenuChooserRelativeLayout fundTypeMenuChooserL;
    private MenuChooserRelativeLayout sortTypeMenuChooserL;
    @ViewInject(R.id.tv_current)
    private TextView tvCurrent;
    // @ViewInject(R.id.tv_increase)
    // private TextView tvChange;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;
    @ViewInject(R.id.tv_fund_type)
    private TextView fundTypeTV;

    @ViewInject(R.id.view_stock_title)
    private View titleView;

    private Context context;


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
        super.onViewCreated(view, savedInstanceState);

        fundTypeMenuChooserL = new MenuChooserRelativeLayout(getActivity());
        sortTypeMenuChooserL = new MenuChooserRelativeLayout(getActivity());
        sortTypeMenuChooserL.setParentView(menuRL);
        fundTypeMenuChooserL.setParentView(menuRL);

        List<MenuBean> types = MenuBean.fundTypeFromXml(getActivity());
        sorts = MenuBean.fundSortFromXml(getActivity());

        fundTypeMenuChooserL.setData(types);
        String type = types.get(0).getValue();
        String sort = sorts.get(0).getValue();

        sortTypeMenuChooserL.setData(sorts);
        setDrawableDown(fundTypeTV);
        setDrawableDown(tvPercentgae);
        replaceDataList(type, sort);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void replaceDataList(String type, String sort) {
        if (null == loadDataListFragment) {
            loadDataListFragment = FundOrderFragment.newInstant(type, sort);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commitAllowingStateLoss();
    }


    @Override
    public void onStart() {
        super.onStart();
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
            default:
                break;
        }

    }

    @Subscribe
    public void update(MenuBean menuBean) {

        if (menuBean instanceof FundTypeMenuBean) {
            fundTypeTV.setText(menuBean.getKey());
            FundTypeMenuBean type = (FundTypeMenuBean) menuBean;
            /**
             * (306, '货币型','hb'),
             (307, '理财型','lc'),
             */
//            if(type.getCode().equals("307")|| type.getCode ().equals("306")) {
            if (StockUitls.isSepFund(type.getCode())) {
//                sorts.removeLast();
                MenuBean m = sorts.getFirst();
                m.setKey(getString(R.string.year_yld));
                m.setValue("-year_yld");
                sortTypeMenuChooserL.notifyDataSetChanged();
                tvCurrent.setText(R.string.tenthou_unit_incm);
                sorts.getLast().setEnable(false);
                tvPercentgae.setText(R.string.year_yld);

            } else {

                MenuBean m = sorts.getFirst();
                m.setKey(getString(R.string.percent_day));
                m.setValue("-percent_day");
                tvCurrent.setText(R.string.net_value);
                MenuBean allNetValue = new MenuBean();
                sorts.getLast().setEnable(true);
//                m.setKey("累计净值");
//                m.setValue("-net_cumulative");
//                sorts.addLast(allNetValue);

                sortTypeMenuChooserL.notifyDataSetChanged();
                tvPercentgae.setText(sortTypeMenuChooserL.getSelectItem().getKey());
            }
        } else {
            tvPercentgae.setText(menuBean.getKey());
        }


        loadDataListFragment.refresh(fundTypeMenuChooserL.getSelectItem().getValue(), sortTypeMenuChooserL.getSelectItem().getValue());
    }

    @Subscribe
    public void menuRLdismiss(MenuChooserRelativeLayout menuChooserRelativeLayout) {
        if (menuChooserRelativeLayout == sortTypeMenuChooserL) {
            setDrawableDown(tvPercentgae);
        } else {
            setDrawableDown(fundTypeTV);
        }

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
//        if (null == viewLastClick) {
//            setDrawableUp(currentSelectView);
//        } else if (viewLastClick != currentSelectView) {
//            setDrawableDown(viewLastClick);
//            setDrawableUp(currentSelectView);
//        } else
        if (select) {
            setDrawableUp(currentSelectView);
        }
//          else {
//            setDrawableDown(currentSelectView);
//        }
//        viewLastClick = currentSelectView;
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
