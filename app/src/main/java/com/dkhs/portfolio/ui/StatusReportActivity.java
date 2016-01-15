package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zjz on 2015/7/27.
 */
public class StatusReportActivity extends ModelAcitivity implements OnClickListener {


    private static final String ARGUMENT_REPORTID = "argument_reportId";
    private static final String ARGUMENT_USERNAME = "argument_username";
    private static final String ARGUMENT_TYPE = "argument_type";
    private static final String ARGUMENT_CONTENT = "argument_content";

    @ViewInject(R.id.tv_report_user)
    private TextView tvReportUser;


    @ViewInject(R.id.tv_report_content)
    private TextView tvReportContent;//最多显示200个字符

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

    private String mUserName;
    private String mContent;
    private String mReportId;
    private int mType;

    public static Intent getIntent(Context context, String reportId, String username, String content,int type) {
        Intent intent = new Intent(context, StatusReportActivity.class);
        intent.putExtra(ARGUMENT_REPORTID, reportId);
        intent.putExtra(ARGUMENT_USERNAME, username);
        intent.putExtra(ARGUMENT_CONTENT, content);
        intent.putExtra(ARGUMENT_TYPE,type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_status_report);
        setContentView(R.layout.activity_status_report);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        ViewUtils.inject(this);
        initView();
    }

    private void handleExtras(Bundle extras) {
        mUserName = extras.getString(ARGUMENT_USERNAME);
        mContent = extras.getString(ARGUMENT_CONTENT);
        mReportId = extras.getString(ARGUMENT_REPORTID);
        mType = extras.getInt(ARGUMENT_TYPE);
    }

    private void initView() {
        tvReportUser.setText(getString(R.string.title_status_report) + "<a href=\"\">@" + mUserName + "</a>" + getString(R.string.report_user));
        if (!TextUtils.isEmpty(mContent) && mContent.length() > 200) {
            mContent = mContent.substring(0, 199) + "……";
        }

        tvReportContent.setText("<a href=\"\">@" + mUserName + "</a>：" + mContent);
        btnReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != cbSelectChoice && cbSelectChoice.isChecked()) {
                    subReport();

                }
            }
        });
    }


    ParseHttpListener reportListener = new ParseHttpListener() {
        @Override
        protected Object parseDateTask(String jsonData) {
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.showSuccessToast(R.string.report_success);
            finish();
        }
    };

    private void subReport() {
        //您的举报信息已提交，感谢您的支持！
        int id = cbSelectChoice.getId();
        int abuse_type = -1;
        switch (id) {
            case R.id.cb_report1:
                abuse_type = 0;
                break;
            case R.id.cb_report2:
                abuse_type = 1;
                break;
            case R.id.cb_report3:
                abuse_type = 2;
                break;
            case R.id.cb_report4:
                abuse_type = 3;
                break;
            case R.id.cb_report5:
                abuse_type = 4;
                break;
            case R.id.cb_report6:
                abuse_type = 5;
                break;
            case R.id.cb_report7:
                abuse_type = 6;
                break;
        }
        StatusEngineImpl.reports(mReportId, abuse_type, reportListener);

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

    @Override
    public int getPageStatisticsStringId() {
        if(mType == 40){
            return R.string.statistics_reward_report;
        }else{
            return R.string.statistics_topic_report;
        }
    }
}


