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
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
public class PositionAdjustActivity extends ModelAcitivity implements IDutyNotify, OnClickListener {

    public static final String KEY_VIEW_TYPE = "key_view_type";
    public static final String KEY_CONBINATION_ID = "key_conbination_id";
    public static final String VALUE_CREATE_CONBINA = "value_create_conbina";
    public static final String VALUE_ADJUST_CONBINA = "value_adjust_conbina";

    private PieGraph pgView;
    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();;
    private OptionalStockAdapter stockAdapter;
    private int surValue;
    private TextView tvSurpusValue;
    private SeekBar surSeekbar;
    private ArrayList<PieSlice> pieList;
    private String mViewType;
    private TextView tvCreateTime;
    private TextView tvTodayNetvalue;
    private EditText etConbinationName;
    private EditText etConbinationDesc;

    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_positionadjust);
        setTitle(R.string.portfolio_position);

        if (null != getIntent()) {
            mViewType = getIntent().getStringExtra(KEY_VIEW_TYPE);
        }
        initData();
        initView();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initView() {
        initConbinationInfoView();

        initPieView();
        initStockPercentView();
        btnConfirm = getRightButton();
        btnConfirm.setText("确定");
        btnConfirm.setTextColor(Color.WHITE);
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(this);

        findViewById(R.id.btn_add_postional).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

    }

    private void initConbinationInfoView() {
        if (mViewType.equalsIgnoreCase(VALUE_CREATE_CONBINA)) {
            ViewStub viewstub = (ViewStub) findViewById(R.id.create_portfolio_info);
            if (viewstub != null) {
                viewstub.inflate();
                tvCreateTime = (TextView) findViewById(R.id.tv_create_time);
                tvTodayNetvalue = (TextView) findViewById(R.id.tv_today_netvalue);
            }

        } else {
            ViewStub viewstub = (ViewStub) findViewById(R.id.portfolio_info);
            if (viewstub != null) {
                viewstub.inflate();
                etConbinationName = (EditText) findViewById(R.id.et_myconbina_name);
                etConbinationDesc = (EditText) findViewById(R.id.et_myconbina_desc);

            }
        }
    }

    /**
     * @Title
     * @Description TODO: 初始化数据
     * @return void
     */
    private void initData() {
        if (mViewType.equalsIgnoreCase(VALUE_CREATE_CONBINA)) {
            surValue = 100;
            ConStockBean stock4 = new SurpusStock(surValue);
            stockList.add(stock4);
        } else {
            ConStockBean stock1 = new ConStockBean(1, 30, getResources().getColor(ColorTemplate.DEFAULTCOLORS[0]),
                    "沪深大盘", "600123");
            ConStockBean stock2 = new ConStockBean(2, 40, getResources().getColor(ColorTemplate.DEFAULTCOLORS[1]),
                    "苏宁云商", "600123");
            ConStockBean stock3 = new ConStockBean(3, 30, getResources().getColor(ColorTemplate.DEFAULTCOLORS[2]),
                    "阿里巴巴", "600123");
            surValue = 0;
            ConStockBean stock4 = new SurpusStock(surValue);
            stockList.add(stock1);
            stockList.add(stock2);
            stockList.add(stock3);
            stockList.add(stock4);
        }
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
        surSeekbar.setProgress(surValue);
        TextView tvName = (TextView) foot.findViewById(R.id.tv_stock_name);
        tvName.setText("剩余资金占比");
        tvSurpusValue = (TextView) foot.findViewById(R.id.tv_stock_percent);
        tvSurpusValue.setText(StringFromatUtils.getPercentValue(surValue));
        ScaleDrawable sd = (ScaleDrawable) ((LayerDrawable) surSeekbar.getProgressDrawable())
                .findDrawableByLayerId(android.R.id.progress);

        GradientDrawable gd = (GradientDrawable) sd.getDrawable();
        gd.setColor(Color.RED);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_confirm:
            case R.id.btn_right: {
                for (ConStockBean stock : stockList) {
                    System.out.println("After adjust stock name:" + stock.getName() + " value:" + stock.getDutyValue());
                }
                Toast.makeText(PositionAdjustActivity.this, "确定调整  ", Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.btn_add_postional: {

                Toast.makeText(PositionAdjustActivity.this, "添加自选股  ", Toast.LENGTH_SHORT).show();
            }
                break;

            default:
                break;
        }
    }
}
