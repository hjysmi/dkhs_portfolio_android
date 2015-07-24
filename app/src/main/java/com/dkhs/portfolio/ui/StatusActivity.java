package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.DKHSTextView;

/**
 * Created by zhangcm on 2015/7/22.
 */
public class StatusActivity extends ModelAcitivity{

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_status);
        initView();
    }

    private void initView() {
        DKHSTextView tvContent = (DKHSTextView) findViewById(R.id.tv_content);
    }
}
