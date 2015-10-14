package com.dkhs.portfolio.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ListView;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/9/24.17:24
 */
public class ForgetTradePasswordActivity extends ModelAcitivity {

    @ViewInject(R.id.lv_bank_card)
    private ListView lvBankCard;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_forget_trade_password);
        ViewUtils.inject(this);
        setTitle(R.string.forget_trade_pwd);
        initViews();
        initData();
    }

    private void initData() {
        ParseHttpListener listenr = new ParseHttpListener<List<MyBankCard>>() {
            @Override
            protected List<MyBankCard> parseDateTask(String jsonData) {
                List<MyBankCard> myCards = null;
                if (!TextUtils.isEmpty(jsonData)) {
                    try {
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        myCards = gson.fromJson(jsonData, new TypeToken<List<MyBankCard>>(){}.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return myCards;
            }

            @Override
            protected void afterParseData(List<MyBankCard> cards) {
                if(cards != null && cards.size() > 0){
                    myCards = cards;
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        new TradeEngineImpl().getMyBankCards(listenr.setLoadingDialog(mContext));
    }

    private void initViews() {
        mAdapter = new MyBankCardAdapter();
        lvBankCard.setAdapter(mAdapter);
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(BankCardInfoActivity.forgetTradePasswordIntent(mContext, myCards.get(position)));
            }
        });
    }
    private List<MyBankCard> myCards = new ArrayList<MyBankCard>();
    private MyBankCardAdapter mAdapter;

    private class MyBankCardAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return myCards.size();
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
            if(convertView == null){
                TextView tv = new TextView(mContext);
                Resources res = getResources();
                tv.setBackgroundResource(R.drawable.btn_white_selector);
                tv.setTextColor(res.getColor(R.color.black));
                tv.setTextSize(res.getDimension(R.dimen.widget_text_8sp));
                tv.setText(String.format(getResources().getString(R.string.blank_card_info), "兴业银行", "储蓄卡", 1248));
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT);
                try{
                    tv.setPadding(res.getDimensionPixelOffset(R.dimen.widget_padding_medium), res.getDimensionPixelOffset(R.dimen.widget_padding_medium), 0 , res.getDimensionPixelOffset(R.dimen.widget_padding_medium));
                }catch (Exception e){

                }
                tv.setLayoutParams(params );
                convertView = tv;
            }
            TextView tv = (TextView) convertView;
            MyBankCard card = myCards.get(position);
            int type = card.getBank_card_type();
            String cardType;
            if(type == 0){
                cardType = "储蓄卡";
            }else{
                cardType = "信用卡";
            }
            tv.setText(String.format(getResources().getString(R.string.blank_card_info),card.getBank().getBank_card_no_tail(),cardType,card.getBank_card_no_tail()));
            return convertView;
        }
    }
}
