package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.PagerFragmentAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.FundManagerFragment;
import com.dkhs.portfolio.ui.fragment.FundProfileFragment;
import com.dkhs.portfolio.ui.fragment.FundTrendFragment;
import com.dkhs.portfolio.ui.widget.BenefitChartView;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.melnykov.fab.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * 基金详情页
 * Created by zjz on 2015/6/4.
 */
public class FundDetailActivity extends ModelAcitivity implements View.OnClickListener {

    public static final String EXTRA_FUND = "extra_fund";
    private SelectStockBean mFundBean;

    public FundQuoteBean mFundQuoteBean;


    @ViewInject(R.id.hs_title)
    private HScrollTitleView hsTitleTab;

    @ViewInject(R.id.layout_sepfund_header)
    private ViewStub vsSepFundHeader;
    @ViewInject(R.id.layout_noramlfund_header)
    private ViewStub vsNormalFundHeader;

    @ViewInject(R.id.floating_action_view)
    private FloatingActionMenu mFloatMenu;


    @ViewInject(R.id.pager)
    private ScrollViewPager pager;

    private TextView tvWanshou;
    private TextView tvQirinianhua;

    private TextView tvNetvalue;
    private TextView tvUpPrice;
    private TextView tvUpPrecent;
    private TextView tvPreNetvalue;
    private TextView tvAllNetvalue;


    private Button btnRefresh;

    private QuotesEngineImpl mQuotesEngine;
    private VisitorDataEngine mVisitorDataEngine;
    private List<SelectStockBean> localList;

    private ArrayList<Fragment> fragmentList;

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
        mQuotesEngine = new QuotesEngineImpl();
        mVisitorDataEngine = new VisitorDataEngine();
        initTitle();
        initView();
        // 已优化的地方 ：减少数据库操作、使用异步进行查询自选股列表
        if (!PortfolioApplication.hasUserLogin()) {
            getLocalOptionList();
        }

        changeFollowView = new ChangeFollowView(this);
        changeFollowView.setmChangeListener(changeFollowListener);
        requestData();
    }

    private void handleExtras(Bundle extras) {
        if (extras != null) {
            mFundBean = Parcels.unwrap(extras.getParcelable(EXTRA_FUND));
        }

    }

    private void getLocalOptionList() {
        new Thread() {
            public void run() {
                localList = mVisitorDataEngine.getOptionalFundList();
            }

        }.start();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        BusProvider.getInstance().register(this);

    }


    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void initTitle() {
        if (null != mFundBean) {

            String nameText = mFundBean.getName();
            if (!TextUtils.isEmpty(nameText) && nameText.length() > 8) {
                nameText = nameText.substring(0, 8) + "…";
            }
            nameText = nameText + "(" + mFundBean.getSymbol() + ")";

            setTitle(nameText);
            setTitleTipString(mFundBean.tradeDay);
        }

    }


    private void updataTitle() {
        if (null != mFundQuoteBean) {
            setTitleTipString(mFundQuoteBean.getTradedate());
        }
    }


    private void initView() {

        String[] stockListTiles = getResources().getStringArray(R.array.fund_tab_titles);
        hsTitleTab.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitleTab.setSelectPositionListener(titleSelectPostion);
        mFloatMenu.attachToScrollView((ObservableScrollView) findViewById(R.id.sv_combinations));
        mFloatMenu.setOnMenuItemSelectedListener(mFloatMenuSelectListner);
        if (StockUitls.isSepFund(mFundBean.symbol_stype)) {
            View header = vsSepFundHeader.inflate();
            tvWanshou = (TextView) header.findViewById(R.id.tv_wanshou_value);
            tvQirinianhua = (TextView) header.findViewById(R.id.tv_qirinihua);
        } else {
            View header = vsNormalFundHeader.inflate();
            tvNetvalue = (TextView) header.findViewById(R.id.tv_current_price);
            tvUpPrice = (TextView) header.findViewById(R.id.tv_up_price);
            tvUpPrecent = (TextView) header.findViewById(R.id.tv_percentage);
            tvPreNetvalue = (TextView) header.findViewById(R.id.tv_pre_netvalue);
            tvAllNetvalue = (TextView) header.findViewById(R.id.tv_all_netvalue);
        }


        btnRefresh = getSecondRightButton();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);
        // btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        btnRefresh.setOnClickListener(this);

