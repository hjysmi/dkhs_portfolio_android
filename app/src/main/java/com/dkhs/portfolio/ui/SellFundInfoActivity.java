package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.FundTradeInfo;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.MyFundsEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zhangcm on 2015/9/24.15:56
 */
public class SellFundInfoActivity extends ModelAcitivity {

    @ViewInject(R.id.tv_fund_name)
    private TextView tv_fund_name;
    @ViewInject(R.id.tv_trade_status)
    private TextView tv_trade_status;
    @ViewInject(R.id.tv_trade_value)
    private TextView tv_trade_value;
    @ViewInject(R.id.tv_trade_time)
    private TextView tv_trade_time;
    @ViewInject(R.id.tv_trade_no)
    private TextView tv_trade_no;
    @ViewInject(R.id.tv_sell_rate)
    private TextView tv_sell_rate;
    @ViewInject(R.id.tv_trade_shares)
    private TextView tv_trade_shares;
    @ViewInject(R.id.tv_info_tip_content1)
    private TextView tv_info_tip_content1;
    @ViewInject(R.id.tv_info_tip_content2)
    private TextView tv_info_tip_content2;
    @ViewInject(R.id.iv_info_tip2)
    private ImageView iv_info_tip2;
    @ViewInject(R.id.rl_fund_info)
    private RelativeLayout rl_fund_info;

    private static String TRADE_ID = "trade_id";
    private String trade_id;
    public static Intent getFundInfoIntent(Context context, String trade_id){
        Intent intent = new Intent(context, SellFundInfoActivity.class);
        intent.putExtra(TRADE_ID, trade_id);
        return  intent;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_sell_fund_info);
        ViewUtils.inject(this);
        setTitle(R.string.sell_fund_info);
        rl_fund_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FundDetailActivity.newIntent(mContext, SelectStockBean.copy(mFund)));
            }
        });
        initData();
    }
    private FundQuoteBean mFund;
    private void initData() {
        ParseHttpListener<FundTradeInfo> listener = new ParseHttpListener<FundTradeInfo>() {
            @Override
            protected FundTradeInfo parseDateTask(String jsonData) {
                FundTradeInfo info = null;
                try{
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    info = DataParse.parseObjectJson(FundTradeInfo.class, jsonData);
                }catch (Exception e){

                }
                return info;
            }

            @Override
            protected void afterParseData(FundTradeInfo info) {
                if(info != null){
                    mFund = info.getFund();
                    tv_fund_name.setText(String.format(getResources().getString(R.string.blank_fund_name), info.getFund().getAbbrName(), info.getFund().getSymbol()));
                    tv_trade_no.setText(info.getAllot_no());
                    tv_trade_time.setText(TimeUtils.getDaySecondString(info.getApply_date()));
                    tv_info_tip_content1.setText(TimeUtils.getDateString(info.getApply_date()));
                    tv_trade_shares.setText(String.format(getResources().getString(R.string.blank_dollar), info.getAmount()));
                    tv_trade_value.setText(String.format(getResources().getString(R.string.blank_shares), info.getShares()));
                    if(info.getStatus() == 0){
                        iv_info_tip2.setImageResource(R.drawable.trade_unsuc);
                        tv_trade_shares.setText(R.string.tobe_confirmed);
                        tv_trade_status.setText(R.string.entrust_suc);
                        tv_info_tip_content2.setText(R.string.confirm_amount_suc);
                    }else if(info.getStatus() == 1){
                        tv_trade_status.setText(R.string.trade_suc);
                        iv_info_tip2.setImageResource(R.drawable.trade_suc);
                        tv_info_tip_content2.setText(R.string.confirm_amount_suc);
                    }else if(info.getStatus() == 2){
                        tv_trade_status.setText(R.string.trade_fail);
                        iv_info_tip2.setImageResource(R.drawable.trade_suc);
                        tv_info_tip_content2.setText(R.string.confirm_amount_fail);
                    }
                    tv_sell_rate.setText(StringFromatUtils.get2PointPercent((float) (info.getDiscount_rate() * info.getFare_ratio())));
                }
            }
        };
        trade_id = getIntent().getExtras().getString(TRADE_ID);
        new MyFundsEngineImpl().getFundsTradesInfo(trade_id, listener.setLoadingDialog(mContext));
    }

}
