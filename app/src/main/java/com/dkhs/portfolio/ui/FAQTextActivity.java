package com.dkhs.portfolio.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.utils.PromptManager;

public class FAQTextActivity extends ModelAcitivity {
    private WebView agreement_webview_web;
    private Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        setTitle("谁牛金融FAQ");
        context = this;

        agreement_webview_web = (WebView) findViewById(R.id.agreement_webview_web);
        agreement_webview_web.getSettings().setJavaScriptEnabled(true);
        agreement_webview_web.loadUrl(DKHSClient.getAbsoluteUrl(getResources().getString(R.string.faq_url)));
        agreement_webview_web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                agreement_webview_web.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                PromptManager.showProgressDialog(context, "");
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
