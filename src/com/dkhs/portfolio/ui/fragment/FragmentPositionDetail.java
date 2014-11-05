/**
 * @Title FragmentPositionDetail.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:33:13
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.bean.PositionDetail.PositionAdjustBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.AdjustHistoryAdapter;
import com.dkhs.portfolio.ui.adapter.PositionContributedapter;
import com.dkhs.portfolio.ui.adapter.PositionDetailIncreaAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName FragmentPositionDetail
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:33:13
 * @version 1.0
 */
public class FragmentPositionDetail extends Fragment implements OnClickListener, FragmentLifecycle {

    private PieGraph pgView;
    private View btnDate;
    private View btnAdjust;
    private TextView tvCurrentDay;
    private TextView tvCombinationName;
    private TextView tvNetValue;
    private ScrollView mScrollview;
    private ArrayList<PieSlice> pieList = new ArrayList<PieSlice>();
    private float surValue;

    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();
    // 涨幅相关
    private ListViewEx lvStock;
    private PositionDetailIncreaAdapter stockAdapter;

    // 净值贡献相关
    private ListViewEx lvContribute;
    private PositionContributedapter mContributeAdapter;

    // 持仓调整相关
    private List<PositionAdjustBean> mAdjustList = new ArrayList<PositionDetail.PositionAdjustBean>();
    private ListViewEx lvAdjustHistory;
    private AdjustHistoryAdapter mAdjustAdapter;

    private int mCombinationId;

    private PositionDetail mPositionDetail;

    private static final String ARGUMENT_COMBINTAION_ID = "combination_id";

    public static FragmentPositionDetail newInstance(int combinationId) {
        FragmentPositionDetail fragment = new FragmentPositionDetail();

        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_COMBINTAION_ID, combinationId);
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
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param extras
     * @return void
     */
    private void handleExtras(Bundle extras) {
        // TODO Auto-generated method stub

    }

    private void handleArguments(Bundle extras) {
        mCombinationId = extras.getInt(ARGUMENT_COMBINTAION_ID);

        // setStockList();
        // setPieList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putSerializable("detail", mPositionDetail);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            mPositionDetail = (PositionDetail) savedInstanceState.getSerializable("detail");

        } else {
            // new MyCombinationEngineImpl().queryCombinationDetail(mCombinationId, new
            // QueryCombinationDetailListener());

        }
        if (null != mPositionDetail) {
            System.out.println("mPositionDetail has date no need reload");
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param activity
     * @return
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }

    class QueryCombinationDetailListener extends ParseHttpListener<PositionDetail> {

