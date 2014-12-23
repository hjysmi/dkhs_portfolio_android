package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.ui.MarketListActivity.LoadViewType;
import com.dkhs.portfolio.ui.adapter.MarketCenterGridAdapter;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.utils.PromptManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 行情中心
 * 
 * @author weiting
 * 
 */
public class MarketCenterActivity extends ModelAcitivity implements OnClickListener {

    private GridView gvMainIndex;
    private List<SelectStockBean> mIndexDataList = new ArrayList<SelectStockBean>();
    private MarketCenterGridAdapter mIndexAdapter;

    private GridView gvPlate;
    // private View btnMoreIndex;
    // private View btnMorePlate;

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

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_marketcenter);
        setTitle(R.string.marketcenter_title);

        initView();

        // layoutMarkerParent = (LinearLayout) findViewById(R.id.layout_marker_parenst);
        // context = this;
        // Button btn = getRightButton();
        // btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select), null,
        // null, null);
        // // btn.setBackgroundResource(R.drawable.btn_search_select);
        // btn.setOnClickListener(this);
        // String[] name = new String[]{"沪深行情","国内指数"};
        // List<Fragment> frag = new ArrayList<Fragment>();
        // frag.add(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.SHEN_HU));
        // frag.add(FragmentMarkerCenter.initFrag(FragmentMarkerCenter.INSIDE_COUNT));
        // new FragmentSelectAdapter(context, name, frag, layoutMarkerParent, getSupportFragmentManager());
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initView() {

        Button addButton = getRightButton();
        // addButton.setBackgroundResource(R.drawable.btn_search_select);
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
        mTurnOverAdapter = new MarketCenterItemAdapter(this, mTurnOverDataList);
        mAmplitAdapter = new MarketCenterItemAdapter(this, mAmpliDataList);
        lvIncease.setAdapter(mIncreaseAdapter);
        lvDown.setAdapter(mDownAdapter);
        lvHandover.setAdapter(mTurnOverAdapter);
        lvAmplit.setAdapter(mAmplitAdapter);

        initData();
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

    private void initData() {
        new OpitionCenterStockEngineImple(mIncreaseListener, OpitionCenterStockEngineImple.ORDER_INCREASE, 10)
                .loadData();
        new OpitionCenterStockEngineImple(mDownListener, OpitionCenterStockEngineImple.ORDER_DOWN, 10).loadData();
        new OpitionCenterStockEngineImple(mTurnOverListener, OpitionCenterStockEngineImple.ORDER_TURNOVER, 10)
                .loadData();
        new OpitionCenterStockEngineImple(mAmpliOverListener, OpitionCenterStockEngineImple.ORDER_AMPLITU, 10)
                .loadData();
        new MarketCenterStockEngineImple(mIndexListener, MarketCenterStockEngineImple.CURRENT, 3).loadData();
    }

    ILoadDataBackListener mIndexListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mIndexDataList.addAll(dataList);
                mIndexAdapter.notifyDataSetChanged();
            }
        }

    };
    ILoadDataBackListener mAmpliOverListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mAmpliDataList.addAll(dataList);
                mAmplitAdapter.notifyDataSetChanged();
            }
        }

    };
    ILoadDataBackListener mTurnOverListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mTurnOverDataList.addAll(dataList);
                mTurnOverAdapter.notifyDataSetChanged();
            }
        }

    };
    ILoadDataBackListener mIncreaseListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mIncreaseDataList.addAll(dataList);
                mIncreaseAdapter.notifyDataSetChanged();
            }
        }

    };
    ILoadDataBackListener mDownListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mDownDataList.addAll(dataList);
                mDownAdapter.notifyDataSetChanged();
            }
        }

    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(this, SelectAddOptionalActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_more_index: {
                // PromptManager.showToast("更多指数");
                startActivity(MarketListActivity.newIntent(this, LoadViewType.IndexUp));
            }
                break;
            case R.id.btn_more_plate: {
                // PromptManager.showToast("更多板块");
                startActivity(MarketListActivity.newIntent(this, LoadViewType.PlateHot));
            }
                break;
            case R.id.btn_more_incease: {
                // PromptManager.showToast("更多涨幅榜");
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockIncease));
            }
                break;
            case R.id.btn_more_down: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockDown));
                // PromptManager.showToast("更多跌幅榜");
            }
                break;
            case R.id.btn_more_handover: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockTurnOver));
                // PromptManager.showToast("更多换手率榜");
            }
                break;
            case R.id.btn_more_amplitude: {
                startActivity(MarketListActivity.newIntent(this, LoadViewType.StockAmplit));
                // PromptManager.showToast("更多振幅榜");
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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(this);
    }
}
