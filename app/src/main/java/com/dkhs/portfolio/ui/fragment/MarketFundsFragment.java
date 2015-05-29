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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.FundTypeBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.eventbus.TabStockTitleChangeEvent;
import com.dkhs.portfolio.ui.widget.MenuChooserRelativeLayout;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 */
public class MarketFundsFragment extends BaseFragment implements IDataUpdateListener, OnClickListener {
    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_funds;
    }

    private FragmentSelectStockFund loadDataListFragment;

    @ViewInject(R.id.menuFloat)
    private MenuChooserRelativeLayout menuChooserRelativeLayout;
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

    private boolean isLoading;
    private String mUserId;



    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);


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
//        replaceDataList();

        List<FundTypeBean> data=new ArrayList<>();

        FundTypeBean item1=new FundTypeBean();
        item1.setName("ddd");
        item1.setEnable(true);
        FundTypeBean item2=new FundTypeBean();
        item2.setName("ddd");
        item2.setEnable(true);
        FundTypeBean item3=new FundTypeBean();
        item3.setName("ddd");
        item3.setEnable(true);
        FundTypeBean item4=new FundTypeBean();
        item4.setName("ddd");
        item4.setEnable(true);
        FundTypeBean item5=new FundTypeBean();
        item5.setName("ddd");
        item5.setEnable(true);

        data.add(item1);
        data.add(item2);
        data.add(item3);
        data.add(item4);
        data.add(item5);
        data.add(item5);

        menuChooserRelativeLayout.setData(data);

    }

    @Override
    public void onResume() {

        super.onResume();
        reloadData();
//        updateHandler.postDelayed(updateRunnable, 5*1000);
        MobclickAgent.onPageStart(mPageName);
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



    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            // loadDataListFragment.refreshNoCaseTime();
//            reloadData();
            loadDataListFragment.refresh();
            updateHandler.postDelayed(updateRunnable, mPollRequestTime);
        }
    };

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            loadDataListFragment = FragmentSelectStockFund.getStockFragmentByUserId(FragmentSelectStockFund.StockViewType.FUND_STOCK,mUserId);
            loadDataListFragment.setDataUpdateListener(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();
    }

    private TextView viewLastClick;
    private String orderType = TYPE_DEFALUT;


    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick({R.id.tv_current, R.id.tv_percentage, R.id.tv_increase})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_current: {


                menuChooserRelativeLayout.show();
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

    private void reloadData() {
        if (null != loadDataListFragment) {
            isLoading = true;
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
        view.setCompoundDrawables(null, null, null, null);
    }

    private void setViewOrderIndicator(TextView currentSelectView) {
        if (null == viewLastClick) {

            setDownType(currentSelectView);
        } else if (viewLastClick != currentSelectView) {
            setTextDrawableHide(viewLastClick);
            setDownType(currentSelectView);
        } else if (viewLastClick == currentSelectView) {
            // if (orderType == TYPE_CHANGE_DOWN || orderType == TYPE_CURRENT_DOWN || orderType == TYPE_PERCENTAGE_DOWN)
            // {
            // setUpType(currentSelectView);
            // } else {
            // setDownType(currentSelectView);
            // }

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
        if (!TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_CHANGE_UP) || orderType.equals(TYPE_CURRENT_UP)
                || orderType.equals(TYPE_PERCENTAGE_UP) || orderType.equals(TYPE_TCAPITAL_UP))) {
            return true;
        }
        return false;
    }

    private boolean isDownOrder(String orderType) {
        if (!TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_CHANGE_DOWN) || orderType.equals(TYPE_CURRENT_DOWN)
                || orderType.equals(TYPE_PERCENTAGE_DOWN) || orderType.equals(TYPE_TCAPITAL_DOWN))) {
            return true;
        }
        return false;
    }

    private boolean isPercentType(String type) {
        if (!TextUtils.isEmpty(orderType)
                && (orderType.equals(TYPE_CHANGE_UP) || orderType.equals(TYPE_CHANGE_DOWN)
                || orderType.equals(TYPE_PERCENTAGE_UP) || orderType.equals(TYPE_PERCENTAGE_DOWN)
                || orderType.equals(TYPE_TCAPITAL_UP) || orderType.equals(TYPE_TCAPITAL_DOWN))) {
            return true;
        }
        return false;
    }

    private boolean isDefOrder(String orderType) {
        if (orderType.equals(TYPE_DEFALUT)) {
            return true;
        }
        return false;
    }

    private int lastPercentTextIds = 0;

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_DOWN;
        } else if (currentSelectView == tvPercentgae) {
            if (tvPercentgae.getText().equals(getString(R.string.day_income))) {

                orderType = TYPE_PERCENTAGE_DOWN;
                lastPercentTextIds = R.string.day_income;
            } else if (tvPercentgae.getText().equals(getString(R.string.month_income))) {

                orderType = TYPE_CHANGE_DOWN;
                lastPercentTextIds = R.string.month_income;

            } else if (tvPercentgae.getText().equals(getString(R.string.year_income))) {

                orderType = TYPE_TCAPITAL_DOWN;
                lastPercentTextIds = R.string.year_income;

            }
        }
        setDrawableDown(currentSelectView);
    }

    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_UP;
        } else if (currentSelectView == tvPercentgae) {

            if (tvPercentgae.getText().equals(getString(R.string.day_income))) {

                orderType = TYPE_PERCENTAGE_UP;
                lastPercentTextIds = R.string.day_income;
            } else if (tvPercentgae.getText().equals(getString(R.string.month_income))) {

                orderType = TYPE_CHANGE_UP;
                lastPercentTextIds = R.string.month_income;

            } else if (tvPercentgae.getText().equals(getString(R.string.year_income))) {
                // 总市值
                orderType = TYPE_TCAPITAL_UP;
                lastPercentTextIds = R.string.year_income;
            }

        }
        setDrawableUp(currentSelectView);
    }

    private void setDefType(TextView currentSelectView) {

        orderType = TYPE_DEFALUT;
        setTextDrawableHide(currentSelectView);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_list);

    @Override
    public void onPause() {
        super.onPause();
        updateHandler.removeCallbacks(updateRunnable);
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        // MobclickAgent.onPause(this);
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onTabTitleChange(TabStockTitleChangeEvent event) {
        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
            // PromptManager.showToast("Change tab text to:总市值");
            int currentTextId = 0;
            if (event.tabType.equalsIgnoreCase(TYPE_PERCENTAGE_UP)) {
                tvPercentgae.setText(R.string.day_income);
                currentTextId = R.string.day_income;
                // PromptManager.showToast("Change tab text to:涨跌幅");
            } else if (event.tabType.equalsIgnoreCase(TYPE_CHANGE_UP)) {
                tvPercentgae.setText(R.string.month_income);
                currentTextId = R.string.month_income;
                // PromptManager.showToast("Change tab text to:涨跌额");

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                tvPercentgae.setText(R.string.year_income);
                currentTextId = R.string.year_income;
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

}
