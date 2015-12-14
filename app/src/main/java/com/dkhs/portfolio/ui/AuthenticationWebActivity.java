package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by xuetong on 2015/12/14.
 */
public class AuthenticationWebActivity extends ModelAcitivity {
    WebView web;
    int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_authentication);
        initViews();
        inivalues();
        initEvents();
    }


    private void initViews() {
        web = (WebView) findViewById(R.id.web);
    }

    private void inivalues() {
        setTitle("认证");
        type = getIntent().getIntExtra("type", 0);
    }

    private void initEvents() {
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(DKHSClient.getAbsoluteUrl(getResources().getString(R.string.authentication_url)));
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
            //    url = "https://www.dkhs.com/accounts/pro_verfications";

                String[] split = url.split("=", 2);
                Intent intent = new Intent(AuthenticationWebActivity.this, BetterRecruitActivity.class);
                if (TextUtils.isEmpty(split[1])) {
                    PromptManager.showShortToast("获取信息有误,请重试");
                } else {
                    switch (split[1]) {
                        case "1":
                            intent.putExtra("type", 1);
                            UIUtils.startAnimationActivity(AuthenticationWebActivity.this, intent);
                            finish();
                            break;
                        case "0":
                            intent.putExtra("type", 0);
                            UIUtils.startAnimationActivity(AuthenticationWebActivity.this, intent);
                            finish();
                            break;
                        default:
                            PromptManager.showShortToast("获取信息有误,请重试");
                            break;
                    }
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                PromptManager.showProgressDialog(AuthenticationWebActivity.this, "");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                PromptManager.closeProgressDialog();
                super.onPageFinished(view, url);
            }

        });
    }


}
