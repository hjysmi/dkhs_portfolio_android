package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dkhs.portfolio.R;

/**
 * Created by zhangcm on 2015/9/21.14:35
 */
public class MyFundTradeAdapter extends BaseAdapter{

    private Context context;
    public MyFundTradeAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
            convertView = View.inflate(context, R.layout.item_fund_trade, null);
        }
        return convertView;
    }
}
