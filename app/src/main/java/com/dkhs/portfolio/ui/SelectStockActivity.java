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
 * @author zjz
 * @version 1.0
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-28 下午12:11:20
 */
public class SelectStockActivity extends BaseSelectActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        hadFragment();
    }

    @Override
    protected void setTabViewPage(List<Fragment> fragmenList) {


        FragmentSelectStockFund mOptionalFragment = FragmentSelectStockFund
                .getStockFragment(StockViewType.STOCK_OPTIONAL);


        fragmenList.add(mOptionalFragment);


    }

    @Override
    protected FragmentSearchStockFund getSearchFragment() {
        return FragmentSearchStockFund.getStockFragment(true);
    }

    @Override
    protected ListViewType getLoadByType() {
        return ListViewType.STOCK;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected int getTitleRes() {
        return -1;
    }

    @Override
    public int getPageStatisticsStringId() {
        if (getLoadByType() == ListViewType.STOCK){
            return R.string.statistics_select_stock;
        }
        return super.getPageStatisticsStringId();
    }
}
