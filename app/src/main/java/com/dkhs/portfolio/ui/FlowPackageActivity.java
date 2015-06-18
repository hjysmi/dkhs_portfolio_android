package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FlowOverViewBean;
import com.dkhs.portfolio.engine.FlowExchangeEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.FlowPackAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowPackageActivity extends ModelAcitivity implements View.OnClickListener {

    @ViewInject(R.id.lv_flow)
    private ListViewEx lvFlowAction;


    @ViewInject(R.id.tv_point_count)
    private TextView tvPointCout;

    @ViewInject(R.id.tv_flow_count)
    private TextView tvFlowCout;


    private FlowOverViewBean overViewBean;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_flow_package);
        setTitle(R.string.flow_package);
        ViewUtils.inject(this);

        lvFlowAction.setAdapter(new FlowPackAdapter(this));



    }

    @Override
    protected void onResume() {
        super.onResume();
        FlowExchangeEngine.overview(overViewListener);
    }

    public void updateUI(FlowOverViewBean overViewBean) {
        tvFlowCout.setText(overViewBean.getTraffic_amount() + "");
        tvPointCout.setText(getResources().getString(R.string.flow_point, overViewBean.getBalance()));
        FlowPackAdapter flowPackAdapter = (FlowPackAdapter) lvFlowAction.getAdapter();
        flowPackAdapter.setOverViewBean(overViewBean);
    }


    ParseHttpListener overViewListener = new ParseHttpListener<FlowOverViewBean>() {
        @Override
        protected FlowOverViewBean parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(FlowOverViewBean.class, jsonData);
        }

        @Override
        protected void afterParseData(FlowOverViewBean object) {
            if (null != object) {
//                overViewBean = object;
                updateUI(object);

            }


        }
    };

    @OnClick({R.id.layout_flowcount})
    public void onClick(View v) {
        startActivity(new Intent(this, ExchangeActivity.class));
    }


}
