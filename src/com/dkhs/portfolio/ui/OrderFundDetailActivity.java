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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentPositionBottom;
import com.dkhs.portfolio.ui.fragment.FragmentPositionDetail;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.InterceptScrollView.ScrollViewListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName OrderFundDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 下午12:04:35
 * @version 1.0
 */
public class OrderFundDetailActivity extends ModelAcitivity implements OnClickListener, ITouchListener {
    private CombinationBean mChampionBean;

    private View mViewHeader;
    private View mViewBottom;

    private TextView tvConName;
    private TextView tvUserName;
    private TextView tvCreateDay;
    private TextView tvConDesc;
    private TextView tvBottomTip;
    private boolean isClickable;
    String type;
    private InterceptScrollView mScrollview; // 滚动条，用于滚动到头部

    public static final String EXTRA_COMBINATION = "extra_combination";

    public static Intent getIntent(Context context, CombinationBean bean, boolean isClickable, String orderType) {
        Intent intent = new Intent(context, OrderFundDetailActivity.class);
        intent.putExtra(EXTRA_COMBINATION, bean);
        intent.putExtra("isClickable", isClickable);
        intent.putExtra("type", orderType);
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
        mChampionBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);
        isClickable = extras.getBoolean("isClickable");
        type = extras.getString("type");
    }
    public InterceptScrollView getScroll(){
    	return mScrollview;
    }
    private void initViews() {
        mScrollview = (InterceptScrollView) findViewById(R.id.sc_content);
        mScrollview.setScrollViewListener(mScrollViewListener);
        mViewHeader = findViewById(R.id.rl_combination_header);
        mViewBottom = findViewById(R.id.combination_position);
        if (isClickable) {
            mViewHeader.setOnClickListener(this);
        }
        tvConName = (TextView) findViewById(R.id.tv_combination_name);
        tvBottomTip = (TextView) findViewById(R.id.tv_position_tip);
        tvUserName = (TextView) findViewById(R.id.tv_combination_user);
        tvCreateDay = (TextView) findViewById(R.id.tv_combination_time);
        tvConDesc = (TextView) findViewById(R.id.tv_combination_desc);

        replaceTrendView();
    }

    ScrollViewListener mScrollViewListener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(InterceptScrollView scrollView, int x, int y, int oldx, int oldy) {
            // TODO Auto-generated method stub
            if (mScrollview.getScrollY() >= getResources().getDimensionPixelOffset(R.dimen.layout_height_all)) {
                chartTounching();
            }
            Log.e("mScrollViewListener", mScrollview.getScrollY() + "---" + mScrollview.getHeight());
        }

    };

    private void replaceTrendView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.combination_layout, FragmentNetValueTrend.newInstance(true, type)).commit();

    }

    private void replaceBottomView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.combination_position, FragmentPositionBottom.newInstance(mChampionBean.getId())).commit();

    }

    private void initData() {
        if (null != mChampionBean) {

            tvConName.setText(mChampionBean.getName());
            tvUserName.setText(mChampionBean.getCreateUser().getUsername());
            tvConDesc.setText(getString(R.string.desc_format, mChampionBean.getDefDescription()));
            tvCreateDay.setText(getString(R.string.format_create_time,
                    TimeUtils.getSimpleDay(mChampionBean.getCreateTime())));
            if (mChampionBean.isPubilc()) {
                // tvBottomTip.setText(R.string.text_combin_open);
                // mViewBottom.setOnClickListener(this);

                // if (tvBottomTip.getVisibility() == View.VISIBLE) {
                tvBottomTip.setVisibility(View.GONE);
                replaceBottomView();
                // }

            } else {
                tvBottomTip.setText(R.string.text_no_open);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_combination_header: {
                // PromptManager.showToast("查看用户信息");
                startActivity(CombinationUserActivity.getIntent(this, mChampionBean.getCreateUser().getUsername(),
                        mChampionBean.getCreateUser().getId(), false));
            }
                break;
            case R.id.tv_position_tip:
            case R.id.combination_position: {
                if (tvBottomTip.getVisibility() == View.VISIBLE) {
                    tvBottomTip.setVisibility(View.GONE);
                    replaceBottomView();
                }
            }
                break;
            default:
                break;
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void chartTounching() {
        if (mScrollview != null) {
            mScrollview.setIsfocus(true);
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loseTouching() {
        if (mScrollview != null) {
            mScrollview.setIsfocus(false);
        }

    }
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onResume(this);
	}
}
