package com.dkhs.portfolio.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
    }

    private void initViews() {

        lvBankCard.setAdapter(new MyBankCardAdapter());
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(BankCardInfoActivity.forgetTradePasswordIntent(mContext));
            }
        });
    }

    private class MyBankCardAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 3;
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
            return convertView;
        }
    }
}
