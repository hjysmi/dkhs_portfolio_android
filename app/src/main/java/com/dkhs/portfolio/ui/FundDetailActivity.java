package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.parceler.Parcels;

/**
 * 基金详情页
 * Created by zjz on 2015/6/4.
 */
public class FundDetailActivity extends ModelAcitivity {

    public static final String EXTRA_FUND = "extra_fund";
    private SelectStockBean mFundBean;


    @ViewInject(R.id.hs_title)
    private HScrollTitleView hsTitleTab;

    @ViewInject(R.id.layout_sepfund_header)
    private ViewStub vsSepFundHeader;
    @ViewInject(R.id.layout_noramlfund_header)
    private ViewStub vsNormalFundHeader;


    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, FundDetailActivity.class);
        intent.putExtra(EXTRA_FUND, Parcels.wrap(bean));

        return intent;
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_fund_detail);
        ViewUtils.inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        updataTitle();
        initView();
    }

    private void handleExtras(Bundle extras) {
        if (extras != null) {
            mFundBean = Parcels.unwrap(extras.getParcelable(EXTRA_FUND));
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void updataTitle() {
        if (null != mFundBean) {
            setTitle(mFundBean.getName());
            setTitleTipString(
                    mFundBean.tradeDay);
        }
//        if (null != mCombinationBean) {
//            updateTitleBackgroudByValue(mCombinationBean.getNetvalue() - 1);
//        }
    }


    private void initView() {

        String[] stockListTiles = getResources().getStringArray(R.array.fund_tab_titles);
        hsTitleTab.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));


        vsNormalFundHeader.inflate();
//        localFloatingActionMenu.attachToScrollView(mScrollView);
//        localFloatingActionMenu.setOnMenuItemSelectedListener(mFloatMenuSelectListner);
//        if (isMyCombination) {
//            yanbaoView.setVisibility(View.VISIBLE);
//            yanbaoView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    startActivity(CombinationNewsActivity
//                            .newIntent(CombinationDetailActivity.this, mCombinationBean));
//                }
//            });
//        } else {
//            yanbaoView.setVisibility(View.GONE);
//        }
//
//        replaceTrendView();
//        replacePostionView();
//        replaceCompareView();
//
//        mChangeFollowView = new ChangeFollowView(this);

    }


}
