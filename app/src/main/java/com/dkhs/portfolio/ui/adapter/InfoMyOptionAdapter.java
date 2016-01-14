package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.List;

public class InfoMyOptionAdapter extends BaseAdapter {
    private Context mContext;
    private List<OptionNewsBean> mDataList;
    private boolean isSecondNotice;

    public InfoMyOptionAdapter(Context mContext, List<OptionNewsBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager m = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
//        m.getDefaultDisplay().getMetrics(dm);
//        Paint p = new Paint();
//        Rect rect = new Rect();
//        String text = "正正正正正";
//        p.setTextSize(mActivity.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
//        p.getTextBounds(text, 0, text.length(), rect);
    }

    public void setSecondNotice(boolean isSecondNotice) {
        this.isSecondNotice = isSecondNotice;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (null == mDataList) {
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHodler holder = null;
        try {
            OptionNewsBean mOptionNewsBean = mDataList.get(position);
            if (null == convertView) {
                holder = new ViewHodler();
                convertView = View.inflate(mContext, R.layout.adapter_info_myoptional, null);
                holder.llRelated1 = (LinearLayout) convertView.findViewById(R.id.ll_related1);
                holder.llRelated2 = (LinearLayout) convertView.findViewById(R.id.ll_related2);
                holder.tvRelatedStock1 = (TextView) convertView.findViewById(R.id.tv_related_stock1);
                holder.tvRelatedStock2 = (TextView) convertView.findViewById(R.id.tv_related_stock2);
                holder.tvRelatedStockPercent1 = (TextView) convertView.findViewById(R.id.tv_releated_stock_percent1);
                holder.tvRelatedStockPercent2 = (TextView) convertView.findViewById(R.id.tv_releated_stock_percent2);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHodler) convertView.getTag();
            }
            List<OptionNewsBean.Symbols> symbols = mOptionNewsBean.getSymbols();
            if(symbols != null){
                if(symbols.size() == 0){
                    holder.llRelated1.setVisibility(View.GONE);
                    holder.llRelated2.setVisibility(View.GONE);
                }else if(symbols.size() == 1){
                    holder.llRelated1.setVisibility(View.VISIBLE);
                    holder.llRelated2.setVisibility(View.GONE);
                    String percentStr;
                    if(symbols.get(0).isStop()){
                        holder.tvRelatedStockPercent1.setTextColor(UIUtils.getResColor(mContext,R.color.gray_text_selector));
                        percentStr = UIUtils.getResString(mContext,R.string.stop_stock);
                    }else if(symbols.get(0).getPercentage() > 0){
                        percentStr = StringFromatUtils.get2PointPercent(symbols.get(0).getPercentage());
                        holder.tvRelatedStockPercent1.setTextColor(UIUtils.getResColor(mContext,R.color.red));
                    }else{
                        percentStr = StringFromatUtils.get2PointPercent(symbols.get(0).getPercentage());
                        holder.tvRelatedStockPercent1.setTextColor(UIUtils.getResColor(mContext,R.color.green));
                    }
                    holder.tvRelatedStock1.setText(symbols.get(0).getAbbrName());
                    holder.tvRelatedStockPercent1.setText(percentStr);
                }else {
                    holder.llRelated1.setVisibility(View.VISIBLE);
                    holder.llRelated2.setVisibility(View.VISIBLE);
                    String percentStr1;
                    if(symbols.get(0).isStop()){
                        holder.tvRelatedStockPercent1.setTextColor(UIUtils.getResColor(mContext,R.color.gray_text_selector));
                        percentStr1 = UIUtils.getResString(mContext,R.string.stop_stock);
                    }else if(symbols.get(0).getPercentage() > 0){
                        percentStr1 = StringFromatUtils.get2PointPercent(symbols.get(0).getPercentage());
                        holder.tvRelatedStockPercent1.setTextColor(UIUtils.getResColor(mContext,R.color.red));
                    }else{
                        percentStr1 = StringFromatUtils.get2PointPercent(symbols.get(0).getPercentage());
                        holder.tvRelatedStockPercent1.setTextColor(UIUtils.getResColor(mContext,R.color.green));
                    }
                    holder.tvRelatedStock1.setText(symbols.get(0).getAbbrName());
                    holder.tvRelatedStockPercent1.setText(percentStr1);

                    String percentStr2;
                    if(symbols.get(1).isStop()){
                        holder.tvRelatedStockPercent2.setTextColor(UIUtils.getResColor(mContext,R.color.gray_text_selector));
                        percentStr2 = UIUtils.getResString(mContext,R.string.stop_stock);
                    }else if(symbols.get(1).getPercentage() > 0){
                        percentStr2 = StringFromatUtils.get2PointPercent(symbols.get(1).getPercentage());
                        holder.tvRelatedStockPercent2.setTextColor(UIUtils.getResColor(mContext,R.color.red));
                    }else{
                        percentStr2 = StringFromatUtils.get2PointPercent(symbols.get(1).getPercentage());
                        holder.tvRelatedStockPercent2.setTextColor(UIUtils.getResColor(mContext,R.color.green));
                    }
                    holder.tvRelatedStock2.setText(symbols.get(1).getAbbrName());
                    holder.tvRelatedStockPercent2.setText(percentStr2);

                }
            }
            holder.tvContent.setText(mOptionNewsBean.getTitle());
            holder.tvTime.setText(TimeUtils.getMMDDString(mOptionNewsBean.getPublish()));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    final static class ViewHodler {
        TextView tvRelatedStock1;
        TextView tvRelatedStock2;
        TextView tvRelatedStockPercent1;
        TextView tvRelatedStockPercent2;
        TextView tvContent;
        TextView tvTime;
        LinearLayout llRelated1;
        LinearLayout llRelated2;
    }
}
