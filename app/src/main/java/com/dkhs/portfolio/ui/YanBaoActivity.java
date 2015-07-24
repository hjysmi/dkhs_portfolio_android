package com.dkhs.portfolio.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class YanBaoActivity extends ModelAcitivity {
    private LinearLayout layout;
    private Context context;
    private Button btn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hadFragment();
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
        String userId = null;
        UserEntity user = UserEngineImpl.getUserEntity();
        if (user != null) {
            userId = user.getId() + "";
        }

        String[] name = getResources().getStringArray(R.array.yanbao_items);
        NewsforModel vo;
        List<Fragment> frag = new ArrayList<Fragment>();

        vo = new NewsforModel();
        vo.setUserid(userId);
        vo.setContentSubType("304");
        Fragment f1 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP);
        frag.add(f1);
        vo = new NewsforModel();
        vo.setUserid(userId);
        vo.setContentSubType("303");
        Fragment f2 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP);
        vo = new NewsforModel();
        vo.setUserid(userId);
        vo.setContentSubType("302");
        Fragment f3 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        frag.add(f3);
        vo = new NewsforModel();
        vo.setUserid(userId);
        vo.setContentSubType("301");
        Fragment f4 = ReportListForAllFragment.getFragment(vo, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        frag.add(f4);
        new FragmentSelectAdapter(context, name, frag, layout, getSupportFragmentManager());
    }

}
