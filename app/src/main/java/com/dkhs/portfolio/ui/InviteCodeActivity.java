package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.FlowExchangeEngine;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zjz on 2015/6/18.
 */
public class InviteCodeActivity extends ModelAcitivity {

    @ViewInject(R.id.et_invite_code)
    private EditText etInviteCode;
    @ViewInject(R.id.rlbutton)
    private Button btnConfirm;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, InviteCodeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_invite_friend);
        setContentView(R.layout.activity_invite_friend);
        ViewUtils.inject(this);


        etInviteCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && etInviteCode.getText().length() > 0) {
                    btnConfirm.setEnabled(true);
                } else {
                    btnConfirm.setEnabled(false);
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfirm.setEnabled(false);
                FlowExchangeEngine.invitecode(etInviteCode.getText().toString(), postCodeListener);
            }
        });
    }


    BasicHttpListener postCodeListener = new BasicHttpListener() {
        @Override
        public void onSuccess(String result) {
            btnConfirm.setEnabled(true);
//            PromptManager.showSuccessToast(R.string.post_success_tip);
//            finish();
            showInviteFriend();

        }


        @Override
        public void onFailure(ErrorBundle errorBundle) {
            btnConfirm.setEnabled(true);

            if (!TextUtils.isEmpty(errorBundle.getErrorKey()) && errorBundle.getErrorKey().equals("invite_limit")) {

                showInviteLimit();
            } else {
                super.onFailure(errorBundle);
            }


        }
    };


    private void showInviteFriend() {
        MAlertDialog mAlertDialog = PromptManager.getAlertDialog(this);
        mAlertDialog.setMessage(R.string.dialog_msg_invitecode_success);

        mAlertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        }).setPositiveButton(R.string.dialog_button_invite, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(InviteCodeActivity.this, InviteFriendsActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();

            }
        });


        mAlertDialog.show();
    }

    private void showInviteLimit() {
        MAlertDialog mAlertDialog = PromptManager.getAlertDialog(this);
        mAlertDialog.setMessage(R.string.dialog_msg_invitecode_limit);

        mAlertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        }).setPositiveButton(R.string.dialog_button_invite, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(InviteCodeActivity.this, InviteFriendsActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();

            }
        });


        mAlertDialog.show();
    }


}
