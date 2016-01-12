package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AccountInfoBean;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.engine.WalletEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PayResEvent;
import com.dkhs.portfolio.ui.eventbus.WithDrawEvent;
import com.dkhs.portfolio.ui.fragment.BalanceChangeFragment;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.util.List;

/**
 * 主贴详情
 */
public class MyPurseActivity extends AssestsBaseActivity implements View.OnClickListener{

    private static final int WITH_DRAW_AVAIL = 0;
    private static final int WITH_DRAW_UNAVAIL = 1;
    public static final String AVAIL_AMOUNT = "avail_amount";
    public static final String MOBILE = "mobile";
    @ViewInject(R.id.tv_balance)
    private TextView mBalanceTv;
    @ViewInject(R.id.btn_balance_in)
    private TextView mBalanceInTv;
    @ViewInject(R.id.btn_balance_out)
    private TextView mBalanceOutTv;

    private boolean withDrawAvailable = false;
    private BigDecimal available;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse);
        setTitle(R.string.info_title_purse);
        ViewUtils.inject(this);
        BusProvider.getInstance().register(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new BalanceChangeFragment()).commitAllowingStateLoss();
        getAccountInfo();
    }

    private void getAccountInfo(){
        WalletEngineImpl.getMineAccountInfo(new ParseHttpListener() {
            @Override
            protected Object parseDateTask(String jsonData) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                return gson.fromJson(jsonData, AccountInfoBean.class);
            }

            @Override
            protected void afterParseData(Object object) {
                AccountInfoBean bean = (AccountInfoBean) object;
                available = bean.available;
                mBalanceTv.setText(String.valueOf(available));
            }
        });
    }

    @OnClick({R.id.btn_balance_in,R.id.btn_balance_out})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_balance_in:
                intent = new Intent(MyPurseActivity.this, RechargeActivity.class);
                break;
            case R.id.btn_balance_out:
                UserEngineImpl.queryThreePlatBind(bindsListener);
                break;
        }
        if (null != intent) {
            startActivity(intent);
        }
    }

    @Subscribe
    public void updateData(PayResEvent event){
        if(event.errCode == 0){
            ((BalanceChangeFragment)getSupportFragmentManager().findFragmentById(R.id.contentFL)).loadData();
            getAccountInfo();
        }
    }

    @Subscribe
    public void updateData(WithDrawEvent event){
        if(event.errCode == 0){
            ((BalanceChangeFragment)getSupportFragmentManager().findFragmentById(R.id.contentFL)).loadData();
            getAccountInfo();
        }
    }


    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }

        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {
            LogUtils.e(jsonData);
            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {
            if (null != entity && entity.size() > 0) {
                for(BindThreePlat bindThreePlat :entity){
                    if(bindThreePlat.getProvider().equals("mobile")&&bindThreePlat.isStatus()){//取provider为mobile中的status这个值判断当前用户是否绑定过手机号
                        withDrawAvailable = true;
                        mobile = bindThreePlat.getUsername();
                    }
                }
                Message msg = Message.obtain();
                /*if(withDrawAvailable){
                    msg.what = WITH_DRAW_AVAIL;
                }else{
                    msg.what = WITH_DRAW_UNAVAIL;
                }
                msg.setTarget();*/
                if(withDrawAvailable){
                    Intent intent =  new Intent(MyPurseActivity.this,WithDrawActivity.class);
                    intent.putExtra(AVAIL_AMOUNT,String.valueOf(available));
                    intent.putExtra(MOBILE,mobile);
                    startActivity(intent);
                }else{
                    showBoundMobileDialog();
                }
            }

        }
    }.setLoadingDialog(MyPurseActivity.this,false);

    private void showBoundMobileDialog(){
        MAlertDialog builder = PromptManager.getAlertDialog(this);
        builder.setMessage(R.string.msg_bound_mobile).setPositiveButton(R.string.btn_bound_mobile, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(RLFActivity.bindPhoneIntent(MyPurseActivity.this));
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, null);
        builder.show();
    }
}
