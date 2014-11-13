/**
 * @Title OptionalStockListActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 上午9:21:53
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.dkhs.portfolio.ui.fragment.MainFragment.RequestCombinationTask;
import com.dkhs.portfolio.ui.fragment.MainFragment.RequestMarketTask;
import com.dkhs.portfolio.ui.fragment.MainFragment.ScrollPageTask;

/**
 * @ClassName OptionalStockListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 上午9:21:53
 * @version 1.0
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

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_optionalstock_list);
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

            loadDataListFragment.refresh();
        }
    }

    private void initView() {

        Button addButton = getRightButton();
        addButton.setBackgroundResource(R.drawable.ic_title_add);
        addButton.setOnClickListener(mAddButtonClickListener);

        Button bottomButton = (Button) findViewById(R.id.btn_add_optional_stock);
        bottomButton.setOnClickListener(mAddButtonClickListener);

        Button btnRefresh = getSecondRightButton();
        // btnRefresh.setOnClickListener(this);
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        btnRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadDataListFragment.refresh();
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
            loadDataListFragment = loadDataListFragment.getStockFragment(ViewType.STOCK_OPTIONAL_PRICE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();
    }

    private TextView viewLastClick;
    private String orderType;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        if (null != loadDataListFragment) {
            loadDataListFragment.refresh();
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

}
