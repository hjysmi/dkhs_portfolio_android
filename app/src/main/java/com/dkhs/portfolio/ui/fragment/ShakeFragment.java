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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ShakeBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.ShakeEngineImpl;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.ShakeActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.LockMenuEvent;
import com.dkhs.portfolio.ui.eventbus.UnLockMenuEvent;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.ShakeDetector;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShakeFragment extends VisiableLoadFragment implements ShakeDetector.Listener {

    public static final String TAG = "ShakeFragment";

    @ViewInject(R.id.tv_title)
    TextView mTvtitle;

    @ViewInject(R.id.activitySloganTv)
    ImageView ivSlogan;
    @ViewInject(R.id.shakeIv)
    ImageView mShakeIv;
    @ViewInject(R.id.ribbonIV)
    ImageView ribbonIV;
    @ViewInject(R.id.btn_back)
    TextView backBtn;
    private AnimationDrawable animationDrawable;
    private AnimationDrawable mLoadingRibbonAD;

    private boolean getData = false;
    private ShakeDetector sd;
    private SensorManager sensorManager;
    private Object mSuccessObject;

    public ShakeFragment() {
    }


    WeakHandler uiHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0:

                    //手动画停止
                    animationDrawable.stop();
                    mShakeIv.setImageResource(R.drawable.shake_02);
                    if (!getData) {
                        vibrator();
                        mLoadingRibbonAD.stop();
                    }

                    if (mSuccessObject != null) {
                        //跳转
                        mLoadingRibbonAD.stop();
                        gotoShakeActivity(mSuccessObject);
                    }
                    break;
                case 1:

                    if (!animationDrawable.isRunning()) {
                        //彩带动画停止
                        vibrator();
                        mLoadingRibbonAD.stop();
                    }
                    break;
                case 3:
                    if (msg.obj != null) {
                        mSuccessObject = msg.obj;
                        if (!animationDrawable.isRunning()) {
                            mLoadingRibbonAD.stop();
                            gotoShakeActivity(mSuccessObject);
                        }
                    }
                    break;
            }
            return false;
        }
    });

    private void vibrator() {
        Vibrator vibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_shakes;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.shake_animation_list);
        mLoadingRibbonAD = (AnimationDrawable) getResources().getDrawable(R.drawable.loading_ribbon_animation_list);
        mShakeIv.setImageDrawable(animationDrawable);
        ribbonIV.setImageDrawable(mLoadingRibbonAD);
        animationDrawable.stop();
        mLoadingRibbonAD.stop();

        mShakeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDataForNet();
                hearShake();
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishShake();
                    }
                }, 2000);
            }
        });
        mShakeIv.setEnabled(false);

        sensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        sd = new ShakeDetector(this);

        backBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_back_selector),
                null, null, null);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        onViewShow();
    }

    @Override
    public void requestData() {
    }


    /**
     * getData from net
     */
    public void getDataForNet() {
        //保证一次只有一次请求

        if (getData || animationDrawable.isRunning()) {
            return;
        }
        vibrator();
        getData = true;
        mShakeIv.setImageDrawable(animationDrawable);
        animationDrawable.start();

        mLoadingRibbonAD.start();
        BusProvider.getInstance().post(new LockMenuEvent());
        ShakeEngineImpl.getShakeInfo(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return ShakeBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                //获取数据
                if (object != null) {
                    finish();
                    Message message = new Message();
                    message.what = 3;
                    message.obj = object;
                    uiHandler.sendMessage(message);

                }
            }

            @Override
            public void requestCallBack() {
                super.requestCallBack();

            }

            private void finish() {
                getData = false;
                uiHandler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(final int errCode, final String errMsg) {
                if (errCode == 401) {
                    ErrorBundle errorBundle = new ErrorBundle();
                    errorBundle.setErrorCode(401);
                    errorBundle.setErrorMessage(mActivity.getString(R.string.shake_err_no_login));
                    mSuccessObject = errorBundle;
                } else {

                    failure(errCode, errMsg);
                }

                finish();
            }

            public void failure(int errCode, String errMsg) {

                if (errCode == 0) {
                    PromptManager.showToast(R.string.message_timeout);
                } else if (errCode == 500 || errCode == 404) { // 服务器内部错误
                    PromptManager.showToast(R.string.message_server_error);
                } else if (errCode == 777) { // 服务器正确响应，错误参数需要提示用户
                    mSuccessObject = parseToErrorBundle(errMsg);
                }
            }

            private ErrorBundle parseToErrorBundle(String errMsg) {
                ErrorBundle errorBundle = new ErrorBundle();
                try {
                    JSONObject errorJson = new JSONObject(errMsg);
                    if (errorJson.has("errors")) {
                        JSONObject eJObject = errorJson.optJSONObject("errors");
                        Iterator keyIter = eJObject.keys();
                        String key = "";
                        while (keyIter.hasNext()) {
                            key = (String) keyIter.next();
                            break;
                        }
                        JSONArray eJArray = eJObject.optJSONArray(key);
                        if (eJArray.length() > 0) {
                            String errorTExt = eJArray.getString(0);

                            LogUtils.e("setErrorMessage : " + errorTExt);
                            errorBundle.setErrorMessage(errorTExt);
                            errorBundle.setErrorKey(key);
//                            PromptManager.showToast(errorTExt);

                        }
                    }

                } catch (JSONException e) {
                    errorBundle.setErrorMessage("请求数据失败");
                    e.printStackTrace();
                }
                return errorBundle;
            }
        });
    }


    @Override
    public void onViewShow() {
        if (sd != null)
            sd.start(sensorManager);
        super.onViewShow();
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());

    }

    @Override
    public void onViewHide() {
        if (sd != null)
            sd.stop();
        super.onViewHide();
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    private void gotoShakeActivity(Object object) {

        if (object instanceof ShakeBean) {

            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // FIXBug:java.lang.IllegalStateException: Fragment ShakeFragment{429d19b8} not attached to Activity
                    //before fix
//                    startActivitySlideFormBottomAnim(ShakeActivity.newIntent(mActivity, (ShakeBean) mSuccessObject));
                    //after fix
                    if (isAdded()) {
                        getActivity().startActivity(ShakeActivity.newIntent(mActivity, (ShakeBean) mSuccessObject));
                        UIUtils.setOverridePendingSlideFormBottomAnim(mActivity);
                        mSuccessObject = null;
                    }
                }
            }, 200);

        } else if (object instanceof ErrorBundle) {
            String errorKey = ((ErrorBundle) object).getErrorKey();
            if (!TextUtils.isEmpty(errorKey) && errorKey.equals("times_invalid")) {

                ivSlogan.setImageResource(R.drawable.bg_after_shake);
            } else {
                PromptManager.showToast(((ErrorBundle) object).getErrorMessage());
            }

            this.mSuccessObject = null;
        }

    }


    @Override
    public void hearShake() {
        getDataForNet();


    }

    @Override
    public void finishShake() {
        uiHandler.sendEmptyMessage(0);
        BusProvider.getInstance().post(new UnLockMenuEvent());

    }


}
