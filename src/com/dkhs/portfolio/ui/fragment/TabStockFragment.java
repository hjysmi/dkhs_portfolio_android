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
    // @ViewInject(R.id.tv_increase)
    // private TextView tvChange;
    @ViewInject(R.id.tv_percentage)
    private TextView tvPercentgae;

    // 当前价格
    public static final String TYPE_DEFALUT = "followed_at";
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
            mMarketTimer.schedule(new RequestMarketTask(), 30, mPollRequestTime);
            System.out.println(" mMarketTimer.schedule(new RequestMarketTask()");
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
            loadDataListFragment.refreshNoCaseTime();
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
            // case R.id.tv_increase: {
            // setViewOrderIndicator(tvChange);
            // }
            // break;

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

    private boolean isDefOrder(String orderType) {
        if (!TextUtils.isEmpty(orderType) && orderType.equals(TYPE_DEFALUT)) {
            return true;
        }
        return false;
    }

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = TYPE_CURRENT_DOWN;
        } else if (currentSelectView == tvPercentgae) {
            if (tvPercentgae.getText().equals(getString(R.string.market_updown_ratio))) {
                // 涨跌幅
                orderType = TYPE_PERCENTAGE_DOWN;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_change))) {
                // 涨跌额
                orderType = TYPE_CHANGE_DOWN;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_total_capit))) {
                // 总市值
                orderType = TYPE_TCAPITAL_DOWN;

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

            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_change))) {
                // 涨跌额
                orderType = TYPE_CHANGE_UP;

            } else if (tvPercentgae.getText().equals(getString(R.string.market_updown_total_capit))) {
                // 总市值
                orderType = TYPE_TCAPITAL_UP;

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
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        // MobclickAgent.onPause(this);
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onTabTitleChange(TabStockTitleChangeEvent event) {
        if (null != event && !TextUtils.isEmpty(event.tabType) && null != tvPercentgae) {
            // PromptManager.showToast("Change tab text to:总市值");
            if (event.tabType.equalsIgnoreCase(TYPE_PERCENTAGE_UP)) {
                tvPercentgae.setText(R.string.market_updown_ratio);
                // PromptManager.showToast("Change tab text to:涨跌幅");
            } else if (event.tabType.equalsIgnoreCase(TYPE_CHANGE_UP)) {
                tvPercentgae.setText(R.string.market_updown_change);
                // PromptManager.showToast("Change tab text to:涨跌额");

            } else {
                // PromptManager.showToast("Change tab text to:总市值");
                tvPercentgae.setText(R.string.market_updown_total_capit);
            }
            setTextDrawableHide(tvPercentgae);
        }
    }

}
