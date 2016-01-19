package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundTradeBean;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/21.14:35
 */
public class MyFundTradeAdapter extends BaseAdapter{

    protected List<FundTradeBean> mData;
    private Context context;
    public MyFundTradeAdapter(Context context, List<FundTradeBean> mData){
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
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
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_fund_trade, null);
            holder = new ViewHolder();
            holder.tv_fund_name = (TextView) convertView.findViewById(R.id.tv_fund_name);
            holder.tv_trade_time = (TextView) convertView.findViewById(R.id.tv_trade_time);
            holder.tv_trade_value = (TextView) convertView.findViewById(R.id.tv_trade_value);
            holder.tv_trade_status = (TextView) convertView.findViewById(R.id.tv_trade_status);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        FundTradeBean tradeBean = mData.get(position);
        holder.tv_fund_name.setText(String.format(context.getResources().getString(R.string.blank_fund_name),tradeBean.getFund().abbr_name, tradeBean.getFund().symbol));
        holder.tv_trade_time.setText(TimeUtils.getBriefTimeString(tradeBean.getApply_date()));
        holder.tv_trade_value.setText(String.format(context.getResources().getString(R.string.blank_dollar),tradeBean.getAmount()));
        if(tradeBean.getStatus() == 0){
            holder.tv_trade_status.setText(R.string.entrust_suc);
        }else if(tradeBean.getStatus() == 1){
            holder.tv_trade_status.setText(R.string.trade_suc);
        }else if(tradeBean.getStatus() == 2){
            holder.tv_trade_status.setText(R.string.trade_fail);
        }else if(tradeBean.getStatus() == 3){
            holder.tv_trade_status.setText(R.string.pay_suc);
        }else if(tradeBean.getStatus() == 4){
            holder.tv_trade_status.setText(R.string.pay_fail);
        }
        return convertView;
    }
    private class ViewHolder{
        TextView tv_fund_name;
        TextView tv_trade_status;
        TextView tv_trade_time;
        TextView tv_trade_value;
    }
}
