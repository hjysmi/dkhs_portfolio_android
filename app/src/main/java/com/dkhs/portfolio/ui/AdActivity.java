package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zwm
 * @version 2.0
 * @ClassName WebActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-5-18 上午10:26:35
 */
public class AdActivity extends ModelAcitivity {

    public static final String KEY_URI = "key_uri";
    public static final String KEY_TITLE = "key_title";


    public static Intent getIntent(Context ctx, String title, String uri) {
        Intent intent = new Intent();
        intent.putExtra(KEY_URI, uri);
        intent.putExtra(KEY_TITLE, title);
        intent.setClass(ctx, AdActivity.class);
        return intent;
    }


    private String title;
    private String uri;
    @ViewInject(R.id.webView)
    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_ad);
        ViewUtils.inject(this);

        messageHandler = new MessageHandler(this);
        initView();

    }

    private void handleIntent(Intent intent) {
        title = intent.getStringExtra(KEY_TITLE);
        uri = intent.getStringExtra(KEY_URI);
    }

    private MessageHandler messageHandler;

    private void initView() {
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.loadUrl(uri);
        webView.setWebViewClient(new WebViewClient());
        setTitle(title);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (messageHandler.handleURL(url))){
//                    // magic
//                    return true;
//                }
//                return false;

                return messageHandler.handleURL(url);
            }
        });

    }
}
