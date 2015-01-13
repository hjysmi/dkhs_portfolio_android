package com.dkhs.portfolio.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

public class FragmentForStockSHC  extends Fragment{
    /**
     * 
     */
    private ListView mListView;

    private Context context;
    private List<SelectStockBean> mDataList;
    private LoadSelectDataEngine mLoadDataEngine;
    boolean first = true;
    private View view;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";
    private TextView tv;
    private boolean getadle = false;
    private MarketCenterItemAdapter mOptionlistAdapter;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 5;
    private boolean isTimerStart = true;
    private final static String EXCHANGE = "exchange";
    private final static String SORT = "sort";
    private final static String STYPE = "symbol_stype";
    private final static String SECTOR = "list_sector";
    private final static String BOOLEAN = "boolean";
    private String exchange;
    private StockViewType sort;
    private String symbol_stype;
    private String list_sector;
    private boolean setColor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }
    public static Fragment newIntent(String exchange,StockViewType sort,String symbol_stype,String list_sector,boolean setColor){
        FragmentForStockSHC mFragmentForStockSHC = new FragmentForStockSHC();
        Bundle b = new Bundle();
        b.putString(EXCHANGE, exchange);
        b.putSerializable(SORT, sort);
        b.putString(STYPE, symbol_stype);
        b.putString(SECTOR, list_sector);
        b.putBoolean(BOOLEAN, setColor);
        mFragmentForStockSHC.setArguments(b);
        return mFragmentForStockSHC;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.activity_option_market_news, null);
        context = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            initDate();
        }
        initView(view);
        if (null != context && context instanceof StockQuotesActivity&& getadle) {
            ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
        }
        return view;
    }

    private void initDate() {
        Bundle bundle = getArguments();

        if (null != bundle) {
            sort = (StockViewType) bundle.getSerializable(SORT);
            exchange = bundle.getString(EXCHANGE);
            symbol_stype = bundle.getString(STYPE);
            list_sector = bundle.getString(SECTOR);
            setColor = bundle.getBoolean(BOOLEAN);
        }

    }

    private void initView(View view) {
        tv = (TextView) view.findViewById(android.R.id.empty);
        mDataList = new ArrayList<SelectStockBean>();
        
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(tv);
        if(setColor){
            mOptionlistAdapter = new MarketCenterItemAdapter(context, mDataList);
        }else{
            mOptionlistAdapter = new MarketCenterItemAdapter(context, mDataList,true);
        }
        mListView.setAdapter(mOptionlistAdapter);
        mLoadDataEngine = new OpitionCenterStockEngineImple(new StockLoadDataListener(),sort, 10,list_sector,symbol_stype,exchange);
        mLoadDataEngine.loadData();
        

    }
    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            startActivity(StockQuotesActivity.newIntent(getActivity(), mDataList.get(position)));
        }
    };

    class StockLoadDataListener implements ILoadDataBackListener {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mDataList.clear();
                mDataList.addAll(dataList);
                mOptionlistAdapter.notifyDataSetChanged();
                loadFinishUpdateView();
            }
        }

        @Override
        public void loadFail(ErrorBundle error) {
            

        }

    }

    private void loadFinishUpdateView() {
        // mOptionMarketAdapter.notifyDataSetChanged();
        try {
            if (null != mOptionlistAdapter) {
                mOptionlistAdapter.notifyDataSetChanged();
            }
            int height = 0;
            for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
                View listItem = mOptionlistAdapter.getView(i, null, mListView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                int list_child_item_height = listItem.getMeasuredHeight()+mListView.getDividerHeight();
                height += list_child_item_height; // 统计所有子项的总高度
            }
            if (null != context
                    && context instanceof StockQuotesActivity && getadle) {
                ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_news);
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        if (mMarketTimer == null && isTimerStart) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        MobclickAgent.onPageStart(mPageName);
    }
    
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }
    }
    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            if (UIUtils.roundAble(mLoadDataEngine.getStatu())) {
                    mLoadDataEngine.loadData();
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            if(isVisibleToUser){
                getadle = true;
                if(null == mDataList || mDataList.size() < 2){
                    if (null != context
                            && context instanceof StockQuotesActivity&& getadle) {
                        ((StockQuotesActivity) getActivity()).setLayoutHeight(0);
                    }
                }else if(null != mDataList){
                    int height = 0;
                    for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
                        View listItem = mOptionlistAdapter.getView(i, null, mListView);
                        listItem.measure(0, 0); // 计算子项View 的宽高
                        int list_child_item_height = listItem.getMeasuredHeight()+mListView.getDividerHeight();
                        height += list_child_item_height; // 统计所有子项的总高度
                    }
                    if (null != context
                            && context instanceof StockQuotesActivity && getadle) {
                        ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
                    }
                }
            }
        } else {
            // 不可见时不执行操作
            getadle = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}