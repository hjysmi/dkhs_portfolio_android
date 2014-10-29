/**
 * @Title FundsOrderActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午1:56:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;

import android.os.Bundle;

/**
 * @ClassName FundsOrderActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午1:56:21
 * @version 1.0
 */
public class FundsOrderActivity extends ModelAcitivity {
    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param arg0
     * @return
     */
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_funds_order);
        setTitle("基金排行");
        initViews();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initViews() {
        List<String> name = new ArrayList<String>();
        name.add("分时");
        name.add("日线");
        name.add("周线");
        name.add("月线");
        HScrollTitleView hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        hsTitle.setTitleList(name, getResources().getDimensionPixelSize(R.dimen.title_2text_length));

    }
}