        /**
         * @Title
         * @Description TODO: (用一句话描述这个方法的功能)
         * @return
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
            return DataParse.parseObjectJson(PositionDetail.class, jsonObject);
        }

        @Override
        protected void afterParseData(PositionDetail object) {
            if (null != object) {

                mPositionDetail = object;

                updateView();
            }

        }
    };

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
        tvCombinationName.setText(mPositionDetail.getPortfolio().getName());
        tvNetValue.setText(StringFromatUtils.get4Point(mPositionDetail.getPortfolio().getNetvalue()));
        tvNetValue.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetail.getPortfolio().getNetvalue() - 1));

    }

    protected void setAdjustHistoryList() {
        mAdjustList = mPositionDetail.getAdjustList();
        mAdjustAdapter.setList(mAdjustList);

    }

    private void setStockList() {
        stockList.clear();
        stockList.addAll(mPositionDetail.getPositionList());
        if (null != stockList && stockList.size() > 0) {

            int listSize = stockList.size();

            for (int i = 0; i < listSize; i++) {
                stockList.get(i).setDutyColor(ColorTemplate.getDefaultColor(i));
            }
        }

        stockAdapter.setList(stockList);
        mContributeAdapter.setList(stockList);

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
                // Ready, move up
                mScrollview.fullScroll(View.FOCUS_UP);
            }
        });
        return view;
    }

    private void initAdjustHistoryView(View view) {

        lvAdjustHistory = (ListViewEx) view.findViewById(R.id.lv_adjust_history);
        mAdjustAdapter = new AdjustHistoryAdapter(getActivity(), mAdjustList);
        lvAdjustHistory.setAdapter(mAdjustAdapter);
    }

    private void initContributeView(View view) {
        lvContribute = (ListViewEx) view.findViewById(R.id.lv_contribute_layout);
        mContributeAdapter = new PositionContributedapter(getActivity(), stockList);
        View headerView = View.inflate(getActivity(), R.layout.layout_detail_contribute_title, null);
        lvContribute.addHeaderView(headerView);
        lvContribute.setAdapter(mContributeAdapter);

    }

    private void initView(View view) {
        btnAdjust = view.findViewById(R.id.btn_adjust_position);
        btnAdjust.setOnClickListener(this);
        btnAdjust.setEnabled(false);
        btnDate = view.findViewById(R.id.btn_detail_date);
        btnDate.setOnClickListener(this);
        tvCurrentDay = (TextView) view.findViewById(R.id.tv_current_day);
        tvCombinationName = (TextView) view.findViewById(R.id.tv_conbination_name);
        tvNetValue = (TextView) view.findViewById(R.id.tv_today_netvalue);

    }

    private void initPieView(View view) {
        pgView = (PieGraph) view.findViewById(R.id.piegrah);

    }

    private void initIncreaseList(View view) {
        lvStock = (ListViewEx) view.findViewById(R.id.lv_optional_layout);
        stockAdapter = new PositionDetailIncreaAdapter(getActivity(), stockList);
        View headerView = View.inflate(getActivity(), R.layout.layout_detail_pos_increase_title, null);
        lvStock.addHeaderView(headerView);
        lvStock.setAdapter(stockAdapter);

    }

    private void setPieList() {
        int valueSize = stockList.size();

        for (int i = 0; i < valueSize; i++) {
            PieSlice slice1 = new PieSlice();

            slice1.setColor(stockList.get(i).getDutyColor());
            slice1.setValue(stockList.get(i).getDutyValue());
            pieList.add(slice1);

        }
        surpulsValue();
        PieSlice emptySlice = new PieSlice();
        emptySlice.setColor(ColorTemplate.DEF_RED);
        emptySlice.setValue(surValue);
        pieList.add(emptySlice);

        pgView.setSlices(pieList);

    }

    private float surpulsValue() {
        float total = 100;
        for (int i = 0; i < stockList.size(); i++) {
            total -= stockList.get(i).getDutyValue();
        }
        surValue = total;

        return total;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        new MyCombinationEngineImpl().queryCombinationDetail(mCombinationId, new QueryCombinationDetailListener());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_adjust_position: {

                // todo :持仓调整
                getActivity().startActivity(PositionAdjustActivity.newIntent(getActivity(), mPositionDetail));

            }

                break;
            case R.id.btn_back: {

            }

                break;
            case R.id.btn_detail_date: {// 选择查询日期
                showPickerDate();

            }

                break;

            default:
                break;
        }

    }

    private int mYear;
    private int mMonth;
    private int mDay;

    private void showPickerDate() {
        DatePickerDialog dpg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), mDateSetListener, mYear, mMonth, mDay);

        dpg.setTitle(R.string.dialog_select_time_title);

        dpg.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String createDate = mPositionDetail.getPortfolio().getCreateTime();

            Calendar calCreate = TimeUtils.toCalendar(createDate);
            Calendar calSelect = GregorianCalendar.getInstance();
            Calendar calToday = GregorianCalendar.getInstance();
            calSelect.set(year, monthOfYear, dayOfMonth);

            String queryDay = "";
            if (calSelect.before(calCreate)) {
                queryDay = TimeUtils.getTimeString(calCreate);
            } else {
                queryDay = TimeUtils.getTimeString(calSelect);
            }
            if (calSelect.before(calToday)) {
                btnAdjust.setVisibility(View.GONE);
            } else {
                btnAdjust.setVisibility(View.VISIBLE);

            }

            tvCurrentDay.setText(queryDay);
            new MyCombinationEngineImpl().queryCombinationDetailByDay(mCombinationId, queryDay,
                    new QueryCombinationDetailListener());
        }
    };

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPauseFragment() {
        // TODO Auto-generated method stub

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResumeFragment() {
        // TODO Auto-generated method stub

    }

}
