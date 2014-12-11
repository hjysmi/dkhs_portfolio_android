/**
 * @Title TrendChartFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetBean;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetValue;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.ui.widget.TrendLinePointEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version 1.0
 */
public class TrendChartFragment extends BaseFragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";
    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType = TREND_TYPE_TODAY;
    private boolean needCloseDialog = false;
    // private boolean isTodayNetValue;
    private TextView tvTimeLeft;
    private TextView tvTimeRight;
    private TextView tvNetValue;
    private TextView tvUpValue;
    private TextView tvIncreaseValue;
    private TextView tvStartText;
    private TextView tvEndText;
    private TextView tvIncreaseText;

    private View viewDashLineTip;

    private TrendChart mMaChart;

    private NetValueEngine mNetValueDataEngine;
    private CombinationBean mCombinationBean;

    private Handler updateHandler;
    private Calendar mCreateCalender;

    // private List<TrendLinePointEntity> lineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> todayLineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> weekLineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> monthLineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> alLineDataList = new ArrayList<TrendLinePointEntity>();

    // private TodayNetValue mTodayNetvalue;
    // private HistoryNetValue mWeekNetvalue;
    // private HistoryNetValue mMonthNetvalue;
    // private HistoryNetValue mAllNetvalue;

    private DrawLineDataEntity mTodayLineData;
    private DrawLineDataEntity mWeekLineData;
    private DrawLineDataEntity mMonthLineData;
    private DrawLineDataEntity mAllLineData;

    public static TrendChartFragment newInstance(String trendType) {
        TrendChartFragment fragment = new TrendChartFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TREND_TYPE, trendType);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        mCreateCalender = TimeUtils.toCalendar(mCombinationBean.getCreateTime());

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);

    }

    public void setSelectType(String type) {
        this.trendType = type;

        if (null != mMaChart) {
            drawCharHandler.sendEmptyMessage(777);
            // updateView();
            // clearViewData();

        }
        // if (isTodayShow()) {
        dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
        // } else {
        // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
        // }
    }

    Handler drawCharHandler = new Handler() {
        public void handleMessage(Message msg) {
            updateView();
        };
    };

    public void showShareImage() {
        if (null != mMaChart) {
            // initImagePath();
            new Thread() {
                public void run() {

                    // initImagePath();
                    save();
                    shareHandler.sendEmptyMessage(999);
                }
            }.start();
        }
    }

    Handler shareHandler = new Handler() {
        public void handleMessage(Message msg) {
            showShare();
        };
    };

    private String SHARE_IMAGE;

    public void showShare() {
        // Context context = getActivity();
        // final OnekeyShare oks = new OnekeyShare();
        //
        // oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        // // oks.setAddress("12345678901");
        // // oks.setTitle(CustomShareFieldsPage.getString("title", context.getString(R.string.evenote_title)));
        // // oks.setTitleUrl(CustomShareFieldsPage.getString("titleUrl", "http://mob.com"));
        // // String customText = CustomShareFieldsPage.getString( "text", null);
        // oks.setTitle("谁牛");
        // oks.setTitleUrl("https://www.dkhs.com/portfolio/wap/");
        // oks.setUrl("https://www.dkhs.com/portfolio/wap/");
        // String customText = "";
        // if (TextUtils.isEmpty(trendType)) {
        // return;
        // }
        //
        // if (isTodayShow() && null != mTodayLineData) {
        // customText = "当前涨幅:" + StringFromatUtils.getPercentValue(mTodayLineData.netvalue);
        //
        // } else if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY) && null != mAllLineData) {
        // float addupValue = (mAllLineData.end - mAllLineData.begin) / mAllLineData.begin * 100;
        // customText = "历史收益率:" + StringFromatUtils.getPercentValue(addupValue);
        //
        // } else if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH) && null != mMonthLineData) {
        // float addupValue = (mMonthLineData.end - mMonthLineData.begin) / mMonthLineData.begin * 100;
        // customText = "月收益率:" + StringFromatUtils.getPercentValue(addupValue);
        //
        // } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY) && null != mWeekLineData) {
        // float addupValue = (mWeekLineData.end - mWeekLineData.begin) / mWeekLineData.begin * 100;
        // customText = "周收益率:" + StringFromatUtils.getPercentValue(addupValue);
        // }
        //
        // oks.setText(customText);
        //
        // // if (customText != null) {
        // // oks.setText(customText);
        // // } {
        // // oks.setText(context.getString(R.string.share_content));
        // // }
        //
        // // if (captureView) {
        // // // oks.setViewToShare(getPage());
        // // } else {
        // oks.setImagePath(SHARE_IMAGE);
        // // oks.setImageUrl(CustomShareFieldsPage.getString("imageUrl", MainActivity.TEST_IMAGE_URL));
        // // oks.setImageArray(new String[] { MainActivity.TEST_IMAGE, MainActivity.TEST_IMAGE_URL });
        // // }
        // // oks.setUrl("http://dev.dkhs.com");
        // oks.setFilePath(SHARE_IMAGE);
        // // oks.setComment("share");
        // // oks.setSite(CustomShareFieldsPage.getString("site", context.getString(R.string.app_name)));
        // // oks.setSiteUrl(CustomShareFieldsPage.getString("siteUrl", "http://mob.com"));
        // // oks.setVenueName(CustomShareFieldsPage.getString("venueName", "ShareSDK"));
        // // oks.setVenueDescription(CustomShareFieldsPage.getString("venueDescription",
        // "This is a beautiful place!"));
        // // oks.setLatitude(23.056081f);
        // // oks.setLongitude(113.385708f);
        // oks.setSilent(false);
        // // oks.setShareFromQQAuthSupport(true);
        // oks.setShareFromQQAuthSupport(false);
        // // if (platform != null) {
        // // oks.setPlatform(platform);
        // // }
        //
        // // 令编辑页面显示为Dialog模式
        // oks.setDialogMode();
        //
        // // 在自动授权时可以禁用SSO方式
        // // if (!CustomShareFieldsPage.getBoolean("enableSSO", true))
        // // oks.disableSSOWhenAuthorize();
        //
        // // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        // // oks.setCallback(new OneKeyShareCallback());
        //
        // oks.show(context);
    }

    public Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    private static final String FILE_NAME = "share_image.jpg";

    private void initImagePath() {
        File file = new File("/sdcard/portfolio/", FILE_NAME);
        SHARE_IMAGE = file.getAbsolutePath();
        try {

            if (!file.exists()) {
                file.createNewFile();
                // Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
                Bitmap pic = convertViewToBitmap(mMaChart);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();

        }
        Log.i("TEST_IMAGE path ==>>>", SHARE_IMAGE);
    }

    private void save() {
        try {

            View content = mMaChart;
            content.setDrawingCacheEnabled(true);
            content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bitmap = content.getDrawingCache();

            /* 方法一，可以recycle */
            // content.buildDrawingCache(true);
            // Bitmap bitmap = content.getDrawingCache(true).copy(Config.RGB_565, false);
            // content.destroyDrawingCache();

            // Bitmap bitmap = convertViewToBitmap(mMaChart);

            String extr = Environment.getExternalStorageDirectory() + "/CrashLog/";
            // File mFolder = new File(extr + "/capture/image");
            // if (!mFolder.exists()) {
            // mFolder.mkdir();
            // }

            String s = "tmp.png";

            File f = new File(extr, s);
            SHARE_IMAGE = f.getAbsolutePath();

            FileOutputStream fos = null;
            fos = new FileOutputStream(f);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            mMaChart.destroyDrawingCache();
            // bitmap.recycle();
            System.out.println("image saved:" + SHARE_IMAGE);
            // Toast.makeText(getActivity(), "image saved", 5000).show();
        } catch (Exception e) {
            System.out.println("Failed To Save");
            e.printStackTrace();
            // Toast.makeText(getActivity(), "Failed To Save", 5000).show();
        }
    }

    // tab切换时Ui更新为选中的tabUI
    private void updateView() {
        if (!TextUtils.isEmpty(trendType)) {

            if (isTodayShow()) {

                initTodayTrendTitle();
                if (null != mTodayLineData) {
                    setTodayViewLoad();

                    // computeTodayDataThread.start();
                }
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
                if (null == mAllLineData) {
                    mNetValueDataEngine.requeryHistory(historyNetValueListener);
                    // historyNetValueListener.setLoadingDialog(getActivity());
                } else {
                    setHistoryViewload(mAllLineData);

                }
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
                if (null == mMonthLineData) {
                    mNetValueDataEngine.requeryOneMonth(historyNetValueListener);
                    // historyNetValueListener.setLoadingDialog(getActivity());
                } else {
                    setHistoryViewload(mMonthLineData);
                }
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                if (null == mWeekLineData) {
                    mNetValueDataEngine.requerySevenDay(historyNetValueListener);
                    // historyNetValueListener.setLoadingDialog(getActivity());
                } else {
                    setHistoryViewload(mWeekLineData);

                }
            }
            setupBottomTextViewData();
        }
        PromptManager.closeProgressDialog();

    }

    public void setUpdateHandler(Handler updateHandler) {
        this.updateHandler = updateHandler;
    }

    private void handleExtras(Bundle extras) {

        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mNetValueDataEngine = new NetValueEngine(mCombinationBean.getId());

    }

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.listener = (FragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend_chart, null);
        mMaChart = (TrendChart) view.findViewById(R.id.machart);
        if (getActivity().getClass().getName().equals("com.dkhs.portfolio.ui.OrderFundDetailActivity")) {
            InterceptScrollView mScrollview = ((OrderFundDetailActivity) getActivity()).getScroll();
            mMaChart.setScroll(mScrollview);
        }
        initMaChart(mMaChart);
        initView(view);
        // setupBottomTextViewData();
        updateView();
        return view;
    }

    private void initView(View view) {
        viewDashLineTip = view.findViewById(R.id.tv_dashline_tip);
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
        tvStartText = (TextView) view.findViewById(R.id.tv_netvalue_text);
        tvEndText = (TextView) view.findViewById(R.id.tv_updown_text);
        tvIncreaseText = (TextView) view.findViewById(R.id.tv_increase_text);

    }

    private void setupBottomTextViewData() {

        if (isTodayShow()) {

            tvStartText.setVisibility(View.INVISIBLE);
            tvIncreaseText.setText(R.string.today_in_rote);
        } else {
            if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {

                tvIncreaseText.setText(R.string.month_income_rate);
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                tvIncreaseText.setText(R.string.week_income_rate);
            } else {

                tvIncreaseText.setText(R.string.all_income_rate);
            }
            tvStartText.setText(R.string.start_netvalue);
            tvEndText.setText(R.string.end_netvalue);
            tvStartText.setVisibility(View.VISIBLE);
        }

    }

    private boolean isTodayShow() {
        if (TextUtils.isEmpty(trendType)) {
            return false;
        }
        return trendType.equalsIgnoreCase(TREND_TYPE_TODAY);
    }

    private void initMaChart(final TrendChart machart) {
        machart.setBoldLine();

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);

        machart.setDisplayBorder(false);

        machart.setLatitudeColor(Color.LTGRAY);

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);
        machart.setBorderColor(Color.TRANSPARENT);
        machart.setBackgroudColor(Color.WHITE);
        machart.setAxisMarginTop(10);
        machart.setAxisMarginLeft(20);
        machart.setAxisMarginRight(10);

        machart.setLongtitudeFontSize(10);
        machart.setLongtitudeFontColor(Color.GRAY);
        machart.setDisplayAxisYTitleColor(true);
        machart.setLatitudeColor(Color.GRAY);
        machart.setLatitudeFontColor(Color.GRAY);
        machart.setLongitudeColor(Color.GRAY);
        // machart.setMaxValue(120);
        // machart.setMinValue(0);

        machart.setDisplayAxisXTitle(true);
        machart.setDisplayAxisYTitle(true);
        machart.setDisplayLatitude(true);
        machart.setDisplayLongitude(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            machart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    private List<LineEntity> lines;
    private LineEntity MA5;

    private void setLineData(List<TrendLinePointEntity> lineDataList) {
        if (isAdded()) {
            if (lines == null) {
                lines = new ArrayList<LineEntity>();
            } else {
                lines.clear();
            }
            if (MA5 == null) {
                MA5 = new LineEntity();
            }
            MA5.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
            MA5.setLineData(lineDataList);
            lines.add(MA5);
            mMaChart.setLineData(lines);
        }
    }

    private void clearViewData() {
        if (null != mMaChart && null != mMaChart.getLineData()) {
            mMaChart.getLineData().clear();
            mMaChart.invalidate();
        }
    }

    private void initTodayTrendTitle() {
        List<String> xtitle = new ArrayList<String>();
        xtitle.add("9:30");
        xtitle.add("10:30");
        xtitle.add("11:30");
        xtitle.add("14:00");
        xtitle.add("15:00");

        mMaChart.setAxisXTitles(xtitle);
        mMaChart.setMaxPointNum(242);

        List<String> rightYtitle = new ArrayList<String>();
        rightYtitle.add(StringFromatUtils.get2PointPercent(-1f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(1f));
        mMaChart.setAxisRightYTitles(rightYtitle);
    }

    private void setTipVisible(boolean isShow) {

        if (isShow) {
            viewDashLineTip.setVisibility(View.VISIBLE);
        } else {
            viewDashLineTip.setVisibility(View.GONE);
        }

    }

    ParseHttpListener todayListener = new ParseHttpListener<DrawLineDataEntity>() {

        @Override
        protected DrawLineDataEntity parseDateTask(String jsonData) {
            TodayNetValue todayNetvalue = DataParse.parseObjectJson(TodayNetValue.class, jsonData);

            DrawLineDataEntity todayLine = null;
            if (null != todayNetvalue && todayNetvalue.getChartlist() != null
                    && todayNetvalue.getChartlist().size() > 0) {
                todayLine = new DrawLineDataEntity();
                // getMaxOffetValue(lineData, histroyValue);
                getMaxOffetValue(todayLine, todayNetvalue);
            }

            return todayLine;
        }

        @Override
        protected void afterParseData(DrawLineDataEntity todayNetvalue) {

            if (todayNetvalue != null) {

                mTodayLineData = todayNetvalue;
                // setTodayViewLoad();
                dataHandler.sendEmptyMessage(77);

            }

        }
    };

    public class DrawLineDataEntity {
        List<TrendLinePointEntity> dataList = new ArrayList<TrendLinePointEntity>();
        int dashLineSize;
        float begin;
        float end;
        String startDay;
        String endDay;
        float maxOffetvalue;
        float addupvalue;
        float netvalue;

    }

    // float maxValue = 0;

    private void setTodayViewLoad() {

        // List<TodayNetBean> dayNetValueList = mTodayNetvalue.getChartlist();
        // mTodayLineData.dataList;

        if (mTodayLineData.dataList != null && mTodayLineData.dataList.size() > 0) {
            setYTitle(mTodayLineData.begin, mTodayLineData.maxOffetvalue);
            mMaChart.setDashLinePointSize(mTodayLineData.dashLineSize);
            if (mMaChart.getDashLinePointSize() > 2) {

                setTipVisible(true);

            } else {
                setTipVisible(false);

            }
            setLineData(mTodayLineData.dataList);

            String lasttime = mTodayLineData.endDay;

            Calendar calender = TimeUtils.toCalendar(lasttime);
            tvTimeLeft.setText(calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1) + "-"
                    + calender.get(Calendar.DAY_OF_MONTH));
            String timeStr = TimeUtils.getTimeString(lasttime);
            tvTimeRight.setText(timeStr);
        }

        tvNetValue.setText(StringFromatUtils.get4Point(mTodayLineData.end));
        if (isTodayShow()) {
            tvNetValue.setVisibility(View.INVISIBLE);
        }

        System.out.println("get current netvalue:" + mTodayLineData.end);
        if (null != updateHandler) {
            System.out.println("send get current netvalue:" + mTodayLineData.end);
            Message msg = updateHandler.obtainMessage();
            msg.obj = mTodayLineData.end;
            updateHandler.sendMessage(msg);
        }
        float addupValue = mTodayLineData.addupvalue;
        tvUpValue.setTextColor(ColorTemplate.getUpOrDrownCSL(addupValue));
        tvUpValue.setText(StringFromatUtils.get4Point(addupValue));
        float increase = mTodayLineData.netvalue;
        tvIncreaseValue.setTextColor(ColorTemplate.getUpOrDrownCSL(increase));
        tvIncreaseValue.setText(StringFromatUtils.getPercentValue(increase));
    }

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(DrawLineDataEntity lineData, TodayNetValue todayNetvalue) {
        List<TodayNetBean> dayNetValueList = todayNetvalue.getChartlist();
        lineData.dataList.clear();
        // lineData.begin = todayNetvalue.getBegin();
        lineData.end = todayNetvalue.getEnd();
        lineData.endDay = dayNetValueList.get(dayNetValueList.size() - 1).getTimestamp();
        lineData.addupvalue = dayNetValueList.get(dayNetValueList.size() - 1).getChange();
        lineData.netvalue = dayNetValueList.get(dayNetValueList.size() - 1).getPercentage();
        int dashLineSize = 0;
        int i = 0;
        float baseNum = todayNetvalue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        for (TodayNetBean bean : todayNetvalue.getChartlist()) {
            i++;
            if (bean.getNetvalue() > maxNum) {
                maxNum = bean.getNetvalue();

            } else if (bean.getNetvalue() < minNum) {
                minNum = bean.getNetvalue();
            }

            TrendLinePointEntity pointEntity = new TrendLinePointEntity();
            // HitstroyNetBean todayBean = dayNetValueList.get(i);
            pointEntity.setTime("时间:" + TimeUtils.getTimeString(bean.getTimestamp()));
            pointEntity.setValue(bean.getNetvalue());
            pointEntity.setIncreaseRange(((bean.getNetvalue() - baseNum) / baseNum) * 100);

            if (dashLineSize == 0 && TimeUtils.toCalendar(bean.getTimestamp()) != null) {
                if (TimeUtils.toCalendar(bean.getTimestamp()).after(mCreateCalender)) {
                    dashLineSize = i;
                }
            }
            lineData.dataList.add(pointEntity);

        }
        if (dashLineSize == 0) {
            dashLineSize = todayNetvalue.getChartlist().size();
        }

        if (dashLineSize > 1) {
            lineData.begin = 1;

        } else {

            lineData.begin = todayNetvalue.getBegin();
        }

        // mMaChart.setDashLinePointSize(dashLineSize);
        lineData.dashLineSize = dashLineSize;
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;
        lineData.maxOffetvalue = offetValue;
        return offetValue;
    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {

        // 增加20的空白区域
        if (offetYvalue != 0) {

            offetYvalue = offetYvalue / 0.8f;
        } else {
            offetYvalue = baseNum * 0.01f;
        }
        List<String> ytitle = new ArrayList<String>();
        float halfOffetValue = offetYvalue / 2.0f;

        ytitle.add(StringFromatUtils.get4Point(baseNum - offetYvalue));
        ytitle.add(StringFromatUtils.get4Point(baseNum - halfOffetValue));
        ytitle.add(StringFromatUtils.get4Point(baseNum));
        ytitle.add(StringFromatUtils.get4Point(baseNum + halfOffetValue));
        ytitle.add(StringFromatUtils.get4Point(baseNum + offetYvalue));
        mMaChart.setAxisYTitles(ytitle);
        mMaChart.setMaxValue(baseNum + offetYvalue);
        mMaChart.setMinValue(baseNum - offetYvalue);

        List<String> rightYtitle = new ArrayList<String>();

        rightYtitle.add(StringFromatUtils.get2PointPercent(-(offetYvalue / baseNum) * 100));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-(halfOffetValue / baseNum) * 100));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent((halfOffetValue / baseNum) * 100));
        rightYtitle.add(StringFromatUtils.get2PointPercent((offetYvalue / baseNum) * 100));

        mMaChart.setDrawRightYTitle(true);
        mMaChart.setAxisRightYTitles(rightYtitle);
        mMaChart.setDrawTrendChart(true);

    }

    Handler dataHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (trendType.equals(TREND_TYPE_TODAY)) {

                // setLineData(initMA(new Random().nextInt(240)));
                setTodayViewLoad();
            } else {

            }
        };
    };

    ParseHttpListener historyNetValueListener = new ParseHttpListener<DrawLineDataEntity>() {

        @Override
        protected DrawLineDataEntity parseDateTask(String jsonData) {
            HistoryNetValue histroyValue = DataParse.parseObjectJson(HistoryNetValue.class, jsonData);
            DrawLineDataEntity lineData = null;
            if (null != histroyValue && histroyValue.getChartlist() != null && histroyValue.getChartlist().size() > 0) {
                lineData = new DrawLineDataEntity();
                getMaxOffetValue(lineData, histroyValue);
            }
            return lineData;
        }

        @Override
        protected void afterParseData(DrawLineDataEntity object) {
            if (object != null && isAdded()) {
                if (!TextUtils.isEmpty(trendType)) {
                    if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
                        mAllLineData = object;
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
                        mMonthLineData = object;
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                        mWeekLineData = object;
                    }
                    setHistoryViewload(object);
                }
            }
        }

    };

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(DrawLineDataEntity lineData, HistoryNetValue historyNetValue) {
        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();

        lineData.dataList.clear();

        lineData.end = historyNetValue.getEnd();
        if (null != historyNetList && historyNetList.size() > 0) {

            lineData.startDay = historyNetList.get(0).getDate();
            lineData.endDay = historyNetList.get(historyNetList.size() - 1).getDate();
        }
        int dashLineSize = 0;
        float baseNum = historyNetValue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        // List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();
        int dataLenght = historyNetList.size();
        for (int i = 0; i < dataLenght; i++) {

            TrendLinePointEntity pointEntity = new TrendLinePointEntity();
            HistoryNetBean todayBean = historyNetList.get(i);
            float value = todayBean.getNetvalue();
            // pointEntity.setDesc(todayBean.getDate());
            pointEntity.setValue(value);
            pointEntity.setTime("日期:" + todayBean.getDate());
            pointEntity.setIncreaseRange(todayBean.getPercentage());
            // pointEntity.setIncreaseRange((value - baseNum) / baseNum * 100);

            if (dashLineSize == 0 && TimeUtils.simpleDateToCalendar(todayBean.getDate()) != null) {
                if (TimeUtils.simpleDateToCalendar(todayBean.getDate()).after(mCreateCalender)) {
                    dashLineSize = i;
                }
            }

            lineData.dataList.add(pointEntity);

            if (value > maxNum) {
                maxNum = value;

            } else if (value < minNum) {
                minNum = value;
            }
        }
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;
        if (dashLineSize == 0) {
            dashLineSize = dataLenght;
        }

        if (dashLineSize > 1) {
            lineData.begin = 1;

        } else {

            lineData.begin = historyNetValue.getBegin();
        }

        // mMaChart.setDashLinePointSize(dashLineSize);
        lineData.dashLineSize = dashLineSize;
        lineData.maxOffetvalue = offetValue;
        // historyNetValue.setMaxOffetValue(offetValue);
        return offetValue;

    }

    private void setHistoryViewload(DrawLineDataEntity historyNetvalue) {
        try {

            // = historyNetvalue.da();
            if (historyNetvalue.dataList != null) {

                // int sizeLength = dayNetValueList.size();
                mMaChart.setDashLinePointSize(historyNetvalue.dashLineSize);
                if (mMaChart.getDashLinePointSize() > 2) {

                    setTipVisible(true);
                    setYTitle(historyNetvalue.begin, historyNetvalue.maxOffetvalue);

                } else {
                    setTipVisible(false);
                    setYTitle(historyNetvalue.begin, historyNetvalue.maxOffetvalue);

                }
                // setHistoryPointTitle();
                setLineData(historyNetvalue.dataList);
                if (strLeft == null) {
                    strLeft = getString(R.string.time_start);
                }
                if (strRight == null) {
                    strRight = getString(R.string.time_end);
                }
                strRight = getString(R.string.time_end, historyNetvalue.endDay);
                tvTimeLeft.setText(String.format(strLeft, historyNetvalue.startDay));
                tvTimeRight.setText(String.format(strRight, historyNetvalue.endDay));

                setXTitle(historyNetvalue);

            }
            tvNetValue.setVisibility(View.VISIBLE);
            tvNetValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvNetValue.setText(StringFromatUtils.get4Point(historyNetvalue.begin));
            float addupValue = (historyNetvalue.end - historyNetvalue.begin) / historyNetvalue.begin * 100;
            tvUpValue.setText(StringFromatUtils.get4Point(historyNetvalue.end));
            // fl
            tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(addupValue));
            tvUpValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvIncreaseValue.setTextColor(ColorTemplate.getUpOrDrownCSL(addupValue));
            PromptManager.closeProgressDialog();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void setXTitle(DrawLineDataEntity historyNetvalue) {
        List<String> xtitle = new ArrayList<String>();
        String endDate = historyNetvalue.startDay;
        if (TextUtils.isEmpty(endDate)) {
            xtitle.add("");
        } else {
            xtitle.add(endDate);

        }
        xtitle.add(historyNetvalue.endDay);
        mMaChart.setMaxPointNum(historyNetvalue.dataList.size());
        mMaChart.setAxisXTitles(xtitle);

    }

    @Override
    public void onPause() {

        super.onPause();

        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    public void onStart() {
        super.onStart();
        // if (trendType.equals(TREND_TYPE_TODAY)) {
        // dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
        // }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            if (null != mNetValueDataEngine) {
                if (!TextUtils.isEmpty(trendType)) {

                    if (isTodayShow()) {
                        mNetValueDataEngine.requeryToday(todayListener);
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
                        mNetValueDataEngine.requeryHistory(historyNetValueListener);
                        // historyNetValueListener.setLoadingDialog(getActivity());
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
                        mNetValueDataEngine.requeryOneMonth(historyNetValueListener);
                        // historyNetValueListener.setLoadingDialog(getActivity());
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                        mNetValueDataEngine.requerySevenDay(historyNetValueListener);
                        // historyNetValueListener.setLoadingDialog(getActivity());
                    }
                    setupBottomTextViewData();
                }

                // todayListener.setLoadingDialog(getActivity()).beforeRequest();
            }
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };
    private String strLeft;
    private String strRight;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isAdded()) {
            super.setUserVisibleHint(isVisibleToUser);
            if (trendType.equals(TREND_TYPE_TODAY)) {
                System.out.println("setUserVisibleHint:" + isVisibleToUser);

                // if (isVisibleToUser) {
                // // 相当于Fragment的onResume{
                // dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
                // } else {
                // System.out.println("dataHandler.removeCallbacks");
                // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
                // // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
                // // 相当于Fragment的onPause
                // }
            }
        }
    }

}
