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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.PositionDetail;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.PositionDetail.PositionAdjustBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.HistoryPositionDetailActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
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
public class FragmentPositionBottom extends Fragment implements OnClickListener, FragmentLifecycle {

    private PieGraph pgView;
    // private View btnDate;
    // private View btnAdjust;
    // private TextView tvCurrentDay;
    // private TextView tvCombinationName;
    // private TextView tvNetValue;
    // private ScrollView mScrollview;
    private ArrayList<PieSlice> pieList = new ArrayList<PieSlice>();

    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();
    // 涨幅相关
    private ListViewEx lvStock;
    private PositionDetailIncreaAdapter stockAdapter;

    // 净值贡献相关
    // private ListViewEx lvContribute;
    // private PositionContributedapter mContributeAdapter;
    //
    // // 持仓调整相关
    // private List<PositionAdjustBean> mAdjustList = new ArrayList<PositionDetail.PositionAdjustBean>();
    // private ListViewEx lvAdjustHistory;
    // private AdjustHistoryAdapter mAdjustAdapter;

    private String mCombinationId;

    private PositionDetail mPositionDetail;

    private TextView tvHistory;

    private static final String ARGUMENT_COMBINTAION_ID = "combination_id";

    public static FragmentPositionBottom newInstance(String combinationId) {
        FragmentPositionBottom fragment = new FragmentPositionBottom();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_COMBINTAION_ID, combinationId);
        fragment.setArguments(arguments);

        return fragment;
    }

    public FragmentPositionBottom() {
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

    }

    private void handleExtras(Bundle extras) {

    }

    private void handleArguments(Bundle extras) {
        mCombinationId = extras.getString(ARGUMENT_COMBINTAION_ID);
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

        @Override
        public void beforeRequest() {
            // TODO Auto-generated method stub
            super.beforeRequest();

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

        // setCombinationInfo();
        setStockList();
        setPieList();
        // setAdjustHistoryList();
        // mScrollview.fullScroll(ScrollView.FOCUS_UP);
    }

    // protected void setCombinationInfo() {
    // tvCurrentDay.setText(mPositionDetail.getCurrentDate());
    // tvCombinationName.setText(mPositionDetail.getPortfolio().getName());
    // tvNetValue.setText(StringFromatUtils.get4Point(mPositionDetail.getPortfolio().getNetvalue()));
    // tvNetValue.setTextColor(ColorTemplate.getUpOrDrownCSL(mPositionDetail.getPortfolio().getNetvalue() - 1));
    //
    // }

    // protected void setAdjustHistoryList() {
    // mAdjustList = mPositionDetail.getAdjustList();
    // mAdjustAdapter.setList(mAdjustList);
    //
    // }

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
        stockAdapter.setFundpercent(mPositionDetail.getFund_percent());

        // mContributeAdapter.setList(stockList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_bottom, null);
        // initView(view);
        initPieView(view);
        initIncreaseList(view);
        tvHistory = (TextView) view.findViewById(R.id.tv_history);
        tvHistory.setOnClickListener(this);
        // initContributeView(view);
        // initAdjustHistoryView(view);
        // mScrollview = (ScrollView) view.findViewById(R.id.sc_content);
        // mScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        // @Override
        // public void onGlobalLayout() {
        // // Ready, move up
        // mScrollview.fullScroll(View.FOCUS_UP);
        // }
        // });
        return view;
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
        // 增加牛人基金持仓明细可点击并跳转至个股行情页----2014.12.12 add by zcm
        lvStock.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 1;
                if (position >= 0 && position < stockList.size()) {
                    ConStockBean selectBean = stockList.get(position);
                    SelectStockBean sStockBean = SelectStockBean.copy(selectBean);
                    sStockBean.symbol_type = "1";
                    startActivity(StockQuotesActivity.newIntent(getActivity(), sStockBean));
                }
            }
        });
    }

    private void setPieList() {
        int valueSize = stockList.size();

        for (int i = 0; i < valueSize; i++) {
            PieSlice slice1 = new PieSlice();

            slice1.setColor(stockList.get(i).getDutyColor());
            slice1.setValue(stockList.get(i).getPercent());
            pieList.add(slice1);

        }
        // surpulsValue();
        PieSlice emptySlice = new PieSlice();
        emptySlice.setColor(ColorTemplate.DEF_RED);
        emptySlice.setValue(mPositionDetail.getFund_percent());
        pieList.add(emptySlice);

        pgView.setSlices(pieList);

    }

    // private float surpulsValue() {
    // float total = 100;
    // for (int i = 0; i < stockList.size(); i++) {
    // total -= stockList.get(i).getPercent();
    // }
    // surValue = total;
    //
    // return total;
    // }

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
                getActivity().startActivity(
                        PositionAdjustActivity.newIntent(getActivity(), mPositionDetail.getPortfolio().getId()));

            }

                break;
            case R.id.btn_back: {

            }

                break;
            case R.id.btn_detail_date: {// 选择查询日期
                // showPickerDate();

            }

                break;
            case R.id.tv_history: {
                if (null != mPositionDetail && null != mPositionDetail.getPortfolio()) {

                    // 历史调仓记录
                    startActivity(HistoryPositionDetailActivity.newIntent(getActivity(), mPositionDetail.getPortfolio()
                            .getId()));
                }
            }
                break;

            default:
                break;
        }

    }

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
