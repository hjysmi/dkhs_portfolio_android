package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FlowPackageBean;
import com.dkhs.portfolio.utils.ColorTemplate;

import java.util.List;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowExPackAdatper extends AutoAdapter {


    public FlowExPackAdatper(Context context, List<?> list) {
        super(context, list);
    }


    private int maxAmount = -1;
    int selectedIndex = -1;

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }


    @Override
    public int setLayoutID() {
        return R.layout.item_flow_ex_package;
    }

    @Override
    public void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh) {
        FlowPackageBean.OppackagesEntity oppackageEntity = (FlowPackageBean.OppackagesEntity) list.get(position);
        CheckBox cbFlowPack = vh.get(R.id.tv_flow_count);
        cbFlowPack.setText(oppackageEntity.getAmount() + "M");

        if (maxAmount > 0 && maxAmount >= oppackageEntity.getAmount()) {
            if (selectedIndex == position) {
                cbFlowPack.setChecked(true);
                cbFlowPack.setBackgroundResource(R.drawable.dia70_red_circle);
                cbFlowPack.setTextColor(ColorTemplate.getTextColor(R.color.tag_red));
            } else {
                cbFlowPack.setChecked(false);
                cbFlowPack.setEnabled(true);
            }
        } else {
            cbFlowPack.setEnabled(false);
        }


    }
}
