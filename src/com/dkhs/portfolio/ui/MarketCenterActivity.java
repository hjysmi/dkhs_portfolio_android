package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.PlateLoadMoreEngineImpl;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.ui.MarketListActivity.LoadViewType;
import com.dkhs.portfolio.ui.adapter.MarketCenterGridAdapter;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.adapter.MarketPlateGridAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 行情中心
 * 
 * @author weiting
 * 
 */
public class MarketCenterActivity extends ModelAcitivity implements OnClickListener {

    public final static String INLAND_INDEX = "inland_index";

    private GridView gvMainIndex;
    private List<SelectStockBean> mIndexDataList = new ArrayList<SelectStockBean>();
    private MarketCenterGridAdapter mIndexAdapter;

    private GridView gvPlate;
    private List<SectorBean> mSecotrList = new ArrayList<SectorBean>();
    private MarketPlateGridAdapter mPlateAdapter;

    // 涨幅榜
    private ListView lvIncease;
    private List<SelectStockBean> mIncreaseDataList = new ArrayList<SelectStockBean>();
    private MarketCenterItemAdapter mIncreaseAdapter;

    // 跌幅榜
    private ListView lvDown;
    private MarketCenterItemAdapter mDownAdapter;
    private List<SelectStockBean> mDownDataList = new ArrayList<SelectStockBean>();

    private ListView lvHandover;
    private MarketCenterItemAdapter mTurnOverAdapter;
    private List<SelectStockBean> mTurnOverDataList = new ArrayList<SelectStockBean>();

