/**
 * @Title LoginRegisterAcitvity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-3 下午1:24:54
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @ClassName LoginRegisterAcitvity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-3 下午1:24:54
 * @version 1.0
 */
public class LoginRegisterAcitvity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        hideHead();
        setContentView(R.layout.guide_five);
        findViewById(R.id.btn_start_login).setOnClickListener(startClickListener);
        findViewById(R.id.btn_start_register).setOnClickListener(registerClickListener);
        findViewById(R.id.iv_start).setOnClickListener(startClickListener);

    }

    OnClickListener registerClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            goMainPage();

        }
    };

    OnClickListener startClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            goLogin();

        }
    };

    private void goLogin() {
        // 跳转
        // Intent intent = new Intent(this, LoginActivity.class);
        // this.startActivity(intent);
        this.startActivity(LoginActivity.loginActivityByAnnoy(this));
        // this.finish();

    }

    private void goMainPage() {
        // 跳转
        Intent intent = new Intent(this, NewMainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
