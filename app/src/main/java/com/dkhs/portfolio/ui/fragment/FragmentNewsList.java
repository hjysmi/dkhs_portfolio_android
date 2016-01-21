package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.base.widget.LinearLayout;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.NewsActivity;
import com.dkhs.portfolio.ui.StockNewsActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.widget.CircularProgress;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 需要优化界面
 * 个股行情界面，个股界面时（公告 TAB）
 */
public class FragmentNewsList extends Fragment implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6565512311564641L;

    //private ListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    // private OptionMarketAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";
    private DisplayMetrics dm;
    private static final String TAG = "FragmentNewsList";
    private NewsforModel vo;
    private TextView tv;
    private boolean getadle = true;
    //   private OptionForOnelistAdapter mOptionlistAdapter;
    //  private RelativeLayout pb;
    // public SwipeRefreshLayout mSwipeLayout;
    private LinearLayout mContentView;
    private LinearLayout ll_loading;
    private View view_empty;
    private CircularProgress loadView;

    public static FragmentNewsList newIntent(String stockCode,String stockName) {
        FragmentNewsList noticeFragemnt = new FragmentNewsList();
        NewsforModel vo;
        Bundle b2 = new Bundle();
        b2.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
        vo = new NewsforModel();
        vo.setSymbol(stockCode);
        vo.setSymboName(stockName);
        vo.setContentType("20");
        vo.setPageTitle("公告正文");
        b2.putParcelable(FragmentNewsList.VO, Parcels.wrap(vo));
        noticeFragemnt.setArguments(b2);
        return noticeFragemnt;
    }

    // private LinearLayout layouts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        BusProvider.getInstance().register(this);
        View view = inflater.inflate(R.layout.activity_option_market_newslist, null);
        loadView = (CircularProgress) view.findViewById(R.id.loadView);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        context = getActivity();
        dm = UIUtils.getDisplayMetrics();
        initView(view);
        if (!isViewShown) {
            initDate();
        }
        return view;
    }

    private void initDate() {

        Bundle bundle = getArguments();

        if (null != bundle) {
            vo = Parcels.unwrap(bundle.getParcelable(VO));
            // layouts = vo.getLayout();
            int types = bundle.getInt(NEWS_TYPE);
            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, types, vo);
            // mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadData();
        }

    }

    private void initView(View view) {
        mContentView = (LinearLayout) view.findViewById(R.id.ll_content);
        view_empty = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, null);
        mFootView = View.inflate(context, R.layout.layout_more_footer, null);
        tv = (TextView) view_empty.findViewById(R.id.tv_empty);
        mDataList = new ArrayList<>();

        mFootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity(getActivity(), StockNewsActivity.newIntent(getActivity(), vo.getSymboName(), vo.getSymbol(), "20","公告"));
            }
        });
    }

    public void loadMore() {
        if (null != mLoadDataEngine && !isLoadingMore && getadle) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            addFooterView(mFootView);

            isLoadingMore = true;
            // mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadMore();
        }
    }

    private void addFooterView(View footView) {
        mContentView.addView(footView);
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {


        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            ll_loading.setVisibility(View.GONE);
            try {
                if (null != dataList && dataList.size() > 0) {
                    if (!isLoadingMore) {
                        mDataList.clear();
                    }
                    mDataList.addAll(dataList);
                    if (first || vo.getContentType().equals("20")) {
                        // initView(view);
                        first = false;
                    }
                    loadFinishUpdateView();
                } else {
                    if (null != vo && null != vo.getPageTitle()) {
                        tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                        mContentView.addView(view_empty);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isLoadingMore = false;
        }

        @Override
        public void loadingFail() {
            ll_loading.setVisibility(View.GONE);
            if (null == mDataList || mDataList.isEmpty()) {
                if (null != vo && null != vo.getPageTitle()) {
                    tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                    mContentView.addView(view_empty);
                }
            }

        }

    };

    private void loadFinishUpdateView() {
        Log.e(TAG, "loadFinishUpdateView");
        mContentView.removeAllViews();
        for (final OptionNewsBean bean : mDataList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_opition_list, null);
            TextView tvTextName = (TextView) view.findViewById(R.id.adapter_market_title);
            TextView tvTextNameNum = (TextView) view.findViewById(R.id.adapter_market_title_num);
            TextView tvTextDate = (TextView) view.findViewById(R.id.option_news_text_date);
            TextView zhengquan = (TextView) view.findViewById(R.id.zhengquan);
            Paint p = new Paint();
            Rect rect = new Rect();
            p.setTextSize(getActivity().getResources().getDimensionPixelOffset(R.dimen.list_text_size));
            p.getTextBounds(bean.getTitle(), 0, bean.getTitle().length(), rect);
            if (dm.widthPixels * 3 / 2 - 50 < rect.width()) {
                int le = (int) (bean.getTitle().length() - bean.getTitle().length() * (rect.width() - dm.widthPixels * 3 / 2 + 50) / rect.width() - 3);
                String text = bean.getTitle().substring(0, le);
                tvTextName.setText(text + "...");
            } else {
                tvTextName.setText(bean.getTitle());
            }
            //ViewTreeObserver observer = tv.getViewTreeObserver();
            tvTextNameNum.setText(bean.getSymbols().get(0).getAbbrName());
            if (null != bean.getSource()) {
                zhengquan.setText(bean.getSource().getTitle());
            }
            tvTextDate.setText(TimeUtils.getBriefTimeString(bean.getPublish()));

//            if (TimeUtils.isSameDay(bean.getPublish())) {
//                tvTextDate.setText(TimeUtils.getTimeString(bean.getPublish()));
//            } else {
//                tvTextDate.setText(TimeUtils.getMMDDString(bean.getPublish()));
//            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (null != bean.getSymbols() && bean.getSymbols().size() > 0) {
                            Intent intent = NewsActivity.newIntent(context, bean.getId(), vo.getPageTitle(),
                                    bean.getSymbols().get(0).getAbbrName(), bean
                                            .getSymbols().get(0).getId());
//                    startActivity(intent);
                            TopicsDetailActivity.startActivity(getActivity(), bean.getId());
                        } else {
                            Intent intent = NewsActivity.newIntent(context, bean.getId(), vo.getPageTitle(),
                                    null, null);
                            TopicsDetailActivity.startActivity(getActivity(), bean.getId());
//                    startActivity(intent);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            mContentView.addView(view);
        }
        if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
            // Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
            return;
        }
        addFooterView(mFootView);
      //  BusProvider.getInstance().post(new StockQuotesStopTopBean());
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_news);
    private boolean isViewShown;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            getadle = true;
            if (getView() != null) {
                isViewShown = true;

                initDate();
            } else {
                isViewShown = false;
            }
        } else {
            // 不可见时不执行操作
            getadle = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

   /* @Subscribe
    public void getLoadMore(StockNewListLoadListBean bean) {
        loadMore();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}
