package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.KChartDataListener;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.UIUtils;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/12/14.
 */
public class StockLandActivity extends FragmentActivity implements StockViewCallBack, KChartDataListener, LandStockViewCallBack {
    private StockLandView landStockview;
    private SelectStockBean mStockBean;
    private StockQuotesBean mStockQuotesBean;
    private int tabPosition;
    private String checkValue = "0";
    private static final String STOCK_BEAN = "stock_bean";
    private static final String QUOTE_BEAN = "quote_bean";
    private static final String TAB_POSITION = "tab_position";
    private static final String DAY_KCHART = "day_kchart";
    private static final String WEEK_KCHART = "week_kchart";
    private static final String MONTH_KCHART = "month_kchart";

    public static Intent getIntent(Context context, SelectStockBean stockBean, StockQuotesBean quotesBean, int tabPosition, ArrayList<OHLCEntity> mDayKChart, ArrayList<OHLCEntity> mWeekKChart, ArrayList<OHLCEntity> mMonthKChart) {
        Intent intent = new Intent(context, StockLandActivity.class);
        intent.putExtra(STOCK_BEAN, stockBean);
        intent.putExtra(QUOTE_BEAN, quotesBean);
        intent.putExtra(TAB_POSITION, tabPosition);
        intent.putExtra(DAY_KCHART, mDayKChart);
        intent.putExtra(WEEK_KCHART, mWeekKChart);
        intent.putExtra(MONTH_KCHART, mMonthKChart);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        landStockview = new StockLandView(this);
        setContentView(landStockview);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handle(extras);
        }
        initLandStockView();

    }

    private void handle(Bundle extras) {
        mStockBean = (SelectStockBean) extras.getSerializable(STOCK_BEAN);
        mStockQuotesBean = (StockQuotesBean) extras.getSerializable(QUOTE_BEAN);
        tabPosition = extras.getInt(TAB_POSITION);
        mDayKChart = (ArrayList<OHLCEntity>) extras.getSerializable(DAY_KCHART);
        mWeekKChart = (ArrayList<OHLCEntity>) extras.getSerializable(WEEK_KCHART);
        mMonthKChart = (ArrayList<OHLCEntity>) extras.getSerializable(MONTH_KCHART);
    }

    private void initLandStockView() {
        landStockview.setStockViewCallback(this);
        landStockview.setKChartDataListener(this);
        DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
        this.landStockview.setLandStockCallBack(this);
        this.landStockview.setStockBean(mStockBean);
        ViewHelper.setPivotY(landStockview, localDisplayMetrics.widthPixels);
        if (null != mStockQuotesBean) {
            landStockview.updateLandStockView(mStockQuotesBean);
        }

    }

    @Override
    public void landViewFadeOut() {
        setResult();
        finish();
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra("day_data", mDayKChart);
        intent.putExtra("week_data", mWeekKChart);
        intent.putExtra("month_data", mMonthKChart);
        intent.putExtra("tab_positon", tabPosition);
        setResult(0, intent);
    }

    @Override
    public void setViewType(int paramInt) {

    }

    @Override
    public void stockMarkShow() {

    }

    private ArrayList<OHLCEntity> mDayKChart;
    private ArrayList<OHLCEntity> mWeekKChart;
    private ArrayList<OHLCEntity> mMonthKChart;

    @Override
    public List<OHLCEntity> getDayLineDatas() {
        return this.mDayKChart;
    }

    @Override
    public void setDayKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mDayKChart = (ArrayList<OHLCEntity>) kLineDatas;

    }

    @Override
    public List<OHLCEntity> getMonthLineDatas() {
        return this.mMonthKChart;
    }

    @Override
    public void setMonthKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mMonthKChart = (ArrayList<OHLCEntity>) kLineDatas;
    }

    @Override
    public List<OHLCEntity> getWeekLineDatas() {
        return this.mWeekKChart;
    }

    @Override
    public void setWeekKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mWeekKChart = (ArrayList<OHLCEntity>) kLineDatas;
        BusProvider.getInstance().post(kLineDatas);
    }

    @Override
    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    @Override
    public String getCheckValue() {
        return checkValue;
    }

    private int stickType = 0;

    @Override
    public int getStickType() {
        return stickType;
    }

    @Override
    public StockQuotesBean getStockQuotesBean() {
        return mStockQuotesBean;
    }

    @Override
    public void setStickType(int stickValue) {
        this.stickType = stickValue;
    }

    @Override
    public int getTabPosition() {
        return tabPosition;
    }

    @Override
    public void setTabPosition(int position) {
        this.tabPosition = position;
    }

    @Override
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
        //统计时长
        MobclickAgent.onResume(this);
        StatService.onPageStart(this, UIUtils.getResString(this, R.string.statistics_trend_landscape));
        MobclickAgent.onPageStart(UIUtils.getResString(this, R.string.statistics_trend_landscape));
    }

    protected void onPause() {
        super.onPause();
        StatService.onPageEnd(this, UIUtils.getResString(this, R.string.statistics_trend_landscape));
        MobclickAgent.onPageEnd(UIUtils.getResString(this, R.string.statistics_trend_landscape));
        MobclickAgent.onPause(this);
    }

}
