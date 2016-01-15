package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryProfitNetValue;
import com.dkhs.portfolio.bean.HistoryProfitNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.http.HttpHandler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class EveryDayValueActivity extends ModelAcitivity implements OnLoadMoreListener {
    public static final String EXTRA_COMBINATION = "extra_combination";
    private CombinationBean mCombinationBean;
    private PullToRefreshListView mListView;
    private NetValueEngine netValueEngine;

    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);
        setContentView(R.layout.activity_everyday_record);
        setTitle(R.string.privacy_everyday_record);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
        initData();
    }

    private int count = 30;
    private int page = 1;
    private int total_count = 0;
    private int total_page = 0;

    private List<HistoryNetBean> lists = new ArrayList<HistoryNetBean>();
    private MyAdapter adapter;
    private MyIhttpListener listener;
    private HttpHandler mHttphandler;

    private void initData() {
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        netValueEngine = new NetValueEngine(mCombinationBean.getId());
        listener = new MyIhttpListener();
        // add by zcm --- 2014.12.17
        if (NetUtil.checkNetWork()) {
            listener.setLoadingDialog(this);
            // PromptManager.showProgressDialog(this, "", false);
            mHttphandler = netValueEngine.requeryHistory(count, page, listener);
        } else {
            PromptManager.showToast(R.string.no_net_connect);
        }
        // add by zcm --- 2014.12.17
    }

    private class MyIhttpListener extends ParseHttpListener<HistoryProfitNetValue> {

        @Override
        protected HistoryProfitNetValue parseDateTask(String jsonData) {
            HistoryProfitNetValue historyValue = DataParse.parseObjectJson(HistoryProfitNetValue.class, jsonData);
            total_count = historyValue.getTotal_count();
            total_page = historyValue.getTotal_page();
            return historyValue;
        }

        @Override
        protected void afterParseData(HistoryProfitNetValue object) {
            // TODO Auto-generated method stub
            if (object != null) {
                if (object.getResults() == null || object.getResults().size() == 0) {
                    mListView.setVisibility(View.GONE);
                    // PromptManager.closeProgressDialog();
                } else {
                    if (page <= total_page) {
                        lists.addAll(object.getResults());
                        adapter.notifyDataSetChanged();
                        page++;
                        // add by zcm --- 2014.12.18
                        if (page <= total_page) {
                            mListView.setCanLoadMore(true);
                            mListView.setAutoLoadMore(true);
                            if (page == 2)
                                mListView.setOnLoadListener(EveryDayValueActivity.this);
                        } else {
                            mListView.setCanLoadMore(false);
                            mListView.setAutoLoadMore(false);
                        }
                        // add by zcm --- 2014.12.18
                        // PromptManager.closeProgressDialog();
                        mListView.onLoadMoreComplete();
                    } else {
                        mListView.setCanLoadMore(false);
                    }
                }
            } else {
                mListView.setVisibility(View.GONE);
                // PromptManager.closeProgressDialog();
            }
        }

    }

    private void initView() {
        mListView = (PullToRefreshListView) findViewById(R.id.mListView);
    }

    private void handleExtras(Bundle extras) {
//        mCombinationBean = (CombinationBean) extras.getParcelable(EXTRA_COMBINATION);
        mCombinationBean = Parcels.unwrap(extras.getParcelable(EXTRA_COMBINATION));


    }

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, EveryDayValueActivity.class);

        intent.putExtra(EXTRA_COMBINATION, Parcels.wrap(combinationBean));

        return intent;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                synchronized (EveryDayValueActivity.this) {
                    convertView = View.inflate(getApplicationContext(), R.layout.item_history_profit, null);
                    holder = new ViewHolder();
                    holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                    holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
                    holder.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);
                    convertView.setTag(holder);
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HistoryNetBean historyNetBean = lists.get(position);
            holder.tv_date.setText(historyNetBean.getDate());
            holder.tv_value.setText(historyNetBean.getNetvalue() + "");
            float percent = historyNetBean.getPercentage();
            if (percent >= 0) {
                holder.tv_percent.setTextColor(getResources().getColor(R.color.red));
            } else {
                holder.tv_percent.setTextColor(getResources().getColor(R.color.green));
            }
            holder.tv_percent.setText(percent + "%");
            return convertView;
        }

        class ViewHolder {
            TextView tv_date;
            TextView tv_value;
            TextView tv_percent;
        }

    }

    @Override
    public void onLoadMore() {
        if (page > total_page) {
            // modify by zcm --- 2012.12.16
            mListView.setAutoLoadMore(false);
            // mListView.setLodaMoreText("当前没有更多了...");
            PromptManager.showToast("当前没有更多了...");
            // modify by zcm --- 2012.12.16
        } else {
            netValueEngine.requeryHistory(count, page, listener);
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_history_value);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (null != mHttphandler) {
            mHttphandler.cancel();
        }
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_everyday_value;
    }
}
