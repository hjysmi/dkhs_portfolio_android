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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;

/**
 * @ClassName OptionalStockListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 上午9:21:53
 * @version 1.0
 */
public class OptionalStockListActivity extends ModelAcitivity {
    private FragmentSelectStockFund loadDataListFragment;

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

}
