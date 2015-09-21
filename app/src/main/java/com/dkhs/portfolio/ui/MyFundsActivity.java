package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.LatestTopicsEngineImpl;
import com.mingle.autolist.AutoList;


/**
 * Created by zhangcm on 2015/9/21.15:58
 */
public class MyFundsActivity extends LoadMoreListActivity{

    private AutoList<TopicsBean> mDataList = new AutoList<TopicsBean>().applyAction(TopicsBean.class);
    private LatestTopicsEngineImpl mTopicsEngine= null;
    private BaseAdapter mAdapter;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.my_funds);
    }

    @Override
    SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        };
    }

    @Override
    AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };
    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadData());
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == mAdapter) {
//            mAdapter = new LatestTopicsAdapter(mActivity, mDataList);
            mAdapter = new MyFundsAdapter();
        }
        return mAdapter;
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mTopicsEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    LatestTopicsEngineImpl getLoadEngine() {

        if (null == mTopicsEngine) {
            mTopicsEngine = new LatestTopicsEngineImpl(this);
        }
        return mTopicsEngine;
    }


    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
    }
    @Override
    public String getEmptyText() {
        return "暂无持仓基金";
    }

    private class MyFundsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mContext, R.layout.item_my_fund, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            return convertView;
        }

        private class ViewHolder{
            TextView tv;
        }
    }

}
