package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class MyBankCardsActivity extends ModelAcitivity {

    private int RED = Color.parseColor("#ff5555");
    private int ORANGE = Color.parseColor("#fca321");
    private int BLUE = Color.parseColor("#0c70c1");
    private int GREEN = Color.parseColor("#009688");
    private int GOLDEN = Color.parseColor("#fca321");
    private SwipeRefreshLayout mSwipeLayout;

    private PullToRefreshListView mListView;

    private TextView tvEmptyText;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_bank_cards);
        ViewUtils.inject(this);
        setTitle(R.string.bank_card_package);
        TextView right = getRightButton();
        right.setText(R.string.add_text);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 添加银行卡
                startActivity(new Intent(mContext, BankCardInfoActivity.class));
            }
        });
        initLoadMoreList();
    }

    private void initLoadMoreList() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        mSwipeLayout.setOnRefreshListener(setOnRefreshListener());
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        mListView.setDivider(null);
        mListView.setAdapter(new MyBankcardPackageAdapter());
        tvEmptyText = (TextView) findViewById(android.R.id.empty);
        mProgressView = (RelativeLayout) findViewById(android.R.id.progress);

//        mListView.setAdapter(getListAdapter());
//        mListView.setOnItemClickListener(getItemClickListener());
//        setListViewInit(mListView);

    }

    private class MyBankcardPackageAdapter extends BaseAdapter{

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getCount() {
            return 1;
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
                convertView = View.inflate(getApplicationContext(), R.layout.item_my_bank_cards, null);
                holder = new ViewHolder();
                holder.llBank = (View)convertView.findViewById(R.id.ll_bank);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            GradientDrawable myGrad = (GradientDrawable)holder.llBank.getBackground();
            myGrad.setColor(getResources().getColor(R.color.orange));
            return convertView;
        }

        private class ViewHolder{
            View llBank;
            ImageView ivBanklogo;

        }
    }

}
