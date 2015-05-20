/**
 * @Title AbsKChartView.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-1 下午1:38:07
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.ui.widget.KChartDataListener;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName AbsKChartView
 * @Description k线图的抽象类
 * @author zjz
 * @date 2015-4-1 下午1:38:07
 * @version 1.0
 */
public abstract class AbstractKChartView extends BaseFragment {
    public static final int TYPE_CHART_DAY = 1;
    public static final int TYPE_CHART_WEEK = 2;
    public static final int TYPE_CHART_MONTH = 3;

    public static final String ARGUMENT_VIEW_TYPE = "view_type";
    public static final String ARGUMENT_STOCK_CODE = "stock_code";
    public static final String ARGUMENT_SYMBOL_TYPE = "symbol_type";

    private Integer mViewType = TYPE_CHART_DAY; // 类型，日K线，周k先，月k线
    private String mStockCode; // 股票code
    private String symbolType;
    private QuotesEngineImpl mQuotesDataEngine;

    protected static final long mPollRequestTime = 1000 * 45;

    private KChartDataListener mKChartDataListener;

    protected Bundle getBundle(Integer type, String stockcode, String symbolType) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_VIEW_TYPE, type);
        arguments.putString(ARGUMENT_STOCK_CODE, stockcode);
        arguments.putString(ARGUMENT_SYMBOL_TYPE, symbolType);
        return arguments;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
        if (mQuotesDataEngine == null) {
            mQuotesDataEngine = new QuotesEngineImpl();
        }
    }

    private void handleArguments(Bundle arguments) {
        mViewType = arguments.getInt(ARGUMENT_VIEW_TYPE);
        mStockCode = arguments.getString(ARGUMENT_STOCK_CODE);
        symbolType = arguments.getString(ARGUMENT_SYMBOL_TYPE);
    }

    /**
     * 获取K线类型，日，周，月
     * 
     * @return
     */
    public String getKLineType() {
        switch (mViewType) {
            case TYPE_CHART_DAY:
                return "d";
            case TYPE_CHART_WEEK:
                return "w";
            case TYPE_CHART_MONTH:
                return "m";
            default:
                break;
        }
        return "d";
    }

    public List<OHLCEntity> getViewTypeData() {
        if (mViewType == TYPE_CHART_DAY) {
            System.out.println("mKChartDataListener.getDayLineDatas size:"
                    + mKChartDataListener.getDayLineDatas().size());
            return mKChartDataListener.getDayLineDatas();
        } else if (mViewType == TYPE_CHART_WEEK) {
            return mKChartDataListener.getWeekLineDatas();
        } else if (mViewType == TYPE_CHART_MONTH) {
            return mKChartDataListener.getMonthLineDatas();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public void setViewTypeData(List<OHLCEntity> lineDatas) {
        if (mViewType == TYPE_CHART_DAY) {
            mKChartDataListener.setDayKlineDatas(lineDatas);
        } else if (mViewType == TYPE_CHART_WEEK) {
            mKChartDataListener.setWeekKlineDatas(lineDatas);
        } else if (mViewType == TYPE_CHART_MONTH) {
            mKChartDataListener.setMonthKlineDatas(lineDatas);
        }
    }

    public KChartDataListener getKChartDataListener() {
        return mKChartDataListener;
    }

    public void setKChartDataListener(KChartDataListener mKChartDataListener) {
        this.mKChartDataListener = mKChartDataListener;
    }

    public Integer getViewType() {
        return mViewType;
    }

    public void setViewType(Integer type) {
        this.mViewType = type;
    }

    public String getStockCode() {
        return mStockCode;
    }

    public void setStockCode(String mStockCode) {
        this.mStockCode = mStockCode;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    public QuotesEngineImpl getQuotesDataEngine() {
        return mQuotesDataEngine;
    }

    public void setQuotesDataEngine(QuotesEngineImpl mQuotesDataEngine) {
        this.mQuotesDataEngine = mQuotesDataEngine;
    }

    public void setViewVisible(boolean isVisibleToUser){};

}
