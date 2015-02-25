/**
 * @Title TabStockFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:07
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.OptionEditActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.OptionalStockListActivity.RequestMarketTask;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TabStockTitleChangeEvent;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName TabStockFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-7 上午11:03:07
 * @version 1.0
 */
public class TabStockFragment extends BaseFragment implements OnClickListener {

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_tab_mystock;
    }

    private FragmentSelectStockFund loadDataListFragment;
    @ViewInject(R.id.tv_current)
    private TextView tvCurrent;
    @ViewInject(R.id.tv_increase)
    private TextView tvChange;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;

    public static final String TYPE_CURRENTUP = "current";
    public static final String TYPE_PERCENTAGEUP = "percentage";
    // 涨跌
    public static final String TYPE_CHANGEUP = "change";
    public static final String TYPE_CURRENTDOWN = "-current";
    public static final String TYPE_PERCENTAGEDOWN = "-percentage";
    // 涨跌
    public static final String TYPE_CHANGEDOWN = "-change";
    // 总市值高到低
    public static final String TYPE_TOTAL_CAPITAL_UP = "total_capital";

    // 5s
    private static final long mPollRequestTime = 1000 * 30;
    private Timer mMarketTimer;
    private Context context;

    private boolean isLoading;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        // setContentView(R.layout.activity_optionalstock_list);
        // context = this;
        // setTitle(R.string.optional_stock);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param view
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        replaceDataList();
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mMarketTimer == null) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        MobclickAgent.onPageStart(mPageName);
        BusProvider.getInstance().register(this);
        // MobclickAgent.onResume(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }

    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            // if (!isLoading) {
            loadDataListFragment.refresh();
            // }

        }
    }

    private void initView(View view) {

    }

    // OnClickListener mAddButtonClickListener = new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    //
    // Intent intent = new Intent(OptionalStockListActivity.this, SelectAddOptionalActivity.class);
    // startActivity(intent);
    // }
    // };

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.STOCK_OPTIONAL_PRICE);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();
    }

    private TextView viewLastClick;
    private String orderType;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    // /**
    // * @Title
    // * @Description TODO: (用一句话描述这个方法的功能)
    // * @return
    // */
    // @Override
    // public void onRestart() {
    // // TODO Auto-generated method stub
    // super.onRestart();
    // if (null != loadDataListFragment) {
    // loadDataListFragment.refreshNoCaseTime();
    // }
    // }

    @OnClick({ R.id.tv_current, R.id.tv_percentage, R.id.tv_increase })
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
            case R.id.tv_increase: {
                setViewOrderIndicator(tvChange);
            }
                break;

            default:
                break;
        }

        if (null != loadDataListFragment && !TextUtils.isEmpty(orderType)) {
            isLoading = true;
            loadDataListFragment.setOptionalOrderType(orderType);
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
            if (orderType == TYPE_CHANGEDOWN || orderType == TYPE_CURRENTDOWN || orderType == TYPE_PERCENTAGEDOWN) {
                setUpType(currentSelectView);
            } else {
                setDownType(currentSelectView);
            }
        }
        viewLastClick = currentSelectView;
    }

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENTDOWN;
        } else if (currentSelectView == tvChange) {
            orderType = TYPE_CHANGEDOWN;
        } else if (currentSelectView == tvPercentgae) {
            orderType = TYPE_PERCENTAGEDOWN;
        }
        setDrawableDown(currentSelectView);
    }

    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENTUP;
        } else if (currentSelectView == tvChange) {
            orderType = TYPE_CHANGEUP;
        } else if (currentSelectView == tvPercentgae) {
            orderType = TYPE_PERCENTAGEUP;
        }
        setDrawableUp(currentSelectView);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        // MobclickAgent.onPause(this);
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onTabTitleChange(TabStockTitleChangeEvent event) {
        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
            // PromptManager.showToast("Change tab text to:总市值");
            if (event.tabType.equalsIgnoreCase(TYPE_PERCENTAGEUP)) {
                tvPercentgae.setText(R.string.market_updown_ratio);
                // PromptManager.showToast("Change tab text to:涨跌幅");
            } else if (event.tabType.equalsIgnoreCase(TYPE_CHANGEUP)) {
                tvPercentgae.setText(R.string.market_updown_change);
                // PromptManager.showToast("Change tab text to:涨跌额");

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                tvPercentgae.setText(R.string.market_updown_total_capit);

            }
        }
    }

}