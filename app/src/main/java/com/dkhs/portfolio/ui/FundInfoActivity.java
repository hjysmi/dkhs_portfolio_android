package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.FundShare;
import com.dkhs.portfolio.bean.MyFundInfo;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.TradeRecord;
import com.dkhs.portfolio.engine.MyFundsEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.parceler.Parcels;

import java.text.DecimalFormat;

/**
 * Created by zhangcm on 2015/9/22.10:56
 */
public class FundInfoActivity extends ModelAcitivity {

    private SwipeRefreshLayout mSwipeLayout;

    private PullToRefreshListView mListView;

    private MyFundInfoAdapter adapter;
    @ViewInject(R.id.bottom)
    private View bottom;
    @ViewInject(R.id.ll_buy_more)
    private View ll_buy_more;
    @ViewInject(R.id.ll_sell_out)
    private View ll_sell_out;

    private static String FUND = "fund";
    private FundQuoteBean myFund;

    public static Intent getFundInfoIntent(Context context, FundQuoteBean fund) {
        Intent intent = new Intent(context, FundInfoActivity.class);
        intent.putExtra(FUND, Parcels.wrap(fund));
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_fund_info);
        ViewUtils.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        setTitle(R.string.my_funds_detail);
        initLoadMoreList();
    }

    private void handleExtras(Bundle extras) {
        myFund = Parcels.unwrap(extras.getParcelable(FUND));
    }

    private void initLoadMoreList() {
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//        mSwipeLayout.setOnRefreshListener(setOnRefreshListener());
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        adapter = new MyFundInfoAdapter();
        mListView.setAdapter(adapter);
        mListView.setCanRefresh(false);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);

    }
