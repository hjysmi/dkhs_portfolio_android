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
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.SurpusStock;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter.IDutyNotify;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName PositionAdjustActivity
 * @Description 持仓组合调整
 * @author zjz
 * @date 2014-8-25 上午9:55:53
 * @version 1.0
 */
public class PositionAdjustActivity extends ModelAcitivity implements IDutyNotify {

    public static final String KEY_VIEW_TYPE = "key_view_type";
    public static final String VALUE_CREATE_CONBINA = "value_create_conbina";
    public static final String VALUE_ADJUST_CONBINA = "value_adjust_conbina";

    private PieGraph pgView;
    private List<ConStockBean> stockList;
    private OptionalStockAdapter stockAdapter;
    private int surValue;
    private TextView tvSurpusValue;
    private SeekBar surSeekbar;
    private ArrayList<PieSlice> pieList;
    private String mViewType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        System.out.println("==============PositionAdjustActivity onCreate ");
        setContentView(R.layout.activity_positionadjust);
        setTitle(R.string.portfolio_position);

        if (null != getIntent()) {
            mViewType = getIntent().getStringExtra(KEY_VIEW_TYPE);

        }
        initConbinationInfoView();
        initData();
        initPieView();
        initStockPercentView();
    }

    private void initConbinationInfoView() {
        if (mViewType.equalsIgnoreCase(VALUE_CREATE_CONBINA)) {
            ViewStub viewstub = (ViewStub) findViewById(R.id.create_portfolio_info);
            if (viewstub != null) {
                viewstub.inflate();
            }
        } else {
            ViewStub viewstub = (ViewStub) findViewById(R.id.portfolio_info);
            if (viewstub != null) {
                viewstub.inflate();
            }
        }
    }

    /**
     * @Title
     * @Description TODO: 初始化数据
     * @return void
     */
    private void initData() {
        stockList = new ArrayList<ConStockBean>();
        ConStockBean stock1 = new ConStockBean(1, 30, ColorTemplate.getRaddomColor(), "沪深大盘", "600123");
        ConStockBean stock2 = new ConStockBean(2, 40, ColorTemplate.getRaddomColor(), "苏宁云商", "600123");
        ConStockBean stock3 = new ConStockBean(3, 30, ColorTemplate.getRaddomColor(), "阿里巴巴", "600123");
        surValue = 0;
        ConStockBean stock4 = new SurpusStock(surValue);
        stockList.add(stock1);
        stockList.add(stock2);
        stockList.add(stock3);
        stockList.add(stock4);
    }

    /**
     * @Title
     * @Description TODO: 初始化自选股占比
     * @return void
     */
    private void initStockPercentView() {
        ListViewEx lvStock = (ListViewEx) findViewById(R.id.lv_optional_layout);
        stockAdapter = new OptionalStockAdapter(this, stockList);
        stockAdapter.setDutyNotifyListener(this);
        lvStock.addFooterView(footerView());
        lvStock.setAdapter(stockAdapter);

    }

    private View footerView() {
        View foot = View.inflate(this, R.layout.layout_optional_percent, null);
        foot.findViewById(R.id.tv_stock_num).setVisibility(View.GONE);
        surSeekbar = (SeekBar) foot.findViewById(R.id.seekBar);
        surSeekbar.setEnabled(false);
        TextView tvName = (TextView) foot.findViewById(R.id.tv_stock_name);
        tvName.setText("剩余资金占比");
        tvSurpusValue = (TextView) foot.findViewById(R.id.tv_stock_percent);
        tvSurpusValue.setText(StringFromatUtils.getPercentValue(surValue));
        ScaleDrawable sd = (ScaleDrawable) ((LayerDrawable) surSeekbar.getProgressDrawable())
                .findDrawableByLayerId(android.R.id.progress);

        GradientDrawable gd = (GradientDrawable) sd.getDrawable();
        gd.setColor(Color.RED);
        // surSeekbar.setThumb(new ColorDrawable(Color.TRANSPARENT));
        return foot;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initPieView() {
        pgView = (PieGraph) findViewById(R.id.piegrah);
        pieList = new ArrayList<PieSlice>();
        int valueSize = stockList.size();
        for (int i = 0; i < valueSize; i++) {
            PieSlice slice1 = new PieSlice();
            slice1.setColor(stockList.get(i).getDutyColor());
            slice1.setValue(stockList.get(i).getDutyValue());
            pieList.add(slice1);

        }
        pgView.setSlices(pieList);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void notifyRefresh(int position, int value) {
        pieList.get(position).setValue(value);
        pieList.get(pieList.size() - 1).setValue(surpulsValue());
        pgView.invalidate();
    }

    private int surpulsValue() {
        int total = 100;
        for (int i = 0; i < pieList.size() - 1; i++) {
            total -= pieList.get(i).getValue();
        }
        surValue = total;
        surSeekbar.setProgress(surValue);
        tvSurpusValue.setText(StringFromatUtils.getPercentValue(surValue));
        return total;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param value
     * @return
     */
    @Override
    public void updateSurpus(int value) {

        // System.out.println("surValue:" + surValue);

    }
}
