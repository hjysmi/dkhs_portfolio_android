/**
 * @Title FragmentPositionDetail.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:33:13
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.adapter.AdjustHistoryAdapter;
import com.dkhs.portfolio.ui.adapter.OptionalStockAdapter;
import com.dkhs.portfolio.ui.adapter.PositionDetailIncreaAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.dkhs.portfolio.ui.widget.PieGraph;
import com.dkhs.portfolio.ui.widget.PieSlice;
import com.dkhs.portfolio.utils.ColorTemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * @ClassName FragmentPositionDetail
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:33:13
 * @version 1.0
 */
public class FragmentPositionDetail extends Fragment implements OnClickListener {

    private PieGraph pgView;
    private ArrayList<PieSlice> pieList = new ArrayList<PieSlice>();
    private int surValue;

    private List<ConStockBean> stockList = new ArrayList<ConStockBean>();
    // 涨幅相关
    private ListViewEx lvStock;
    private PositionDetailIncreaAdapter stockAdapter;

    // 净值贡献相关
    private ListViewEx lvContribute;
    private PositionDetailIncreaAdapter mContributeAdapter;

    // 持仓调整相关
    private ListViewEx lvAdjustHistory;
    private AdjustHistoryAdapter mAdjustAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStockList();
    }

    private void setStockList() {
        ConStockBean stock1 = new ConStockBean(1, 30, getResources().getColor(ColorTemplate.DEFAULTCOLORS[0]), "沪深大盘",
                "600123");
        ConStockBean stock2 = new ConStockBean(2, 40, getResources().getColor(ColorTemplate.DEFAULTCOLORS[1]), "苏宁云商",
                "600123");
        ConStockBean stock3 = new ConStockBean(3, 30, getResources().getColor(ColorTemplate.DEFAULTCOLORS[2]), "阿里巴巴",
                "600123");

        stockList.add(stock1);
        stockList.add(stock2);
        stockList.add(stock3);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_detail, null);
        initView(view);
        initPieView(view);
        initIncreaseList(view);
        initContributeView(view);
        initAdjustHistoryView(view);
        return view;
    }

    private void initAdjustHistoryView(View view) {

        lvAdjustHistory = (ListViewEx) view.findViewById(R.id.lv_adjust_history);
        mAdjustAdapter = new AdjustHistoryAdapter(getActivity());
        lvAdjustHistory.setAdapter(mAdjustAdapter);
    }

    private void initContributeView(View view) {
        lvContribute = (ListViewEx) view.findViewById(R.id.lv_contribute_layout);
        mContributeAdapter = new PositionDetailIncreaAdapter(getActivity(), stockList);
        View headerView = View.inflate(getActivity(), R.layout.layout_detail_contribute_title, null);
        lvContribute.addHeaderView(headerView);
        lvContribute.setAdapter(mContributeAdapter);

    }

    private void initView(View view) {
        view.findViewById(R.id.btn_adjust_position).setOnClickListener(this);

    }

    private void initPieView(View view) {
        pgView = (PieGraph) view.findViewById(R.id.piegrah);
        setPieList();
    }

    private void initIncreaseList(View view) {
        lvStock = (ListViewEx) view.findViewById(R.id.lv_optional_layout);
        stockAdapter = new PositionDetailIncreaAdapter(getActivity(), stockList);
        View headerView = View.inflate(getActivity(), R.layout.layout_detail_pos_increase_title, null);
        lvStock.addHeaderView(headerView);
        lvStock.setAdapter(stockAdapter);

    }

    private void setPieList() {
        int valueSize = stockList.size();

        for (int i = 0; i < valueSize; i++) {
            PieSlice slice1 = new PieSlice();

            slice1.setColor(stockList.get(i).getDutyColor());
            slice1.setValue(stockList.get(i).getDutyValue());
            pieList.add(slice1);

        }
        surpulsValue();
        PieSlice emptySlice = new PieSlice();
        emptySlice.setColor(Color.RED);
        emptySlice.setValue(surValue);
        pieList.add(emptySlice);

        pgView.setSlices(pieList);
    }

    private int surpulsValue() {
        int total = 100;
        for (int i = 0; i < stockList.size(); i++) {
            total -= stockList.get(i).getDutyValue();
        }
        surValue = total;

        return total;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_adjust_position: {
                Intent intent = new Intent(getActivity(), PositionAdjustActivity.class);
                intent.putExtra(PositionAdjustActivity.KEY_VIEW_TYPE, PositionAdjustActivity.VALUE_ADJUST_CONBINA);
                getActivity().startActivity(intent);
            }

                break;
            case R.id.btn_back: {

            }

                break;

            default:
                break;
        }

    }

}
