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
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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

    private final String typeCurrentUp = "current";
    private final String typePercentageUp = "percentage";
    // 涨跌
    private final String typeChangeUP = "change";
    private final String typeCurrentDown = "-current";
    private final String typePercentageDown = "-percentage";
    // 涨跌
    private final String typeChangeDown = "-change";

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
            if (orderType == typeChangeDown || orderType == typeCurrentDown || orderType == typePercentageDown) {
                setUpType(currentSelectView);
            } else {
                setDownType(currentSelectView);
            }
        }
        viewLastClick = currentSelectView;
    }

    private void setDownType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = typeCurrentDown;
        } else if (currentSelectView == tvChange) {
            orderType = typeChangeDown;
        } else if (currentSelectView == tvPercentgae) {
            orderType = typePercentageDown;
        }
        setDrawableDown(currentSelectView);
    }

    private void setUpType(TextView currentSelectView) {
        if (currentSelectView == tvCurrent) {
            orderType = typeCurrentUp;
        } else if (currentSelectView == tvChange) {
            orderType = typeChangeUP;
        } else if (currentSelectView == tvPercentgae) {
            orderType = typePercentageUp;
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
    }

}
