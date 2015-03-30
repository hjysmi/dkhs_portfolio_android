package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.test.UiThreadTest;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.FiveRangeItem;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.KChartsLandFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartLandFragment;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;

public class StockLandView extends RelativeLayout {

    private Context mContext;

    public StockLandView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public StockLandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public StockLandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    private void init() {
        View landStock = View.inflate(mContext, R.layout.activity_landscape_kchart, null);
        landStock.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initView(landStock);
        addView(landStock);
    }

    private QuotesEngineImpl mQuotesEngine;
    private StockQuotesBean mStockQuotesBean;
    private HScrollTitleView hsTitle;
    private ScrollViewPager pager;
    private ArrayList<Fragment> fragmentList;
    private StockQuotesChartLandFragment mStockQuotesChartFragment;
    private TextView landKlinTextTitle;
    private TextView landKlinTextPrice;
    private TextView landKlinTextValum;
    private TextView landKlinTextData;
    private SelectStockBean mStockBean;
    private int mViewType;
    private StockViewCallBack stockViewCallback;

    private void initView(View view) {
        mQuotesEngine = new QuotesEngineImpl();
        // landKlineLayout = (LinearLayout) findViewById(R.id.land_kline_layout);
        hsTitle = (HScrollTitleView) view.findViewById(R.id.hs_title);
        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);
        hsTitle.setLayoutWidth(UIUtils.getDisplayMetrics().heightPixels);
        view.findViewById(R.id.lank_klind_exit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != stockViewCallback) {
                    stockViewCallback.landViewFadeOut();
                }
            }
        });
        landKlinTextTitle = (TextView) view.findViewById(R.id.land_klin_text_title);
        landKlinTextPrice = (TextView) view.findViewById(R.id.land_klin_text_price);
        landKlinTextValum = (TextView) view.findViewById(R.id.land_klin_text_valum);
        landKlinTextData = (TextView) view.findViewById(R.id.land_klin_text_data);
        if (mViewType != 0)
            hsTitle.setSelectIndex(mViewType);

        pager = (ScrollViewPager) view.findViewById(R.id.pager);
        pager.setCanScroll(false);
        // stockLayout.setOnTouchListener(new OnLayoutlistener());

        // setupViewData();
        // scrollview + listview 会滚动到底部，需要滚动到头部
        // setAddOptionalButton();
    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                /*
                 * if(position == 0){
                 * landKlineLayout.setVisibility(View.GONE); }else{
                 * landKlineLayout.setVisibility(View.VISIBLE); }
                 */
                // pager.setCurrentItem(position);
                showView(position);
                if (null != mLandStockCallBack) {
                    mLandStockCallBack.setTabPosition(view_position);
                }
                // PortfolioApplication.getInstance().setkLinePosition(position);
            }
        }
    };

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == View.VISIBLE) {
            if (null != mQuotesEngine && mStockBean != null) {
                // mQuotesEngine.quotes(mStockBean.code, listener);
                if (mLandStockCallBack.getTabPosition() != view_position) {
                    showView(mLandStockCallBack.getTabPosition());
                }
            }
            this.fragmentList.get(view_position).setUserVisibleHint(true);
            // this.requestFocus();
        } else {
            this.fragmentList.get(view_position).setUserVisibleHint(false);
        }
    };

    public void updateLandStockView(StockQuotesBean stockBean) {
        if (null != stockBean) {
            this.mStockQuotesBean = stockBean;
            mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
            // landKlinTextTitle.setText(object.getName());
            landKlinTextPrice.setText(stockBean.getCurrent() + "");
            landKlinTextPrice.setTextColor(ColorTemplate.getUpOrDrownCSL(stockBean.getPercentage()));
            landKlinTextValum.setText(UIUtils.getshou(stockBean.getVolume()));
            landKlinTextData.setText(TimeUtils.getTimeString(stockBean.getMoment()));
        }
    }

    private boolean isFloatText(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    private void initTabPage() {

        FragmentActivity activity = (FragmentActivity) mContext;

        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        mStockQuotesChartFragment = StockQuotesChartLandFragment.newInstance(
                StockQuotesChartLandFragment.TREND_TYPE_TODAY, mStockBean.code);
        KChartsLandFragment fragment = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_DAY,
                mStockBean.code, mStockBean.symbol_type);
        KChartsLandFragment fragment2 = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_WEEK,
                mStockBean.code, mStockBean.symbol_type);
        KChartsLandFragment fragment3 = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_MONTH,
                mStockBean.code, mStockBean.symbol_type);
        fragmentList.add(mStockQuotesChartFragment);
        fragmentList.add(fragment);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);

        mStockQuotesChartFragment.setLandCallBack(mLandStockCallBack);
        fragment.setLandCallBack(mLandStockCallBack);
        fragment2.setLandCallBack(mLandStockCallBack);
        fragment3.setLandCallBack(mLandStockCallBack);
        // fragment.setUserVisibleHint(true);

        FragmentTransaction ftransaction = activity.getSupportFragmentManager().beginTransaction();

        ftransaction.add(R.id.land_page, (Fragment) this.fragmentList.get(0), String.valueOf(0));
        ftransaction.add(R.id.land_page, (Fragment) this.fragmentList.get(1), String.valueOf(1));
        ftransaction.add(R.id.land_page, (Fragment) this.fragmentList.get(2), String.valueOf(2));
        ftransaction.add(R.id.land_page, (Fragment) this.fragmentList.get(3), String.valueOf(3));
        ftransaction.show(mStockQuotesChartFragment);
        ftransaction.hide(fragment);
        ftransaction.hide(fragment2);
        ftransaction.hide(fragment3);
        ftransaction.commit();

        // ftransaction.replace(R.id.land_page, fragment).commit();
        // fragment3.setITouchListener(this);
        // fragmentList.add(fragment3);
        // fragmentList.add(new TestFragment());

        // pager.setOffscreenPageLimit(4);

        // pager.removeAllViews();
        // pager.setAdapter(new MyPagerFragmentAdapter(activity.getSupportFragmentManager(), fragmentList));
        // TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        // indicator.setViewPager(pager);
        // showView(view_position);

    }

    private int view_position = 0;

    public void showView(int postion) {
        if (view_position != postion) {

            FragmentActivity activity = (FragmentActivity) mContext;
            activity.getSupportFragmentManager().beginTransaction().show((Fragment) this.fragmentList.get(postion))
                    .commitAllowingStateLoss();
            this.fragmentList.get(postion).setUserVisibleHint(true);
            this.fragmentList.get(view_position).setUserVisibleHint(false);
            activity.getSupportFragmentManager().beginTransaction()
                    .hide((Fragment) this.fragmentList.get(view_position)).commitAllowingStateLoss();
            view_position = postion;

        }

    }

    public void loadMore() {
        ((KChartsLandFragment) this.fragmentList.get(view_position)).loadMordKline();
    }

    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        // private String[] titleList;
        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2) {
            super(fm);
            this.fragmentList = fragmentList2;
            // this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return (titleList.length > position) ? titleList[position] : "";
            return "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    public void setStockBean(SelectStockBean bean) {
        this.mStockBean = bean;
        if (null != mStockBean) {
            landKlinTextTitle.setText(mStockBean.name);
        }
        initTabPage();
    }

    public void setViewType(int type) {
        this.mViewType = type;
    }

    public StockViewCallBack getStockViewCallback() {
        return stockViewCallback;
    }

    public void setStockViewCallback(StockViewCallBack stockViewCallback) {
        this.stockViewCallback = stockViewCallback;
    }

    private LandStockViewCallBack mLandStockCallBack;

    public LandStockViewCallBack getLandStockCallBack() {
        return mLandStockCallBack;
    }

    public void setLandStockCallBack(LandStockViewCallBack mLandStockCallBack) {
        this.mLandStockCallBack = mLandStockCallBack;
    }

}
