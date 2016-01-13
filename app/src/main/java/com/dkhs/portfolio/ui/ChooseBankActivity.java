package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.Bank;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.utils.ActivityCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 
 * @author zhangcm
 * @date 2015年5月12日 下午4:28:06
 * @version 1.0
 */
public class ChooseBankActivity extends LoadMoreListActivity{

    private List<Bank> banks = new ArrayList<Bank>();
    private TradeEngineImpl tradeEngine;
    private BaseAdapter mAdapter;
    
    private BitmapUtils bitmapUtils;


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.choose_bank);
        bitmapUtils = new BitmapUtils(this);
        mListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
        mListView.setCanRefresh(false);
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
                if(banks != null && banks.size() > 0){
                    Bank bank = banks.get(position + 1);
                    Intent data = new Intent();
                    data.putExtra(BankCardInfoActivity.BANK, bank);
                    setResult(ActivityCode.CHOOSE_BANK_RESULT.ordinal(), data);
                    finish();
                }
            }
        };
    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().getBanks(new ParseHttpListener<List<Bank>>() {
            @Override
            protected List<Bank> parseDateTask(String jsonData) {
                List<Bank> banks = null;
                if (!TextUtils.isEmpty(jsonData)) {
                    try {
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        banks = gson.fromJson(jsonData, new TypeToken<List<Bank>>(){}.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return banks;
            }

            @Override
            protected void afterParseData(List<Bank> banks) {
                if(banks != null && banks.size() > 0){
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setEnabled(false);
                    ChooseBankActivity.this.banks = banks;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
//        setHttpHandler(getLoadEngine().loadData());
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == mAdapter) {
            mAdapter = new MyBankAdapter();
        }
        return mAdapter;
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (tradeEngine.getCurrentpage() == 1) {
            banks.clear();
        }
        banks.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    TradeEngineImpl getLoadEngine() {

        if (null == tradeEngine) {
            tradeEngine = new TradeEngineImpl(this);
//            tradeEngine.getBanks();
        }
        return tradeEngine;
    }


    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
    }
    @Override
    public String getEmptyText() {
        return "暂无银行列表";
    }



    private class MyBankAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(banks != null)
                return banks.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(banks != null && banks.size() > 0)
                return banks.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(banks != null && banks.size() >0){
                ViewHolder holder;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(), R.layout.layout_bank_item, null);
                    holder = new ViewHolder();
                    holder.ivBank = (ImageView) convertView.findViewById(R.id.iv_bank);
                    holder.tvBank = (TextView) convertView.findViewById(R.id.tv_bank);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                Bank bank = banks.get(position);
                bitmapUtils.display(holder.ivBank, bank.getLogo(),null,null);
                holder.tvBank.setText(bank.getName());
                return convertView;
            }
            return null;
        }
        private class ViewHolder{
            ImageView ivBank;
            TextView tvBank;
        }
        
    }

}
