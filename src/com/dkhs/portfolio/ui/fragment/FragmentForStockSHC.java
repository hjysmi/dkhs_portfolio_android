package com.dkhs.portfolio.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.NewsActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.adapter.OptionForOnelistAdapter;
import com.umeng.analytics.MobclickAgent;

public class FragmentForStockSHC  extends Fragment implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6565512311564641L;

    private ListView mListView;

    private Context context;
    private List<SelectStockBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private View view;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";
    private NewsforImpleEngine vo;
    private int types;
    private TextView tv;
    private boolean getadle = false;
    private boolean onreflush =  false;
    private MarketCenterItemAdapter mOptionlistAdapter;

    // private LinearLayout layouts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }
    public static Fragment newIntent(){
        FragmentForStockSHC mFragmentForStockSHC = new FragmentForStockSHC();
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
        if (null != context && context.getClass().getName().equals("com.dkhs.portfolio.ui.StockQuotesActivity")&& getadle) {
            ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
        }
        initView(view);
        //if (null != vo && vo.getContentType().equals("20")) {
            
        //}
        return view;
    }

    private void initDate() {
        Bundle bundle = getArguments();

        if (null != bundle) {
            vo = (NewsforImpleEngine) bundle.getSerializable(VO);
            // layouts = vo.getLayout();
            types = bundle.getInt(NEWS_TYPE);
        }

    }

    private void initView(View view) {
        tv = (TextView) view.findViewById(android.R.id.empty);
        mDataList = new ArrayList<SelectStockBean>();
        
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(tv);
        mOptionlistAdapter = new MarketCenterItemAdapter(context, mDataList,true);
        mListView.setAdapter(mOptionlistAdapter);
        /*mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, types, vo);
        mLoadDataEngine.setLoadingDialog(getActivity());
        mLoadDataEngine.loadData();
        mLoadDataEngine.setFromYanbao(false);*/
        if (null != context && context.getClass().getName().equals("com.dkhs.portfolio.ui.StockQuotesActivity")&& getadle) {
            ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
        }
        mListView.setOnItemClickListener(itemBackClick);

    }
    public void OnReFlush(){
        if(null != mLoadDataEngine && !onreflush){
            mLoadDataEngine.loadData();
            onreflush = true;
        }
    }
    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
           
        }
    };

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                if (null != dataList && dataList.size() > 0) {
                    if (null != mOptionlistAdapter) {
                        mOptionlistAdapter.notifyDataSetChanged();
                    }
                    loadFinishUpdateView();
                } else {
                    if (null != vo && null != vo.getPageTitle()) {
                        tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                    }
                    if (null != context
                            && context.getClass().getName().equals("com.dkhs.portfolio.ui.StockQuotesActivity") && getadle) {
                        ((StockQuotesActivity) getActivity()).setLayoutHeight(0);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    private void loadFinishUpdateView() {
        // mOptionMarketAdapter.notifyDataSetChanged();
        if (null != mOptionlistAdapter) {
            mOptionlistAdapter.notifyDataSetChanged();
        }
        onreflush = false;
        int height = 0;
        for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
            View listItem = mOptionlistAdapter.getView(i, null, mListView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            int list_child_item_height = listItem.getMeasuredHeight()+mListView.getDividerHeight();
            height += list_child_item_height; // 统计所有子项的总高度
        }
        if (null != context
                && context.getClass().getName().equals("com.dkhs.portfolio.ui.StockQuotesActivity") && getadle) {
            ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
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
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            if(isVisibleToUser){
                getadle = true;
                if(null == mDataList || mDataList.size() < 2){
                    if (null != context
                            && context.getClass().getName().equals("com.dkhs.portfolio.ui.StockQuotesActivity")&& getadle) {
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
                            && context.getClass().getName().equals("com.dkhs.portfolio.ui.StockQuotesActivity") && getadle) {
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