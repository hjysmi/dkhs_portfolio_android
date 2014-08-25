/**
 * @Title OptionalStockAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version 1.0
 */
public class OptionalStockAdapter extends BaseAdapter {
    private Context mContext;
    private int maxValue = 50;

    public OptionalStockAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_optional_percent, null);
            viewHolder.colorView = convertView.findViewById(R.id.view_color);
            viewHolder.tvPercent = (TextView) convertView.findViewById(R.id.tv_stock_percent);
            viewHolder.seekbar = (SeekBar) convertView.findViewById(R.id.seekBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // viewHolder.colorView.setBackgroundResource(R.color.orange);
        viewHolder.colorView.setBackgroundColor(ColorTemplate.getRaddomColor());

        final ViewHolder viewHolderFinal = viewHolder;
        viewHolder.seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                System.out.println("onStopTrackingTouch");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("onStartTrackingTouch");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("onProgressChanged");
                if (progress >= maxValue) {
                    viewHolderFinal.seekbar.setProgress(maxValue);
                    viewHolderFinal.tvPercent.setText(Integer.toString(maxValue) + "%");
                    return;
                }
                viewHolderFinal.tvPercent.setText(Integer.toString(progress) + "%");
            }
        });

        return convertView;
    }

    public final static class ViewHolder {

        View colorView;
        TextView tvStockName;
        TextView tvStockNum;
        TextView tvPercent;
        SeekBar seekbar;
    }

}
