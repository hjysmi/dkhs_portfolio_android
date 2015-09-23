package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zhangcm on 2015/9/22.10:56
 */
public class FundInfoActivity extends ModelAcitivity{

    private SwipeRefreshLayout mSwipeLayout;

    private PullToRefreshListView mListView;

    private MyFundInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_fund_info);
        ViewUtils.inject(this);
        setTitle(R.string.my_funds);
        initLoadMoreList();
    }

    private void initLoadMoreList() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        mSwipeLayout.setOnRefreshListener(setOnRefreshListener());
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        adapter = new MyFundInfoAdapter();
        mListView.setAdapter(adapter);

    }

    @OnClick({R.id.ll_buy_more,R.id.ll_sell_out})
    private void onClick(View view){
        if(view.getId() == R.id.ll_buy_more){
            startActivity(new Intent(mContext, BuyFundActivity.class));
        }else if(view.getId() == R.id.ll_sell_out){
            startActivity(new Intent(mContext, SellFundActivity.class));
        }
    }

    private class MyFundInfoAdapter extends BaseAdapter{

        private int TYPE_HEAD = 0;
        private int TYPE_POSITION = 1;
        private int TYPE_TRADE_TITLE = 2;
        private int TYPE_TRADE_RECORD = 3;
        private int TYPE_BOTTOM = 4;

        @Override
        public int getViewTypeCount() {
            return 5;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return  TYPE_HEAD;
            }else if( position >0 && position <3){
                return  TYPE_POSITION;
            }else if(position == 3){
                return  TYPE_TRADE_TITLE;
            }else if(position > 3 && position < 19){
                return  TYPE_TRADE_RECORD;
            }else{
                return  TYPE_BOTTOM;
            }

        }

        @Override
        public int getCount() {
            return 20;
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
            int type = getItemViewType(position);
            ViewHolder1 holder1;
            ViewHolder2 holder2;
            ViewHolder4 holder4;
            if(convertView == null){
                if(type == TYPE_HEAD){
                    convertView = View.inflate(mContext, R.layout.item_fund_info1, null);
                    holder1 = new ViewHolder1();
                    holder1.rl_fund_info = convertView.findViewById(R.id.rl_fund_info);
                    holder1.tv_info_title = (TextView) convertView.findViewById(R.id.tv_info_title);
                    holder1.tv_total_profit = (TextView) convertView.findViewById(R.id.tv_total_profit);
                    holder1.tv_recent_profit = (TextView) convertView.findViewById(R.id.tv_recent_profit);
                    holder1.tv_net_new = (TextView) convertView.findViewById(R.id.tv_net_new);
                    holder1.tv_fund_value = (TextView) convertView.findViewById(R.id.tv_fund_value);
                    convertView.setTag(holder1);
                }else if(type == TYPE_POSITION){
                    convertView = View.inflate(mContext, R.layout.item_fund_info2, null);
                    holder2 = new ViewHolder2();
                    holder2.tv_bank = (TextView) convertView.findViewById(R.id.tv_bank);
                    holder2.tv_bank_no = (TextView) convertView.findViewById(R.id.tv_bank_no);
                    holder2.tv_fund_no = (TextView) convertView.findViewById(R.id.tv_fund_no);
                    convertView.setTag(holder2);
                }else if(type == TYPE_TRADE_TITLE){
                    convertView = View.inflate(mContext, R.layout.item_fund_info3, null);
                }else if(type == TYPE_TRADE_RECORD){
                    convertView = View.inflate(mContext, R.layout.item_fund_info4, null);
                    holder4 = new ViewHolder4();
                    holder4.tv_trade_time = (TextView) convertView.findViewById(R.id.tv_trade_time);
                    holder4.tv_bank_no = (TextView) convertView.findViewById(R.id.tv_bank_no);
                    holder4.tv_trade_type = (TextView) convertView.findViewById(R.id.tv_trade_type);
                    holder4.tv_trade_value = (TextView) convertView.findViewById(R.id.tv_trade_value);
                    convertView.setTag(holder4);
                }else{
                    convertView = View.inflate(mContext, R.layout.item_fund_info5, null);
                }
            }else{
                if(type == TYPE_HEAD){
                    holder1 = (ViewHolder1)convertView.getTag();
                }else if(type == TYPE_POSITION){
                    holder2 = (ViewHolder2)convertView.getTag();
                }else if(type == TYPE_TRADE_RECORD){
                    holder4 = (ViewHolder4)convertView.getTag();
                }
            }
            if(type == TYPE_HEAD){
                holder1 = (ViewHolder1)convertView.getTag();
                holder1.rl_fund_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 打开详情
                    }
                });
            }else if(type == TYPE_POSITION){
                holder2 = (ViewHolder2)convertView.getTag();
            }else if(type == TYPE_TRADE_RECORD){
                holder4 = (ViewHolder4)convertView.getTag();
                if(position > 10 && position < 15){
                    holder4.tv_trade_time.setText("2015-09-30 18:00:00");
                    holder4.tv_bank_no.setText("兴业银行 尾号9999");
                    holder4.tv_trade_value.setText("500份");
                    holder4.tv_trade_type.setText("卖出");

                }
            }
            return convertView;
        }
        private class ViewHolder1{
            View rl_fund_info;
            TextView tv_info_title;
            TextView tv_recent_profit;
            TextView tv_total_profit;
            TextView tv_net_new;
            TextView tv_fund_value;
        }
        private class ViewHolder2{
            TextView tv_bank;
            TextView tv_bank_no;
            TextView tv_fund_no;
        }
        private class ViewHolder4{
            TextView tv_trade_time;
            TextView tv_bank_no;
            TextView tv_trade_type;
            TextView tv_trade_value;
        }

    }
}
