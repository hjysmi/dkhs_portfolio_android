package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.engine.SymbolsEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.adapter.AchivementAdapter;
import com.dkhs.portfolio.ui.widget.BenefitChartView;
import com.dkhs.portfolio.ui.widget.ExpandableTextView;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.ArrayList;
import java.util.List;

public class FundManagerActivity extends ModelAcitivity  implements AchivementAdapter.IRemoveChartViewListener{


    String id;
    private TextView mName;
    private ExpandableTextView mDesc;
    private TextView mSeniority;
    private View mWinMarket;
    private View mTotalBenfit;
    private TextView mWinRatWeek;
    private TextView mWinRatMonth;
    private TextView mWinRatYear;
    private TextView mWinRatTyear;
    private LinearLayout mProfitChart;
//    private TextView mWinRateDayName;
//    private TextView mWinRateDayvVlue;

    private String mWinRateDayValueStr;
    private String mNamStre;
    private String mAvatarMdStr;
    private ImageView mAvatarIm;
    private View mProgressView;


    private List<FundManagerInfoBean.AchivementsEntity> dataL = new ArrayList<>();

    private DKBaseAdapter achivementsAdapter;


    public static Intent newIntent(Context ctx, String pk) {
        Intent intent = new Intent(ctx, FundManagerActivity.class);
        intent.putExtra("pk", pk);
        return intent;
    }

    public static Intent newIntent(Context ctx, FundManagerBean fundManagerBean) {
        Intent intent = new Intent(ctx, FundManagerActivity.class);
        intent.putExtra("pk", String.valueOf(fundManagerBean.id));
        intent.putExtra("name", fundManagerBean.name);
        intent.putExtra("win_rate_day", fundManagerBean.win_rate_day);
        intent.putExtra("avatar_md", fundManagerBean.avatar_md);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("pk");
        setContentView(R.layout.activity_fund_manager);
        updateTitleBackgroud(R.color.theme_blue);
        setBackButtonDrawRes(R.drawable.btn_white_back_selector);
        findViewById(R.id.bottom_line).setVisibility(View.GONE);
        setTitle(R.string.title_activity_fund_manager);
        ((TextView) findViewById(R.id.tv_title)).setTextColor(getResources().getColor(R.color.white));
        ListView lv = (ListView) findViewById(R.id.listView);
        mProgressView = findViewById(R.id.my_progressbar);
        View view = LayoutInflater.from(FundManagerActivity.this).inflate(R.layout.layout_head_fund_manager, null);
        mName = (TextView) view.findViewById(R.id.name);
        mDesc = (ExpandableTextView) view.findViewById(R.id.desc);
        mDesc.setCollapseUpAble(false);
        mSeniority = (TextView) view.findViewById(R.id.tv_time_com);
        mAvatarIm = (ImageView) view.findViewById(R.id.im_avatar);
        mWinMarket = view.findViewById(R.id.ll_win_market);
        mTotalBenfit = view.findViewById(R.id.ll_total_benefit);
        mWinRatWeek = (TextView) view.findViewById(R.id.tv_week);
        mWinRatMonth = (TextView) view.findViewById(R.id.tv_month);
        mWinRatYear = (TextView) view.findViewById(R.id.tv_year);
        mWinRatTyear = (TextView) view.findViewById(R.id.tv_this_year);
        mProfitChart = (LinearLayout) view.findViewById(R.id.ll_profit_chart);
//        mWinRateDayName = (TextView) view.findViewById(R.id.win_rate_day_name);
//        mWinRateDayvVlue = (TextView) view.findViewById(R.id.win_rate_day_value);
        lv.addHeaderView(view);
//        achivementsAdapter = new AchivementAdapter(this, dataL);
        AchivementAdapter adapter = new AchivementAdapter(this, dataL);
        adapter.setListener(this);
        achivementsAdapter = new DKBaseAdapter(this, dataL).buildSingleItemView(adapter);
        lv.setAdapter(achivementsAdapter);
//        mWinRateDayvVlue.getScrollX();
        mProgressView.setVisibility(View.VISIBLE);
        new SymbolsEngine().getFundManagerInfo(id, new ParseHttpListener<FundManagerInfoBean>() {
            @Override
            protected FundManagerInfoBean parseDateTask(String jsonData) {
                jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                return DataParse.parseObjectJson(FundManagerInfoBean.class, jsonData);
            }

            @Override
            protected void afterParseData(FundManagerInfoBean object) {
                if (object != null)
                    updateUI(object);
                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(ErrorBundle errorBundle) {
                mProgressView.setVisibility(View.GONE);
                super.onFailure(errorBundle);
            }
        });

    }

    /**
     * iniView initData
     */
    public void initData() {
    }

    /**
     * getData from net
     */
    public void getDataForNet() {
    }

    private void updateUI(FundManagerInfoBean object) {
        List<FundManagerInfoBean.AchivementsEntity> curList = new ArrayList<>();
        List<FundManagerInfoBean.AchivementsEntity> hisList = new ArrayList<>();
        for(FundManagerInfoBean.AchivementsEntity entity : object.getAchivements()){
            if(entity.getEnd_date() == null){
                curList.add(entity);
            }else{
                hisList.add(entity);
            }
        }
        dataL.addAll(curList);
        dataL.addAll(hisList);
        achivementsAdapter.notifyDataSetChanged();
        mName.setText(object.getName());
        mDesc.setText(String.format(getResources().getString(R.string.blank_intro),object.getResume().trim()));
        if(TextUtils.isEmpty(object.getFund_company())){
            mSeniority.setText(String.format(getResources().getString(R.string.blank_work_time),object.work_seniority , ""));
        }else{
            mSeniority.setText(String.format(getResources().getString(R.string.blank_work_time),object.work_seniority , object.getFund_company()));
        }

//        mWinRateDayName.setVisibility(View.VISIBLE);
        ImageLoaderUtils.setHeanderImage(object.avatar_md, mAvatarIm);
//        mWinRateDayvVlue.setText(object.getValueString("-win_rate_day"));
        if(object.getManager_type() == 300){
            //需要展示总收益率
            mWinMarket.setVisibility(View.VISIBLE);
            mTotalBenfit.setVisibility(View.VISIBLE);

            mWinRatWeek.setText(TextUtils.isEmpty(object.win_rate_week) ?"—":StringFromatUtils.get2PointPercent(object.getValue("-win_rate_week")));
            mWinRatMonth.setText(TextUtils.isEmpty(object.win_rate_month) ?"—":StringFromatUtils.get2PointPercent(object.getValue("-win_rate_month")));
            mWinRatYear.setText(TextUtils.isEmpty(object.win_rate_year) ?"—":StringFromatUtils.get2PointPercent(object.getValue("-win_rate_year")));
            mWinRatTyear.setText(TextUtils.isEmpty(object.win_rate_tyear) ?"—":StringFromatUtils.get2PointPercent(object.getValue("-win_rate_tyear")));

            BenefitChartView benefitChartView = new BenefitChartView(mContext);
            benefitChartView.setCanTouable(true);
            mProfitChart.removeAllViews();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            View view = benefitChartView.getBenifitView();

            mProfitChart.addView(view, params);
            benefitChartView.drawFundIndex(object.getIndex());
        }else{
            mWinMarket.setVisibility(View.GONE);
            mTotalBenfit.setVisibility(View.GONE);
        }
    }

    @Override
    public void collapseView() {
        achivementsAdapter.notifyDataSetChanged();
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_fund_manager_detail;
    }
}
