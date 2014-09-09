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
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.widget.LineChart;
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
    private List<CombinationBean> mDataList;
    private boolean isDelStatus;
    private ArrayList<Integer> mSelectList = new ArrayList<Integer>();

    public CombinationAdapter(Context context, List<CombinationBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View row = convertView;
        if (position == mDataList.size()) {
            row = View.inflate(mContext, R.layout.item_add_myconbination, null);
            row.setTag("add_more");
            row.setLayoutParams(mItemViewLayoutParams);
            row.setOnClickListener(this);
            return row;
        }
        if (row == null || row.getTag().equals("add_more")) {
            viewHolder = new ViewHolder();
            row = LayoutInflater.from(mContext).inflate(R.layout.item_my_combination, null);
            viewHolder.tvTitle = (TextView) row.findViewById(R.id.tv_combin_title);
            viewHolder.tvCurrent = (TextView) row.findViewById(R.id.tv_mycob_curren_value);
            viewHolder.tvAddup = (TextView) row.findViewById(R.id.tv_mycob_add_value);
            viewHolder.tvIndex = (TextView) row.findViewById(R.id.tv_combination_index);
            viewHolder.tvDesc = (TextView) row.findViewById(R.id.tv_combination_desc);
            // viewHolder.etTitle = (EditText) row.findViewById(R.id.et_combin_title);
            // viewHolder.machart = (LineChart) row.findViewById(R.id.machart);
            // viewHolder.btnEidt = (Button) row.findViewById(R.id.btn_edit_contitle);
            viewHolder.checkBox = (CheckBox) row.findViewById(R.id.cb_select_conbin);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        CombinationBean item = mDataList.get(position);
        final ViewHolder viewhold = viewHolder;
        viewHolder.tvTitle.setText(item.getName());
        viewHolder.tvIndex.setText((position+1)+"");
        
        
        float currenValue = item.getCurrentValue();
        if (currenValue > 0) {
            ColorStateList redCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.red);
            viewHolder.tvAddup.setTextColor(redCsl);
            viewHolder.tvCurrent.setTextColor(redCsl);
            viewHolder.tvAddup.setText("+" + item.getAddUpValue() + "%");
            viewHolder.tvCurrent.setText("+" + currenValue + "%");

        } else {
            ColorStateList greenCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.green);
            viewHolder.tvCurrent.setTextColor(greenCsl);
            viewHolder.tvAddup.setTextColor(greenCsl);
            viewHolder.tvAddup.setText(item.getAddUpValue() + "%");
            viewHolder.tvCurrent.setText(currenValue + "%");
        }
        
        
        // viewHolder.etTitle.setOnFocusChangeListener(new OnFocusChangeListener() {
        //
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        //
        // if (!hasFocus) {
        // if (viewhold.etTitle.getVisibility() != View.GONE) {// 一定要先判断一下，不然只要你一点屏幕就会清空你标题上的文字
        // if (viewhold.etTitle.getText().length() > 0) {
        // viewhold.tvTitle.setText(viewhold.etTitle.getText().toString());
        // }
        // viewhold.tvTitle.setVisibility(View.VISIBLE);
        // viewhold.etTitle.setVisibility(View.GONE);
        // }
        // }
        //
        // }
        // });

        // initMaChart(viewHolder.machart);
        // viewHolder.btnEidt.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // if (viewhold.etTitle.getVisibility() != View.GONE) {// 一定要先判断一下，不然只要你一点屏幕就会清空你标题上的文字
        // // if (viewhold.etTitle.getText().length() > 0) {
        // // viewhold.tvTitle.setText(viewhold.etTitle.getText().toString());
        // // }
        // // viewhold.tvTitle.setVisibility(View.VISIBLE);
        // viewhold.etTitle.clearFocus();
        // InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
        // Context.INPUT_METHOD_SERVICE);
        // if (imm != null) {
        // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        // }
        // } else {
        //
        // viewhold.tvTitle.setVisibility(View.GONE);
        // String title = viewhold.tvTitle.getText().toString();
        // viewhold.etTitle.setText(title);
        // viewhold.etTitle.setVisibility(View.VISIBLE);
        // viewhold.etTitle.requestFocus();
        // viewhold.etTitle.setSelection(title.length());
        // }
        //
        // }
        // });

        // 长按标题进入编辑状态

        // viewHolder.tvTitle.setOnLongClickListener(new OnLongClickListener() {
        //
        // @Override
        // public boolean onLongClick(View v) {
        // viewhold.tvTitle.setVisibility(View.GONE);
        // viewhold.etTitle.setVisibility(View.VISIBLE);
        // viewhold.etTitle.requestFocus();
        // viewhold.etTitle.setText(viewhold.tvTitle.getText());
        // return false;
        // }
        // });

        if (isDelStatus) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            // viewHolder.btnEidt.setVisibility(View.GONE);
            viewHolder.checkBox.setTag(position);
            viewHolder.checkBox.setChecked(mSelectList.contains(position));
            viewHolder.checkBox.setOnCheckedChangeListener(this);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            // viewHolder.btnEidt.setVisibility(View.VISIBLE);

        }

        row.setLayoutParams(mItemViewLayoutParams);
        return row;
    }

    public final static class ViewHolder {
        TextView tvTitle;
        TextView tvCurrent;
        TextView tvAddup;
        TextView tvIndex;
        TextView tvDesc;
        // EditText etTitle;
        // LineChart machart;
        CheckBox checkBox;
        // Button btnEidt;
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

    private void initMaChart(LineChart machart) {

        // machart.setAxisXColor(Color.LTGRAY);
        // machart.setAxisYColor(Color.LTGRAY);
        List<LineEntity> lines = new ArrayList<LineEntity>();

        LineEntity MA5 = new LineEntity();
        // MA5.setTitle("MA5");
        // MA5.setLineColor(ColorTemplate.getRaddomColor())
        MA5.setLineColor(mContext.getResources().getColor(ColorTemplate.MY_COMBINATION_LINE));
        MA5.setLineData(initMA(new Random().nextInt(72)));
        lines.add(MA5);
        machart.setLineData(lines);
        machart.setDisplayBorder(false);
        // machart.setDrawXBorke(true);

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
        machart.setDisplayLatitude(false);
        machart.setDisplayLongitude(false);
        machart.setFill(true);
    }

    @Override
    public int getCount() {
        return this.mDataList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public void onClick(View v) {

        addItem();

    }

    public void addItem() {
        Intent intent = new Intent(mContext, PositionAdjustActivity.class);
        intent.putExtra(PositionAdjustActivity.KEY_VIEW_TYPE, PositionAdjustActivity.VALUE_CREATE_CONBINA);
        mContext.startActivity(intent);
    }

    public ArrayList<Integer> getDelPosition() {
        return mSelectList;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mSelectList.add((Integer) buttonView.getTag());
        } else {
            mSelectList.remove((Integer) buttonView.getTag());
        }
    }

    public void setDelStatus(boolean isDelStatus) {
        this.isDelStatus = isDelStatus;
        notifyDataSetChanged();
    }

}
