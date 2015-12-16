package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PersonalQualificationEventBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by xuetong on 2015/12/8.
 * 牛人招募之个人资料
 */
public class PersonalIntroduceActivity extends ModelAcitivity implements View.OnClickListener {
    private EditText et_content;
    private int width;
    private TextView btnRight;
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
        btnRight = getRightButton();
        btnRight.setBackgroundDrawable(null);
        btnRight.setText(R.string.post);
        int padding_left = (int) (0.05 * width);
        et_content.setPadding(padding_left, 0, padding_left, padding_left);

        et_content.addTextChangedListener(et_content_textwatcher);
    }

    private String content;

    private void initValues() {
        Intent intent = getIntent();
        if (null != intent) {
            content = intent.getStringExtra(RESULT_CONTENT);
            if (content.length() >= 0) {
                btnRight.setEnabled(true);
                et_content.setText(content);
            } else {
                btnRight.setEnabled(false);
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
                btnRight.setEnabled(true);
            } else {
                btnRight.setEnabled(false);
            }
        }
    };

    private void initEvents() {
        btnRight.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_next:

                BusProvider.getInstance().post(new PersonalQualificationEventBean(et_content.getText().toString().trim()));
                finish();
                break;
            case BACKBUTTON_ID:
                needShowSaveDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(!needShowSaveDialog()){
            super.onBackPressed();
        }
    }

    private boolean needShowSaveDialog() {
        String newContent = et_content.getText().toString().trim();
        if(TextUtils.isEmpty(newContent)){
            return false;
        }else{
            if(TextUtils.isEmpty(content) || !newContent.equals(content)){
                showSaveDialog(newContent);
                return true;
            }
            return false;
        }
    }

    private void showSaveDialog(final String newContent) {
        MAlertDialog builder = PromptManager.getAlertDialog(this);

        builder.setMessage(R.string.dialog_msg_save_introduce)
                .setNegativeButton(R.string.notsave, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                BusProvider.getInstance().post(new PersonalQualificationEventBean(newContent));
                dialog.dismiss();
                finish();

            }
        });


        builder.show();
    }
}
