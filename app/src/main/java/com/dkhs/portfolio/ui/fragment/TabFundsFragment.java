/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
public class TabFundsFragment extends BaseFragment implements IDataUpdateListener, OnClickListener {

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
    public static final String TYPE_PERCENTAGE_UP = "percent_day";
    public static final String TYPE_PERCENTAGE_DOWN = "-percent_day";
    // 月收益
    // public static final String TYPE_CHANGE_DEF = "-change";
    public static final String TYPE_CHANGE_DOWN = "-percent_month";
    public static final String TYPE_CHANGE_UP = "percent_month";

    // 今年以来收益
    public static final String TYPE_TCAPITAL_UP = "percent_tyear";
    public static final String TYPE_TCAPITAL_DOWN = "-percent_tyear";
//
//    // 5s
//    private static final long mPollRequestTime = 1000 * 10;

//    private Context context;

    //    private boolean isLoading;
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

    Handler updateHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // reloadData();

        }

        ;
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceDataList();
    }

    @Override
    public void onResume() {

        super.onResume();

        reloadData();
//        updateHandler.postDelayed(updateRunnable, 5*1000);

        MobclickAgent.onPageStart(mPageName);
        BusProvider.getInstance().register(this);
        // refreshEditView();

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
        if (null == loadDataListFragment) {
            loadDataListFragment = FragmentSelectStockFund.getStockFragmentByUserId(FragmentSelectStockFund.StockViewType.OPTIONAL_FUNDS, mUserId);
            // if (null != dataUpdateListener) {
            loadDataListFragment.setDataUpdateListener(this);
            // }
        }
        getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();
    }

//    private TextView viewLastClick;
//    private String orderType = TYPE_DEFALUT;


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
//                setViewOrderIndicator(tvPercentgae);

            }
            break;

            default:
                break;
        }

//        reloadData();

    }

    private void reloadData() {
        if (null != loadDataListFragment) {
//            loadDataListFragment.setOptionalOrderType(orderType);
            loadDataListFragment.refreshNoCaseTime();
        }
    }

