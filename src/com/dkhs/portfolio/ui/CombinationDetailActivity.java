/**
 * @Title CombinationDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentSelectCombinStock;

import android.os.Bundle;

/**
 * @ClassName CombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version 1.0
 */
public class CombinationDetailActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_combination_detail);
        setTitle(R.string.netvalue_trend);
        replaceContentView();
    }

    private void replaceContentView() {
        // if (null == mSearchFragment) {
        // mSearchFragment = FragmentSelectCombinStock.getInstance();
        // }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.combination_contentview, new FragmentNetValueTrend()).commit();
    }
}
