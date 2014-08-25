/**
 * @Title PositionAdjustActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 上午9:55:53
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;

/**
 * @ClassName PositionAdjustActivity
 * @Description 持仓组合调整
 * @author zjz
 * @date 2014-8-25 上午9:55:53
 * @version 1.0
 */
public class PositionAdjustActivity extends ModelAcitivity {

    PieGraph pgView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_positionadjust);
        setTitle(R.string.portfolio_position);
        initPieView();
        initStockPercentView();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initStockPercentView() {
        ListViewEx lvStock = (ListViewEx) findViewById(R.id.lv_optional_layout);
        lvStock.setAdapter(new OptionalStockAdapter(this));
        // UiUtils.setListViewHeightBasedOnChildren(lvStock);
        // StockPercentView stock3 = new StockPercentView(this, llStock);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initPieView() {
        pgView = (PieGraph) findViewById(R.id.piegrah);
        ArrayList<PieSlice> pieList = new ArrayList<PieSlice>();
        PieSlice slice1 = new PieSlice();
        slice1.setColor(Color.parseColor("#99CC00"));
        // slice1.setTitle(choices.get(0).getName());
        slice1.setValue(30);
        pieList.add(slice1);
        PieSlice slice2 = new PieSlice();
        slice2.setColor(Color.parseColor("#FFBB33"));
        // slice2.setTitle(choices.get(1).getName());
        // tvLab2.setText(choices.get(1).getName());
        slice2.setValue(40);
        pieList.add(slice2);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        slice.setValue(30);

        pieList.add(slice);
        // pgView.addSlice(slice);
        pgView.setSlices(pieList);

    }

}
