package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.ShakeFragment;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;

public class ShakeDetectorActivity extends BaseActivity {

    private MessageHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        PortfolioApplication.getInstance().addActivity(this);
        handler = new MessageHandler(this);
        setContentView(R.layout.activity_shake_detector);

        FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
        Bundle bunlde = new Bundle();
        t.replace(R.id.content_layout, new ShakeFragment());

        t.commitAllowingStateLoss();


    }

}
