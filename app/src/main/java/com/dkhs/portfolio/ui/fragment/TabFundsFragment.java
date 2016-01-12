/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.eventbus.TabFundsTitleChangeEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.eventbus.UpdateSelectStockEvent;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.Collections;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundsFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-7 上午11:03:26
 */
public class TabFundsFragment extends VisiableLoadFragment implements IDataUpdateListener, OnClickListener {
    private static final String TAG = TabFundsFragment.class.getSimpleName();

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_tab_myfunds;
    }

    private FragmentSelectStockFund loadDataListFragment;
    @ViewInject(R.id.tv_current)
    private TextView tvCurrent;
    // @ViewInject(R.id.tv_increase)
    // private TextView tvChange;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;

    @ViewInject(R.id.view_stock_title)
    private View titleView;

    // 净值  net_value
    public static final String TYPE_DEFALUT = "";
    public static final String TYPE_CURRENT_UP = "net_value";
    public static final String TYPE_CURRENT_DOWN = "-net_value";
    //
    // 日收益
    // public static final String TYPE_PERCENTAGE_DEF = "percentage";
    public static final String TYPE_PER_DAY_UP = "percent_day";
    public static final String TYPE_PER_DAY_DOWN = "-percent_day";
    // 月收益
    public static final String TYPE_PER_MONTH_DOWN = "-percent_month";
    public static final String TYPE_PER_MONTH_UP = "percent_month";

    // 今年以来收益
    public static final String TYPE_PER_TYEAR_UP = "percent_tyear";
    public static final String TYPE_PER_TYEAR_DOWN = "-percent_tyear";


    private TextView viewLastClick;
    private String orderType = TYPE_DEFALUT;
    private int lastPercentTextIds = 0;

