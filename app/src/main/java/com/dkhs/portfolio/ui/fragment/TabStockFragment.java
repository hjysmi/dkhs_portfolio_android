/**
 * @Title TabStockFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:07
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.eventbus.TabStockTitleChangeEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabStockFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-7 上午11:03:07
 */
public class TabStockFragment extends VisiableLoadFragment implements OnClickListener, IDataUpdateListener {


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_tab_mystock;
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

    // 当前价格
    public static final String TYPE_DEFALUT = "";
    public static final String TYPE_CURRENT_UP = "current";
    public static final String TYPE_CURRENT_DOWN = "-current";

    // 涨跌幅
    // public static final String TYPE_PERCENTAGE_DEF = "percentage";
    public static final String TYPE_PERCENTAGE_UP = "percentage";
    public static final String TYPE_PERCENTAGE_DOWN = "-percentage";
    // 涨跌额
    // public static final String TYPE_CHANGE_DEF = "-change";
    public static final String TYPE_CHANGE_DOWN = "-change";
    public static final String TYPE_CHANGE_UP = "change";

    // 总市值高到低
    // public static final String TYPE_TCAPITAL_DEF = "total_capital";
    public static final String TYPE_TCAPITAL_UP = "total_capital";
    public static final String TYPE_TCAPITAL_DOWN = "-total_capital";

    // 5s
    private static final long mPollRequestTime = 1000 * 10;

    private Context context;

    private String mUserId;


    public static TabStockFragment getTabStockFragment(String userId) {
        TabStockFragment fragment = new TabStockFragment();
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
    WeakHandler updateHandler = new WeakHandler();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceDataList();

    }

    @Override
    public void requestData() {

    }

    private static final String TAG = TabStockFragment.class.getSimpleName();

    @Override
    public void onViewShow() {
        reloadData();
        updateHandler.postDelayed(updateRunnable, 5 * 1000);
        StatService.onPageStart(getActivity(), TAG);


    }

    @Override
    public void onViewHide() {
        StatService.onPageEnd(getActivity(), TAG);
        updateHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onResume() {

        super.onResume();
        BusProvider.getInstance().register(this);
        // refreshEditView();

    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_tab_stock;
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

            if (loadDataListFragment != null) {
                // FIXME: 2015/7/6 在部分机型部分时机会爆java.lang.NullPointerException  待测试这个方法是否有效
                // at com.dkhs.portfolio.ui.fragment.TabStockFragment$1.run(Unknown Source)
                // at com.dkhs.portfolio.common.WeakHandler$WeakRunnable.run(Unknown Source)
                loadDataListFragment.refresh();
                updateHandler.postDelayed(this, mPollRequestTime);
            }

        }
    };

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            loadDataListFragment = FragmentSelectStockFund.getStockFragmentByUserId(StockViewType.STOCK_OPTIONAL_PRICE, mUserId);
            // if (null != dataUpdateListener) {
            loadDataListFragment.setDataUpdateListener(this);
            // }
        }
        getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commitAllowingStateLoss();
    }

    private TextView viewLastClick;
    private String orderType = TYPE_DEFALUT;

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    @OnClick({R.id.tv_current, R.id.tv_percentage, R.id.tv_increase})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_current: {
                setViewOrderIndicator(tvCurrent);
            }
            break;
            case R.id.tv_percentage: {
                setViewOrderIndicator(tvPercentgae);

            }
            break;
            // case R.id.tv_increase: {
            // setViewOrderIndicator(tvChange);
            // }
            // break;

            default:
                break;
        }

        reloadData();

    }

    private void reloadData() {
        if (null != loadDataListFragment) {
            loadDataListFragment.setOptionalOrderType(orderType);
            loadDataListFragment.refreshNoCaseTime();
        }
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

    private boolean isUpOrder(String orderType) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_CHANGE_UP) || orderType.equals(TYPE_CURRENT_UP)
                || orderType.equals(TYPE_PERCENTAGE_UP) || orderType.equals(TYPE_TCAPITAL_UP));
    }

    private boolean isDownOrder(String orderType) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_CHANGE_DOWN) || orderType.equals(TYPE_CURRENT_DOWN)
                || orderType.equals(TYPE_PERCENTAGE_DOWN) || orderType.equals(TYPE_TCAPITAL_DOWN));
    }

    private boolean isPercentType(String type) {
        return !TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_CHANGE_UP) || orderType.equals(TYPE_CHANGE_DOWN)
                || orderType.equals(TYPE_PERCENTAGE_UP) || orderType.equals(TYPE_PERCENTAGE_DOWN)
                || orderType.equals(TYPE_TCAPITAL_UP) || orderType.equals(TYPE_TCAPITAL_DOWN));
    }

    private boolean isDefOrder(String orderType) {
        return orderType.equals(TYPE_DEFALUT);
    }

    private int lastPercentTextIds = 0;

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_DOWN;
        } else if (currentSelectView == tvPercentgae) {
            if (tvPercentgae.getText().equals(getString(R.string.market_updown_ratio))) {
                // 涨跌幅
                orderType = TYPE_PERCENTAGE_DOWN;
                lastPercentTextIds = R.string.market_updown_ratio;
            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_change))) {
                // 涨跌额
                orderType = TYPE_CHANGE_DOWN;
                lastPercentTextIds = R.string.market_updown_change;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_total_capit))) {
                // 总市值
                orderType = TYPE_TCAPITAL_DOWN;
                lastPercentTextIds = R.string.market_updown_total_capit;

            }
        }
        setDrawableDown(currentSelectView);
    }

    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_UP;
        } else if (currentSelectView == tvPercentgae) {

            if (tvPercentgae.getText().equals(getString(R.string.market_updown_ratio))) {
                // 涨跌幅
                orderType = TYPE_PERCENTAGE_UP;
                lastPercentTextIds = R.string.market_updown_ratio;
            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_change))) {
                // 涨跌额
                orderType = TYPE_CHANGE_UP;
                lastPercentTextIds = R.string.market_updown_change;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_total_capit))) {
                // 总市值
                orderType = TYPE_TCAPITAL_UP;
                lastPercentTextIds = R.string.market_updown_total_capit;
            }

        }
        setDrawableUp(currentSelectView);
    }

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

        // MobclickAgent.onPause(this);
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onTabTitleChange(TabStockTitleChangeEvent event) {
        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
            // PromptManager.showToast("Change tab text to:总市值");
            int currentTextId = 0;
            if (event.tabType.equalsIgnoreCase(TYPE_PERCENTAGE_UP)) {
                tvPercentgae.setText(R.string.market_updown_ratio);
                currentTextId = R.string.market_updown_ratio;
                // PromptManager.showToast("Change tab text to:涨跌幅");
            } else if (event.tabType.equalsIgnoreCase(TYPE_CHANGE_UP)) {
                tvPercentgae.setText(R.string.market_updown_change);
                currentTextId = R.string.market_updown_change;
                // PromptManager.showToast("Change tab text to:涨跌额");

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                tvPercentgae.setText(R.string.market_updown_total_capit);
                currentTextId = R.string.market_updown_total_capit;
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

        if (requestCode == 777 && null != viewLastClick) {
            // System.out.println("set defalut order");
            setDefType(viewLastClick);
            viewLastClick = null;
            reloadData();
        }
    }

    public List<SelectStockBean> getDataList() {
        if (null != loadDataListFragment) {
            return loadDataListFragment.getDataList();
        }
        return Collections.EMPTY_LIST;
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
