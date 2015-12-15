package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PersonalQualificationEventBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by xuetong on 2015/12/8.
 * 牛人招募之个人资料
 */
public class PersonalIntroduceActivity extends ModelAcitivity implements View.OnClickListener {
    private EditText et_content;
    private int width;
    private Button but_next;
    public static final String RESULT_CONTENT = "result_content";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_personalintroduce);
        setTitle("简介");
        initViews();
        initValues();
        initEvents();
    }


    private void initViews() {
        width = UIUtils.getDisplayMetrics().widthPixels;
        et_content = (EditText) findViewById(R.id.et_content);
        but_next = (Button) findViewById(R.id.but_next);
        int padding_left = (int) (0.05 * width);
        et_content.setPadding(padding_left, 0, padding_left, padding_left);

        et_content.addTextChangedListener(et_content_textwatcher);
    }

    private void initValues() {
        Intent intent = getIntent();
        if (null != intent) {
            String s = intent.getStringExtra(RESULT_CONTENT);
            if (s.length() >= 0) {
                but_next.setEnabled(true);
                et_content.setText(s);
            } else {
                but_next.setEnabled(false);
            }

        }
    }

    TextWatcher et_content_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (str.length() > 0) {
                but_next.setEnabled(true);
            } else {
                but_next.setEnabled(false);
            }
        }
    };

    private void initEvents() {
        but_next.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_next:
               /* Intent intent = new Intent();
                intent.putExtra(RESULT_CONTENT, et_content.getText().toString().trim());
                setResult(PersonalFragment.RESULT_INTRODUCE_BACK, intent);*/
                BusProvider.getInstance().post(new PersonalQualificationEventBean(et_content.getText().toString().trim()));
                finish();
                break;
        }
    }
    private void checkInfo(){

    }
}
