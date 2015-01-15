package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.YanbaoNewsActivity;
import com.dkhs.portfolio.ui.adapter.OptionlistAdapter;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

public class FragmentForOptionOnr extends Fragment{
	private ListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private OptionlistAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private static final String SYMBOL = "symbol";
    private static final String NAME = "name";
    private static final String SUB = "sub";
    private String symbol;
    private String name;
    private String subType;
    private View view;
	private boolean getadble = false;
	private RelativeLayout pb;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_option_market_news, null);
		context = getActivity();
		pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if(!(null != mDataList && mDataList.size() > 0)){
            pb.setVisibility(View.VISIBLE);
        }
		mDataList = new ArrayList<OptionNewsBean>();
		
		iv = (TextView) view.findViewById(android.R.id.empty);
        // iv.setText("暂无公告");
		Bundle extras = getArguments();
		if(null != extras){
			symbol = extras.getString(SYMBOL);
			name = extras.getString(NAME);
			subType = extras.getString(SUB);
		}
		if(null != view.findViewById(R.id.tv_title)){
			((TextView) view.findViewById(R.id.tv_title)).setText("研报-" + name);
		}
		initView(view);
		
		return view;
	}

	public static Fragment newIntent(Context context, String symbolName,String name,String subType) {
        Fragment f = new FragmentForOptionOnr();
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(NAME, name);
        b.putString(SUB, subType);
         f.setArguments(b);
        return f;
    }
	private void initDate(){
			try {
    			    Bundle extras = getArguments();
    		        if(null != extras){
    		            symbol = extras.getString(SYMBOL);
    		            name = extras.getString(NAME);
    		            subType = extras.getString(SUB);
    		        }
					NewsforImpleEngine vo = new NewsforImpleEngine();
					vo.setSymbol(symbol);
					vo.setContentSubType(subType);
					mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,OpitionNewsEngineImple.NEWS_OPITION_FOREACH,vo);
					//mLoadDataEngine.setLoadingDialog(context);;
					((OpitionNewsEngineImple) mLoadDataEngine).loadDatas();
					mLoadDataEngine.setFromYanbao(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	private void initView(View view) {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        
        mListView.setEmptyView(iv);
        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new OptionlistAdapter(context, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);

        mListView.removeFooterView(mFootView);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:

                    {
                        // 判断是否滚动到底部
                        if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
                            loadMore();

                        }
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
            mListView.setOnItemClickListener(itemBackClick);

    }
    OnItemClickListener itemBackClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			try {
				Intent intent;
				if(null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() >0){
					intent = YanbaoNewsActivity.newIntent(context, mDataList.get(position).getId(), mDataList.get(position).getSymbols().get(0).getSymbol(),mDataList.get(position).getSymbols().get(0).getAbbrName());
				}else{
					intent = YanbaoNewsActivity.newIntent(context, mDataList.get(position).getId(), null,null);
				}
				startActivity(intent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    };

    public void loadMore() {
        if (null != mLoadDataEngine&& !isLoadingMore && getadble) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);

            isLoadingMore = true;
            //mLoadDataEngine.setLoadingDialog(context);;
            mLoadDataEngine.loadMore();
        }
    }
    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                pb.setVisibility(View.GONE);
				if (null != dataList&&dataList.size()>0) {
				    mDataList.addAll(dataList);
				    if(first){
				    	//initView(view);
				    	first = false;
				    }
				    mOptionMarketAdapter.notifyDataSetChanged();
				    loadFinishUpdateView();
				    
				}else{
					if (null != context
                            && context instanceof StockQuotesActivity&&getadble) {
                        ((StockQuotesActivity) getActivity()).setLayoutHeight(0);
                    }
				    iv.setText("暂无研报");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }

    };
    private void loadFinishUpdateView() {
    	mOptionMarketAdapter.notifyDataSetChanged();
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }
        int height = 0;
        for (int i = 0, len = mOptionMarketAdapter.getCount(); i < len; i++) {
            View listItem = mOptionMarketAdapter.getView(i, null, mListView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            int list_child_item_height = listItem.getMeasuredHeight()+mListView.getDividerHeight();
            height += list_child_item_height; // 统计所有子项的总高度
        }
        if (null != context
                && context instanceof StockQuotesActivity && getadble) {
            ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
        }
    }

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if(isVisibleToUser){
		    initDate();
			getadble = true;
			if(null == mDataList || mDataList.size() < 2){
				if (null != context
                        && context instanceof StockQuotesActivity&& getadble) {
                    ((StockQuotesActivity) getActivity()).setLayoutHeight(0);
                }
			}else if(null != mDataList){
			    int height = 0;
		        for (int i = 0, len = mOptionMarketAdapter.getCount(); i < len; i++) {
		            View listItem = mOptionMarketAdapter.getView(i, null, mListView);
		            listItem.measure(0, 0); // 计算子项View 的宽高
		            int list_child_item_height = listItem.getMeasuredHeight()+mListView.getDividerHeight();
		            height += list_child_item_height; // 统计所有子项的总高度
		        }
		        if (null != context
		                && context instanceof StockQuotesActivity && getadble) {
		            ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
		        }
			}
		}else{
			getadble = false;
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_yanbao);
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
}
