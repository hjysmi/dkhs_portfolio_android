/**
 * @Title OrderFundDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 下午12:04:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName OrderFundDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 下午12:04:35
 * @version 1.0
 */
public class OrderFundDetailActivity extends ModelAcitivity implements OnClickListener {
    private CombinationBean mChampionBean;

    private View mViewHeader;
    private TextView tvConName;
    private TextView tvUserName;
    private TextView tvCreateDay;
    private TextView tvConDesc;
    private boolean isClickable;

    public static Intent getIntent(Context context, CombinationBean bean, boolean isClickable) {
        Intent intent = new Intent(context, OrderFundDetailActivity.class);
        intent.putExtra("championbean", bean);
        intent.putExtra("isClickable", isClickable);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order_funddetail);
        setTitle("牛人基金");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initViews();
        initData();
    }

    private void handleExtras(Bundle extras) {
        mChampionBean = (CombinationBean) extras.getSerializable("championbean");
        isClickable = extras.getBoolean("isClickable");
    }

    private void initViews() {
        mViewHeader = findViewById(R.id.rl_combination_header);
        if (isClickable) {
            mViewHeader.setOnClickListener(this);
        }
        tvConName = (TextView) findViewById(R.id.tv_combination_name);
        tvUserName = (TextView) findViewById(R.id.tv_combination_user);
        tvCreateDay = (TextView) findViewById(R.id.tv_combination_time);
        tvConDesc = (TextView) findViewById(R.id.tv_combination_desc);

    }

    private void replaceSearchView() {

        getSupportFragmentManager().beginTransaction().replace(R.id.combination_layout, new FragmentNetValueTrend())
                .commit();

    }

    private void initData() {
        if (null != mChampionBean) {

            tvConName.setText(mChampionBean.getName());
            tvUserName.setText(getString(R.string.format_create_name, mChampionBean.getCreateUser().getUsername()));
            tvConDesc.setText(getString(R.string.desc_format, mChampionBean.getDescription()));
            tvCreateDay.setText(getString(R.string.format_create_time,
                    TimeUtils.getSimpleDay(mChampionBean.getCreateTime())));

        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_combination_header: {
                PromptManager.showToast("查看用户信息");
            }
                break;

            default:
                break;
        }

    }

}
