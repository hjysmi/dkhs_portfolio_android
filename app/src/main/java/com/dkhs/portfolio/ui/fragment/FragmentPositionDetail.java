/**
 * @Title FragmentPositionDetail.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:33:13
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.PositionAdjustBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.PositionAdjustHistoryAdapter;
import com.dkhs.portfolio.ui.adapter.PositionContributedapter;
import com.dkhs.portfolio.ui.adapter.PositionDetailIncreaAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FragmentPositionDetail
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-3 上午9:33:13
 */
public class FragmentPositionDetail extends Fragment implements OnClickListener {

    private PieGraph pgView;
    private View btnAdjust;
    private TextView tvCurrentDay;
    // private TextView tvCombinationName;
    // private TextView tvNetValue;
    private ScrollView mScrollview;
    private View shareView;

    private ArrayList<PieSlice> pieList = new ArrayList<PieSlice>();
    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();

    // 涨幅相关
    private PositionDetailIncreaAdapter stockAdapter;

    // 净值贡献相关
    private PositionContributedapter mContributeAdapter;

    // 持仓调整相关
    private List<PositionAdjustBean> mAdjustList = new ArrayList<PositionAdjustBean>();
    private PositionAdjustHistoryAdapter mAdjustAdapter;

    private String mCombinationId;

    private PositionDetail mPositionDetail;

    private static final String ARGUMENT_COMBINTAION_ID = "combination_id";
    private Calendar mCurrentCalendar;
    private boolean isDefalutRequest = true;
    private Calendar hisDate = null;

    public static FragmentPositionDetail newInstance(String combinationId) {
        FragmentPositionDetail fragment = new FragmentPositionDetail();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_COMBINTAION_ID, combinationId);
        fragment.setArguments(arguments);

