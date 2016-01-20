package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
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
import com.dkhs.portfolio.ui.widget.MySwipeRefreshLayout;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yang.gesturepassword.GesturePasswordManager;
import com.yang.gesturepassword.ISecurityGesture;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class MyAssestsActivity extends AssestsBaseActivity implements ISecurityGesture {

    public static final int REQUESTCODE_CHECK_MOBILE = 1000;

    @ViewInject(R.id.tv_total_profit)
    private TextView tvTotalProfit;
    @ViewInject(R.id.tv_recent_profit)
    private TextView tvRecentProfit;
    @ViewInject(R.id.tv_total_assests)
    private TextView tvTotalAssests;
    @ViewInject(R.id.tv_total_assests_time)
    private TextView tvTotalAssestsTimen;

    @ViewInject(R.id.lv_assests)
    private ListView lvAssests;
    @ViewInject(R.id.swipe_container)
    private MySwipeRefreshLayout mSwipeLayout;


    private MyAssestsAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_assests);
        ViewUtils.inject(this);
        findViewById(R.id.bottom_line).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tv_title)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) findViewById(R.id.tv_title_info)).setTextColor(getResources().getColor(R.color.white));
        updateTitleBackgroud(R.color.theme_blue);
        setTitle(R.string.my_assets);
        setBackButtonDrawRes(R.drawable.btn_white_back_selector);
        tvTotalAssestsTimen.setText(String.format(getResources().getString(R.string.blank_total_assets), new SimpleDateFormat("MM-dd").format(new Date())));
        TextView addButton = getRightButton();
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_white_setting_selecter),
                null, null, null);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

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
                    case 0: //我的零钱
                        startActivity(new Intent(mContext, MyPurseActivity.class));
                        break;

                    case 1: //持仓基金
                        startActivity(new Intent(mContext, MyFundsActivity.class));
                        break;

                    case 2: //交易记录
                        startActivity(new Intent(mContext, TradeRecordActivity.class));
                        break;

                    case 3: //银行卡
                        //先判断是否绑定了手机号
//                        startActivity(new Intent(mContext, MyBankCardsActivity.class));
                        bindsListener.setLoadingDialog(mContext, false);
                        UserEngineImpl.queryThreePlatBind(bindsListener);
                        break;
                }
            }
        });
//        GesturePasswordManager.getInstance().startWatch(getApplication());
        showProgress();
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
                holder.tvAssests = (TextView) convertView.findViewById(R.id.tv_assests);
                holder.divider = convertView.findViewById(R.id.divider);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvInfoTitle.setText(titleTexts[position]);
            holder.tvInfoTitle.setCompoundDrawablesWithIntrinsicBounds(iconRes[position],
                    0, 0, 0);
            holder.divider.setVisibility(View.GONE);
            if(position == 0){
                holder.tvInfoTip.setVisibility(View.GONE);
                holder.tvAssests.setVisibility(View.VISIBLE);
                holder.tvAssests.setText(new DecimalFormat("0.00").format(wallets_available));
            }else if(position == 1){
                holder.tvInfoTip.setVisibility(View.VISIBLE);
                holder.tvAssests.setVisibility(View.VISIBLE);
                holder.tvAssests.setText(new DecimalFormat("0.00").format(fund_assests));
                holder.tvInfoTip.setText(String.format(getResources().getString(R.string.blank_funds_count), funds_count));
            }else if(position == 2){
                holder.tvInfoTip.setVisibility(View.GONE);
                holder.tvAssests.setVisibility(View.GONE);
                holder.divider.setVisibility(View.VISIBLE);
                holder.tvInfoTip.setText(String.format(getResources().getString(R.string.blank_funds_count),funds_count));

            }else if(position == 3){
                holder.tvInfoTip.setVisibility(View.VISIBLE);
                holder.tvAssests.setVisibility(View.GONE);
                holder.tvInfoTip.setText(String.format(getResources().getString(R.string.blank_bank_cards_count), bank_cards_count));

            }
            return convertView;
        }

        private class ViewHolder {
            ImageView ivImageDetail;
            TextView tvInfoTitle;
            TextView tvInfoTip;
            TextView tvAssests;
            View divider;
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

                    if (palt.getProvider().contains("mobile")) {
                        if (palt.isStatus()) {
                            //TODO 绑定了手机号，直接管理银行卡
                            startActivity(new Intent(mContext, MyBankCardsActivity.class));
                        } else {
                            //去绑定手机号
                            startActivityForResult(RLFActivity.bindPhoneIntent(MyAssestsActivity.this), REQUESTCODE_CHECK_MOBILE);
                        }
                    }
                }
            }

        }
    };

    //持仓基金数量
    private int funds_count;
    //持有卡数量
    private int bank_cards_count;
    //基金资产
    private double fund_assests;
    //零钱资产量
    private double wallets_available;

    @Override
    protected void onResume() {
        super.onResume();
//        if (GesturePasswordManager.getInstance().isGesturePasswordOpen(mContext, GlobalParams.MOBILE)) {
//            if (GlobalParams.needShowGesture) {
//                startActivityForResult(GesturePasswordActivity.verifyPasswordIntent(this, true), 100);
//                GlobalParams.needShowGesture = false;
//            }
//            onUserInteraction();
//        }
        loadData();
    }

    private void loadData(){
        new TradeEngineImpl().getMyAssests(new ParseHttpListener<String>() {
            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                mSwipeLayout.setRefreshing(false);
                dismissProgress();
            }

            @Override
            protected String parseDateTask(String jsonData) {
                return jsonData;
            }

            @Override
            protected void afterParseData(String object) {
                mSwipeLayout.setRefreshing(false);
                dismissProgress();
                if (!TextUtils.isEmpty(object)) {
                    try {
                        JSONObject json = new JSONObject(object);
                        if (json.has("assets_total")) {
                            // 总资产
                            tvTotalAssests.setText(new DecimalFormat("0.00").format(json.getDouble("assets_total")));
                        }
                        if (json.has("worth_value")) {
                            // 基金资产
                            fund_assests = json.getDouble("worth_value");
                        }
                        if (json.has("wallets_available")) {
                            // 钱包资产
                            wallets_available = json.getDouble("wallets_available");
                        }
                        if (json.has("funds_count")) {
                            // 持仓基金数量
                            funds_count = json.getInt("funds_count");
                        }
                        if (json.has("bank_cards_count")) {
                            // 银行卡数量
                            bank_cards_count = json.getInt("bank_cards_count");
                        }
                        if (json.has("income_latest")) {
                            //最新收益
                            tvRecentProfit.setText(new DecimalFormat("0.00").format(json.getDouble("income_latest")));
                        }
                        if (json.has("income_total")) {
                            //累计收益
                            tvTotalProfit.setText(new DecimalFormat("0.00").format(json.getDouble("income_total")));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_CHECK_MOBILE) {
            // TODO 管理银行卡
            PromptManager.showToast("绑定成功");
        } else if (requestCode == 100 && resultCode == 500) {
            GlobalParams.needShowGesture = true;
            manualFinish();
        }
    }

//    @Override
//    public void onUserInteraction() {
//        if (!TextUtils.isEmpty(GlobalParams.MOBILE) &&GesturePasswordManager.getInstance().isGesturePasswordOpen(mContext, GlobalParams.MOBILE)) {
//            GesturePasswordManager.getInstance().userInteraction();
//        }
//    }


    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_my_assests;
    }
}
