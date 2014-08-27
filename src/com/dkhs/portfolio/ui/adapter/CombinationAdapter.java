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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
public class CombinationAdapter extends BaseAdapter implements OnCheckedChangeListener, OnClickListener {
    private Context mContext;
    private int mItemHeight = 0;
    private GridView.LayoutParams mItemViewLayoutParams;
    private int mDataLenght = 5;

    private ArrayList<Integer> positions = new ArrayList<Integer>();

    // private Map<Integer, Boolean> checkMap = new HashMap<Integer, Boolean>();

    public CombinationAdapter(Context context) {
        this.mContext = context;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View row = convertView;
        if (position == mDataLenght) {
            row = View.inflate(mContext, R.layout.item_add_myconbination, null);
            row.setTag("add_more");
            row.setLayoutParams(mItemViewLayoutParams);
            row.setOnClickListener(this);
            return row;
        }
        //
        if (row == null || row.getTag().equals("add_more")) {
            viewHolder = new ViewHolder();
            row = LayoutInflater.from(mContext).inflate(R.layout.item_my_combination, null);
            viewHolder.tvTitle = (TextView) row.findViewById(R.id.tv_combin_title);
            viewHolder.etTitle = (EditText) row.findViewById(R.id.et_combin_title);
            viewHolder.machart = (MAChart) row.findViewById(R.id.machart);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        final ViewHolder viewhold = viewHolder;
        viewHolder.tvTitle.setText("我的组合" + (position + 1));
        viewHolder.etTitle.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (viewhold.etTitle.getVisibility() != View.GONE) {// 一定要先判断一下，不然只要你一点屏幕就会清空你标题上的文字
                        if (viewhold.etTitle.getText().length() > 0) {
                            viewhold.tvTitle.setText(viewhold.etTitle.getText().toString());
                        }
                        viewhold.tvTitle.setVisibility(View.VISIBLE);
                        viewhold.etTitle.setVisibility(View.GONE);
                    }
                }

            }
        });
        initMaChart(viewHolder.machart);
        viewHolder.tvTitle.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                viewhold.tvTitle.setVisibility(View.GONE);
                viewhold.etTitle.setVisibility(View.VISIBLE);
                viewhold.etTitle.requestFocus();
                return false;
            }
        });

        // viewHolder.checkBox.setTag(position);
        // viewHolder.checkBox.setChecked(checkMap.contains(position));
        // viewHolder.checkBox.setOnCheckedChangeListener(this);

        row.setLayoutParams(mItemViewLayoutParams);
        return row;
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

        List<String> ytitle = new ArrayList<String>();
        ytitle.add("1.1031");
        ytitle.add("1.0522");
        ytitle.add("1.0001");
        ytitle.add("1.0001");
        ytitle.add("1.0522");
        ytitle.add("1.1031");
        machart.setLatitudeColor(Color.LTGRAY);
        machart.setMaxValue(120);
        machart.setMinValue(0);
        machart.setMaxPointNum(72);
        machart.setDisplayAxisYTitle(false);
        machart.setDisplayLatitude(true);
        machart.setFill(true);
    }

    public final static class ViewHolder {

        TextView tvTitle;
        EditText etTitle;
        MAChart machart;
        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        return mDataLenght + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param v
     * @return
     */
    @Override
    public void onClick(View v) {

        mDataLenght++;
        notifyDataSetChanged();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            positions.add((Integer) buttonView.getTag());
        }

        else {
            positions.remove((Integer) buttonView.getTag());
        }
    }

}
