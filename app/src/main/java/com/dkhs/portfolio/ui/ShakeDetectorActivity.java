package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.ShakeFragment;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

public class ShakeDetectorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PortfolioApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_shake_detector);
        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        t.add(R.id.content_layout, new ShakeFragment());
        t.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(mContext, UIUtils.getResString(mContext, R.string.statistics_ShakeDetector));
        MobclickAgent.onPageStart(UIUtils.getResString(mContext, R.string.statistics_ShakeDetector));
    }

    protected void onPause() {
        StatService.onPageEnd(mContext, UIUtils.getResString(mContext, R.string.statistics_ShakeDetector));
        MobclickAgent.onPageEnd(UIUtils.getResString(mContext, R.string.statistics_ShakeDetector));
        super.onPause();
    }
}
