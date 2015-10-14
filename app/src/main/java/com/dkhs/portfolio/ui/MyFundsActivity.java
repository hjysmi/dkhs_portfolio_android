package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.MyFund;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.MyFundsEngineImpl;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mingle.autolist.AutoList;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by zhangcm on 2015/9/21.15:58
 */
public class MyFundsActivity extends LoadMoreListActivity{

    private AutoList<TopicsBean> mDataList = new AutoList<TopicsBean>().applyAction(TopicsBean.class);
    private TradeEngineImpl mTradeEngine = null;
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
                //TODO 打开基金详情
                startActivity(FundInfoActivity.getFundInfoIntent(mContext, myFunds.get(position).getFund()));
            }
        };
    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
//        setHttpHandler(getLoadEngine().loadData());
        new MyFundsEngineImpl().getMyFunds(new ParseHttpListener<List<MyFund>>() {
            @Override
            protected List<MyFund> parseDateTask(String jsonData) {
                List<MyFund> myFunds = null;
                if(!TextUtils.isEmpty(jsonData)){
                    try{
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        JSONObject json = new JSONObject(jsonData);
                        if(json.has("results")){
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            myFunds = gson.fromJson(json.getString("results"), new TypeToken<List<MyFund>>(){}.getType());

                        }
                    }catch (Exception e){

                    }
                }
                return myFunds;
            }

            @Override
            protected void afterParseData(List<MyFund> funds) {
                if(funds != null && funds.size() > 0){
                    myFunds = funds;
                    mAdapter.notifyDataSetChanged();
                }
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(false);
            }
        });
    }

    private List<MyFund> myFunds;

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
        if (mTradeEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    TradeEngineImpl getLoadEngine() {

        if (null == mTradeEngine) {
            mTradeEngine = new TradeEngineImpl(this);
        }
        return mTradeEngine;
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
            return myFunds == null?0:myFunds.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            MyFund fund = null;
            if(myFunds != null){
                fund = myFunds.get(position);
            }
            return fund;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mContext, R.layout.item_my_fund, null);
                holder = new ViewHolder();
                holder.tv_fund_name = (TextView) convertView.findViewById(R.id.tv_fund_name);
                holder.tv_recent_profit = (TextView) convertView.findViewById(R.id.tv_recent_profit);
                holder.tv_percent_new = (TextView) convertView.findViewById(R.id.tv_percent_new);
                holder.tv_fund_value = (TextView) convertView.findViewById(R.id.tv_fund_value);
                holder.tv_total_profit = (TextView) convertView.findViewById(R.id.tv_total_profit);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            MyFund myFund = myFunds.get(position);
            holder.tv_fund_name.setText(myFund.getFund().getName());
            double recentProfit = Double.parseDouble(myFund.getIncome_latest());
            if(recentProfit < 0){
                holder.tv_recent_profit.setTextColor(getResources().getColor(R.color.green));
            }else{
                holder.tv_recent_profit.setTextColor(getResources().getColor(R.color.red));
            }
            holder.tv_recent_profit.setText(myFund.getIncome_latest());
            holder.tv_percent_new.setText(myFund.getFund().getPercent_latest()+"%");
            holder.tv_fund_value.setText(myFund.getWorth_value());
            holder.tv_total_profit.setText(myFund.getIncome_total());
            return convertView;
        }

        private class ViewHolder{
            TextView tv_fund_name;
            TextView tv_recent_profit;
            TextView tv_percent_new;
            TextView tv_fund_value;
            TextView tv_total_profit;
        }
    }

}