    // 振幅榜
    private ListView lvAmplit;
    private MarketCenterItemAdapter mAmplitAdapter;
    private List<SelectStockBean> mAmpliDataList = new ArrayList<SelectStockBean>();

    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 5;
    private boolean start = false;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_marketcenter);
        setTitle(R.string.marketcenter_title);
        initView();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMarketTimer == null && start) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        MobclickAgent.onResume(this);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initView() {

        Button addButton = getRightButton();
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                null, null, null);
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketCenterActivity.this, SelectAddOptionalActivity.class);
                startActivity(intent);

            }
        });

        gvMainIndex = (GridView) findViewById(R.id.gv_mainindex);
        gvPlate = (GridView) findViewById(R.id.gv_plate);
        View btnMoreIndex = findViewById(R.id.btn_more_index);
        View btnMorePlate = findViewById(R.id.btn_more_plate);
        View btnMoreIncease = findViewById(R.id.btn_more_incease);
        View btnMoreDown = findViewById(R.id.btn_more_down);
        View btnMoreHand = findViewById(R.id.btn_more_handover);
        View btnMoreAmplit = findViewById(R.id.btn_more_amplitude);

        lvIncease = (ListView) findViewById(R.id.lv_market_incease);
        lvDown = (ListView) findViewById(R.id.lv_market_down);
        lvHandover = (ListView) findViewById(R.id.lv_market_handover);
        lvAmplit = (ListView) findViewById(R.id.lv_market_amplitude);

        btnMoreIndex.setOnClickListener(this);
        btnMorePlate.setOnClickListener(this);
        btnMoreIncease.setOnClickListener(this);
        btnMoreDown.setOnClickListener(this);
        btnMoreHand.setOnClickListener(this);
        btnMoreAmplit.setOnClickListener(this);

        mIndexAdapter = new MarketCenterGridAdapter(this, mIndexDataList, false);
        mPlateAdapter = new MarketPlateGridAdapter(this, mSecotrList);
        gvPlate.setAdapter(mPlateAdapter);
        gvPlate.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SectorBean bean = mSecotrList.get(position);
                startActivity(MarketListActivity.newIntent(MarketCenterActivity.this, LoadViewType.PlateList,
                        bean.getId(), bean.getAbbr_name()));

            }
        });

        gvMainIndex.setAdapter(mIndexAdapter);
        gvMainIndex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // PromptManager.showToast("position:" + position);
                startQuoteActivity(mIndexDataList.get(position));

            }
        });

        // gvPlate.setAdapter(new MarketCenterGridAdapter(this, 6, true));
        mIncreaseAdapter = new MarketCenterItemAdapter(this, mIncreaseDataList);
        mDownAdapter = new MarketCenterItemAdapter(this, mDownDataList);
        mTurnOverAdapter = new MarketCenterItemAdapter(this, mTurnOverDataList, true);
        mAmplitAdapter = new MarketCenterItemAdapter(this, mAmpliDataList, true);
        lvIncease.setAdapter(mIncreaseAdapter);
        lvDown.setAdapter(mDownAdapter);
        lvHandover.setAdapter(mTurnOverAdapter);
        lvAmplit.setAdapter(mAmplitAdapter);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    private List<LoadSelectDataEngine> engineList;
    private PlateLoadMoreEngineImpl plateEngine;

    private void initData() {
        engineList = new ArrayList<LoadSelectDataEngine>();
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_INCREASE), StockViewType.MARKET_STOCK_UPRATIO, 10));
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_DOWN), StockViewType.MARKET_STOCK_DOWNRATIO, 10));
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_TURNOVER), StockViewType.MARKET_STOCK_TURNOVER, 10));
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_AMPLITU), StockViewType.MARKET_STOCK_AMPLIT, 10));
        engineList.add(new MarketCenterStockEngineImple(new StockLoadDataListener(INLAND_INDEX),
                MarketCenterStockEngineImple.CURRENT, 3));

        plateEngine = new PlateLoadMoreEngineImpl(plateListener);
        for (LoadSelectDataEngine mLoadDataEngine : engineList) {
            mLoadDataEngine.loadData();
        }
        plateEngine.loadData();
    }

    com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener plateListener = new com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener<SectorBean>() {

        @Override
        public void loadFinish(MoreDataBean<SectorBean> object) {
            if (null != object) {
                mSecotrList.clear();
                List<SectorBean> sectorList = object.getResults();
                mSecotrList.addAll(sectorList);
                mPlateAdapter.notifyDataSetChanged();
            } else {
                System.out.println("MoreDataBean is null");
            }

        }

    };

    class StockLoadDataListener implements ILoadDataBackListener {

        private String type;

        public StockLoadDataListener(String loadType) {
            this.type = loadType;
        }

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList && !TextUtils.isEmpty(type)) {
                if (type.equals(OpitionCenterStockEngineImple.ORDER_INCREASE)) {
                    mIncreaseDataList.clear();
                    mIncreaseDataList.addAll(dataList);
                    mIncreaseAdapter.notifyDataSetChanged();
                } else if (type.equals(OpitionCenterStockEngineImple.ORDER_DOWN)) {
                    mDownDataList.clear();
                    mDownDataList.addAll(dataList);
                    mDownAdapter.notifyDataSetChanged();
                } else if (type.equals(OpitionCenterStockEngineImple.ORDER_TURNOVER)) {
                    mTurnOverDataList.clear();
                    mTurnOverDataList.addAll(dataList);
                    mTurnOverAdapter.notifyDataSetChanged();
                } else if (type.equals(OpitionCenterStockEngineImple.ORDER_AMPLITU)) {
                    mAmpliDataList.clear();
                    mAmpliDataList.addAll(dataList);
                    mAmplitAdapter.notifyDataSetChanged();
                } else if (type.equals(INLAND_INDEX)) {
                    mIndexDataList.clear();
                    mIndexDataList.addAll(dataList);
                    mIndexAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void loadFail(ErrorBundle error) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(this, SelectAddOptionalActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_more_index: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.IndexUp));
            }
                break;
            case R.id.btn_more_plate: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.PlateHot));
            }
                break;
            case R.id.btn_more_incease: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockIncease));
            }
                break;
            case R.id.btn_more_down: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockDown));
            }
                break;
            case R.id.btn_more_handover: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockTurnOver));
            }
                break;
            case R.id.btn_more_amplitude: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockAmplit));
            }
                break;
            default:
                break;
        }
    }

    private void startQuoteActivity(SelectStockBean itemStock) {
        startActivity(StockQuotesActivity.newIntent(this, itemStock));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(this);
    }

    //
    // @Override
    // public void onResume() {
    // // TODO Auto-generated method stub
    // super.onResume();
    // // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
    // MobclickAgent.onResume(this);
    // }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {

            loadAllData();
            // if (loadDataListFragment instanceof FragmentSelectStockFund) {
            //
            // ((FragmentSelectStockFund) loadDataListFragment).refreshForMarker();
            // }
        }
    }

    private void loadAllData() {
        int i = 0;
        for (LoadSelectDataEngine mLoadDataEngine : engineList) {
            if (UIUtils.roundAble(mLoadDataEngine.getStatu())) {
                mLoadDataEngine.loadData();
                if (i == 0) {
                    plateEngine.loadData();
                }
            }
            i++;
        }
    }
}