//    private void setDrawableUp(TextView view) {
//
//        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_up);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        view.setCompoundDrawables(null, null, drawable, null);
//        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
//    }
//
//    private void setDrawableDown(TextView view) {
//        // orderType = typeCurrentDown;
//        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        view.setCompoundDrawables(null, null, drawable, null);
//        view.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
//    }
//
//    private void setTextDrawableHide(TextView view) {
//        // Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
//        // drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        view.setCompoundDrawables(null, null, null, null);
//        // tvCurrent.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
//
//    }
//
//    private void setViewOrderIndicator(TextView currentSelectView) {
//        if (null == viewLastClick) {
//
//            setDownType(currentSelectView);
//        } else if (viewLastClick != currentSelectView) {
//            setTextDrawableHide(viewLastClick);
//            setDownType(currentSelectView);
//        } else if (viewLastClick == currentSelectView) {
//            // if (orderType == TYPE_CHANGE_DOWN || orderType == TYPE_CURRENT_DOWN || orderType == TYPE_PERCENTAGE_DOWN)
//            // {
//            // setUpType(currentSelectView);
//            // } else {
//            // setDownType(currentSelectView);
//            // }
//
//            if (isDefOrder(orderType)) {
//                setDownType(currentSelectView);
//            } else if (isDownOrder(orderType)) {
//                setUpType(currentSelectView);
//            } else {
//                setDefType(currentSelectView);
//            }
//        }
//        viewLastClick = currentSelectView;
//    }
//
//    private boolean isUpOrder(String orderType) {
//        if (!TextUtils.isEmpty(orderType)
//                && (orderType.equals(TYPE_CHANGE_UP) || orderType.equals(TYPE_CURRENT_UP)
//                || orderType.equals(TYPE_PERCENTAGE_UP) || orderType.equals(TYPE_TCAPITAL_UP))) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isDownOrder(String orderType) {
//        if (!TextUtils.isEmpty(orderType)
//                && (orderType.equals(TYPE_CHANGE_DOWN) || orderType.equals(TYPE_CURRENT_DOWN)
//                || orderType.equals(TYPE_PERCENTAGE_DOWN) || orderType.equals(TYPE_TCAPITAL_DOWN))) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isPercentType(String type) {
//        if (!TextUtils.isEmpty(orderType)
//                && (orderType.equals(TYPE_CHANGE_UP) || orderType.equals(TYPE_CHANGE_DOWN)
//                || orderType.equals(TYPE_PERCENTAGE_UP) || orderType.equals(TYPE_PERCENTAGE_DOWN)
//                || orderType.equals(TYPE_TCAPITAL_UP) || orderType.equals(TYPE_TCAPITAL_DOWN))) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isDefOrder(String orderType) {
//        if (orderType.equals(TYPE_DEFALUT)) {
//            return true;
//        }
//        return false;
//    }
//
//    private int lastPercentTextIds = 0;
//
//    private void setDownType(TextView currentSelectView) {
//        if (currentSelectView == tvCurrent) {
//            orderType = TYPE_CURRENT_DOWN;
//        } else if (currentSelectView == tvPercentgae) {
//            if (tvPercentgae.getText().equals(getString(R.string.market_updown_ratio))) {
//                // 涨跌幅
//                orderType = TYPE_PERCENTAGE_DOWN;
//                lastPercentTextIds = R.string.market_updown_ratio;
//            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_change))) {
//                // 涨跌额
//                orderType = TYPE_CHANGE_DOWN;
//                lastPercentTextIds = R.string.market_updown_change;
//
//            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_total_capit))) {
//                // 总市值
//                orderType = TYPE_TCAPITAL_DOWN;
//                lastPercentTextIds = R.string.market_updown_total_capit;
//
//            }
//        }
//        setDrawableDown(currentSelectView);
//    }
//
//    private void setUpType(TextView currentSelectView) {
//        if (currentSelectView == tvCurrent) {
//            orderType = TYPE_CURRENT_UP;
//        } else if (currentSelectView == tvPercentgae) {
//
//            if (tvPercentgae.getText().equals(getString(R.string.market_updown_ratio))) {
//                // 涨跌幅
//                orderType = TYPE_PERCENTAGE_UP;
//                lastPercentTextIds = R.string.market_updown_ratio;
//            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_change))) {
//                // 涨跌额
//                orderType = TYPE_CHANGE_UP;
//                lastPercentTextIds = R.string.market_updown_change;
//
//            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_total_capit))) {
//                // 总市值
//                orderType = TYPE_TCAPITAL_UP;
//                lastPercentTextIds = R.string.market_updown_total_capit;
//            }
//
//        }
//        setDrawableUp(currentSelectView);
//    }
//
//    private void setDefType(TextView currentSelectView) {
//        // if (currentSelectView == tvCurrent) {
//        // orderType = "";
//        // } else if (currentSelectView == tvChange) {
//        // orderType = TYPE_CHANGE_DEF;
//        // } else if (currentSelectView == tvPercentgae) {
//        // orderType = TYPE_PERCENTAGE_DEF;
//        // }
//        orderType = TYPE_DEFALUT;
//        setTextDrawableHide(currentSelectView);
//    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        updateHandler.removeCallbacks(updateRunnable);
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        // MobclickAgent.onPause(this);
        BusProvider.getInstance().unregister(this);
    }

//    @Subscribe
//    public void onTabTitleChange(TabStockTitleChangeEvent event) {
//        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
//            // PromptManager.showToast("Change tab text to:总市值");
//            int currentTextId = 0;
//            if (event.tabType.equalsIgnoreCase(TYPE_PERCENTAGE_UP)) {
//                tvPercentgae.setText(R.string.market_updown_ratio);
//                currentTextId = R.string.market_updown_ratio;
//                // PromptManager.showToast("Change tab text to:涨跌幅");
//            } else if (event.tabType.equalsIgnoreCase(TYPE_CHANGE_UP)) {
//                tvPercentgae.setText(R.string.market_updown_change);
//                currentTextId = R.string.market_updown_change;
//                // PromptManager.showToast("Change tab text to:涨跌额");
//
//            } else {
//                // PromptManager.showToast("Change tab text to:总市值");
//                tvPercentgae.setText(R.string.market_updown_total_capit);
//                currentTextId = R.string.market_updown_total_capit;
//            }
//
//            setTextDrawableHide(tvPercentgae);
//            if (isPercentType(orderType) && lastPercentTextIds > 0 && lastPercentTextIds == currentTextId) {
//
//                if (isDefOrder(orderType)) {
//                    setDefType(tvPercentgae);
//                } else if (isDownOrder(orderType)) {
//                    setDownType(tvPercentgae);
//                } else {
//                    setUpType(tvPercentgae);
//                }
//
//            }
//        }
//    }

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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 777 && null != viewLastClick) {
//            // System.out.println("set defalut order");
//            setDefType(viewLastClick);
//            viewLastClick = null;
//            reloadData();
//        }
//    }

    public List<SelectStockBean> getDataList() {
        if (null != loadDataListFragment) {
            return loadDataListFragment.getDataList();
        }
        return Collections.EMPTY_LIST;
    }

}