        return fragment;
    }

    public FragmentPositionDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setStockList();
        setRetainInstance(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param extras
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void handleExtras(Bundle extras) {
        // TODO Auto-generated method stub

    }

    private void handleArguments(Bundle extras) {
        mCombinationId = extras.getString(ARGUMENT_COMBINTAION_ID);

        // setStockList();
        // setPieList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable("detail", Parcels.wrap(mPositionDetail));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            mPositionDetail = Parcels.unwrap(savedInstanceState.getParcelable(("detail")));

        } else {
            // new
            // MyCombinationEngineImpl().queryCombinationDetail(mCombinationId,
            // new
            // QueryCombinationDetailListener());

        }
        if (null != mPositionDetail) {
            System.out.println("mPositionDetail has date no need reload");
        }

    }

    /**
     * @param activity
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }

    class QueryCombinationDetailListener extends ParseHttpListener<PositionDetail> {

        /**
         * @return
         * @Title
         * @Description TODO: (用一句话描述这个方法的功能)
         */
        @Override
        public void beforeRequest() {
            // TODO Auto-generated method stub
            super.beforeRequest();
            // if(null!=mPositionDetail){
            //
            // mPositionDetail.getAdjustList().clear();
            // mPositionDetail.getPositionList().clear();
            //
            // updateView();
            // btnAdjust.setEnabled(false);
            // }
        }

        @Override
        protected PositionDetail parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PositionDetail bean = DataParse.parseObjectJson(PositionDetail.class, jsonObject);
            stockList.clear();
            pieList.clear();
            // System.out.println("getPositionList size:" + bean.getPositionList().size());
            stockList.addAll(bean.getPositionList());

            if (null != stockList && stockList.size() > 0) {

                int listSize = stockList.size();

                for (int i = 0; i < listSize; i++) {
                    stockList.get(i).setDutyColor(ColorTemplate.getDefaultColor(i));
                }
            }

            int valueSize = stockList.size();

            for (int i = 0; i < valueSize; i++) {
                PieSlice slice1 = new PieSlice();

                slice1.setColor(stockList.get(i).getDutyColor());
                slice1.setValue(stockList.get(i).getPercent());
                pieList.add(slice1);

            }
            // surpulsValue();

            return bean;
        }

        @Override
        protected void afterParseData(PositionDetail object) {
            if (null != object) {
                if (isDefalutRequest) {
                    mCurrentCalendar = TimeUtils.simpleDateToCalendar(object.getCurrentDate());
                }
                mPositionDetail = object;

                updateView();
            }

        }
    }

    ;

    private void updateView() {

        btnAdjust.setEnabled(true);
        setCombinationInfo();
        setStockList();
        setPieList();
        setAdjustHistoryList();
        // mScrollview.fullScroll(ScrollView.FOCUS_UP);
    }

    protected void setCombinationInfo() {
        tvCurrentDay.setText(mPositionDetail.getCurrentDate());
        // tvCombinationName.setText(mPositionDetail.getPortfolio().getName());
        // tvNetValue.setText(StringFromatUtils.get4Point(mPositionDetail.getPortfolio().getNetvalue()));
        // tvNetValue.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetail.getPortfolio().getNetvalue() - 1));
        //
    }

    protected void setAdjustHistoryList() {
        mAdjustList = mPositionDetail.getAdjustList();
        mAdjustAdapter.setList(mAdjustList);

    }

    private void setStockList() {

        mContributeAdapter.setList(stockList);
        stockAdapter.setFundpercent(mPositionDetail.getFund_percent());
        stockAdapter.setList(stockList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_detail, null);
        initView(view);
        initPieView(view);
        initIncreaseList(view);
        initContributeView(view);
        initAdjustHistoryView(view);
        mScrollview = (ScrollView) view.findViewById(R.id.sc_content);
        mScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isAdded()) {

                    // Ready, move up
                    mScrollview.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;
                    mScrollview.fullScroll(View.FOCUS_UP);
                }
            }
        });
        return view;
    }

    private void initAdjustHistoryView(View view) {

        ListViewEx lvAdjustHistory = (ListViewEx) view.findViewById(R.id.lv_adjust_history);
        mAdjustAdapter = new PositionAdjustHistoryAdapter(getActivity(), mAdjustList);
        lvAdjustHistory.setAdapter(mAdjustAdapter);
    }

    private void initContributeView(View view) {
        ListViewEx lvContribute = (ListViewEx) view.findViewById(R.id.lv_contribute_layout);
        mContributeAdapter = new PositionContributedapter(getActivity(), stockList);
        View headerView = View.inflate(getActivity(), R.layout.layout_detail_contribute_title, null);
        lvContribute.addHeaderView(headerView);
        lvContribute.setAdapter(mContributeAdapter);

    }

    private void initView(View view) {
        btnAdjust = view.findViewById(R.id.btn_adjust_position);
        btnAdjust.setOnClickListener(this);
        btnAdjust.setEnabled(false);
        View btnDate = view.findViewById(R.id.btn_detail_date);
        btnDate.setOnClickListener(this);
        tvCurrentDay = (TextView) view.findViewById(R.id.tv_current_day);
        tvCurrentDay.setOnClickListener(this);
        // tvCombinationName = (TextView) view.findViewById(R.id.tv_conbination_name);
        // tvNetValue = (TextView) view.findViewById(R.id.tv_today_netvalue);
        shareView = view.findViewById(R.id.ll_shareview);

    }

    private void initPieView(View view) {
        pgView = (PieGraph) view.findViewById(R.id.piegrah);

    }

    private void initIncreaseList(View view) {
        ListViewEx lvStock = (ListViewEx) view.findViewById(R.id.lv_optional_layout);
        stockAdapter = new PositionDetailIncreaAdapter(getActivity(), stockList);
        View headerView = View.inflate(getActivity(), R.layout.layout_detail_pos_increase_title, null);
        lvStock.addHeaderView(headerView);
        lvStock.setAdapter(stockAdapter);
        lvStock.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 1;
                if (position >= 0 && position < stockList.size()) {

                    ConStockBean selectBean = stockList.get(position);
                    SelectStockBean sStockBean = SelectStockBean.copy(selectBean);
                    sStockBean.symbol_type = "1";
                    startActivity(StockQuotesActivity.newIntent(getActivity(), sStockBean));

                    // PromptManager.showToast(selectBean.getStockName() + ":" +
                    // selectBean.getStockCode());
                }

            }
        });

    }

    private void setPieList() {
        // pieList.clear();
        PieSlice emptySlice = new PieSlice();
        emptySlice.setColor(ColorTemplate.DEF_RED);
        emptySlice.setValue(mPositionDetail.getFund_percent());
        pieList.add(emptySlice);

        System.out.println("pieList size:" + pieList.size());

        pgView.setSlices(pieList);

    }