//
// 5s
//    private static final long mPollRequestTime = 1000 * 10;

    private String mUserId;


    public static TabFundsFragment getFragment(String userId) {
        TabFundsFragment fragment = new TabFundsFragment();
        Bundle args = new Bundle();
        args.putString(FragmentSelectStockFund.ARGUMENT_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Bundle bundle = getArguments();

        if (null != bundle) {
            mUserId = bundle.getString(FragmentSelectStockFund.ARGUMENT_USER_ID);

        }

    }

    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        replaceDataList();
    }

    @Override
    public void requestData() {

//        reloadData();

    }

    @Override
    public void onViewShow() {
        reloadData();
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

    }

    @Override
    public void onViewHide() {
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {

        super.onResume();
        BusProvider.getInstance().register(this);

    }

    public void refreshEditView() {
        if (null != dataUpdateListener && null != loadDataListFragment) {
            loadDataListFragment.refreshEditView();
        }
    }

    public void setDataUpdateListener(IDataUpdateListener listen) {
        this.dataUpdateListener = listen;
        if (null != loadDataListFragment) {
            loadDataListFragment.setDataUpdateListener(this);
        }
    }

    private IDataUpdateListener dataUpdateListener;

    @Override
    public void onStop() {
        super.onStop();

    }



    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            loadDataListFragment.refresh();
//            updateHandler.postDelayed(updateRunnable, mPollRequestTime);
        }
    };

    private void replaceDataList() {
//        if (null == loadDataListFragment) {
        loadDataListFragment = FragmentSelectStockFund.getStockFragmentByUserId(FragmentSelectStockFund.StockViewType.OPTIONAL_FUNDS, mUserId);
        // if (null != dataUpdateListener) {
        loadDataListFragment.setDataUpdateListener(this);
        // }
//        }
        getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commitAllowingStateLoss();
    }

    private void reloadData() {
        if (null != loadDataListFragment) {
            loadDataListFragment.setOptionalOrderType(orderType);
            loadDataListFragment.refreshNoCaseTime();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick({R.id.tv_current, R.id.tv_percentage, R.id.tv_increase})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_current: {
//                setViewOrderIndicator(tvCurrent);
            }
            break;
            case R.id.tv_percentage: {
                setViewOrderIndicator(tvPercentgae);

            }
            break;

            default:
                break;
        }

        reloadData();

    }





    private void setDrawableUp(TextView view) {

        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setDrawableDown(TextView view) {
        // orderType = typeCurrentDown;
        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setTextDrawableHide(TextView view) {
        // Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
        // drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, null);
        // tvCurrent.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));

    }

    //
    private void setViewOrderIndicator(TextView currentSelectView) {
        if (null == viewLastClick) {

            setDownType(currentSelectView);
        } else if (viewLastClick != currentSelectView) {
            setTextDrawableHide(viewLastClick);
            setDownType(currentSelectView);
        } else if (viewLastClick == currentSelectView) {


            if (isDefOrder(orderType)) {
                setDownType(currentSelectView);
            } else if (isDownOrder(orderType)) {
                setUpType(currentSelectView);
            } else {
                setDefType(currentSelectView);
            }
        }
        viewLastClick = currentSelectView;
    }

    private boolean isDownOrder(String orderType) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_PER_MONTH_DOWN) || orderType.equals(TYPE_CURRENT_DOWN)
                || orderType.equals(TYPE_PER_DAY_DOWN) || orderType.equals(TYPE_PER_TYEAR_DOWN));
    }

    private boolean isPercentType(String type) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_PER_MONTH_UP) || orderType.equals(TYPE_PER_MONTH_DOWN)
                || orderType.equals(TYPE_PER_DAY_UP) || orderType.equals(TYPE_PER_DAY_DOWN)
                || orderType.equals(TYPE_PER_TYEAR_UP) || orderType.equals(TYPE_PER_TYEAR_DOWN));
    }

    private boolean isDefOrder(String orderType) {
        return orderType.equals(TYPE_DEFALUT);
    }

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_DOWN;
        } else if (currentSelectView == tvPercentgae) {
            if (tvPercentgae.getText().equals(getString(R.string.market_fund_per_day))) {
                // 日收益
                orderType = TYPE_PER_DAY_DOWN;
                lastPercentTextIds = R.string.market_fund_per_day;
            } else if (tvPercentgae.getText().equals(getString(R.string.market_fund_per_month))) {
                // 月收益
                orderType = TYPE_PER_MONTH_DOWN;
                lastPercentTextIds = R.string.market_fund_per_month;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_fund_per_tyear))) {
                // 年来收益
                orderType = TYPE_PER_TYEAR_DOWN;
                lastPercentTextIds = R.string.market_fund_per_tyear;

            }
        }
        setDrawableDown(currentSelectView);
    }

    //
    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_UP;
        } else if (currentSelectView == tvPercentgae) {

            if (tvPercentgae.getText().equals(getString(R.string.market_fund_per_day))) {
                // 日收益
                orderType = TYPE_PER_DAY_UP;
                lastPercentTextIds = R.string.market_fund_per_day;
            } else if (tvPercentgae.getText().equals(getString(R.string.market_fund_per_month))) {
                // 月收益
                orderType = TYPE_PER_MONTH_UP;
                lastPercentTextIds = R.string.market_fund_per_month;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_fund_per_tyear))) {
                // 年来收益
                orderType = TYPE_PER_TYEAR_UP;
                lastPercentTextIds = R.string.market_fund_per_tyear;
            }

        }
        setDrawableUp(currentSelectView);
    }

    //
    private void setDefType(TextView currentSelectView) {
        // if (currentSelectView == tvCurrent) {
        // orderType = "";
        // } else if (currentSelectView == tvChange) {
        // orderType = TYPE_CHANGE_DEF;
        // } else if (currentSelectView == tvPercentgae) {
        // orderType = TYPE_PERCENTAGE_DEF;
        // }
        orderType = TYPE_DEFALUT;
        setTextDrawableHide(currentSelectView);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        updateHandler.removeCallbacks(updateRunnable);
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        // MobclickAgent.onPause(this);
    }

    @Subscribe
    public void onTabTitleChange(TabFundsTitleChangeEvent event) {
        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
            // PromptManager.showToast("Change tab text to:总市值");
            int currentTextId = 0;
            if (event.tabType.equalsIgnoreCase(TYPE_PER_DAY_UP)) {
                tvPercentgae.setText(R.string.market_fund_per_day);
                currentTextId = R.string.market_fund_per_day;
                // PromptManager.showToast("Change tab text to:涨跌幅");
            } else if (event.tabType.equalsIgnoreCase(TYPE_PER_MONTH_UP)) {
                tvPercentgae.setText(R.string.market_fund_per_month);
                currentTextId = R.string.market_fund_per_month;
                // PromptManager.showToast("Change tab text to:涨跌额");

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                tvPercentgae.setText(R.string.market_fund_per_tyear);
                currentTextId = R.string.market_fund_per_tyear;
            }

            setTextDrawableHide(tvPercentgae);
            if (isPercentType(orderType) && lastPercentTextIds > 0 && lastPercentTextIds == currentTextId) {

                if (isDefOrder(orderType)) {
                    setDefType(tvPercentgae);
                } else if (isDownOrder(orderType)) {
                    setDownType(tvPercentgae);
                } else {
                    setUpType(tvPercentgae);
                }

            }
        }
    }

    /**
     * @param isEmptyData
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void dataUpdate(boolean isEmptyData) {
        if (isEmptyData) {
            titleView.setVisibility(View.GONE);

        } else {
            titleView.setVisibility(View.VISIBLE);

        }

        if (null != dataUpdateListener) {
            dataUpdateListener.dataUpdate(isEmptyData);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && null != viewLastClick) {
            // System.out.println("set defalut order");
            setDefType(viewLastClick);
            viewLastClick = null;
        }
        reloadData();
    }

    public List<SelectStockBean> getDataList() {
        if (null != loadDataListFragment) {
            return loadDataListFragment.getDataList();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void forward2Top(TopEvent event){
        if(event != null && isVisible()&& getUserVisibleHint()){
            if(loadDataListFragment != null){
                loadDataListFragment.smoothScrollToTop();
            }
        }
    }
}
