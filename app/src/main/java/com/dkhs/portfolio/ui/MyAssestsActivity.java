package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yang.gesturepassword.GesturePasswordManager;
import com.yang.gesturepassword.ISecurityGesture;

import org.json.JSONObject;
import org.parceler.transfuse.annotations.OnActivityResult;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class MyAssestsActivity extends ModelAcitivity implements ISecurityGesture{

    public static final int REQUESTCODE_CHECK_MOBILE = 1000;

    @ViewInject(R.id.tv_total_profit)
    private TextView tvTotalProfit;
    @ViewInject(R.id.tv_recent_profit)
    private TextView tvRecentProfit;
    @ViewInject(R.id.tv_total_assests)
    private TextView tvTotalAssests;

    @ViewInject(R.id.lv_assests)
    private ListView lvAssests;

    private MyAssestsAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_assests);
        ViewUtils.inject(this);
        ((TextView) findViewById(R.id.tv_title)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) findViewById(R.id.tv_title_info)).setTextColor(getResources().getColor(R.color.white));
        updateTitleBackgroud(R.color.theme_blue);
        setTitle(R.string.my_assets);
        setBackButtonDrawRes(R.drawable.btn_white_back_selector);
        TextView addButton = getRightButton();
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_white_setting_selecter),
                null, null, null);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 资产设置
//                ParseHttpListener<Boolean> isTradePwdSetListener = new ParseHttpListener<Boolean>() {
//                    @Override
//                    protected Boolean parseDateTask(String jsonData) {
//                        try{
//                            JSONObject json = new JSONObject(jsonData);
//                            if(json.has("status")){
//                                return json.getBoolean("status");
//                            }
//
//                        }catch (Exception e){
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void afterParseData(Boolean object) {
//                        if(null != object){
//                            if(object){
//                                //TODO 设置过交易密码
////                                startActivity(new Intent(mContext, ResetTradePasswordActivity.class));
//                                startActivity(new Intent(mContext, TradeSettingActivity.class));
//                            }else{
//                                //TODO 没设置过交易密码
//                                startActivity(TradePasswordSettingActivity.firstSetPwdIntent(mContext));
//                            }
//                        }
//                    }
//                };
//                new TradeEngineImpl().isTradePasswordSet(isTradePwdSetListener.setLoadingDialog(mContext));
                startActivity(new Intent(mContext, TradeSettingActivity.class));
            }
        });
        initIconResource();
        adapter = new MyAssestsAdapter();
        lvAssests.setAdapter(adapter);
        lvAssests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //持仓基金
                        startActivity(new Intent(mContext, MyFundsActivity.class));

                        break;

                    case 1: //交易记录

                        startActivity(new Intent(mContext, TradeRecordActivity.class));

                        break;

                    case 2: //银行卡
                        //先判断是否绑定了手机号
//                        startActivity(new Intent(mContext, MyBankCardsActivity.class));
                        bindsListener.setLoadingDialog(mContext, false);
                        UserEngineImpl.queryThreePlatBind(bindsListener);
                        break;
                }
            }
        });
        GesturePasswordManager.getInstance().startWatch(getApplication());
    }

    private String[] titleTexts = PortfolioApplication.getInstance().getResources().getStringArray(R.array.my_assests_title);
    private int[] iconRes;

    private void initIconResource() {
        TypedArray ar = mContext.getResources().obtainTypedArray(R.array.ic_assest_ids);
        int len = ar.length();
        iconRes = new int[len];
        for (int i = 0; i < len; i++) iconRes[i] = ar.getResourceId(i, 0);
        ar.recycle();
    }

    private class MyAssestsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return titleTexts.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return titleTexts[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_assests_info, null);
                holder = new ViewHolder();
                holder.ivImageDetail = (ImageView) convertView.findViewById(R.id.image_detail);
                holder.tvInfoTitle = (TextView) convertView.findViewById(R.id.tv_info_title);
                holder.tvInfoTip = (TextView) convertView.findViewById(R.id.tv_info_tip);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvInfoTitle.setText(titleTexts[position]);
            holder.tvInfoTitle.setCompoundDrawablesWithIntrinsicBounds(iconRes[position],
                    0, 0, 0);
            return convertView;
        }

        private class ViewHolder {
            ImageView ivImageDetail;
            TextView tvInfoTitle;
            TextView tvInfoTip;
        }
    }

    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }


        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {
            if (!entity.isEmpty()) {
                for (int i = 0; i < entity.size(); i++) {
                    BindThreePlat palt = entity.get(i);

                    if(palt.getProvider().contains("mobile")){
                        if(palt.isStatus()){
                            //TODO 绑定了手机号，直接管理银行卡
                            startActivity(new Intent(mContext, MyBankCardsActivity.class));
                        }else{
                            //去绑定手机号
                            startActivityForResult(RLFActivity.bindPhoneIntent(MyAssestsActivity.this), REQUESTCODE_CHECK_MOBILE);
                        }
                    }
                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(GesturePasswordManager.getInstance().isGesturePasswordOpen(mContext,GlobalParams.MOBILE)){
            if(GlobalParams.needShowGesture){
                startActivityForResult(GesturePasswordActivity.verifyPasswordIntent(this, true), 100);
                GlobalParams.needShowGesture = false;
            }
            onUserInteraction();
        }
        new TradeEngineImpl().getMyAssests(new ParseHttpListener<String>() {
            @Override
            protected String parseDateTask(String jsonData) {
                return jsonData;
            }

            @Override
            protected void afterParseData(String object) {
                if(!TextUtils.isEmpty(object)){
                    try {
                        JSONObject json = new JSONObject(object);
                        if(json.has("worth_value")) {
                            // 总资产
                            tvTotalAssests.setText(json.getString("worth_value"));
                        }
                        if(json.has("income_latest")){
                            //最新收益
                            tvRecentProfit.setText(json.getString("income_latest"));
                        }
                        if(json.has("income_total")){
                            //累计收益
                            tvTotalProfit.setText(json.getString("income_total"));
                        }
                    }catch (Exception e){

                    }
                }
            }
        }.setLoadingDialog(mContext));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE_CHECK_MOBILE){
            // TODO 管理银行卡
            PromptManager.showToast("绑定成功");
        }else if(requestCode == 100 && resultCode == 500){
            manualFinish();
        }
    }

    @Override
    public void onUserInteraction() {
        Log.i("onUserInteraction", getComponentName().toString());
        super.onUserInteraction();
        if(!TextUtils.isEmpty(GlobalParams.MOBILE)){
            GesturePasswordManager.getInstance().userInteraction();
        }
    }

}
