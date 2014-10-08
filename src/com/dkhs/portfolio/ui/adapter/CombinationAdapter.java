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

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.utils.StringFromatUtils;

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
    private ArrayList<CombinationBean> mSelectList = new ArrayList<CombinationBean>();

    public CombinationAdapter(Context context, List<CombinationBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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
            // viewHolder.checkBox = (CheckBox) row.findViewById(R.id.cb_select_conbin);
            viewHolder.ivDel = (ImageButton) row.findViewById(R.id.ib_del_conbin);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        CombinationBean item = mDataList.get(position);
        final ViewHolder viewhold = viewHolder;
        viewHolder.tvTitle.setText(item.getName());
        viewHolder.tvIndex.setText((position + 1) + "");

        float currenValue = item.getCurrentValue();
        if (currenValue > 0) {
            ColorStateList redCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.red);
            viewHolder.tvCurrent.setTextColor(redCsl);
            viewHolder.tvCurrent.setText("+" + currenValue + "%");

        } else {
            ColorStateList greenCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.green);
            viewHolder.tvCurrent.setTextColor(greenCsl);
            viewHolder.tvCurrent.setText(currenValue + "%");
        }
        float addValue = item.getAddUpValue();
        if (addValue > 0) {
            ColorStateList redCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.red);
            viewHolder.tvAddup.setTextColor(redCsl);
            viewHolder.tvAddup.setText("+" + StringFromatUtils.getPercentValue(addValue));

        } else {
            ColorStateList greenCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.green);
            viewHolder.tvAddup.setTextColor(greenCsl);

            viewHolder.tvAddup.setText(StringFromatUtils.getPercentValue(addValue));
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
        viewHolder.ivDel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mDelListener)
                    mDelListener.clickDeleteButton(position);
            }
        });
        if (isDelStatus) {
            viewHolder.ivDel.setVisibility(View.VISIBLE);
            // viewHolder.btnEidt.setVisibility(View.GONE);
            // viewHolder.checkBox.setTag(item);
            // viewHolder.checkBox.setChecked(mSelectList.contains(position));
            // viewHolder.checkBox.setOnCheckedChangeListener(this);
        } else {
            viewHolder.ivDel.setVisibility(View.GONE);
            // viewHolder.btnEidt.setVisibility(View.VISIBLE);

        }
        viewHolder.tvDesc.setText(item.getDescription());

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
        // CheckBox checkBox;
        ImageButton ivDel;
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
        // Intent intent = new Intent(mContext, PositionAdjustActivity.class);
        // intent.putExtra(PositionAdjustActivity.KEY_VIEW_TYPE, PositionAdjustActivity.VALUE_CREATE_CONBINA);
        mContext.startActivity(PositionAdjustActivity.newIntent(mContext, null));
    }

    public ArrayList<CombinationBean> getDelPosition() {
        return mSelectList;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mSelectList.add((CombinationBean) buttonView.getTag());
        } else {
            mSelectList.remove((CombinationBean) buttonView.getTag());
        }
    }

    public void setDelStatus(boolean isDelStatus) {
        this.isDelStatus = isDelStatus;
        notifyDataSetChanged();
    }

    public interface IDelButtonListener {
        void clickDeleteButton(int position);
    }

    private IDelButtonListener mDelListener;

    public void setDeleteButtonClickListener(IDelButtonListener listener) {
        this.mDelListener = listener;
    }

}
