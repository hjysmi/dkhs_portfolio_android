package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.NewsActivity;
import com.dkhs.portfolio.ui.NoticesActivity;
import com.dkhs.portfolio.ui.OptionListAcitivity;
import com.dkhs.portfolio.ui.ReportForOneListActivity;
import com.dkhs.portfolio.ui.YanbaoDetailActivity;
import com.dkhs.portfolio.ui.adapter.InfoOptionAdapter;
import com.dkhs.portfolio.ui.adapter.OptionForOnelistAdapter;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.adapter.OptionlistAdapter;
import com.dkhs.portfolio.ui.adapter.ReportNewsAdapter;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReportListForAllFragment extends Fragment implements OnLoadMoreListener {
    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private BaseAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private RelativeLayout pb;

    public SwipeRefreshLayout mSwipeLayout;
    public static final String TYPE_NAME = "typename";
    public static final String VO_NAME = "voname";
    private int type;
    private NewsforModel vo;

    public static ReportListForAllFragment getFragment(NewsforModel vo, int type) {
        ReportListForAllFragment fragment = new ReportListForAllFragment();
        Bundle args = new Bundle();
        args.putSerializable(VO_NAME, vo);
        args.putInt(TYPE_NAME, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle bundle = getArguments();
        vo = (NewsforModel) bundle.getSerializable(VO_NAME);
        type = bundle.getInt(TYPE_NAME);
        View view = inflater.inflate(R.layout.activity_option_market_news, null);
        context = getActivity();
        mDataList = new ArrayList<OptionNewsBean>();
        iv = (TextView) view.findViewById(android.R.id.empty);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        initView(view);

        return view;
    }

    private void initDate() {
        if (vo != null) {
            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, type, vo);
            // System.out.println("new OpitionNewsEngineImple type:" + type + " vo:" + vo);
            mLoadDataEngine.loadData();
        } else {
            iv.setText("暂无添加自选股");
            pb.setVisibility(View.GONE);
        }

    }

    private boolean isRefresh;

    private void refreshData() {
        isRefresh = true;
        mLoadDataEngine.loadData();
    }

    private void initView(View view) {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);

        mListView.setEmptyView(iv);
        // mListView.addFooterView(mFootView);
        switch (type) {
            case OpitionNewsEngineImple.NEWS_GROUP_TWO:
                mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.NEWSFOREACH:
                mOptionMarketAdapter = new OptionForOnelistAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.NEWS_OPITION_FOREACH:
                mOptionMarketAdapter = new OptionlistAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.GROUP_FOR_ONE:
                mOptionMarketAdapter = new OptionlistAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.NEWS_GROUP:
                mOptionMarketAdapter = new InfoOptionAdapter(context, mDataList);
                break;
            default:
                mOptionMarketAdapter = new OptionMarketAdapter(context, mDataList);
                break;
        }

        mListView.setAdapter(mOptionMarketAdapter);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshData();

            }
        });
        mListView.setOnItemClickListener(itemBackClick);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mLoadDataEngine) {
            initDate();
        } else {
            refreshData();
        }
    };

    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                String name = "";
                Intent intent;
                switch (type) {
                    case OpitionNewsEngineImple.NEWSALL:
                        intent = OptionListAcitivity.newIntent(context, mDataList.get(position).getSymbols().get(0)
                                .getSymbol()
                                + "", "20", mDataList.get(position).getSymbols().get(0).getAbbrName());
                        startActivity(intent);
                        break;
                    case OpitionNewsEngineImple.NEWS_GROUP:
                        if (null != mDataList.get(position).getSymbols()
                                && mDataList.get(position).getSymbols().size() > 0) {

                            intent = ReportForOneListActivity.newIntent(context, mDataList.get(position).getSymbols()
                                    .get(0).getSymbol(), mDataList.get(position).getSymbols().get(0).getAbbrName(),
                                    vo.getContentSubType());
                        } else {
                            intent = ReportForOneListActivity.newIntent(context, null, null, null);
                        }
                        startActivity(intent);
                        break;
                    case OpitionNewsEngineImple.NEWS_GROUP_FOREACH:
                        if (vo.getContentType().equals("20")) {
                            intent = OptionListAcitivity.newIntent(context, mDataList.get(position).getSymbols().get(0)
                                    .getSymbol()
                                    + "", "20", mDataList.get(position).getSymbols().get(0).getAbbrName());
                            startActivity(intent);
                        } else {
                            if (null != mDataList.get(position).getSymbols()
                                    && mDataList.get(position).getSymbols().size() > 0) {
                                intent = ReportForOneListActivity.newIntent(context, mDataList.get(position)
                                        .getSymbols().get(0).getSymbol(), mDataList.get(position).getSymbols().get(0)
                                        .getAbbrName(), vo.getContentSubType());
                            } else {
                                intent = ReportForOneListActivity.newIntent(context, null, null, null);
                            }
                            startActivity(intent);
                        }

                        break;
                    default:
                        try {
                            switch (type) {
                                case 1:
                                    name = "公告正文";
                                    if (null != mDataList.get(position).getSymbols()
                                            && mDataList.get(position).getSymbols().size() > 0) {
                                        intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), name,
                                                mDataList.get(position).getSymbols().get(0).getAbbrName(), mDataList
                                                        .get(position).getSymbols().get(0).getId());
                                        startActivity(intent);
                                    } else {
                                        intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), name,
                                                null, null);
                                        startActivity(intent);
                                    }
                                    break;

                                default:
                                    name = "研报正文";
                                    if (null != mDataList.get(position).getSymbols()
                                            && mDataList.get(position).getSymbols().size() > 0) {
                                        intent = YanbaoDetailActivity.newIntent(context, mDataList.get(position)
                                                .getId(), mDataList.get(position).getSymbols().get(0).getSymbol(),
                                                mDataList.get(position).getSymbols().get(0).getAbbrName());
                                    } else {
                                        intent = YanbaoDetailActivity.newIntent(context, mDataList.get(position)
                                                .getId(), null, null);
                                    }
                                    startActivity(intent);
                                    break;
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }

            } catch (Exception e) {
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
            mLoadDataEngine.setLoadingDialog(context);
            mLoadDataEngine.loadMore();
        }
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                pb.setVisibility(View.GONE);
                mListView.onLoadMoreComplete();
                if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                    mListView.setCanLoadMore(false);
                    mListView.setAutoLoadMore(false);
                } else {
                    mListView.setCanLoadMore(true);
                    mListView.setAutoLoadMore(true);
                    if (mLoadDataEngine.getCurrentpage() == 1)
                        mListView.setOnLoadListener(ReportListForAllFragment.this);
                }
                if (isRefresh) {
                    mDataList.clear();
                    isRefresh = false;
                }
                if (null != dataList && dataList.size() > 0) {

                    mDataList.addAll(dataList);
                    mOptionMarketAdapter.notifyDataSetChanged();
                } else {
                    iv.setText("暂无公告");
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
