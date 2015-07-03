package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShakeBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.ShakeEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.ShakeActivity;
import com.dkhs.portfolio.utils.ShakeDetector;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShakeFragment extends  VisiableLoadFragment implements ShakeDetector.Listener{



    @ViewInject(R.id.tv_title)
    TextView mTvtitle;
    @ViewInject(R.id.shakeIv)
    ImageView mShakeIv;
    @ViewInject(R.id.ribbonIV)
    ImageView ribbonIV;
    private AnimationDrawable animationDrawable;
    private AnimationDrawable mLoadingRibbonAD;

    private boolean getData=false;
    private ShakeDetector sd;
    private SensorManager sensorManager;

    public ShakeFragment() {
    }


    WeakHandler uiHandler=new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case 0:

                    //手动画停止
                    animationDrawable.stop();
                    if(!getData){
                        mLoadingRibbonAD.stop();
                    }

                    break;
                case 1:
                    Vibrator vibrator= (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(800);

                    if (!animationDrawable.isRunning()) {
                        //彩带动画停止
                        mLoadingRibbonAD.stop();
                    }
                    break;
                case 3:

                    if(msg.obj != null) {
                        mLoadingRibbonAD.stop();
                        gotoShakeActivity((ShakeBean) msg.obj);
                    }
                    break;
            }

             return false;
        }
    });

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_shakes;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animationDrawable= (AnimationDrawable) getResources().getDrawable(R.drawable.shake_animation_list);
        mLoadingRibbonAD= (AnimationDrawable) getResources().getDrawable(R.drawable.loading_ribbon_animation_list);
              mShakeIv.setImageDrawable(animationDrawable);
        ribbonIV.setImageDrawable(mLoadingRibbonAD);
        animationDrawable.stop();
        mLoadingRibbonAD.stop();
        mShakeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDataForNet();

            }
        });
         sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
         sd = new ShakeDetector(this);

    }

    @Override
    public void requestData() {
    }


    /**
     *  getData from net
     */
    public void getDataForNet() {
        //保证一次只有一次请求

        if(getData ){
            return;
        }
        getData=true;
        animationDrawable.start();
        uiHandler.sendEmptyMessageDelayed(0,400*4);
        mLoadingRibbonAD.start();
        ShakeEngineImpl.getShakeInfo(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return ShakeBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                //获取数据

                if(object != null){

                    Message message=new Message();
                    message.what=3;
                    message.obj=object;
                    uiHandler.sendMessage(message);

                }
            }
            @Override
            public void requestCallBack() {
                super.requestCallBack();
                onFinish();
            }

            private void onFinish() {
                getData=false;

                uiHandler.sendEmptyMessage(1);
            }
        });
    }


    @Override
    public void onViewShow() {
        if(sd !=null)
        sd.start(sensorManager);
        super.onViewShow();
    }

    @Override
    public void onViewHide() {
        if(sd !=null)
            sd.stop();
        super.onViewHide();
    }

    private void gotoShakeActivity(ShakeBean object) {


        startActivitySlideFormBottomAnim(ShakeActivity.newIntent(mActivity, object));
    }


    @Override
    public void hearShake() {
        getDataForNet();
    }


}
