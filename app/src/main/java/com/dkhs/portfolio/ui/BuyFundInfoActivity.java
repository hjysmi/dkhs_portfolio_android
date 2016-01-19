package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
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
 * Created by zhangcm on 2015/9/24.15:57
 */
public class BuyFundInfoActivity extends ModelAcitivity {

    @ViewInject(R.id.tv_fund_name)
    private TextView tv_fund_name;
    @ViewInject(R.id.tv_trade_status)
    private TextView tv_trade_status;
    @ViewInject(R.id.tv_trade_shares)
    private TextView tv_trade_shares;
    @ViewInject(R.id.tv_trade_value)
    private TextView tv_trade_value;
    @ViewInject(R.id.tv_trade_time)
    private TextView tv_trade_time;
    @ViewInject(R.id.tv_trade_no)
    private TextView tv_trade_no;
    @ViewInject(R.id.tv_trade_rate)
    private TextView tv_trade_rate;
    @ViewInject(R.id.tv_info_tip_content0)
    private TextView tv_info_tip_content0;
    @ViewInject(R.id.tv_info_tip1)
    private TextView tv_info_tip1;
    @ViewInject(R.id.iv_info_tip1)
    private ImageView iv_info_tip1;
    @ViewInject(R.id.iv_info_tip2)
    private ImageView iv_info_tip2;
    @ViewInject(R.id.tv_info_tip_content2)
    private TextView tv_info_tip_content2;
    @ViewInject(R.id.rl_fund_info)
    private RelativeLayout rl_fund_info;
    @ViewInject(R.id.rl_bottom)
    private RelativeLayout rl_bottom;
    @ViewInject(R.id.btn_complete)
    private Button btn_complete;

    private static String TRADE_ID = "trade_id";
    private static String IS_FROM_BUY_FUND = "is_from_buy_fund";
    private String trade_id;
    private boolean isFromBuyFund;
    public static Intent getFundInfoIntent(Context context, String fund_id,boolean isFromBuyFund){
        Intent intent = new Intent(context, BuyFundInfoActivity.class);
        intent.putExtra(TRADE_ID, fund_id);
        intent.putExtra(IS_FROM_BUY_FUND, isFromBuyFund);
        return  intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_buy_fund_info);
        ViewUtils.inject(this);
        setTitle(R.string.buy_fund_info);
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
                    tv_info_tip_content0.setText(TimeUtils.getDateString(info.getApply_date()));
                    tv_trade_value.setText(String.format(getResources().getString(R.string.blank_dollar), info.getAmount()));
                    tv_trade_shares.setText(String.format(getResources().getString(R.string.blank_shares),info.getShares()));
                    if(info.getStatus() == 0){
                        iv_info_tip1.setImageResource(R.drawable.intrade_unsuc);
                        iv_info_tip2.setImageResource(R.drawable.trade_unsuc);
                        tv_trade_shares.setText(R.string.tobe_confirmed);
                        tv_trade_status.setText(R.string.entrust_suc);
                        tv_info_tip_content2.setText(R.string.confirm_shares_suc);
                    }else if(info.getStatus() == 1){
                        tv_trade_status.setText(R.string.trade_suc);
                        iv_info_tip1.setImageResource(R.drawable.intrade_suc);
                        iv_info_tip2.setImageResource(R.drawable.trade_suc);
                        tv_info_tip_content2.setText(R.string.confirm_shares_suc);
                    }else if(info.getStatus() == 2){
                        tv_trade_status.setText(R.string.trade_fail);
                        iv_info_tip1.setImageResource(R.drawable.intrade_suc);
                        iv_info_tip2.setImageResource(R.drawable.trade_suc);
                        tv_info_tip_content2.setText(R.string.confirm_shares_fail);
                    }else if(info.getStatus() == 3){
                        tv_trade_status.setText(R.string.pay_suc);
                        iv_info_tip2.setImageResource(R.drawable.trade_unsuc);
                        iv_info_tip1.setImageResource(R.drawable.intrade_suc);
                        tv_info_tip_content2.setText(R.string.confirm_shares_suc);
                    }else if(info.getStatus() == 4){
                        tv_trade_status.setText(R.string.pay_fail);
                        rl_bottom.setVisibility(View.GONE);
                        tv_info_tip1.setText(R.string.buy_fund_info_tip1_fail);
                        iv_info_tip1.setImageResource(R.drawable.trade_suc);
                    }
                    tv_trade_rate.setText(StringFromatUtils.get2PointPercent((float) (info.getDiscount_rate() * info.getFare_ratio())));
                }
            }
        };
        trade_id = getIntent().getExtras().getString(TRADE_ID);
        isFromBuyFund = getIntent().getExtras().getBoolean(IS_FROM_BUY_FUND);
        btn_complete.setVisibility(isFromBuyFund?View.VISIBLE:View.INVISIBLE);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualFinish();
            }
        });
        new MyFundsEngineImpl().getFundsTradesInfo(trade_id, listener.setLoadingDialog(mContext));
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_buy_fund_info;
    }

}
