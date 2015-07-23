/**
 * @Title OptionalStockListActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 上午9:21:53
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zjz
 * @version 1.0
 * @ClassName OptionalStockListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-22 上午9:21:53
 */
public class OptionalStockListActivity extends ModelAcitivity implements OnClickListener {
    private FragmentSelectStockFund loadDataListFragment;
    private TextView tvCurrent;
    private TextView tvChange;
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
    private static final long mPollRequestTime = 1000 * 5;
    private Timer mMarketTimer;
    private Context context;

    private boolean isLoading;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
        setContentView(R.layout.activity_optionalstock_list);
        context = this;
        setTitle(R.string.optional_stock);
        replaceDataList();
        initView();
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mMarketTimer == null) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
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

    private void initView() {

        TextView addButton = getRightButton();
//        addButton.setBackgroundResource(R.drawable.btn_search_select);
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select), null, null,
                null);
        addButton.setOnClickListener(mAddButtonClickListener);

        /*Button bottomButton = (Button) findViewById(R.id.btn_add_optional_stock);
        bottomButton.setOnClickListener(mAddButtonClickListener);*/

        TextView btnRefresh = getSecondRightButton();
        // btnRefresh.setOnClickListener(this);
        // btnRefresh.setBackgroundDrawable(null);
        btnRefresh.setText("编辑");
        // btnRefresh.setTextColor(Color.WHITE);
        // btnRefresh.setBackgroundResource(R.drawable.white_black_selector);
        btnRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // loadDataListFragment.refresh();
                Intent intent = new Intent(context, EditTabStockActivity.class);
                startActivity(intent);
            }
        });

        tvCurrent = (TextView) findViewById(R.id.tv_current);
        tvChange = (TextView) findViewById(R.id.tv_increase);
        tvPercentgae = (TextView) findViewById(R.id.tv_percentage);
        tvCurrent.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tvPercentgae.setOnClickListener(this);

    }

    OnClickListener mAddButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(OptionalStockListActivity.this, SelectAddOptionalActivity.class);
            startActivity(intent);
        }
    };

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            loadDataListFragment = FragmentSelectStockFund.getStockFragment(StockViewType.STOCK_OPTIONAL_PRICE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();
    }

    private TextView viewLastClick;
    private String orderType;

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        if (null != loadDataListFragment) {
            loadDataListFragment.refreshNoCaseTime();
        }
    }

    @Override
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
            if (orderType.equals(typeChangeDown) || orderType.equals(typeCurrentDown) || orderType.equals(typePercentageDown)) {
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


}
