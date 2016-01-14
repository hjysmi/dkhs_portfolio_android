package com.dkhs.portfolio.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.dkhs.portfolio.ui.fragment.ReportListForAllFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends ModelAcitivity implements FragmentSelectAdapter.OnPageSelectedListener {

    private TextView mBtnrefresh;
    private List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle(R.string.title_activity_info);
        BusProvider.getInstance().register(this);
        mBtnrefresh = getRightButton();
        mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                null, null, null);
        mBtnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((View.OnClickListener) fragmentList.get(selectedPosition)).onClick(v);
            }
        });
        LinearLayout layout = (LinearLayout) findViewById(R.id.contentFL);
        String userId = null;
        UserEntity user = UserEngineImpl.getUserEntity();
        if (user != null) {
            userId = user.getId() + "";
        }

        String[] name = getResources().getStringArray(R.array.main_info_title);
        NewsforModel infoEngine;
        fragmentList = new ArrayList<>();


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

        FragmentSelectAdapter adapter = new FragmentSelectAdapter(this, name, fragmentList, layout, getSupportFragmentManager());

        adapter.setOnPageSelectedListener(this);


    }

    @Subscribe
    public void rotateRefreshButton(RotateRefreshEvent rotateRefreshEvent) {
        mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing),
                null, null, null);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_around_center_point);
        mBtnrefresh.startAnimation(animation);
    }

    @Subscribe
    public void stopRefreshAnimation(StopRefreshEvent stopRefreshEvent) {
        mBtnrefresh.clearAnimation();
        mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                null, null, null);
    }

    private int selectedPosition;
    @Override
    public void onPageSelected(int position) {
        this.selectedPosition = position;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);

    }
}
