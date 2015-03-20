package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.OptionlistAdapter;
import com.dkhs.portfolio.ui.adapter.ReportNewsAdapter;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

public class ReportForOneListActivity extends ModelAcitivity implements OnLoadMoreListener {
    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    // private View mFootView;
    private Context context;
    // private ReportNewsAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    // private TextView iv;
    private static final String SYMBOL = "symbol";
    private static final String NAME = "name";
    private static final String SUB = "sub";
    private static final String KEY_CONTENTTYPE = "key_contenttype";
    private String mContentType;
    private String symbol;
    private String name;
    private String subType;
    // private RelativeLayout pb;
    private Fragment loadDataListFragment;
    public SwipeRefreshLayout mSwipeLayout;

    public static Intent newIntent(Context context, String symbolName, String name, String subType, String contentType) {
        Intent intent = new Intent(context, ReportForOneListActivity.class);
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(NAME, name);
        b.putString(SUB, subType);
        b.putString(KEY_CONTENTTYPE, contentType);
        intent.putExtras(b);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.fragment_report_news);
        context = this;
        mDataList = new ArrayList<OptionNewsBean>();

        /*
         * iv = (TextView) findViewById(android.R.id.empty);
         * pb = (RelativeLayout) findViewById(android.R.id.progress);
         * pb.setVisibility(View.VISIBLE);
         */
        // iv.setText("暂无公告");
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            symbol = extras.getString(SYMBOL);
            name = extras.getString(NAME);
            subType = extras.getString(SUB);
            mContentType = extras.getString(KEY_CONTENTTYPE);
        }
        if (!TextUtils.isEmpty(mContentType) && mContentType.equals("20")) {
            ((TextView) findViewById(R.id.tv_title)).setText("公告-" + name);
        } else {
            ((TextView) findViewById(R.id.tv_title)).setText("研报-" + name);
        }
        // initView();
        // initDate();
        replaceDataList();
    }

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            try {
                NewsforModel vo = new NewsforModel();
                vo.setSymbol(symbol);
                vo.setContentSubType(subType);
                if (!TextUtils.isEmpty(mContentType) && mContentType.equals("20")) {
                    loadDataListFragment = ReportListForAllFragment.getFragment(vo,
                            ReportListForAllFragment.NEWS_SECOND_NOTICE);
                } else if (null == subType) {
                    loadDataListFragment = ReportListForAllFragment.getFragment(vo,
                            OpitionNewsEngineImple.NEWS_OPITION_FOREACH);
                } else {
                    loadDataListFragment = ReportListForAllFragment.getFragment(vo,
                            OpitionNewsEngineImple.GROUP_FOR_ONE);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment)
                        .commit();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // private void initDate() {
    // try {
    // NewsforModel vo = new NewsforModel();
    // vo.setSymbol(symbol);
    // vo.setContentSubType(subType);
    // if (null == subType) {
    // mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
    // OpitionNewsEngineImple.NEWS_OPITION_FOREACH, vo);
    // } else {
    // mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
    // OpitionNewsEngineImple.GROUP_FOR_ONE, vo);
    // }
    // // mLoadDataEngine.setLoadingDialog(context);
    // mLoadDataEngine.loadData();
    // mLoadDataEngine.setFromYanbao(false);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }

    // private void initView() {
    // // mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
    // mListView = (PullToRefreshListView) findViewById(android.R.id.list);
    //
    // // mListView.setEmptyView(iv);
    // // mListView.addFooterView(mFootView);
    // // mOptionMarketAdapter = new OptionlistAdapter(context, mDataList);
    //
    // // mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
    // // mListView.setAdapter(mOptionMarketAdapter);
    //
    // // mListView.removeFooterView(mFootView);
    // // mListView.setOnScrollListener(new OnScrollListener() {
    // //
    // // @Override
    // // public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    // //
    // // switch (scrollState) {
    // // case OnScrollListener.SCROLL_STATE_IDLE:
    // //
    // // {
    // // // 判断是否滚动到底部
    // // if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
    // // loadMore();
    // //
    // // }
    // // }
    // //
    // // }
    // //
    // // }
    // //
    // // @Override
    // // public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    // //
    // // }
    // // });
    // mListView.setOnItemClickListener(itemBackClick);
    // mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    // mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
    //
    // @Override
    // public void onRefresh() {
    // new Handler().postDelayed(new Runnable() {
    // @Override
    // public void run() {
    // mSwipeLayout.setRefreshing(false);
    // }
    // }, 2000);
    //
    // }
    // });
    // mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
    //
    // }

    // OnItemClickListener itemBackClick = new OnItemClickListener() {
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    // // TODO Auto-generated method stub
    // try {
    // Intent intent;
    // if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
    // intent = YanbaoDetailActivity.newIntent(context, mDataList.get(position).getId(),
    // mDataList.get(position).getSymbols().get(0).getSymbol(), mDataList.get(position)
    // .getSymbols().get(0).getAbbrName(), mDataList.get(position).getContentType());
    // } else {
    // intent = YanbaoDetailActivity.newIntent(context, mDataList.get(position).getId(), null, null, null);
    // }
    // startActivity(intent);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // };
    //
    // private void loadMore() {
    // if (null != mLoadDataEngine) {
    // if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
    // // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
    // return;
    // }
    // // mListView.addFooterView(mFootView);
    //
    // isLoadingMore = true;
    // // mLoadDataEngine.setLoadingDialog(context);
    // mLoadDataEngine.loadMore();
    // }
    // }
    //
    // ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {
    //
    // @Override
    // public void loadFinish(List<OptionNewsBean> dataList) {
    // try {
    // // pb.setVisibility(View.GONE);
    // mListView.onLoadMoreComplete();
    // if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
    // mListView.setCanLoadMore(false);
    // mListView.setAutoLoadMore(false);
    // } else {
    // mListView.setCanLoadMore(true);
    // mListView.setAutoLoadMore(true);
    // if (mLoadDataEngine.getCurrentpage() == 1)
    // mListView.setOnLoadListener(ReportForOneListActivity.this);
    // }
    // if (null != dataList && dataList.size() > 0) {
    // mDataList.addAll(dataList);
    // /*
    // * if (first) {
    // * initView();
    // * first = false;
    // * }
    // */
    // // mOptionMarketAdapter.notifyDataSetChanged();
    // // loadFinishUpdateView();
    //
    // } else {
    // // iv.setText("暂无研报");
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }
    //
    // };

    // private void loadFinishUpdateView() {
    // // mOptionMarketAdapter.notifyDataSetChanged();
    // isLoadingMore = false;
    // if (mListView != null) {
    // // mListView.removeFooterView(mFootView);
    // }
    // }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_yanbao_list);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onLoadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }

            isLoadingMore = true;
            mLoadDataEngine.loadMore();
        }
    }
}
