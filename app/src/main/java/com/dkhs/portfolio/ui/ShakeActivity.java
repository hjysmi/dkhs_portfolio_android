package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.ShakeBean;
import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by zjz on 2015/6/24.
 */
public class ShakeActivity extends ModelAcitivity  {


    @com.lidroid.xutils.view.annotation.ViewInject(R.id.timeLineTV)
    android.widget.TextView mTimeLineTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.chanceTV)
    android.widget.TextView mChanceTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.freeFlow)
    android.widget.TextView mFreeFlow;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.titleTV)
    android.widget.TextView mTitleTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.dateTV)
    android.widget.TextView mDateTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.contextTV)
    android.widget.TextView mContextTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.symbolTV)
    android.widget.TextView mSymbolTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.capitalFlowTV)
    android.widget.TextView mCapitalFlowTV;
    @com.lidroid.xutils.view.annotation.ViewInject(R.id.upRateTV)
    android.widget.TextView mUpRateTV;
    private ShakeBean mShakeBean;

    private CountDownTask countDownTask;

    public static Intent newIntent(Context context,ShakeBean shakeBean){
        Intent intent = new Intent(context, ShakeActivity.class);
        intent.putExtra("shakeBean", DataParse.objectToJson(shakeBean));
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle("谁牛小道");
        setContentView(R.layout.activity_shake);
        ViewUtils.inject(this);
        handleIntent();


    }

    /**
     *  iniView initData
     */
    public void initData() {
    }

    /**
     *  getData from net
     */
    public void getDataForNet() {
    }



    private void handleIntent() {
        if(getIntent().hasExtra("shakeBean")){
            mShakeBean=DataParse.parseObjectJson(ShakeBean.class,getIntent().getStringExtra("shakeBean"));
            countDownTask =new CountDownTask(mShakeBean.display_time*1000,1000);
            countDownTask.start();
            mChanceTV.setText(String.format(getString(R.string.the_number_of_times),mShakeBean.times_used,mShakeBean.times_left));

            mTitleTV.setText(mShakeBean.title);

            mContextTV.setText(mShakeBean.content);
            Spanny spanny = new Spanny(getString(R.string.recommend_symbol), new ForegroundColorSpan(getResources().getColor(R.color.theme_color)))
                    .append(mShakeBean.symbol.abbr_name,new ForegroundColorSpan(getResources().getColor(R.color.ma20_color)));
            mSymbolTV.setText(spanny);
            mCapitalFlowTV.setText(String.format(getString(R.string.recently_come_in), mShakeBean.capital_flow))
            ;
            mUpRateTV.setText(String.format(getString(R.string.up_precent),mShakeBean.up_rate+"%"))
            ;
            mDateTV.setText(  TimeUtils.getSimpleFormatTime(mShakeBean.modified_at));


            if(mShakeBean.coins_bonus==0){
                mFreeFlow.setVisibility(View.GONE);
            }else{

                mFreeFlow.setText(new Spanny("恭喜你获得", new ForegroundColorSpan(getResources().getColor(R.color.tag_gray)))
                        .append(" "+mShakeBean.coins_bonus+"M ",new ForegroundColorSpan(getResources().getColor(R.color.tag_red)))
                        .append("流量", new ForegroundColorSpan(getResources().getColor(R.color.tag_gray))));
            }

            mSymbolTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectStockBean selectStockBean=new SelectStockBean();
                    selectStockBean.symbol=mShakeBean.symbol.symbol;
                    selectStockBean.symbol_type="1";
                    selectStockBean.name=mShakeBean.symbol.abbr_name;
                    startActivity(StockQuotesActivity.newIntent(mActivity,selectStockBean));
                    ShakeActivity.this.finish();
                }
            });
        }
    }

    class  CountDownTask extends CountDownTimer {

        public CountDownTask(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTimeLineTV.setText("倒计时 "+(millisUntilFinished/1000)+" s");
        }

        @Override
        public void onFinish() {
            ShakeActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(countDownTask != null){
            countDownTask.cancel();
        }
    }
}
