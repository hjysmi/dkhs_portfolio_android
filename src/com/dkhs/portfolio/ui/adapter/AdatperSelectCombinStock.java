/**
 * @Title AdatperSelectConbinStock.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-29 上午10:25:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.AddCombinationStockActivity;

/**
 * @ClassName AdatperSelectConbinStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-29 上午10:25:51
 * @version 1.0
 */
public class AdatperSelectCombinStock extends BaseAdapter implements OnCheckedChangeListener {
    private Context mContext;
    private List<ConStockBean> mDataList;

    public AdatperSelectCombinStock(Context context, List<ConStockBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_select_conbin_stock, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.mCheckbox = (CheckBox) convertView.findViewById(R.id.cb_select_stock);
            viewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_current_value);
            viewHolder.tvIncreaseValue = (TextView) convertView.findViewById(R.id.tv_increase_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        ConStockBean item = mDataList.get(position);
        // if(AddConbinationStockActivity.mSelectIdList.contains(item.getId()))
        // System.out.println("getView id:" + item.getId());
        // for (ConStockBean i : AddConbinationStockActivity.mSelectIdList) {
        // System.out.println("mSelectIdList id:" + i.getId());
        // }

        viewHolder.mCheckbox.setOnCheckedChangeListener(null);
        viewHolder.mCheckbox.setTag(item);
        viewHolder.mCheckbox.setChecked(AddCombinationStockActivity.mSelectList.contains(item));
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);

        viewHolder.tvStockName.setText(item.getName());

        ColorStateList textCsl;
        if (position % 3 == 0) {
            textCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.red);

        } else {
            textCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.green);

        }
        viewHolder.tvCurrentValue.setTextColor(textCsl);
        viewHolder.tvCurrentValue.setText("" + item.getCurrentValue());

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvIncreaseValue;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param buttonView
     * @param isChecked
     * @return
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ConStockBean csBean = (ConStockBean) buttonView.getTag();

        if (isChecked && !AddCombinationStockActivity.mSelectList.contains(csBean)) {
            System.out.println("add mSelectIdList id:" + csBean);
            AddCombinationStockActivity.mSelectList.add(csBean);
        } else {
            boolean isRmove = AddCombinationStockActivity.mSelectList.remove(csBean);

            if (isRmove) {

                System.out.println("remove mSelectIdList lenght:" + AddCombinationStockActivity.mSelectList.size());
            }
        }
        if (null != mSelectLisenter) {
            mSelectLisenter.onCheckedChanged(buttonView, isChecked);
        }
    }

    private ISelectChangeListener mSelectLisenter;

    public interface ISelectChangeListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }

    public void setCheckChangeListener(ISelectChangeListener listener) {
        this.mSelectLisenter = listener;
    }

}