//    private float surpulsValue() {
//        float total = 100;
//        for (int i = 0; i < stockList.size(); i++) {
//            total -= stockList.get(i).getPercent();
//        }
//        surValue = total;
//
//        return total;
//    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        isDefalutRequest = true;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_adjust_position: {

                // todo :持仓调整
                getActivity().startActivity(
                        PositionAdjustActivity.newIntent(getActivity(), mPositionDetail.getPortfolio().getId()));

            }

            break;
            case R.id.btn_back: {

            }

            break;
            case R.id.tv_current_day:
            case R.id.btn_detail_date: {// 选择查询日期
                showPickerDate();

            }

            break;

            default:
                break;
        }

    }

    private String SHARE_IMAGE;

    public void showShareImage() {

        // initImagePath();
        // new Thread() {
        // public void run() {

        // initImagePath();
        saveShareBitmap();
        shareHandler.sendEmptyMessage(999);
        // }
        // }.start();

    }


    private WeakHandler shareHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            showShare();
            return true;
        }
    });

    private void showShare() {
        if (null != mPositionDetail) {
            Context context = getActivity();
            final OnekeyShare oks = new OnekeyShare();

//            oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
            oks.setTitle(mPositionDetail.getPortfolio().getName() + "持仓明细");
            String shareUrl = DKHSClient.getAbsoluteUrl(DKHSUrl.User.share) + mCombinationId;
            oks.setTitleUrl(shareUrl);
            oks.setUrl(shareUrl);
            String customText = "这是我的组合「" + mPositionDetail.getPortfolio().getName() + "」于"
                    + mPositionDetail.getCurrentDate() + "的持仓明细。你也来创建属于你的组合吧.";
            oks.setText(customText);

            oks.setImagePath(SHARE_IMAGE);

            oks.setFilePath(SHARE_IMAGE);

            oks.setSilent(false);

            oks.setShareFromQQAuthSupport(false);

            // 令编辑页面显示为Dialog模式
            oks.setDialogMode();

            oks.show(context);
        }
    }

    private void saveShareBitmap() {
        try {

            View v = shareView;
            // content.setDrawingCacheEnabled(true);
            // content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            // Bitmap bitmap = content.getDrawingCache();
            //
            // /* 方法一，可以recycle */
            // // content.buildDrawingCache(true);
            // // Bitmap bitmap =
            // content.getDrawingCache(true).copy(Config.RGB_565, false);
            // // content.destroyDrawingCache();
            //
            // // Bitmap bitmap = convertViewToBitmap(mMaChart);
            //
            // String extr = Environment.getExternalStorageDirectory() +
            // "/CrashLog/";
            // // File mFolder = new File(extr + "/capture/image");
            // // if (!mFolder.exists()) {
            // // mFolder.mkdir();
            // // }
            //
            // String s = "tmp.png";
            //
            // File f = new File(extr, s);
            // SHARE_IMAGE = f.getAbsolutePath();
            //
            // FileOutputStream fos = null;
            // fos = new FileOutputStream(f);
            // bitmap.compress(CompressFormat.JPEG, 100, fos);
            // fos.flush();
            // fos.close();
            // content.destroyDrawingCache();
            // // bitmap.recycle();

            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(b);
            c.drawColor(Color.WHITE);

            v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
            v.draw(c);

            String s = "tmp.png";
            String extr = Environment.getExternalStorageDirectory() + "/CrashLog/";
            File f = new File(extr, s);
            SHARE_IMAGE = f.getAbsolutePath();

            FileOutputStream fos = null;
            fos = new FileOutputStream(f);
            b.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // content.destroyDrawingCache();
            b.recycle();

            System.out.println("image saved:" + SHARE_IMAGE);
            // updateView();
            setCombinationInfo();
            setStockList();
            pgView.setSlices(pieList);

            // Toast.makeText(getActivity(), "image saved", 5000).show();
        } catch (Exception e) {
            System.out.println("Failed To Save");
            e.printStackTrace();
            // Toast.makeText(getActivity(), "Failed To Save", 5000).show();
        }
    }

    private int mYear;
    private int mMonth;
    private int mDay;

    private void showPickerDate() {
        if (null != hisDate) {
            mYear = hisDate.get(Calendar.YEAR);
            mMonth = hisDate.get(Calendar.MONTH);
            mDay = hisDate.get(Calendar.DAY_OF_MONTH);
        }
        dpg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), null, mYear, mMonth, mDay);
        dpg.setTitle(R.string.dialog_select_time_title);
        dpg.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = dpg.getDatePicker();
                int year = datePicker.getYear();
                int monthOfYear = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                Calendar curCalendar = Calendar.getInstance();
                curCalendar.set(year, monthOfYear, dayOfMonth);
                if (curCalendar.after(Calendar.getInstance())) {
                    PromptManager.showToast("查询开始时间不能大于当前时间");
                    return;
                }
                String createDate = mPositionDetail.getPortfolio().getCreateTime();

                Calendar calCreate = TimeUtils.toCalendar(createDate);
                Calendar calSelect = GregorianCalendar.getInstance();
                // Calendar calToday = GregorianCalendar.getInstance();
                calSelect.set(year, monthOfYear, dayOfMonth);

                String queryDay = "";
                if (calSelect.before(calCreate)) {
                    queryDay = TimeUtils.getTimeString(calCreate);
                    hisDate = calCreate;
                } else {
                    queryDay = TimeUtils.getTimeString(calSelect);
                    hisDate = calSelect;
                }
                // if (calSelect.before(calToday)) {
                // btnAdjust.setVisibility(View.GONE);
                // } else {
                // btnAdjust.setVisibility(View.VISIBLE);
                // }

                if (null != mCurrentCalendar && isEqualsCalenderDay(calSelect, mCurrentCalendar)) {
                    btnAdjust.setVisibility(View.VISIBLE);
                } else {

                    btnAdjust.setVisibility(View.GONE);
                }

                tvCurrentDay.setText(queryDay);
                isDefalutRequest = false;
                QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
                listener.setLoadingDialog(getActivity());
                new MyCombinationEngineImpl().queryCombinationDetailByDay(mCombinationId, queryDay, listener);
            }
        });
        dpg.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String createDate = mPositionDetail.getPortfolio().getCreateTime();

            Calendar calCreate = TimeUtils.toCalendar(createDate);
            Calendar calSelect = GregorianCalendar.getInstance();
            // Calendar calToday = GregorianCalendar.getInstance();
            calSelect.set(year, monthOfYear, dayOfMonth);

            String queryDay = "";
            if (calSelect.before(calCreate)) {
                queryDay = TimeUtils.getTimeString(calCreate);
            } else {
                queryDay = TimeUtils.getTimeString(calSelect);
            }
            // if (calSelect.before(calToday)) {
            // btnAdjust.setVisibility(View.GONE);
            // } else {
            // btnAdjust.setVisibility(View.VISIBLE);
            // }

            if (null != mCurrentCalendar && isEqualsCalenderDay(calSelect, mCurrentCalendar)) {
                btnAdjust.setVisibility(View.VISIBLE);
            } else {

                btnAdjust.setVisibility(View.GONE);
            }

            tvCurrentDay.setText(queryDay);
            isDefalutRequest = false;
            QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
            listener.setLoadingDialog(getActivity());
            new MyCombinationEngineImpl().queryCombinationDetailByDay(mCombinationId, queryDay, listener);
        }
    };
    private DatePickerDialog dpg;

    private boolean isEqualsCalenderDay(Calendar cSelect, Calendar cToday) {
        return (cSelect.get(Calendar.YEAR) == cToday.get(Calendar.YEAR)
                && cSelect.get(Calendar.MONTH) == cToday.get(Calendar.MONTH) && cSelect.get(Calendar.DAY_OF_MONTH) == cToday
                .get(Calendar.DAY_OF_MONTH));
    }


    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_combination_adjust);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        QueryCombinationDetailListener listener = new QueryCombinationDetailListener();
        listener.setLoadingDialog(getActivity());

        new MyCombinationEngineImpl().queryCombinationDetail(mCombinationId, listener);
    }
}
