package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.WapShareBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * @author zwm
 * @version 2.0
 * @ClassName WebActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-5-18 上午10:26:35
 */
public class AdActivity extends ModelAcitivity implements View.OnClickListener{

    public static final String KEY_URI = "key_uri";


    private static  final  String js="javascript:(function(){" +
            "window.shareMan.setTitleAction(document.title);" +
            "window.shareMan.getShareEntity(share());" +
            "})();";
    private static  final  String functionJS="javascript:%s();";

    private WeakHandler mWeakHandler =new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    setTitle(mTitle);
                    break;
                case 2:
                    showShareButton();
                    break;
            }
            return false;
        }
    });

    private WapShareBean mWapShareBean;

   public static Intent getIntent(Context ctx, String url) {
        Intent intent = new Intent();
        intent.putExtra(KEY_URI, url);
        intent.setClass(ctx, AdActivity.class);
        return intent;
    }


    private String mTitle;
    private String mUrl;
    @ViewInject(R.id.webView)
    private WebView mWebView;

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
        mUrl = intent.getStringExtra(KEY_URI);
    }

    private MessageHandler messageHandler;

    private void initView() {
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl(mUrl);

        mWebView.getSettings().setJavaScriptEnabled(true);
        String userAgent=    mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(userAgent+" dkhs_shuiniu");
        mWebView.setWebChromeClient(new WebChromeClient());

            mWebView.addJavascriptInterface(new JavascriptInterface(), "shareMan");
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return messageHandler.handleURL(url);
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    mWebView.loadUrl(js);
                    super.onPageFinished(view, url);
                }
            });


    }

    public class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void getShareEntity(String string) {
            LogUtils.e("getShareEntity   "+string);
            mWapShareBean= DataParse.parseObjectJson(WapShareBean.class,string);
            mWeakHandler.sendEmptyMessage(2);

        }
        @android.webkit.JavascriptInterface
        public void setTitleAction(String title){
            LogUtils.e("setTitleAction   "+title);
            mTitle=title;
            mWeakHandler.sendEmptyMessage(1);

        }

    }

    private void showShareButton() {

        if(!TextUtils.isEmpty(mWapShareBean.getUrl())){
            getRightButton().setOnClickListener(this);
            getRightButton().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_share), null,
                    null, null);
        }else{
            getRightButton().setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {

        if(mWapShareBean != null){


            ImageLoaderUtils.loadImage(mWapShareBean.getImg(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    shareAction();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    ImageLoader loader = ImageLoader.getInstance();
                    mWapShareBean.setImgPath(loader.getDiskCache().get(s).getPath());
                    shareAction();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

        }

    }

    private void shareAction() {
        Context context = AdActivity.this;
        OnekeyShare oks = new OnekeyShare();
        oks.setWapShareBean(mWapShareBean);
        oks.show(context);
        oks.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                mWebView.loadUrl(String.format(functionJS,mWapShareBean.getSuccessCallback()));
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                mWebView.loadUrl(String.format(functionJS,mWapShareBean.getErrorCallback()));
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }


}
