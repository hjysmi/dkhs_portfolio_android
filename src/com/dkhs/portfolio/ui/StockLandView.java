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
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
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
        view.findViewById(R.id.lank_klind_exit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != stockViewCallback) {
                    stockViewCallback.fadeOut();
                }
            }
        });
        landKlinTextTitle = (TextView) view.findViewById(R.id.land_klin_text_title);
        landKlinTextPrice = (TextView) view.findViewById(R.id.land_klin_text_price);
        landKlinTextValum = (TextView) view.findViewById(R.id.land_klin_text_valum);
        landKlinTextData = (TextView) view.findViewById(R.id.land_klin_text_data);
        if (mViewType != 0)
            hsTitle.setSelectIndex(mViewType);
        if (null != mStockBean) {
            landKlinTextTitle.setText(mStockBean.name);
        }

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
                pager.setCurrentItem(position);
                PortfolioApplication.getInstance().setkLinePosition(position);
            }
        }
    };

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == View.VISIBLE) {
            if (null != mQuotesEngine && mStockBean != null) {
                mQuotesEngine.quotes(mStockBean.code, listener);
            }
            this.requestFocus();
        }
    };

    ParseHttpListener listener = new ParseHttpListener<StockQuotesBean>() {

        @Override
        protected StockQuotesBean parseDateTask(String jsonData) {
            StockQuotesBean stockQuotesBean = null;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject jsonOb = jsonArray.getJSONObject(0);
                stockQuotesBean = DataParse.parseObjectJson(StockQuotesBean.class, jsonOb);
                if (null != stockQuotesBean && UIUtils.roundAble(stockQuotesBean)) {
                    // quoteHandler.removeCallbacks(runnable);
                }
                List<FiveRangeItem> buyList = new ArrayList<FiveRangeItem>();
                List<FiveRangeItem> sellList = new ArrayList<FiveRangeItem>();
                int i = 0;
                for (; i < 5; i++) {
                    // String buyPrice :
                    // stockQuotesBean.getBuyPrice().getBuyPrice()
                    FiveRangeItem buyItem = new FiveRangeItem();
                    if (i < stockQuotesBean.getBuyPrice().getBuyVol().size()) {
                        String buyPrice = stockQuotesBean.getBuyPrice().getBuyPrice().get(i);
                        if (isFloatText(buyPrice)) {
                            buyItem.price = Float.parseFloat(buyPrice);
                        } else {
                            buyItem.price = 0;
                        }
                        String volText = stockQuotesBean.getBuyPrice().getBuyVol().get(i);
                        if (isFloatText(volText)) {
                            buyItem.vol = Integer.parseInt(volText);
                        } else {
                            buyItem.vol = 0;
                        }
                    } else {
                        buyItem.vol = 0;
                    }
                    buyItem.tag = "" + (i + 1);
                    buyList.add(buyItem);
                }
                for (int j = 4; j >= 0; j--) {
                    FiveRangeItem sellItem = new FiveRangeItem();
                    if (j < stockQuotesBean.getSellPrice().getSellVol().size()) {
                        String sellPrice = stockQuotesBean.getSellPrice().getSellPrice().get(j);
                        if (isFloatText(sellPrice)) {
                            sellItem.price = Float.parseFloat(sellPrice);
                        } else {
                            sellItem.price = 0;
                        }
                        // sellItem.price =
                        // Float.parseFloat(stockQuotesBean.getSellPrice().getSellPrice().get(j));
                        String sellVol = stockQuotesBean.getSellPrice().getSellVol().get(j);
                        if (isFloatText(sellVol)) {
                            sellItem.vol = Integer.parseInt(sellVol);
                        } else {
                            sellItem.vol = 0;
                        }
                    } else {
                        sellItem.vol = 0;
                    }
                    sellItem.tag = "" + (j + 1);
                    sellList.add(sellItem);
                }
                if (null != stockQuotesBean) {

                    stockQuotesBean.setBuyList(buyList);
                    stockQuotesBean.setSellList(sellList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stockQuotesBean;
        }

        @Override
        protected void afterParseData(StockQuotesBean object) {
            // requestUiHandler.sendEmptyMessage(MSG_WHAT_AFTER_REQUEST);
            if (null != object) {
                mStockQuotesBean = object;
                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
                // landKlinTextTitle.setText(object.getName());
                landKlinTextPrice.setText(object.getCurrent() + "");
                landKlinTextPrice.setTextColor(ColorTemplate.getUpOrDrownCSL(object.getPercentage()));
                landKlinTextValum.setText(UIUtils.getshou(object.getVolume()));
                landKlinTextData.setText(TimeUtils.getTimeString(object.getMoment()));
            }
        }
    };

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
        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        mStockQuotesChartFragment = StockQuotesChartLandFragment.newInstance(
                StockQuotesChartLandFragment.TREND_TYPE_TODAY, mStockBean.code);
        // mStockQuotesChartFragment.setITouchListener(this);
        fragmentList.add(mStockQuotesChartFragment);
        KChartsLandFragment fragment = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_DAY,
                mStockBean.code, mStockBean.symbol_type);
        // fragment.setITouchListener(this);
        fragmentList.add(fragment);
        KChartsLandFragment fragment2 = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_WEEK,
                mStockBean.code, mStockBean.symbol_type);
        // fragment2.setITouchListener(this);
        fragmentList.add(fragment2);
        KChartsLandFragment fragment3 = KChartsLandFragment.getKChartFragment(KChartsLandFragment.TYPE_CHART_MONTH,
                mStockBean.code, mStockBean.symbol_type);
        // fragment3.setITouchListener(this);
        fragmentList.add(fragment3);
        // fragmentList.add(new TestFragment());

        // pager.setOffscreenPageLimit(4);
        FragmentActivity activity = (FragmentActivity) mContext;
        pager.setAdapter(new MyPagerFragmentAdapter(activity.getSupportFragmentManager(), fragmentList));
        // TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        // indicator.setViewPager(pager);
    }

    public void loadMore() {
        ((KChartsLandFragment) fragmentList.get(pager.getCurrentItem())).loadMordKline();
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

}
