package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.OptionForOnelistAdapter;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class OptionListAcitivity extends ModelAcitivity {

    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    // private View mFootView;
    private Context context;
    private OptionForOnelistAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private static final String SYMBOL = "symbol";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private String symbol;
    private String type;
    private String name;
    private RelativeLayout pb;

    public SwipeRefreshLayout mSwipeLayout;
    private ReportListForAllFragment loadDataListFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
        setContentView(R.layout.fragment_report_news);
        // iv.setText("暂无公告");
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            symbol = extras.getString(SYMBOL);
            type = extras.getString(TYPE);
            name = extras.getString(NAME);
        }
        ((TextView) findViewById(R.id.tv_title)).setText(name + "公告");
        replaceDataList();
    }

    public static Intent newIntent(Context context, String symbolName, String type, String name) {
        Intent intent = new Intent(context, OptionListAcitivity.class);
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(TYPE, type);
        b.putString(NAME, name);
        intent.putExtras(b);
        return intent;
    }

    private void replaceDataList() {
        // view_datalist
        if (null == loadDataListFragment) {
            UserEntity user = UserEngineImpl.getUserEntity();
            if (user != null) {
                String userId = user.getId() + "";
                NewsforModel vo = new NewsforModel();
                vo.setUserid(userId);
                vo.setSymbol(symbol);
                vo.setContentType(type);
                loadDataListFragment = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWSFOREACH);
            } else {
                loadDataListFragment = ReportListForAllFragment.getFragment(null, OpitionNewsEngineImple.NEWSFOREACH);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.view_datalist, loadDataListFragment).commit();

        }
    }

    // private void initDate() {
    // UserEntity user;
    // try {
    // user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
    // if (user != null) {
    // if (!TextUtils.isEmpty(user.getAccess_token())) {
    // user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
    // }
    // String userId = user.getId() + "";
    // NewsforModel vo = new NewsforModel();
    // vo.setUserid(userId);
    // vo.setSymbol(symbol);
    // vo.setContentType(type);
    // mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
    // OpitionNewsEngineImple.NEWSFOREACH, vo);
    // // mLoadDataEngine.setLoadingDialog(mContext);
    // mLoadDataEngine.loadData();
    // mLoadDataEngine.setFromYanbao(false);
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }

    // private void initView() {
    // mListView = (PullToRefreshListView) findViewById(android.R.id.list);
    //
    // mListView.setEmptyView(iv);
    // mOptionMarketAdapter = new OptionForOnelistAdapter(mContext, mDataList);
    // mListView.setAdapter(mOptionMarketAdapter);
    // mListView.setOnLoadListener(new OnLoadMoreListener() {
    //
    // @Override
    // public void onLoadMore() {
    //
    // loadMore();
    // }
    // });
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
    // //
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
    // // mListView.setOnScrollListener(new OnScrollListener() {
    // //
    // // @Override
    // // public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    // //
    // // switch (scrollState) {
    // // case OnScrollListener.SCROLL_STATE_IDLE:
    // //
    // // {
    // // // åˆ¤æ–­æ˜¯å¦æ»šåŠ¨åˆ°åº•éƒ¨
    // // if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
    // //
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
    //
    // mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    // mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
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
    // }
    //
    // OnItemClickListener itemBackClick = new OnItemClickListener() {
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    // // TODO Auto-generated method stub
    // try {
    // if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
    // Intent intent = NewsActivity.newIntent(mContext, mDataList.get(position).getId(), "公告正文", mDataList
    // .get(position).getSymbols().get(0).getAbbrName(),
    // mDataList.get(position).getSymbols().get(0).getId());
    // startActivity(intent);
    // } else {
    // Intent intent = NewsActivity
    // .newIntent(mContext, mDataList.get(position).getId(), "公告正文", null, null);
    // startActivity(intent);
    // }
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
    // // Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
    // return;
    // }
    //
    // isLoadingMore = true;
    // // mLoadDataEngine.setLoadingDialog(mContext);
    // mLoadDataEngine.loadMore();
    // }
    // }

    // ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {
    //
    // @Override
    // public void loadFinish(List<OptionNewsBean> dataList) {
    // try {
    // pb.setVisibility(View.GONE);
    // if (null != dataList && dataList.size() > 0) {
    // mDataList.addAll(dataList);
    // if (first) {
    // initView();
    // first = false;
    // }
    // mOptionMarketAdapter.notifyDataSetChanged();
    // loadFinishUpdateView();
    //
    // } else {
    // iv.setText("暂无公告");
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
    // mOptionMarketAdapter.notifyDataSetChanged();
    // isLoadingMore = false;
    // if (mListView != null) {
    // }
    // }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_market_one);


}
