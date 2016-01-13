package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.LinearLayout;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.widget.CircularProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要优化界面
 * 个股行情界面，个股界面时（研报 TAB）
 */

public class FragmentForOptionOnr extends Fragment {
    //private ListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    // private OptionlistAdapter mOptionMarketAdapter;
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
    private boolean getadble = false;
    //private RelativeLayout pb;
    private LinearLayout mContentView;
    private DisplayMetrics dm;
    private View view_empty;
    private TextView tv;
    private CircularProgress loadView;
    public static Fragment newIntent(Context context, String symbolName, String name, String subType) {
        Fragment f = new FragmentForOptionOnr();
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(NAME, name);
        b.putString(SUB, subType);
        f.setArguments(b);
        return f;
    }

    private void initDate() {
        try {
            Bundle extras = getArguments();
            if (null != extras) {
                symbol = extras.getString(SYMBOL);
                name = extras.getString(NAME);
                subType = extras.getString(SUB);
            }
            NewsforModel vo = new NewsforModel();
            vo.setSymbol(symbol);
            vo.setContentSubType(subType);
            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
                    OpitionNewsEngineImple.NEWS_OPITION_FOREACH, vo);
            ((OpitionNewsEngineImple) mLoadDataEngine).loadDatas();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    LinearLayout ll_content;
    LinearLayout ll_loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        BusProvider.getInstance().register(this);
        View view = inflater.inflate(R.layout.activity_option_market_newslist, null);
        ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        loadView = (CircularProgress) view.findViewById(R.id.loadView);
        dm = UIUtils.getDisplayMetrics();
        context = getActivity();
        mDataList = new ArrayList<>();

        iv = (TextView) view.findViewById(android.R.id.empty);
        // iv.setText("暂无公告");
        Bundle extras = getArguments();
        if (null != extras) {
            symbol = extras.getString(SYMBOL);
            name = extras.getString(NAME);
            subType = extras.getString(SUB);
        }
        if (null != view.findViewById(R.id.tv_title)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText("研报-" + name);
        }
        initView(view);
        initDate();
        return view;
    }

    private void initView(View view) {
        mContentView = (LinearLayout) view.findViewById(R.id.ll_content);
        view_empty = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, null);
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        tv = (TextView) view_empty.findViewById(R.id.tv_empty);
    }


    public void loadMore() {
        if (null != mLoadDataEngine && !isLoadingMore && getadble) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            addFooterView(mFootView);
            isLoadingMore = true;
            // mLoadDataEngine.setLoadingDialog(mContext);;
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
                    mDataList.addAll(dataList);
                    if (first) {
                        first = false;
                    }
                    loadFinishUpdateView();
                } else {
                    tv.setText("暂无研报");
                    mContentView.addView(view_empty);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void loadingFail() {
            ll_loading.setVisibility(View.GONE);
            //    pb.setVisibility(View.GONE);
            if (null == mDataList || mDataList.isEmpty()) {
                //   iv.setText("暂无研报");
                tv.setText("暂无研报");
                mContentView.addView(view_empty);
            }

        }

    };

    private void loadFinishUpdateView() {
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
            tvTextNameNum.setText(bean.getSymbols().get(0).getAbbrName());
            if (null != bean.getSource()) {
                zhengquan.setText(bean.getSource().getTitle());
            }

            if (TimeUtils.isSameDay(bean.getPublish())) {
                tvTextDate.setText(TimeUtils.getTimeString(bean.getPublish()));
            } else {
                tvTextDate.setText(TimeUtils.getMMDDString(bean.getPublish()));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent;
                        if (null != bean.getSymbols() && bean.getSymbols().size() > 0) {
//                    intent = YanbaoDetailActivity.newIntent(mContext, mDataList.get(position).getId(),
//                            mDataList.get(position).getSymbols().get(0).getSymbol(), mDataList.get(position)
//                                    .getSymbols().get(0).getAbbrName(), mDataList.get(position).getContentType());

                            TopicsDetailActivity.startActivity(getActivity(), bean.getId());
                        } else {
//                    intent = YanbaoDetailActivity.newIntent(mContext, mDataList.get(position).getId(), null, null, null);
                            TopicsDetailActivity.startActivity(getActivity(), bean.getId());
                        }
                        // startActivity(intent);
//                UIUtils.startAnimationActivity(getActivity(), intent);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        getadble = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

  /*  @Subscribe
    public void getLoadMore(StockNewListLoadListBean bean) {
        loadMore();
    }*/

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}
