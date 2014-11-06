/**
 * @Title OrderFundDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 下午12:04:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @ClassName OrderFundDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 下午12:04:35
 * @version 1.0
 */
public class OrderFundDetailActivity extends ModelAcitivity {
    private ChampionBean mChampionBean;

    public static Intent getIntent(Context context, ChampionBean bean) {
        Intent intent = new Intent(context, OrderFundDetailActivity.class);
        intent.putExtra("championbean", bean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order_funddetail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initViews();
        initData();
    }

    private void handleExtras(Bundle extras) {
        mChampionBean = (ChampionBean) extras.getSerializable("championbean");

    }

    private void initViews() {

    }

    private void initData() {
        if (null != mChampionBean) {
            setTitle(mChampionBean.getName());
        }

    }

}
