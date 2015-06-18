package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FlowPackageBean;
import com.dkhs.portfolio.engine.FlowExchangeEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.FlowExPackAdatper;
import com.dkhs.portfolio.ui.widget.HorizontalListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zjz on 2015/6/18.
 */
public class ExchangeActivity extends ModelAcitivity {


    @ViewInject(R.id.HorizontalListView)
    private HorizontalListView mHlvFlow;
    private FlowExPackAdatper packAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_flow_exchange);
        setContentView(R.layout.activity_flow_exchange);
        ViewUtils.inject(this);

        FlowExchangeEngine.packages(overViewListener);
    }


    ParseHttpListener overViewListener = new ParseHttpListener<FlowPackageBean>() {
        @Override
        protected FlowPackageBean parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(FlowPackageBean.class, jsonData);
        }

        @Override
        protected void afterParseData(FlowPackageBean object) {
            if (null != object) {
                updateUI(object);

            }

        }
    };


    private void updateUI(final FlowPackageBean packBean) {
        packAdapter = new FlowExPackAdatper(this, packBean.getOppackages());
        packAdapter.setMaxAmount(packBean.getMax_amount());
        mHlvFlow.setAdapter(packAdapter);
        mHlvFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (packBean.getOppackages().get(position).getAmount() > packBean.getMax_amount()) {
                    return;
                }

                packAdapter.setSelectedIndex(position);
                packAdapter.notifyDataSetChanged();
            }
        });
    }
}
