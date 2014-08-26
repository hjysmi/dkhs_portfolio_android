/**
 * @Title CombinationAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 下午3:44:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.MAChart;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName CombinationAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-26 下午3:44:16
 * @version 1.0
 */
public class CombinationAdapter extends DKHSBaseAdapter {
    private Context mContext;
    private int mItemHeight = 0;
    private GridView.LayoutParams mItemViewLayoutParams;

    public CombinationAdapter(Context context) {
        this.mContext = context;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_combination, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_combin_title);
            viewHolder.etTitle = (EditText) convertView.findViewById(R.id.et_combin_title);
            viewHolder.machart = (MAChart) convertView.findViewById(R.id.machart);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // if (mItemHeight != 0) {
        // convertView.getLayoutParams().height = mItemHeight;
        // }
        final ViewHolder viewhold = viewHolder;
        initMaChart(viewHolder.machart);
        viewHolder.tvTitle.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                viewhold.tvTitle.setVisibility(View.GONE);
                viewhold.etTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });

        convertView.setLayoutParams(mItemViewLayoutParams);
        return convertView;
    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();

    }

    private List<Float> initMA(int length) {
        List<Float> MA5Values = new ArrayList<Float>();
        for (int i = 0; i < length; i++) {
            // MA5Values.add((float) new Random().nextInt(99));
            MA5Values.add(new Random().nextFloat() * 100);
        }
        return MA5Values;

    }

    private void initMaChart(MAChart machart) {

        // machart.setAxisXColor(Color.LTGRAY);
        // machart.setAxisYColor(Color.LTGRAY);
        List<LineEntity> lines = new ArrayList<LineEntity>();

        LineEntity MA5 = new LineEntity();
        // MA5.setTitle("MA5");
        MA5.setLineColor(ColorTemplate.getRaddomColor());
        MA5.setLineData(initMA(new Random().nextInt(72)));
        lines.add(MA5);
        machart.setLineData(lines);
        machart.setDisplayBorder(false);
        machart.setDrawXBorke(true);
        // machart.setBorderColor(Color.TRANSPARENT);
        // machart.setDisplayAxisYTitle(false);
        // machart.setBackgroudColor(Color.WHITE);
        // machart.setAxisMarginTop(10);
        // machart.setAxisMarginLeft(20);
        // machart.setAxisMarginRight(10);
        List<String> ytitle = new ArrayList<String>();
        ytitle.add("1.1031");
        ytitle.add("1.0522");
        ytitle.add("1.0001");
        ytitle.add("1.0001");
        ytitle.add("1.0522");
        ytitle.add("1.1031");
        // machart.setAxisYTitles(ytitle);
        // machart.setAxisYColor(Color.LTGRAY);
        // machart.setAxisXTitles(xtitle);
        // machart.setLongtitudeFontSize(10);
        // machart.setLongtitudeFontColor(Color.GRAY);
        // machart.setDisplayAxisYTitleColor(true);
        machart.setLatitudeColor(Color.LTGRAY);
        // machart.setLatitudeFontColor(Color.GRAY);
        machart.setMaxValue(120);
        machart.setMinValue(0);
        machart.setMaxPointNum(72);
        machart.setDisplayAxisYTitle(false);
        machart.setDisplayLatitude(true);
        // machart.setDisplayLongitude(true);
        machart.setFill(true);
    }

    public final static class ViewHolder {

        TextView tvTitle;
        EditText etTitle;
        MAChart machart;
    }

}
