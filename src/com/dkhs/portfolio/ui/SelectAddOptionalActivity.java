/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;

/**
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version 1.0
 */
public class SelectAddOptionalActivity extends BaseSelectActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        findViewById(R.id.rl_search_stock).setVisibility(View.GONE);
        findViewById(R.id.rl_add_stocklist).setVisibility(View.GONE);
        getRightButton().setVisibility(View.GONE);
    }

    @Override
    protected void setTabViewPage(ArrayList<String> titleList, List<FragmentSelectStockFund> fragmenList) {

        String[] tArray = getResources().getStringArray(R.array.select_optional_stock);
        int titleLenght = tArray.length;
        for (int i = 0; i < titleLenght; i++) {
            titleList.add(tArray[i]);

        }
        FragmentSelectStockFund mIncreaseFragment = FragmentSelectStockFund
                .getItemClickBackFragment(ViewType.STOCK_INCREASE);
        FragmentSelectStockFund mDownFragment = FragmentSelectStockFund
                .getItemClickBackFragment(ViewType.STOCK_DRAWDOWN);
        FragmentSelectStockFund mHandoverFragment = FragmentSelectStockFund
                .getItemClickBackFragment(ViewType.STOCK_HANDOVER);

        fragmenList.add(mIncreaseFragment);
        fragmenList.add(mDownFragment);
        fragmenList.add(mHandoverFragment);

    }

    @Override
    protected FragmentSearchStockFund getSearchFragment() {
        return FragmentSearchStockFund.getStockFragment();
    }

    @Override
    protected ListViewType getLoadByType() {
        return ListViewType.ADD_OPTIONAL;
    }

}
