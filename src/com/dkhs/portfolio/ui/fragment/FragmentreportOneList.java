package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.ReportForOneListActivity;
import com.dkhs.portfolio.ui.adapter.ReportNewsAdapter;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

public class FragmentreportOneList extends Fragment implements OnLoadMoreListener {
    private PullToRefreshListView mListView;

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
    private TextView tv;
    private String subType;
    private boolean uservivible = false;
    private RelativeLayout pb;

    public SwipeRefreshLayout mSwipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.activity_option_market_news, null);
        context = getActivity();
        tv = (TextView) view.findViewById(android.R.id.empty);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if (!(null != mDataList && mDataList.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        initView(view);
        Bundle bundle = getArguments();
        NewsforModel vo = (NewsforModel) bundle.getSerializable(VO);
        subType = vo.getContentSubType();
        // initDate();
        UserEntity user;
        try {
            user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
            if (null == user) {
                tv.setText("暂无添加自选股");
                pb.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        return view;
    }

    private void initDate() {
        Bundle bundle = getArguments();
        NewsforModel vo = (NewsforModel) bundle.getSerializable(VO);
        if (null != bundle) {
            mDataList = new ArrayList<OptionNewsBean>();
            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, bundle.getInt(NEWS_TYPE), vo);
            mLoadDataEngine.loadData();
            // mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.setFromYanbao(false);
        }

    }

    private void initView(View view) {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        // mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);

        // mListView.removeFooterView(mFootView);
        // mListView.setOnScrollListener(new OnScrollListener() {
        //
        // @Override
        // public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //
        // switch (scrollState) {
        // case OnScrollListener.SCROLL_STATE_IDLE:
        //
        // {
        // // 判断是否滚动到底部
        // if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
        // loadMore();
        //
        // }
        // }
        //
        // }
        //
        // }
        //
        // @Override
        // public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //
        // }
        // });
        mListView.setOnItemClickListener(itemBackClick);

    }

    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            try {
                Intent intent;
                if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
                    intent = ReportForOneListActivity.newIntent(context, mDataList.get(position).getSymbols().get(0)
                            .getSymbol(), mDataList.get(position).getSymbols().get(0).getAbbrName(), subType, mDataList
                            .get(position).getContentType());
                } else {
                    intent = ReportForOneListActivity.newIntent(context, null, null, null, null);
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
                // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);

            isLoadingMore = true;
            mLoadDataEngine.loadMore();
            mLoadDataEngine.setLoadingDialog(getActivity());
        }
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                mListView.onLoadMoreComplete();
                pb.setVisibility(View.GONE);
                if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                    mListView.setCanLoadMore(false);
                    mListView.setAutoLoadMore(false);
                } else {
                    mListView.setCanLoadMore(true);
                    mListView.setAutoLoadMore(true);
                    if (mLoadDataEngine.getCurrentpage() == 1)
                        mListView.setOnLoadListener(FragmentreportOneList.this);
                }
                if (null != dataList && dataList.size() > 0) {
                    // mDataList.clear();
                    mDataList.addAll(dataList);
                    if (first) {
                        initView(view);
                        first = false;
                    }
                    mOptionMarketAdapter.notifyDataSetChanged();
                    // loadFinishUpdateView();
                    mOptionMarketAdapter.notifyDataSetChanged();
                    isLoadingMore = false;
                    if (mListView != null) {
                        mListView.removeFooterView(mFootView);
                    }

                } else {
                    tv.setText("暂无研报");
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

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_yanbao);

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
        /*
         * if(uservivible){
         * initDate();
         * }
         */
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            // fragment可见时加载数据
            // initDate();
            initDate();
        } else {
            // 不可见时不执行操作
        }
        uservivible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onLoadMore() {
        mLoadDataEngine.loadMore();
    }

}
