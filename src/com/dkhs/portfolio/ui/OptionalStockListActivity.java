/**
 * @Title OptionalStockListActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 上午9:21:53
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;

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
    private TextView tvIncrease;
    private TextView tvPercentgae;

    private final String typeCurrentUp = "current";
    private final String typePercentageUp = "percentage";
    // 涨跌
    private final String typeChangeUP = "change";
    private final String typeCurrentDown = "-current";
    private final String typePercentageDown = "-percentage";
    // 涨跌
    private final String typeChangeDown = "-change";

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_optionalstock_list);
        setTitle(R.string.optional_stock);
        replaceDataList();
        initView();
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
        tvIncrease = (TextView) findViewById(R.id.tv_increase);
        tvPercentgae = (TextView) findViewById(R.id.tv_percentage);
        tvCurrent.setOnClickListener(this);
        tvIncrease.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_current: {
                if (null == viewLastClick) {
                    viewLastClick = tvCurrent;
                    setCurrentDown();
                } else if (viewLastClick != tvCurrent) {
                    setTextDrawableHide(viewLastClick);
                }
            }
                break;
            case R.id.tv_percentage: {

            }
                break;
            case R.id.tv_increase: {

            }
                break;

            default:
                break;
        }

    }

    private void setCurrentUp() {
        orderType = typeCurrentUp;
        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_up);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvCurrent.setCompoundDrawables(null, null, drawable, null);
        tvCurrent.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setCurrentDown() {
        orderType = typeCurrentDown;
        Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvCurrent.setCompoundDrawables(null, null, drawable, null);
        tvCurrent.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));
    }

    private void setTextDrawableHide(TextView view) {
        // Drawable drawable = getResources().getDrawable(R.drawable.market_icon_down);
        // drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, null, null);
        // tvCurrent.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.text_drawable_margin));

    }

}
