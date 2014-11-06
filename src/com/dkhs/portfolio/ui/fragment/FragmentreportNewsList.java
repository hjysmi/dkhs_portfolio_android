package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.NewsActivity;
import com.dkhs.portfolio.ui.YanbaoNewsActivity;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.adapter.ReportNewsAdapter;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

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
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentreportNewsList extends Fragment{
	private ListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private ReportNewsAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private View view;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        /*if (bundle != null) {
        	initDate();
        }*/
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_option_market_news,null);
		context = getActivity();
		
		initView(view);
		return view;
	}
	private void initDate(){
		Bundle bundle = getArguments();
		NewsforImpleEngine vo = (NewsforImpleEngine) bundle.getSerializable(VO);
		if(null != bundle){
			mDataList = new ArrayList<OptionNewsBean>();
			mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,bundle.getInt(NEWS_TYPE),vo);
			mLoadDataEngine.loadData();
		}

	}
	private void initView(View view) {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
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
				if(mDataList.get(position).getSymbols().size() >0){
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

    private void loadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);

            isLoadingMore = true;
            mLoadDataEngine.loadMore();
        }
    }
    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
				if (null != dataList) {
				    mDataList.addAll(dataList);
				    if(first){
				    	initView(view);
				    	first = false;
				    }
				    mOptionMarketAdapter.notifyDataSetChanged();
				    loadFinishUpdateView();
				    
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
    }
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initDate();
		super.onResume();
	}
	
}