//        replaceTrendView();


//
//        mChangeFollowView = new ChangeFollowView(this);

    }

    HScrollTitleView.ISelectPostionListener titleSelectPostion = new HScrollTitleView.ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);

            }
        }
    };


    private void replaceTrendView() {

        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        fragmentList.add(FundTrendFragment.newInstance(BenefitChartView.FundTrendType.OfficeDay, mFundQuoteBean));
        fragmentList.add(FundTrendFragment.newInstance(BenefitChartView.FundTrendType.Month, mFundQuoteBean));
        fragmentList.add(FundTrendFragment.newInstance(BenefitChartView.FundTrendType.Season, mFundQuoteBean));
        fragmentList.add(FundTrendFragment.newInstance(BenefitChartView.FundTrendType.OneYear, mFundQuoteBean));
        fragmentList.add(FundTrendFragment.newInstance(BenefitChartView.FundTrendType.ToYear, mFundQuoteBean));

        pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.removeAllViews();
        pager.setCanScroll(false);
        pager.setOffscreenPageLimit(5);
        pager.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), fragmentList));


    }

    private FundManagerFragment mFragmentManager;

    private void replaceManagerView() {
//        if (null == mFragmentManager) {
        mFragmentManager = FundManagerFragment.newInstance(mFundQuoteBean.getManagers());
//        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fund_manager_view, mFragmentManager).commit();

    }

    private FundProfileFragment mFragmentProfile;

    private void replaceFundProfile() {
//        if (null == mFragmentProfile) {
        mFragmentProfile = FundProfileFragment.newIntent(mFundQuoteBean);
//        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fund_overview, mFragmentProfile).commit();

    }


    private final int MENU_FOLLOW = 1;
    private final int MENU_DELFOLLOW = 2;
    private final int MENU_REMIND = 3;
    private final int MENU_SHARE = 6;

    FloatingActionMenu.OnMenuItemSelectedListener mFloatMenuSelectListner = new FloatingActionMenu.OnMenuItemSelectedListener() {

        @Override
        public boolean onMenuItemSelected(int selectIndex) {

            switch (selectIndex) {

                case MENU_FOLLOW:
                case MENU_DELFOLLOW: {
                    handFollowOrUnfollowAction();
                }
                break;
                case MENU_REMIND: {
                    if (!UIUtils.iStartLoginActivity(FundDetailActivity.this)) {


                        startActivity(StockRemindActivity.newStockIntent(FundDetailActivity.this, mFundBean, true));
                    }

                }
                break;
                case MENU_SHARE: {

                }
                break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.btn_right_second: {
                // rotateRefreshButton();
                // quoteHandler.removeCallbacks(updateRunnable);
                requestData();

            }
            break;
            default:
                break;
        }
    }

    private ChangeFollowView changeFollowView;

    private void handFollowOrUnfollowAction() {
        final SelectStockBean selectBean = SelectStockBean.copy(mFundQuoteBean);
        if (null != changeFollowView && null != selectBean) {
            changeFollowView.changeFollow(selectBean);
        }
    }

    private ChangeFollowView.IChangeSuccessListener changeFollowListener = new ChangeFollowView.IChangeSuccessListener() {
        @Override
        public void onChange(boolean isFollow) {
            mFundQuoteBean.setFollowed(isFollow);
            setAddOptionalButton();
            if (!PortfolioApplication.hasUserLogin()) {
                getLocalOptionList();
            }
        }
    };


    //    @Subscribe
//    public void updateCombination(UpdateCombinationEvent updateCombinationEvent) {
//        if (null != updateCombinationEvent && null != updateCombinationEvent.mCombinationBean) {
//            initFloatingActionMenu();
//        }
//    }

    private void initFloatingActionMenu(QuotesBean quoteBean) {
        mFloatMenu.removeAllItems();

        if (quoteBean.isFollowed()) {
            mFloatMenu.addItem(MENU_DELFOLLOW, R.string.float_menu_delfollow,
                    R.drawable.btn_del_item_normal);
            mFloatMenu.addItem(MENU_REMIND, R.string.float_menu_remind, R.drawable.ic_fm_remind);
//            mFloatMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);
        } else {
            mFloatMenu.addItem(MENU_FOLLOW, R.string.float_menu_follow, R.drawable.ic_add);
//            mFloatMenu.addItem(MENU_SHARE, R.string.float_menu_share, R.drawable.ic_fm_share);

        }

    }


    private void rotateRefreshButton() {

        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing), null,
                null, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        btnRefresh.startAnimation(animation);
        // btnRefresh.startAnimation(ani);
    }

    private void stopRefreshAnimation() {
        btnRefresh.clearAnimation();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh), null,
                null, null);
    }

    private void requestData() {
        if (null != mQuotesEngine && mFundBean != null) {
            rotateRefreshButton();
            mQuotesEngine.quotes(mFundBean.symbol, quoteListener);
        }
    }

    ParseHttpListener quoteListener = new ParseHttpListener<FundQuoteBean>() {

        @Override
        protected FundQuoteBean parseDateTask(String jsonData) {

            FundQuoteBean fundQuoteBean = null;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject jsonOb = jsonArray.getJSONObject(0);
                fundQuoteBean = DataParse.parseObjectJson(FundQuoteBean.class, jsonOb);
//                if (null != stockQuotesBean && UIUtils.roundAble(stockQuotesBean)) {
//                    quoteHandler.removeCallbacks(updateRunnable);
//                } else {
//                    quoteHandler.postDelayed(updateRunnable, mPollRequestTime);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return fundQuoteBean;
        }

        @Override
        protected void afterParseData(FundQuoteBean object) {

            stopRefreshAnimation();
            if (null != object) {
                mFundQuoteBean = object;
                updateFundView();
//                if (null != landStockview) {
//                    landStockview.updateLandStockView(mStockQuotesBean);
//                }
//                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
                setAddOptionalButton();
            }
        }
    };


    private void setAddOptionalButton() {
        if (mFundQuoteBean == null) {
            mFloatMenu.setVisibility(View.GONE);
            return;
        }
        mFloatMenu.setVisibility(View.VISIBLE);

        if (!PortfolioApplication.hasUserLogin()) {
            SelectStockBean selectBean = new SelectStockBean();
            selectBean.id = mFundQuoteBean.getId();
            selectBean.code = mFundQuoteBean.getCode();
            if (localList != null && localList.contains(selectBean)) {
                mFundQuoteBean.setFollowed(true);
            }
        }

        initFloatingActionMenu(mFundQuoteBean);

    }


    private void updateFundView() {
        updataTitle();
        updateNetValue();
        replaceFundProfile();
        replaceManagerView();
        replaceTrendView();
    }

    private void updateNetValue() {
        ColorStateList cls = null;
        if (StockUitls.isSepFund(mFundBean.symbol_stype)) {
            cls = ColorTemplate.getUpOrDrownCSL(mFundQuoteBean.getYear_yld());
            tvWanshou.setTextColor(ColorTemplate.getUpOrDrownCSL(mFundQuoteBean.getNet_value()));
            tvWanshou.setTextColor(cls);
            tvQirinianhua.setTextColor(cls);
            tvWanshou.setText(mFundQuoteBean.getTenthou_unit_incm() + "");
            tvQirinianhua.setText(StringFromatUtils.get2PointPercent(mFundQuoteBean.getYear_yld()));

        } else {
            cls = ColorTemplate.getUpOrDrownCSL(mFundQuoteBean.getPercentage());
            tvNetvalue.setTextColor(cls);
            tvUpPrice.setTextColor(cls);
            tvUpPrecent.setTextColor(cls);

            tvNetvalue.setText(mFundQuoteBean.getNet_value() + "");
            tvAllNetvalue.setText(mFundQuoteBean.getNet_cumulative() + "");
            tvUpPrecent.setText(StringFromatUtils.get2PointPercentPlus(mFundQuoteBean.getPercentage()));
            tvUpPrice.setText(StringFromatUtils.get2PointPlus(mFundQuoteBean.getChange()));
            tvPreNetvalue.setText(mFundQuoteBean.getLast_net_value() + "");
        }
    }


}
