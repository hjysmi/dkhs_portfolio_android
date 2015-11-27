package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.ShakeFragment;

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

}