//    GET /api/v1/funds/fund/fund_info/


    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        ParseHttpListener listenr = new ParseHttpListener<MyFundInfo>() {
            @Override
            protected MyFundInfo parseDateTask(String jsonData) {
                MyFundInfo fundInfo = null;
                if (!TextUtils.isEmpty(jsonData)) {
                    try {
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        fundInfo = DataParse.parseObjectJson(MyFundInfo.class, jsonData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return fundInfo;
            }

            @Override
            protected void afterParseData(MyFundInfo fundInfo) {
                if (fundInfo != null) {
                    mFundInfo = fundInfo;
                    adapter.notifyDataSetChanged();
                    bottom.setVisibility(View.VISIBLE);
                    FundQuoteBean fund = mFundInfo.getFund();
//                    if()R.color.person_setting_line
                    if (fund.isAllow_buy()) {
                        ll_buy_more.setBackgroundResource(R.drawable.bg_blue_gray_selector);
                    } else {
                        ll_buy_more.setBackgroundColor(getResources().getColor(R.color.person_setting_line));
                    }
                    if (fund.isAllow_sell()) {
                        ll_sell_out.setBackgroundResource(R.drawable.bg_white_gray_selector);
                    } else {
                        ll_sell_out.setBackgroundColor(getResources().getColor(R.color.person_setting_line));
                    }
                }
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(false);
            }
        };
        new MyFundsEngineImpl().getMyFundInfo(String.valueOf(myFund.getId()), listenr);

    }

    @OnClick({R.id.ll_buy_more, R.id.ll_sell_out})
    private void onClick(View view) {
        if (view.getId() == R.id.ll_buy_more && mFundInfo.getFund().isAllow_buy()) {
            startActivity(BuyFundActivity.buyIntent(mContext, mFundInfo.getFund()));
        } else if (view.getId() == R.id.ll_sell_out && mFundInfo.getFund().isAllow_buy()) {
            startActivity(SellFundActivity.sellIntent(mContext, mFundInfo));

        }
    }

    private MyFundInfo mFundInfo = new MyFundInfo();

    private class MyFundInfoAdapter extends BaseAdapter {

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
            if (position == 0) {
                return TYPE_HEAD;
            } else if (position > 0 && position < mFundInfo.getShares_list().size() + 1) {
                return TYPE_POSITION;
            } else if (position == mFundInfo.getShares_list().size() + 1) {
                return TYPE_TRADE_TITLE;
            } else if (position > mFundInfo.getShares_list().size() + 1 && position < mFundInfo.getShares_list().size() + mFundInfo.getTrade_record_list().size() + 2) {
                return TYPE_TRADE_RECORD;
            } else {
                return TYPE_BOTTOM;
            }

        }

        @Override
        public int getCount() {
            if (mFundInfo.getShares_list() == null && mFundInfo.getTrade_record_list() == null)
                return 0;
            return 3 + mFundInfo.getShares_list().size() + mFundInfo.getTrade_record_list().size();
        }

        @Override
        public long getItemId(int position) {
            return position;
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
            if (convertView == null) {
                if (type == TYPE_HEAD) {
                    convertView = View.inflate(mContext, R.layout.item_fund_info1, null);
                    holder1 = new ViewHolder1();
                    holder1.rl_fund_info = convertView.findViewById(R.id.rl_fund_info);
                    holder1.tv_info_title = (TextView) convertView.findViewById(R.id.tv_info_title);
                    holder1.tv_total_profit = (TextView) convertView.findViewById(R.id.tv_total_profit);
                    holder1.tv_recent_profit = (TextView) convertView.findViewById(R.id.tv_recent_profit);
                    holder1.tv_net_new = (TextView) convertView.findViewById(R.id.tv_net_new);
                    holder1.tv_fund_value = (TextView) convertView.findViewById(R.id.tv_fund_value);
                    holder1.tv_recent_profit_time = (TextView) convertView.findViewById(R.id.tv_recent_profit_time);
                    convertView.setTag(holder1);
                } else if (type == TYPE_POSITION) {
                    convertView = View.inflate(mContext, R.layout.item_fund_info2, null);
                    holder2 = new ViewHolder2();
                    holder2.tv_bank = (TextView) convertView.findViewById(R.id.tv_bank);
                    holder2.tv_bank_no = (TextView) convertView.findViewById(R.id.tv_bank_no);
                    holder2.tv_fund_no = (TextView) convertView.findViewById(R.id.tv_fund_no);
                    convertView.setTag(holder2);
                } else if (type == TYPE_TRADE_TITLE) {
                    convertView = View.inflate(mContext, R.layout.item_fund_info3, null);
                } else if (type == TYPE_TRADE_RECORD) {
                    convertView = View.inflate(mContext, R.layout.item_fund_info4, null);
                    holder4 = new ViewHolder4();
                    holder4.tv_trade_time = (TextView) convertView.findViewById(R.id.tv_trade_time);
                    holder4.tv_bank_no = (TextView) convertView.findViewById(R.id.tv_bank_no);
                    holder4.tv_trade_type = (TextView) convertView.findViewById(R.id.tv_trade_type);
                    holder4.tv_trade_value = (TextView) convertView.findViewById(R.id.tv_trade_value);
                    convertView.setTag(holder4);
                } else {
                    convertView = View.inflate(mContext, R.layout.item_fund_info5, null);
                }
            }
            if (type == TYPE_HEAD) {
                holder1 = (ViewHolder1) convertView.getTag();
                if (myFund != null) {
                    holder1.tv_info_title.setText(String.format(getResources().getString(R.string.blank_fund_name),myFund.getAbbrName(),myFund.getSymbol()));
                    holder1.tv_total_profit.setText(mFundInfo.getIncome_total());
                    holder1.tv_recent_profit.setText(mFundInfo.getIncome_latest());
                    holder1.tv_net_new.setText(StringFromatUtils.get4Point(myFund.getNet_value()));
                    holder1.tv_fund_value.setText(mFundInfo.getWorth_value());
                    holder1.tv_recent_profit_time.setText(String.format(getResources().getString(R.string.blank_recent_profit_time), TimeUtils.getMMDDString(myFund.getTradedate())));
                }
                holder1.rl_fund_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 打开详情
                        startActivity(FundDetailActivity.newIntent(mContext, SelectStockBean.copy(myFund)));
                    }
                });
            } else if (type == TYPE_POSITION) {
                holder2 = (ViewHolder2) convertView.getTag();
                FundShare s = mFundInfo.getShares_list().get(position - 1);
                if (s != null) {
                    holder2.tv_bank.setText(String.format(getResources().getString(R.string.blank_bank), s.getBank().getName(), s.getBank().getBank_card_no_tail()));
//                    holder2.tv_bank_no.setText(String.format(getResources().getString(R.string.blank_bank_card),s.getBank().getBank_card_no_tail()));
                    holder2.tv_fund_no.setText(String.format(getResources().getString(R.string.blank_shares), s.getShares_current()));
                }
            } else if (type == TYPE_TRADE_RECORD) {
                holder4 = (ViewHolder4) convertView.getTag();
                TradeRecord record = mFundInfo.getTrade_record_list().get(position - mFundInfo.getShares_list().size() - 2);
                if (record != null) {

                    holder4.tv_trade_time.setText(TimeUtils.getDaySecondString(record.getTime()));
                    holder4.tv_bank_no.setText(record.getBank().getName() + " 尾号" + record.getBank().getBank_card_no_tail());
                    if (record.getDirection() == 0) {
                        //买入
                        holder4.tv_trade_type.setText("买入");
                        holder4.tv_trade_value.setText(String.format(getResources().getString(R.string.blank_dollar), record.getAmount()));

                    } else {
                        holder4.tv_trade_value.setText(String.format(getResources().getString(R.string.blank_shares), record.getShares()));
                        holder4.tv_trade_type.setText("卖出");
                    }

                }
            }
            return convertView;
        }

        private class ViewHolder1 {
            View rl_fund_info;
            TextView tv_info_title;
            TextView tv_recent_profit;
            TextView tv_total_profit;
            TextView tv_net_new;
            TextView tv_fund_value;
            TextView tv_recent_profit_time;
        }

        private class ViewHolder2 {
            TextView tv_bank;
            TextView tv_bank_no;
            TextView tv_fund_no;
        }

        private class ViewHolder4 {
            TextView tv_trade_time;
            TextView tv_bank_no;
            TextView tv_trade_type;
            TextView tv_trade_value;
        }

    }
}
