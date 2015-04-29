package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsTextEngineImple;
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
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.test.UiThreadTest;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReportListForAllFragment extends BaseFragment implements OnLoadMoreListener {
    private PullToRefreshListView mListView;

    // 二级公告界面
    public final static int NEWS_SECOND_NOTICE = 88;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private BaseAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private OpitionNewsEngineImple mLoadDataEngine;
    boolean first = true;
    private TextView tvEmpty;
    private RelativeLayout pb;

    public SwipeRefreshLayout mSwipeLayout;
    public static final String TYPE_NAME = "typename";
    public static final String VO_NAME = "voname";
    private int viewType;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        vo = (NewsforModel) bundle.getSerializable(VO_NAME);
        viewType = bundle.getInt(TYPE_NAME);
        context = getActivity();
        mDataList = new ArrayList<OptionNewsBean>();
        tvEmpty = (TextView) view.findViewById(android.R.id.empty);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        initView(view);
    }

    private void initDate() {
        if (vo != null) {
            // if (type == NEWS_SECOND_NOTICE) {
            // NewsTextEngineImple mLoadDataEngine = new NewsTextEngineImple(null, vo.getSymbol());
            // // System.out.println("new OpitionNewsEngineImple type:" + type + " vo:" + vo);
            // mLoadDataEngine.loadData();
            // } else {

            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, viewType, vo);
            // System.out.println("new OpitionNewsEngineImple type:" + type + " vo:" + vo);
            mLoadDataEngine.loadData();
            // }
        } else {
            setEmptyText();
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

        // mListView.setEmptyView(iv);
        // mListView.addFooterView(mFootView);
        switch (viewType) {
            case OpitionNewsEngineImple.NEWS_GROUP_FOREACH:
            case OpitionNewsEngineImple.NEWS_GROUP_TWO:
                mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.NEWSFOREACH:
                mOptionMarketAdapter = new OptionForOnelistAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.NEWS_OPITION_FOREACH:
                mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
                break;
            case OpitionNewsEngineImple.GROUP_FOR_ONE:
                mOptionMarketAdapter = new ReportNewsAdapter(context, mDataList);
                ((ReportNewsAdapter) mOptionMarketAdapter).setSecondYanBao(true);
                break;
            case OpitionNewsEngineImple.NEWS_GROUP:
                mOptionMarketAdapter = new InfoOptionAdapter(context, mDataList);
                break;
            case NEWS_SECOND_NOTICE: {
                mOptionMarketAdapter = new InfoOptionAdapter(context, mDataList);
                ((InfoOptionAdapter) mOptionMarketAdapter).setSecondNotice(true);

            }
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
                OptionNewsBean optionNewsBean = mDataList.get(position);

                switch (viewType) {
                    case OpitionNewsEngineImple.NEWSALL:
                        intent = OptionListAcitivity.newIntent(context, optionNewsBean.getSymbols().get(0).getSymbol()
                                + "", "20", optionNewsBean.getSymbols().get(0).getAbbrName());
                        UIUtils.startAminationActivity(getActivity(), intent);
                        break;
                    case OpitionNewsEngineImple.NEWS_GROUP:
                        if (null != optionNewsBean.getSymbols() && optionNewsBean.getSymbols().size() > 0) {

                            intent = ReportForOneListActivity.newIntent(context, optionNewsBean.getSymbols().get(0)
                                    .getSymbol(), optionNewsBean.getSymbols().get(0).getAbbrName(),
                                    vo.getContentSubType(), optionNewsBean.getContentType());
                            // intent = ReportForOneListActivity.newIntent(context, optionNewsBean.getSymbols().get(0)
                            // .getId(), optionNewsBean.getSymbols().get(0).getAbbrName(), vo.getContentSubType(),
                            // optionNewsBean.getContentType());
                        } else {
                            intent = ReportForOneListActivity.newIntent(context, null, null, null, null);
                        }
                        // startActivity(intent);
                        UIUtils.startAminationActivity(getActivity(), intent);
                        break;
                    case OpitionNewsEngineImple.NEWS_GROUP_FOREACH:
                        if (vo.getContentType().equals("20")) {
                            intent = OptionListAcitivity.newIntent(context, optionNewsBean.getSymbols().get(0)
                                    .getSymbol()
                                    + "", "20", optionNewsBean.getSymbols().get(0).getAbbrName());
                            UIUtils.startAminationActivity(getActivity(), intent);
                        } else {
                            if (null != optionNewsBean.getSymbols() && optionNewsBean.getSymbols().size() > 0) {
                                intent = ReportForOneListActivity.newIntent(context, optionNewsBean.getSymbols().get(0)
                                        .getSymbol(), optionNewsBean.getSymbols().get(0).getAbbrName(),
                                        vo.getContentSubType(), optionNewsBean.getContentType());
                            } else {
                                intent = ReportForOneListActivity.newIntent(context, null, null, null, null);
                            }
                            UIUtils.startAminationActivity(getActivity(), intent);
                        }

                        break;
                    default:
                        try {
                            switch (viewType) {
                                case 1:
                                    name = "公告正文";
                                    if (null != optionNewsBean.getSymbols() && optionNewsBean.getSymbols().size() > 0) {
                                        intent = NewsActivity.newIntent(context, optionNewsBean.getId(), name,
                                                optionNewsBean.getSymbols().get(0).getAbbrName(), optionNewsBean
                                                        .getSymbols().get(0).getId());
                                        UIUtils.startAminationActivity(getActivity(), intent);
                                    } else {
                                        intent = NewsActivity.newIntent(context, optionNewsBean.getId(), name, null,
                                                null);
                                        UIUtils.startAminationActivity(getActivity(), intent);
                                    }
                                    break;

                                default:
                                    name = "研报正文";
                                    if (null != optionNewsBean.getSymbols() && optionNewsBean.getSymbols().size() > 0) {
                                        intent = YanbaoDetailActivity.newIntent(context, optionNewsBean.getId(),
                                                optionNewsBean.getSymbols().get(0).getSymbol(), optionNewsBean
                                                        .getSymbols().get(0).getAbbrName(),
                                                optionNewsBean.getContentType());
                                    } else {
                                        intent = YanbaoDetailActivity.newIntent(context, optionNewsBean.getId(), null,
                                                null, null);
                                    }
                                    UIUtils.startAminationActivity(getActivity(), intent);
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
            pb.setVisibility(View.GONE);
            mSwipeLayout.setRefreshing(false);
            mListView.onLoadMoreComplete();
            try {

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
                    hideEmptyText();
                } else {
                    setEmptyText();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void loadingFail() {
            pb.setVisibility(View.GONE);
            mSwipeLayout.setRefreshing(false);
            if (null == mDataList || mDataList.isEmpty()) {
                // iv.setText("暂无资讯");
                setEmptyText();
            } else {
                hideEmptyText();
            }

        }

    };

    private void setEmptyText() {
        if (viewType == OpitionNewsEngineImple.NEWS_GROUP) {
            tvEmpty.setText("尚未添加自选股");
        } else {
            tvEmpty.setText("暂无资讯");
        }
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmptyText() {
        tvEmpty.setVisibility(View.GONE);
    }

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

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_option_market_news;
    }
}
