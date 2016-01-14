/**
 * @Title TrendChartFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FSDataBean;
import com.dkhs.portfolio.bean.FSDataBean.TimeStock;
import com.dkhs.portfolio.bean.FiveRangeItem;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.FiveRangeAdapter;
import com.dkhs.portfolio.ui.widget.FSLinePointEntity;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.LinePointEntity;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.TimesharingplanChart;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;

import org.parceler.Parcels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-3 上午10:32:39
 */
public class StockQuotesChartFragment extends BaseFragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";
    public static final String ARGUMENT_STOCK_CODE = "stock_code";

    // private static final String SAVED_LIST_POSITION = "list_position";

    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType;
    private boolean isTodayNetValue;

    private TimesharingplanChart mMaChart;

    private QuotesEngineImpl mQuotesDataEngine;
    private CombinationBean mCombinationBean;
    //
    private FiveRangeAdapter mBuyAdapter, mSellAdapter;

    private View viewFiveRange;

    // private long mStockId;
    private String mStockCode;
    private LineEntity fenshiPiceLine;
    private LineEntity averageLine;

    private SelectStockBean mSelectStockBean;
    private RelativeLayout pb;

    // public static final String TREND_TYPE_TODAY="trend_today";
    public static StockQuotesChartFragment newInstance(String trendType, String stockCode) {
        StockQuotesChartFragment fragment = new StockQuotesChartFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TREND_TYPE, trendType);
        arguments.putString(ARGUMENT_STOCK_CODE, stockCode);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_stock_quotes_chart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // System.out.println("====StockQuotesChartFragment=onCreate=====");

        // handle fragment arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // restore saved state
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        fenshiPiceLine = new LineEntity();
        averageLine = new LineEntity();
        averageLine.setLineColor(PortfolioApplication.getInstance().getResources().getColor(R.color.orange));
        // MA5.setTitle("MA5");
        // MA5.setLineColor(ColorTemplate.getRaddomColor())
        fenshiPiceLine.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
        mBuyAdapter = new FiveRangeAdapter(getActivity(), true, mSelectStockBean.symbol, false);
        mSellAdapter = new FiveRangeAdapter(getActivity(), false, mSelectStockBean.symbol, false);
        // mBuyAdapter.setList(getDates(5), mSelectStockBean.code);
        // mSellAdapter.setList(getDates(-5), mSelectStockBean.code);
        // fenshiPiceLine.setLineData(lineDataList);

    }

    private List<FiveRangeItem> getDates(int k) {
        List<FiveRangeItem> list = new ArrayList<FiveRangeItem>();
        FiveRangeItem fr;
        int tmp;
        tmp = k + 6;
        for (int i = 0; i < 5; i++) {
            fr = new FiveRangeItem();
            fr.price = 0;
            if (k > 0) {
                fr.tag = k + "";
                k--;
            } else {
                fr.tag = tmp + "";
                tmp++;
            }
            fr.vol = 0;
            list.add(fr);
        }
        return list;
    }

    private void handleExtras(Bundle extras) {
//        mSelectStockBean = (SelectStockBean) extras.getSerializable(StockQuotesActivity.EXTRA_STOCK);
        mSelectStockBean = Parcels.unwrap(extras.getParcelable(StockQuotesActivity.EXTRA_STOCK));
        // System.out.println("mSelectStockBean type:" + mSelectStockBean.symbol_type);
    }

    /**
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mQuotesDataEngine = new QuotesEngineImpl();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);
        mStockCode = arguments.getString(ARGUMENT_STOCK_CODE);
        if (trendType.equals(TREND_TYPE_TODAY)) {
            isTodayNetValue = true;
        }
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
    }

    // private void handleExtras(Bundle extras) {
    // // TODO private void handleExtras(Bundle extras) {
    // // mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
    // // if (null != mCombinationBean) {
    // SelectStockBean mSelectBean = (SelectStockBean) extras.getSerializable(StockQuotesActivity.EXTRA_STOCK);
    // if (null != mSelectBean) {
    // mStockId = mSelectBean.id;
    // mStockCode = mSelectBean.code;
    // }
    //
    // // }
    // }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * @param view
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        mMaChart = (TimesharingplanChart) view.findViewById(R.id.timesharingchart);
        mMaChart.setCallBack((StockViewCallBack) getActivity());
        mMaChart.setLongitudeColor(getResources().getColor(R.color.drivi_line));
        mMaChart.setLatitudeColor(getResources().getColor(R.color.drivi_line));
        initMaChart(mMaChart);
        initView(view);
    }

    private void initView(View view) {
        viewFiveRange = view.findViewById(R.id.rl_fiverange);
        if (mSelectStockBean != null && null != mSelectStockBean.symbol_type
                && mSelectStockBean.symbol_type.equalsIgnoreCase(StockUitls.SYMBOLTYPE_INDEX)) {
            viewFiveRange.setVisibility(View.GONE);
            mMaChart.resetLayoutWeight(0, 0);
        } else {

            ListView mListviewBuy = (ListView) view.findViewById(R.id.list_five_range_buy);
            ListView mListviewSell = (ListView) view.findViewById(R.id.list_five_range_sall);
            mBuyAdapter.setContainerView(mListviewBuy);
            mListviewBuy.setAdapter(mBuyAdapter);
            mSellAdapter.setContainerView(mListviewSell);
            mListviewSell.setAdapter(mSellAdapter);
        }
        // tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        // tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        // tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        // tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        // tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
        // tvStartText = (TextView) view.findViewById(R.id.tv_netvalue_text);
        // tvEndText = (TextView) view.findViewById(R.id.tv_updown_text);
        // tvIncreaseText = (TextView) view.findViewById(R.id.tv_increase_text);
    }

    private void initMaChart(TrendChart machart) {

        machart.setMaxValue(120);
        machart.setMinValue(0);

        // machart.setDisplayAxisXTitle(true);
        // machart.setDisplayAxisYTitle(true);
        // machart.setDisplayLatitude(true);
        // machart.setDisplayLongitude(true);
        machart.setFill(true);
        machart.setDrawFirstLineInfo(true);

        // machart.setITouchListener(mTouchListener);
        List<String> ytitle = new ArrayList<String>();
        List<String> rightYtitle = new ArrayList<String>();

        ytitle.add("—");
        ytitle.add("—");
        ytitle.add("—");
        ytitle.add("—");
        ytitle.add("—");

        mMaChart.setAxisYTitles(ytitle);
        mMaChart.setDisplayAxisYTitleColor(false);
        mMaChart.setDisplayYRightTitleByZero(true);
        if (isTodayNetValue) {
            initTodayTrendTitle();
        } else {
        }

    }

    // private ITouchListener mTouchListener;

    // public void setITouchListener(ITouchListener touchListener) {
    // this.mTouchListener = touchListener;
    // }

    private void setLineData(List<FSLinePointEntity> lineDataList, List<LinePointEntity> avaLines) {
        if (isAdded()) {
            LinkedList<LineEntity> lines = new LinkedList<LineEntity>();

            fenshiPiceLine.setLineData(lineDataList);

            averageLine.setLineData(avaLines);

            lines.add(0, fenshiPiceLine);
            if (null != mSelectStockBean && !StockUitls.isIndexStock(mSelectStockBean.getSymbol_type())) {
                lines.add(averageLine);
            }
            mMaChart.setLineData(lines);
        }
    }

    // private void set

    private void initTodayTrendTitle() {

        List<String> xtitle = new ArrayList<String>();
        xtitle.add("9:30");
         xtitle.add("10:30");
         xtitle.add("11:30");
         xtitle.add("14:00");
        xtitle.add("15:00");

        mMaChart.setAxisXTitles(xtitle);
        mMaChart.setMaxPointNum(242);
        mMaChart.setDrawRightYTitle(true);

        List<String> rightYtitle = new ArrayList<String>();

        rightYtitle.add(StringFromatUtils.get2PointPercent(-1f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(1f));
        mMaChart.setAxisRightYTitles(rightYtitle);
        mMaChart.setIsBenefitDash(true);
    }

    // private void initTrendTitle() {
    // // List<String> ytitle = new ArrayList<String>();
    // // ytitle.add("1.1051");
    // // ytitle.add("1.0532");
    // // ytitle.add("1.0000");
    // // ytitle.add("1.0001");
    // // ytitle.add("1.0522");
    //
    // List<String> xtitle = new ArrayList<String>();
    // xtitle.add("2014-09-15");
    // xtitle.add("2014-09-22");
    // mMaChart.setMaxPointNum(7);
    // mMaChart.setAxisYTitles(ytitle);
    // mMaChart.setAxisXTitles(xtitle);
    // }

    private List<LinePointEntity> initMA(int length) {
        List<LinePointEntity> MA5Values = new ArrayList<LinePointEntity>();
        NetValueEngine outer = new NetValueEngine("0");
        for (int i = 0; i < length; i++) {
            // MA5Values.add((float) new Random().nextInt(99));
            LinePointEntity bean = new LinePointEntity();
            bean.setDesc("2014-09-23");
            bean.setValue(1);
            MA5Values.add(bean);
        }
        return MA5Values;

    }

    private StockQuotesBean mStockBean;

    public void setStockQuotesBean(StockQuotesBean bean) {
        if (isAdded()) {

            this.mStockBean = bean;
            mHandler.sendEmptyMessage(111);
            // System.out.println("====StockQuotesChartFragment=setStockQuotesBean=====");

        }

    }

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (viewFiveRange.getVisibility() == View.VISIBLE) {
                mBuyAdapter.setList(mStockBean.getBuyPrice().getBuyVol(), mStockBean.getBuyPrice().getBuyPrice(),
                        mStockBean.getSymbol());
                mSellAdapter.setList(mStockBean.getSellPrice().getSellVol(), mStockBean.getSellPrice().getSellPrice(),
                        mStockBean.getSymbol());
                // mSellAdapter.setList(mStockBean.getSellList(), mStockBean.getSymbol());
                mBuyAdapter.setCompareValue(mStockBean.getLastClose());
                mSellAdapter.setCompareValue(mStockBean.getLastClose());
            }
            //
            // }

            if (isStopStock() || null == fenshiLineData || fenshiLineData.size() < 1) {
                // setYTitle(mStockBean.getLastClose(), mStockBean.getLastClose() * 0.01f);
                // mMaChart.invalidate();
                setStopYTitle(mStockBean.getLastClose());
            }
            return false;
        }
    });

    private boolean isStopStock() {
        return mSelectStockBean != null && mSelectStockBean.isStop;
    }

    // @Override
    // public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    // // Checks the orientation of the screen
    // if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
    // viewFiveRange.setVisibility(View.GONE);
    // } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
    // // viewFiveRange.setVisibility(View.GONE);
    // new Handler().postDelayed(new Runnable() {
    //
    // public void run() {
    // if (isAdded() && mStockBean != null && null != viewFiveRange) {
    // if (!StockUitls.isIndexStock(mStockBean.getSymbol_type())) {
    // viewFiveRange.setVisibility(View.VISIBLE);
    // }
    // }
    //
    // }
    //
    // }, 1200);
    // }
    // };

    FSDataBean mFsDataBean = new FSDataBean();
    ParseHttpListener todayListener = new ParseHttpListener<FSDataBean>() {

        @Override
        protected FSDataBean parseDateTask(String jsonData) {

            FSDataBean fsDataBean = null;
            try {
                fsDataBean = DataParse.parseObjectJson(FSDataBean.class, jsonData);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fsDataBean;
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            pb.setVisibility(View.GONE);
        }

        ;

        @Override
        protected void afterParseData(FSDataBean fsDataBean) {

            pb.setVisibility(View.GONE);
            try {
                StockQuotesBean m = ((StockQuotesActivity) getActivity()).getmStockQuotesBean();
                if (null != m && UIUtils.roundAble(m)) {
                    dataHandler.removeCallbacks(requestRunnable);
                }

                if (fsDataBean != null) {
                    mFsDataBean.setCurtime(fsDataBean.getCurtime());
                    if (null == mFsDataBean.getMainstr()) {
                        mFsDataBean.setMainstr(fsDataBean.getMainstr());
                    } else {
                        if (fsDataBean.getMainstr() == null || fsDataBean.getMainstr().size() == 0) {
                            return;
                        }
                        mFsDataBean.getMainstr().addAll(fsDataBean.getMainstr());
                    }
                    List<TimeStock> mainList = mFsDataBean.getMainstr();

                    // List<TodayNetBean> dayNetValueList = todayNetvalue.getChartlist();

                    if (mainList != null && mainList.size() > 0) {
                        setYTitle(fsDataBean.getLast_close(), getMaxOffetValue(fsDataBean.getLast_close(), mainList));
                        setTodayPointTitle();
                        setLineData(fenshiLineData, averagelineData);
                        //
                        // String lasttime = dayNetValueList.get(dayNetValueList.size() - 1).getTimestamp();
                        // // int zIndex = lasttime.indexOf("T");
                        // Calendar calender = TimeUtils.toCalendar(lasttime);
                        // // String dateStr = lasttime.substring(0, zIndex);
                    }

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(float baseNum, List<TimeStock> mainList) {
        fenshiLineData.clear();
        averagelineData.clear();

        // float baseNum = mainList.get(0).getCurrent();
        // float baseNum = mFsDataBean.getLast_close();
        float maxNum = baseNum, minNum = baseNum;

        int minVolCount = mainList.get(0).getVolume();
        int maxVolCount = minVolCount;

        for (TimeStock bean : mainList) {
            float iPrice = bean.getCurrent();
            int volCount = bean.getVolume();

            if (iPrice > maxNum) {
                maxNum = iPrice;

            } else if (iPrice < minNum) {
                minNum = iPrice;
            }

            if (volCount < minVolCount) {
                minVolCount = volCount;
            } else if (volCount > maxVolCount) {
                maxVolCount = volCount;
            }

            FSLinePointEntity pointEntity = new FSLinePointEntity();
            LinePointEntity averagePoint = new LinePointEntity();

            // pointEntity.setDesc(TimeUtils.getTimeString(bean.getTime()));
            if (null != mStockBean && StockUitls.isShangZhengB(mStockBean.getSymbol())) {
                pointEntity.setAvgPrice(StringFromatUtils.get3Point(bean.getAvgline()));
                pointEntity.setPrice(StringFromatUtils.get3Point(iPrice));
            } else {

                pointEntity.setAvgPrice(StringFromatUtils.get2Point(bean.getAvgline()));
                pointEntity.setPrice(StringFromatUtils.get2Point(iPrice));

            }
            if (null != mStockBean) {
                pointEntity.setIndexType(StockUitls.isIndexStock(mStockBean.getSymbol_type()));
            }

            pointEntity.setValue(iPrice);
            pointEntity.setTime(TimeUtils.getTimeString(bean.getTime()));
            pointEntity.setIncreaseValue(iPrice - baseNum);
            pointEntity.setIncreaseRange(bean.getPercentage());
            pointEntity.setMinchange(bean.getMinchange());
            pointEntity.setTurnover(bean.getVolume());

            averagePoint.setValue(bean.getAvgline());
            fenshiLineData.add(pointEntity);
            averagelineData.add(averagePoint);
        }

        fenshiPiceLine.setMaxVolNum(maxVolCount);
        fenshiPiceLine.setMinVolNum(minVolCount);

        float offetValue;
        // maxNum = maxNum - baseNum;
        // minNum = baseNum - minNum;

        offetValue = Math.max(Math.abs(maxNum - baseNum), Math.abs(minNum - baseNum));

        return offetValue;
    }

    List<FSLinePointEntity> fenshiLineData = new ArrayList<FSLinePointEntity>();
    List<LinePointEntity> averagelineData = new ArrayList<LinePointEntity>();

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        // int baseNum = 1;
        // offetYvalue = offetYvalue / 0.8f;
        // if ((offetYvalue / baseNum) > 0.105f) {
        // offetYvalue = 0.105f * baseNum;
        // }
        // System.out.println("max offet vlaue：" + offetYvalue);
        // System.out.println("baseNumvlaue：" + baseNum);
        List<String> ytitle = new ArrayList<String>();
        List<String> rightYtitle = new ArrayList<String>();
        // float halfOffetValue = offetYvalue / 2.0f;

        // float topValue = (float) Math.min(baseNum + offetYvalue * (1 + 0.1), baseNum * 1.1f);
        // float bottomValue = (float) Math.max(baseNum - offetYvalue * (1 + 0.1), baseNum * 0.9f);
        // float topValue = (float) Math.min(baseNum + offetYvalue * (1), baseNum * 1f);
        // float bottomValue = (float) Math.max(baseNum - offetYvalue * (1), baseNum * 1f);
        float topValue = baseNum + offetYvalue;
        float bottomValue = baseNum - offetYvalue;

        // System.out.println("topValue:" + topValue);
        // System.out.println("bottomValue:" + bottomValue);
        BigDecimal t = new BigDecimal(topValue);
        topValue = t.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        BigDecimal b = new BigDecimal(bottomValue);
        bottomValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        // float halfOffetValue = topValue-bottomValue / 2.0f;

        if (mStockBean != null && StockUitls.isShangZhengB(mStockBean.getSymbol())) {
            ytitle.add(StringFromatUtils.get3Point(bottomValue));
            ytitle.add(StringFromatUtils.get3Point((bottomValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get3Point(baseNum));
            ytitle.add(StringFromatUtils.get3Point((topValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get3Point(topValue));
        } else {
            ytitle.add(StringFromatUtils.get2Point(bottomValue));
            ytitle.add(StringFromatUtils.get2Point((bottomValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get2Point(baseNum));
            ytitle.add(StringFromatUtils.get2Point((topValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get2Point(topValue));
        }

        mMaChart.setAxisYTitles(ytitle);
        mMaChart.setMaxValue(topValue);
        mMaChart.setMinValue(bottomValue);

        if (baseNum == 0) {
            rightYtitle.add(StringFromatUtils.get2PointPercent(0));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        } else {

            rightYtitle.add(StringFromatUtils.get2PointPercent(((bottomValue - baseNum) / baseNum) * 100f));
            rightYtitle.add(StringFromatUtils
                    .get2PointPercent((((bottomValue + baseNum) / 2 - baseNum) / baseNum) * 100));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
            rightYtitle.add(StringFromatUtils.get2PointPercent((((topValue + baseNum) / 2 - baseNum) / baseNum) * 100));
            rightYtitle.add(StringFromatUtils.get2PointPercent(((topValue - baseNum) / baseNum) * 100f));
        }

        mMaChart.setAxisRightYTitles(rightYtitle);

    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setStopYTitle(float baseNum) {
        // int baseNum = 1;
        // offetYvalue = offetYvalue / 0.8f;
        // if ((offetYvalue / baseNum) > 0.105f) {
        // offetYvalue = 0.105f * baseNum;
        // }

        List<String> ytitle = new ArrayList<String>();
        List<String> rightYtitle = new ArrayList<String>();
        float topValue = (float) (baseNum * (1 + 0.01));
        float bottomValue = (float) (baseNum * (1 - 0.01));

        // System.out.println("topValue:" + topValue);
        // System.out.println("bottomValue:" + bottomValue);
        BigDecimal t = new BigDecimal(topValue);
        topValue = t.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        BigDecimal b = new BigDecimal(bottomValue);
        bottomValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        // float halfOffetValue = topValue-bottomValue / 2.0f;

        if (mStockBean != null && StockUitls.isShangZhengB(mStockBean.getSymbol())) {
            ytitle.add(StringFromatUtils.get3Point(bottomValue));
            ytitle.add(StringFromatUtils.get3Point((bottomValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get3Point(baseNum));
            ytitle.add(StringFromatUtils.get3Point((topValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get3Point(topValue));
        } else {
            ytitle.add(StringFromatUtils.get2Point(bottomValue));
            ytitle.add(StringFromatUtils.get2Point((bottomValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get2Point(baseNum));
            ytitle.add(StringFromatUtils.get2Point((topValue + baseNum) / 2));
            ytitle.add(StringFromatUtils.get2Point(topValue));
        }

        mMaChart.setAxisYTitles(ytitle);
        mMaChart.setMaxValue(topValue);
        mMaChart.setMinValue(bottomValue);

        if (baseNum == 0) {
            rightYtitle.add(StringFromatUtils.get2PointPercent(0));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        } else {

            rightYtitle.add(StringFromatUtils.get2PointPercent(-1));
            rightYtitle.add(StringFromatUtils.get2PointPercent(-0.5f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(0.5f));
            rightYtitle.add(StringFromatUtils.get2PointPercent(1f));
        }

        mMaChart.setAxisRightYTitles(rightYtitle);
        mMaChart.invalidate();

    }

    private void setTodayPointTitle() {
        List<String> titles = new ArrayList<String>();
        titles.add("时间");
        titles.add("价格");
        mMaChart.setPointTitleList(titles);
    }

    @SuppressLint("HandlerLeak")
    Handler dataHandler = new Handler();

    private void setXTitle(List<HistoryNetBean> dayNetValueList) {
        List<String> xtitle = new ArrayList<String>();
        xtitle.add(dayNetValueList.get(dayNetValueList.size() - 1).getDate());
        xtitle.add(dayNetValueList.get(0).getDate());
        mMaChart.setMaxPointNum(dayNetValueList.size());
        mMaChart.setAxisXTitles(xtitle);

    }

    public void onStart() {
        // System.out.println("====StockQuotesChartFragment=onStart=====");
        super.onStart();
        if (null != mStockBean) {
            setStockQuotesBean(mStockBean);
        }
        if (trendType.equals(TREND_TYPE_TODAY)) {
            dataHandler.postDelayed(requestRunnable, 6);// 打开定时器，60ms后执行runnable操作
        }

    }

    ;

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        todayListener.isStopRequest();
    }

    public void onStop() {
        super.onStop();
        dataHandler.removeCallbacks(requestRunnable);// 关闭定时器处理

    }

    public void setStockViewCallBack(StockViewCallBack callBack) {
        // this.mMaChart.setCallBack(callBack);
    }

    Runnable requestRunnable = new Runnable() {
        @Override
        public void run() {
            // System.out.println("====StockQuotesChartFragment=run update=====");

            dataHandler.sendEmptyMessage(1722);
            if (mQuotesDataEngine == null) {
                return;
            }
            // todayListener.setLoadingDialog(getActivity());
            if (TextUtils.isEmpty(mFsDataBean.getCurtime())) {
                // System.out.println("====StockQuotesChartFragment=queryTimeShare=====");
                mQuotesDataEngine.queryTimeShare(mStockCode, todayListener);
            } else {
                mQuotesDataEngine.queryMoreTimeShare(mStockCode, mFsDataBean.getCurtime(), todayListener);
            }
            dataHandler.postDelayed(this, 30 * 1000);// 隔30s再执行一次
        }
    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_time);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}
