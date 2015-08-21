package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;

public class ChangeCheckType extends Activity implements OnClickListener {
    public static String CHECK_TYPE = "checktype";
    private String checkValue;
    private TextView tvUnCheck;
    private TextView tvBeforeCheck;
    private TextView tvAfterCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_rehabilita);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
        switch (checkValue) {
            case "0":
                tvUnCheck.setSelected(true);
                tvBeforeCheck.setSelected(false);
                tvAfterCheck.setSelected(false);
                break;
            case "1":
                tvUnCheck.setSelected(false);
                tvBeforeCheck.setSelected(true);
                tvAfterCheck.setSelected(false);
                break;
            default:
                tvUnCheck.setSelected(false);
                tvBeforeCheck.setSelected(false);
                tvAfterCheck.setSelected(true);
                break;
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


}
