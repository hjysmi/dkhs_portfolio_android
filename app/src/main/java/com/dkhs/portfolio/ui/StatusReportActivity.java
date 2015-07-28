package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zjz on 2015/7/27.
 */
public class StatusReportActivity extends ModelAcitivity implements OnClickListener {


    @ViewInject(R.id.tv_report_user)
    private TextView tvReportUser;
    @ViewInject(R.id.tv_report_content)
    private TextView tvReportContent;

    @ViewInject(R.id.btn_report)
    private Button btnReport;
    @ViewInject(R.id.cb_report1)
    private RadioButton cbReport1;
    @ViewInject(R.id.cb_report2)
    private RadioButton cbReport2;
    @ViewInject(R.id.cb_report3)
    private RadioButton cbReport3;
    @ViewInject(R.id.cb_report4)
    private RadioButton cbReport4;
    @ViewInject(R.id.cb_report5)
    private RadioButton cbReport5;
    @ViewInject(R.id.cb_report6)
    private RadioButton cbReport6;
    @ViewInject(R.id.cb_report7)
    private RadioButton cbReport7;


    private RadioButton cbSelectChoice;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_status_report);
        setContentView(R.layout.activity_status_report);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        btnReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != cbSelectChoice && cbSelectChoice.isChecked()) {

                    PromptManager.showLToast("提交举报内容:" + cbSelectChoice.getText());
                }
            }
        });
    }


    @Override
    @OnClick({R.id.cb_report1, R.id.cb_report2, R.id.cb_report3, R.id.cb_report4, R.id.cb_report5, R.id.cb_report6, R.id.cb_report7,})
    public void onClick(View v) {
        int id = v.getId();

        if (null != cbSelectChoice && cbSelectChoice != v) {
            cbSelectChoice.setChecked(false);
        }
        cbSelectChoice = (RadioButton) v;

        if (cbSelectChoice.isChecked()) {
            btnReport.setEnabled(true);
        } else {
            btnReport.setEnabled(false);
        }
    }
}


