/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;

import java.util.List;

/**
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version 1.0
 */
public class SelectStockActivity extends BaseSelectActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        hadFragment();
    }

    @Override
    protected void setTabViewPage(List<Fragment> fragmenList) {

        // String[] tArray = getResources().getStringArray(R.array.select_stock);
        // int titleLenght = tArray.length;
        // for (int i = 0; i < titleLenght; i++) {
        // titleList.add(tArray[i]);
        //
        // }
        FragmentSelectStockFund mOptionalFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_OPTIONAL);
        FragmentSelectStockFund mIncreaseFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_INCREASE);
        FragmentSelectStockFund mDownFragment = FragmentSelectStockFund.getStockFragment(StockViewType.STOCK_DRAWDOWN);
        FragmentSelectStockFund mHandoverFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_HANDOVER);
//        mOptionalFragment.setDefLoad(true);
//        mIncreaseFragment.setDefLoad(true);
//        mDownFragment.setDefLoad(true);
//        mHandoverFragment.setDefLoad(true);
        fragmenList.add(mOptionalFragment);
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
        return ListViewType.STOCK;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    protected int getTitleRes() {
        return R.array.select_stock;
    }


}
