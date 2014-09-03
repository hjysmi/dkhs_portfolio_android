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
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @ClassName CombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version 1.0
 */
public class CombinationDetailActivity extends ModelAcitivity implements OnClickListener {

    private Button btnMore;
    private Button btnTrend;

    private FragmentNetValueTrend mFragmentTrend;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_combination_detail);
        setTitle(R.string.netvalue_trend);
        mFragmentTrend = new FragmentNetValueTrend();
        replaceContentView(mFragmentTrend);

        initView();
    }

    private void initView() {
        btnMore = getRightButton();
        btnMore.setBackgroundResource(R.drawable.nav_more_selector);

        btnTrend = (Button) findViewById(R.id.btn_trend);
        btnTrend.setEnabled(false);
        btnTrend.setOnClickListener(this);

    }

    private void replaceContentView(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.combination_contentview, fragment).commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }
}
