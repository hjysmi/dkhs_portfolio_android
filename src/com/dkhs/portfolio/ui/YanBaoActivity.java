package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentreportNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentreportOneList;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;

public class YanBaoActivity extends ModelAcitivity {
    private LinearLayout layout;
    private Context context;
    private Button btn;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_yanbao);
        setTitle(getResources().getString(R.string.report));
        context = this;
        initView();
        initList();
    }

    private void initView() {
        layout = (LinearLayout) findViewById(R.id.yanbao_layout);
        // btn = getRightButton();
        // btn.setBackgroundResource(R.drawable.ic_search_title);
    }

    private void initList() {
        UserEntity user;
        String userId = null;
        try {
            user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
            if (user != null) {
                if (!TextUtils.isEmpty(user.getAccess_token())) {
                    user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                }
                userId = user.getId() + "";
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] name = getResources().getStringArray(R.array.yanbao_items);
        NewsforImpleEngine vo;
        List<Fragment> frag = new ArrayList<Fragment>();
        
        vo = new NewsforImpleEngine();
        vo.setUserid(userId);
        vo.setContentSubType("304");
        Fragment f1 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP);
        frag.add(f1);
        vo = new NewsforImpleEngine();
        vo.setUserid(userId);
        vo.setContentSubType("303");
        Fragment f2 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP);
        vo = new NewsforImpleEngine();
        vo.setUserid(userId);
        vo.setContentSubType("302");
        Fragment f3 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        frag.add(f3);
        vo = new NewsforImpleEngine();
        vo.setUserid(userId);
        vo.setContentSubType("301");
        Fragment f4 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        frag.add(f4);
        new FragmentSelectAdapter(context, name, frag, layout, getSupportFragmentManager());
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(this);
    }
}
