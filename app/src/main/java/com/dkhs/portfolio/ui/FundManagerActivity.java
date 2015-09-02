package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.engine.SymbolsEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.AchivementAdapter;
import com.dkhs.portfolio.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

public class FundManagerActivity extends ModelAcitivity {


    String id;
    private TextView mName;
    private TextView mDesc;
    private TextView mWinRateDayName;
    private TextView mWinRateDayvVlue;

    private String mWinRateDayValueStr;
    private String mNamStre;
    private String mAvatarMdStr;
    private ImageView mAvatarIm;


    private List<FundManagerInfoBean.AchivementsEntity> dataL = new ArrayList<>();

    private DKBaseAdapter achivementsAdapter;


    public static Intent newIntent(Context ctx, String pk) {
        Intent intent = new Intent(ctx, FundManagerActivity.class);
        intent.putExtra("pk", pk);
        return intent;
    }

    public static Intent newIntent(Context ctx, FundManagerBean fundManagerBean) {
        Intent intent = new Intent(ctx, FundManagerActivity.class);
        intent.putExtra("pk", fundManagerBean.id);
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
        setTitle(R.string.title_activity_fund_manager);
        ListView lv = (ListView) findViewById(R.id.listView);
        View view = LayoutInflater.from(FundManagerActivity.this).inflate(R.layout.layout_head_fund_manager, null);
        mName = (TextView) view.findViewById(R.id.name);
        mDesc = (TextView) view.findViewById(R.id.desc);
        mAvatarIm = (ImageView) view.findViewById(R.id.im_avatar);
        mWinRateDayName = (TextView) view.findViewById(R.id.win_rate_day_name);
        mWinRateDayvVlue = (TextView) view.findViewById(R.id.win_rate_day_value);
        lv.addHeaderView(view);
//        achivementsAdapter = new AchivementAdapter(this, dataL);
        achivementsAdapter = new DKBaseAdapter(this, dataL).buildSingleItemView(new AchivementAdapter(this, dataL));
        lv.setAdapter(achivementsAdapter);
        mWinRateDayvVlue.getScrollX();

        new SymbolsEngine().getFundManagerInfo(id, new ParseHttpListener<FundManagerInfoBean>() {
            @Override
            protected FundManagerInfoBean parseDateTask(String jsonData) {
                return DataParse.parseObjectJson(FundManagerInfoBean.class, jsonData);
            }

            @Override
            protected void afterParseData(FundManagerInfoBean object) {
                if (object != null)
                    updateUI(object);
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

        dataL.addAll(object.getAchivements());
        achivementsAdapter.notifyDataSetChanged();
        mName.setText(object.getName());
        mDesc.setText(object.getResume());
        mWinRateDayName.setVisibility(View.VISIBLE);
        ImageLoaderUtils.setHeanderImage(object.avatar_md, mAvatarIm);
        mWinRateDayvVlue.setText(object.getValueString("-win_rate_day"));
    }

}
