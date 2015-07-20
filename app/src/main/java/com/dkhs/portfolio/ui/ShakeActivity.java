package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShakeBean;
import com.dkhs.portfolio.common.Spanny;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.utils.ShakeDetector;
import com.lidroid.xutils.ViewUtils;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import me.add1.common.ParcelUtils;

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

    private CutDownTask mCutDownTask;

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
            mCutDownTask=new CutDownTask(mShakeBean.display_time*1000,1000);
            mCutDownTask.start();
            mChanceTV.setText(String.format("已使用%d次机会,今日还剩%d次",mShakeBean.times_used,mShakeBean.times_left));

            mTitleTV.setText(mShakeBean.title);

            mContextTV.setText(mShakeBean.content);
            Spanny spanny = new Spanny("推荐股票:", new ForegroundColorSpan(getResources().getColor(R.color.theme_color)))
                    .append(mShakeBean.symbol.abbr_name,new ForegroundColorSpan(getResources().getColor(R.color.ma20_color)));
            mSymbolTV.setText(spanny);
            mCapitalFlowTV.setText(String.format("近5日资金流入:%S", mShakeBean.capital_flow))
            ;
            mUpRateTV.setText(String.format("上涨概率:%s",mShakeBean.up_rate))
            ;

            mSymbolTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity();
                }
            });
        }
    }

    class  CutDownTask extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CutDownTask(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTimeLineTV.setText("倒计时 "+(millisUntilFinished/1000)+"s");
        }

        @Override
        public void onFinish() {
            ShakeActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mCutDownTask != null){
            mCutDownTask.cancel();
        }
    }
}
