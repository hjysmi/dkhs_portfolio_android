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
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-28 下午12:11:20
 */
public class SelectStatusStockActivity extends BaseSelectActivity implements OnClickListener {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
        findViewById(R.id.rl_add_stocklist).setVisibility(View.GONE);
        getRightButton().setVisibility(View.GONE);
    }


    @Override
    protected void setTabViewPage(List<Fragment> fragmenList) {

        fragmenList.add(FragmentSearchStockFund.getHistoryFragment(true, true));

    }

    @Override
    protected FragmentSearchStockFund getSearchFragment() {
        return FragmentSearchStockFund.getItemClickBackFragment(true);
    }

    @Override
    protected ListViewType getLoadByType() {
        return ListViewType.ADD_OPTIONAL;
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


}
