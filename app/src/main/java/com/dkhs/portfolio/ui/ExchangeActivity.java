package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FlowPackageBean;
import com.dkhs.portfolio.engine.FlowExchangeEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.FlowExPackAdatper;
import com.dkhs.portfolio.ui.widget.HorizontalListView;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

/**
 * Created by zjz on 2015/6/18.
 */
public class ExchangeActivity extends ModelAcitivity {


    @ViewInject(R.id.HorizontalListView)
    private HorizontalListView mHlvFlow;
    private FlowExPackAdatper packAdapter;
    private FlowPackageBean packageBean;


    @ViewInject(R.id.tv_exchange_maxtip)
    private TextView tvMaxtip;
    @ViewInject(R.id.tv_prephone_num)
    private TextView tvPaidPhone;

    @ViewInject(R.id.exchange)
    private Button btnExchange;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_flow_exchange);
        setContentView(R.layout.activity_flow_exchange);
        ViewUtils.inject(this);

        tvMaxtip.setText(getString(R.string.max_flow_tip, 0));
        FlowExchangeEngine.packages(overViewListener);
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExchangeDialog();
            }
        });
    }


    ParseHttpListener overViewListener = new ParseHttpListener<FlowPackageBean>() {
        @Override
        protected FlowPackageBean parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(FlowPackageBean.class, jsonData);
        }

        @Override
        protected void afterParseData(FlowPackageBean object) {
            if (null != object) {
                packageBean = object;
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

                btnExchange.setEnabled(true);
                packAdapter.setSelectedIndex(position);
                packAdapter.notifyDataSetChanged();
            }
        });

        tvMaxtip.setText(getString(R.string.max_flow_tip, packBean.getMax_amount()));
        tvPaidPhone.setText(packBean.getMobile());
    }


    //‘确认用积分兑换1000M流量？ 确认后请求API，提示‘兑换成功’或‘兑换失败’，返回流量包页面
    public void showExchangeDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(this);
        builder.setTitle(R.string.tips);
        builder.setMessage(getString(R.string.dialog_exchange_message, packageBean.getOppackages().get(packAdapter.getSelectedIndex()).getAmount()));
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                exchangePost();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }


    private void exchangePost() {
        btnExchange.setEnabled(false);
        FlowExchangeEngine.recharge(packageBean.getOppackages().get(packAdapter.getSelectedIndex()).getAmount(), exchangeListener.setLoadingDialog(this));

    }


    ParseHttpListener exchangeListener = new ParseHttpListener<Boolean>() {
        @Override
        protected Boolean parseDateTask(String jsonData) {
            boolean isSuccess = false;
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                isSuccess = jsonObject.optBoolean("status");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isSuccess;
        }

        @Override
        protected void afterParseData(Boolean object) {
            btnExchange.setEnabled(true);
            if (null != object) {
                showExchangeToast(object);
            }

        }


        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            btnExchange.setEnabled(true);
//            showExchangeToast(false);
        }
    };


    private void showExchangeToast(boolean isSuccess) {
        if (isSuccess) {
            PromptManager.showSuccessToast(R.string.exchange_success);
        } else {
            PromptManager.showCancelToast(R.string.exchange_failure);
        }

    }


}
