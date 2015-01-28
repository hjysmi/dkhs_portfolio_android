package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.umeng.analytics.MobclickAgent;

public class ChangeCheckType extends Activity implements OnClickListener {
    private Context context;
    public static String CHECK_TYPE = "checktype";
    private String checkValue;
    private TextView tvUnCheck;
    private TextView tvBeforeCheck;
    private TextView tvAfterCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_check_layout);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        context = this;
        checkValue = getIntent().getExtras().getString(CHECK_TYPE);
        initView();
        setListener();
    }

    private void initView() {
        tvUnCheck = (TextView) findViewById(R.id.dialog_button_uncheck);
        tvBeforeCheck = (TextView) findViewById(R.id.dialog_button_before_checn);
        tvAfterCheck = (TextView) findViewById(R.id.dialog_button_after_check);
        setSelect();
        findViewById(R.id.dialog_bg).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void setSelect() {
        if (checkValue.equals("0")) {
            tvUnCheck.setSelected(true);
            tvBeforeCheck.setSelected(false);
            tvAfterCheck.setSelected(false);
        } else if (checkValue.equals("1")) {
            tvUnCheck.setSelected(false);
            tvBeforeCheck.setSelected(true);
            tvAfterCheck.setSelected(false);
        } else {
            tvUnCheck.setSelected(false);
            tvBeforeCheck.setSelected(false);
            tvAfterCheck.setSelected(true);
        }
    }

    private void setListener() {
        tvUnCheck.setOnClickListener(this);
        tvBeforeCheck.setOnClickListener(this);
        tvAfterCheck.setOnClickListener(this);
        findViewById(R.id.dialog_button_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.dialog_button_uncheck:
                if (!tvUnCheck.isSelected()) {
                    intent = new Intent();
                    intent.putExtra(CHECK_TYPE, "0");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.dialog_button_before_checn:
                if (!tvBeforeCheck.isSelected()) {
                    intent = new Intent();
                    intent.putExtra(CHECK_TYPE, "1");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.dialog_button_after_check:
                if (!tvAfterCheck.isSelected()) {
                    intent = new Intent();
                    intent.putExtra(CHECK_TYPE, "2");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.dialog_button_cancle:
                finish();
                break;
            default:
                break;
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_dialog_getphoto);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}
