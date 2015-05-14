/**
 * @Title OptionalStockAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version 1.0
 */
public class OptionalStockAdapter extends BaseAdapter {
    private Context mContext;
    private float maxValue = 0;
    private List<ConStockBean> stockList;

    public OptionalStockAdapter(Context mContext, List<ConStockBean> stocks) {
        this.mContext = mContext;
        this.stockList = stocks;
        setSurpusValue();
    }

    public void setFundpercent(float fundpercent) {
        // this.maxValue = fundpercent;
    }

    public void setList(List stocklist) {
        this.stockList = stocklist;
        setSurpusValue();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.stockList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == stockList)
            return null;
        else {
            return stockList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // ViewHolder viewHolder = null;
        // if (convertView == null) {
        // viewHolder = new ViewHolder();
        // convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_optional_percent, null);
        // viewHolder.colorView = convertView.findViewById(R.id.view_color);
        // viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
        // viewHolder.tvPercent = (TextView) convertView.findViewById(R.id.tv_stock_percent);
        // viewHolder.seekbar = (SeekBar) convertView.findViewById(R.id.seekBar);
        // convertView.setTag(viewHolder);
        // } else {
        // viewHolder = (ViewHolder) convertView.getTag();
        // }
        convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_optional_percent, null);
        View colorView = convertView.findViewById(R.id.view_color);
        TextView tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
        TextView tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
        final TextView tvPercent = (TextView) convertView.findViewById(R.id.tv_stock_percent);
        TextView tvIstop = (TextView) convertView.findViewById(R.id.tv_isStop);
        final SeekBar seekbar = (SeekBar) convertView.findViewById(R.id.seekBar);

        ConStockBean item = stockList.get(position);
        if (item.isStop()) {
            convertView.setBackgroundResource(R.color.theme_gray);
            seekbar.setEnabled(false);
            tvIstop.setVisibility(View.VISIBLE);
        }
        tvStockName.setText(item.getName());
        tvStockNum.setText(item.getStockCode());
        colorView.setBackgroundColor(item.getDutyColor());
        seekbar.setProgress((int) (item.getPercent()));

        ScaleDrawable sd = (ScaleDrawable) ((LayerDrawable) seekbar.getProgressDrawable())
                .findDrawableByLayerId(android.R.id.progress);

        GradientDrawable gd = (GradientDrawable) sd.getDrawable();
        gd.setColor(item.getDutyColor());
        tvPercent.setText(StringFromatUtils.get2PointPercent((item.getPercent())));

        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stockList.get(position).setPercent(seekBar.getProgress());

                if (null != mDutyNotify) {

                    mDutyNotify.notifyRefresh(position, seekBar.getProgress());

                }
                setSurpusValue();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int p = progress;
                System.out.println("maxValue:" + maxValue);
                int maxScoll = (int) (setSurpusValue() + stockList.get(position).getPercent());

                if (progress >= maxScoll) {
                    p = maxScoll;
                    maxScoll = maxScoll < 0 ? 0 : maxScoll;
                    seekbar.setProgress(maxScoll);
                    tvPercent.setText(StringFromatUtils.get2PointPercent(maxScoll));
                    return;
                } else {
                    p = progress < 0 ? 0 : progress;
                    // p = progress;
                    tvPercent.setText(StringFromatUtils.get2PointPercent(p));
                }
                // notifySurpusValue(stockList.get(position).getDutyValue() - p);
                // setSurpusValue(progress);
                // mDutyNotify.updateSurpus(stockList.get(position).getDutyValue() - p);

                // return;
            }
        });

        return convertView;
    }

    public float setSurpusValue() {
        float surpusValu = 100;
        System.out.println("stockList size:" + stockList.size());
        for (int i = 0; i < stockList.size(); i++) {
            surpusValu -= stockList.get(i).getPercent();
            System.out.println("Value:" + stockList.get(i).getPercent());
        }
        maxValue = surpusValu;
        System.out.println("setSurpusValue:" + maxValue);
        return maxValue;

    }

    private void notifySurpusValue(int value) {
        ConStockBean sur = stockList.get(stockList.size() - 1);
        stockList.get(stockList.size() - 1).setPercent(sur.getPercent() + value);
        notifyDataSetChanged();
    }

    public final static class ViewHolder {

        View colorView;
        TextView tvStockName;
        TextView tvStockNum;
        TextView tvPercent;
        SeekBar seekbar;
    }

    private IDutyNotify mDutyNotify;

    public void setDutyNotifyListener(IDutyNotify dutyNotify) {
        this.mDutyNotify = dutyNotify;
    }

    public interface IDutyNotify {
        void notifyRefresh(int position, int value);

        void updateSurpus(int value);
    }

}