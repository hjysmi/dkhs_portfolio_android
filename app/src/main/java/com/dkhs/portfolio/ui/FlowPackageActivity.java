package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.FlowPackAdapter;
import com.dkhs.portfolio.ui.widget.ListViewEx;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowPackageActivity extends ModelAcitivity {

    @ViewInject(R.id.lv_flow)
    private ListViewEx lvFlowAction;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_flow_package);
        setTitle(R.string.flow_package);
        ViewUtils.inject(this);

        lvFlowAction.setAdapter(new FlowPackAdapter(this));

    }


}
