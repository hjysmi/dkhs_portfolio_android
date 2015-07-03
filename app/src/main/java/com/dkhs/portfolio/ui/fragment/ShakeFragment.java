package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShakeBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.ShakeEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.AdActivity;
import com.dkhs.portfolio.ui.ShakeActivity;
import com.dkhs.portfolio.utils.ShakeDetector;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShakeFragment extends  VisiableLoadFragment implements ShakeDetector.Listener{


    @ViewInject(R.id.btn_header_right)
    Button mBtnheaderright;
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
            animationDrawable.stop();

            String s=" {\"id\": 2, \"title\": \"中国联通\", \"content\": \"中国联通降低宽带资费中国联通降低宽带资费中国联通降低宽带资费\", \"symbol\": {\"symbol\": \"SZ300459\", \"abbr_name\": \"浙江金科\"}, \"capital_flow\": \"4452.12万元\", \"up_rate\": 0.0, \"display_time\": 120, \"modified_at\": \"2015-07-01T02:51:10Z\", \"coins_bonus\": 4, \"times_used\": 3, \"times_left\": 0}\n";
           gotoShakeActivity(DataParse.parseObjectJson(ShakeBean.class, s));
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
         sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
         sd = new ShakeDetector(this);
        mBtnheaderright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AdActivity.getIntent(mContext, DKHSClient.getAbsoluteUrl(DKHSUrl.Shake.getDescriptionOfActivities),false));
            }
        });
    }




    @Override
    public void requestData() {


    }





    /**
     *  getData from net
     */
    public void getDataForNet() {
        if(getData ){
            return;
        }
        getData=true;
        animationDrawable.start();
        uiHandler.sendEmptyMessageDelayed(1,400*4);
        mLoadingRibbonAD.start();
        ShakeEngineImpl.getShakeInfo(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return ShakeBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                if(object != null){
                    gotoShakeActivity((ShakeBean) object);
                }
            }

//            @Override
//            public void onFailure(int errCode, String errMsg) {
//                super.onFailure(errCode, errMsg);
//                onFinish();
//            }
//
//            @Override
//            public void onSuccess(String jsonObject) {
//                onFinish();
//                super.onSuccess(jsonObject);
//            }

            @Override
            public void requestCallBack() {
                super.requestCallBack();
                onFinish();
            }

            private void onFinish() {
                getData=false;
                mLoadingRibbonAD.stop();
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
        startActivity(ShakeActivity.newIntent(mContext, object));
    }


    @Override
    public void hearShake() {
        getDataForNet();
    }


}
