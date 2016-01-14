package com.dkhs.portfolio.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends ModelAcitivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle(R.string.title_activity_info);

        LinearLayout layout = (LinearLayout) findViewById(R.id.contentFL);
        String userId = null;
        UserEntity user = UserEngineImpl.getUserEntity();
        if (user != null) {
            userId = user.getId() + "";
        }

        String[] name = getResources().getStringArray(R.array.main_info_title);
        NewsforModel infoEngine;
        List<Fragment> fragmentList = new ArrayList<Fragment>();


        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        Fragment hongguanFragment = ReportListForAllFragment.getFragment(infoEngine,
                OpitionNewsEngineImple.NEWS_TODAY);
        fragmentList.add(hongguanFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("304");
        Fragment optionalInfoFragment = ReportListForAllFragment.getFragment(infoEngine,
                OpitionNewsEngineImple.NEWS_MY_OPTION);
        fragmentList.add(optionalInfoFragment);

//        infoEngine = new NewsforModel();
//        infoEngine.setUserid(userId);
//        infoEngine.setContentSubType("302");
//        Fragment celueFragment = ReportListForAllFragment
//                .getFragment(infoEngine, OpitionNewsEngineImple.NEWS_GROUP_TWO);
//        fragmentList.add(celueFragment);

        new FragmentSelectAdapter(this, name, fragmentList, layout, getSupportFragmentManager());



    }


}
