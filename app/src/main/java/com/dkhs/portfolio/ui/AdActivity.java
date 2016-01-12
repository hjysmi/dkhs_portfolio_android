package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.ShareBean;
import com.dkhs.portfolio.bean.WapShareBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.jockeyjs.Jockey;
import com.jockeyjs.JockeyAsyncHandler;
import com.jockeyjs.JockeyCallback;
import com.jockeyjs.JockeyImpl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

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
public class AdActivity extends ModelAcitivity implements View.OnClickListener {

    public static final String KEY_URI = "key_uri";

    public static AdActivity instance;
    private static final String js = "javascript:(function(){" +
            "window.shareMan.setTitleAction(document.title);" +
            "window.shareMan.getShareEntity(share());" +
            "})();";
    private static final String functionJS = "javascript:%s();";

    private WeakHandler mWeakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setTitle(mTitle);
                    break;
                case 2:
                    showShareButton();
                    break;
                case 3:
                    if(!loadFinish){
                        mWebView.stopLoading();
                        mWebViewClient.onReceivedError(mWebView, -6, "time out", mUrl);
                    }
                    break;
                default:

                    break;
            }
            return false;
        }
    });

    private WapShareBean mWapShareBean;
    private WebViewClient mWebViewClient;

    private boolean loadFinish = false;
    private Thread mTimeoutThread = new Thread() {
        @Override
        public void run() {
            SystemClock.sleep(10000);
            if (!loadFinish) {
                mWeakHandler.sendEmptyMessage(3);
            }
        }
    };

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

    private Jockey jockey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        instance = this;
        setContentView(R.layout.activity_ad);
        ViewUtils.inject(this);
        messageHandler = new MessageHandler(this);
        initView();
    }

    private void handleIntent(Intent intent) {
        mUrl = intent.getStringExtra(KEY_URI);
    }

    private MessageHandler messageHandler;
    TextView rightButton;

    private void initView() {
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        jockey = JockeyImpl.getDefault();

        jockey.configure(mWebView);
        setJockeyEvents();
        final Map<String, String> headers = new HashMap<>();
        if (mUrl.startsWith(DKHSClient.getHeadUrl()) && !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
            headers.put("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        String userAgent = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(userAgent + " dkhs_shuiniu");
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.addJavascriptInterface(new JavascriptInterface(), "shareMan");
        mWebViewClient = new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                String ajaxUrl = url;
                // 如标识:req=ajax
                if (url.contains("req=ajax")) {
                    ajaxUrl += "&Authorization=" + "Bearer " + GlobalParams.ACCESS_TOCKEN;
                }
                return super.shouldInterceptRequest(view, ajaxUrl);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == -6) {
                    PromptManager.showToast(R.string.no_net_connect);
                }
                if (errorCode == 403) {
                    if (!PortfolioApplication.hasUserLogin()) {
                        startActivity(LoginActivity.loginActivityByAnnoy(mContext));
                    }
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if(url.startsWith("jockey:")){
//                    return super.shouldOverrideUrlLoading(view,url);
//                }else{
//                    if(!messageHandler.needHandle(url))
//                        loadUrl(headers);
////                    return true;
//                    return super.shouldOverrideUrlLoading(view,url);
//                }
                if(url.startsWith("jockey:")){
                    return false;
                }else{
                    if(!messageHandler.needHandle(url)){
                        loadUrl(url, headers);
                    }
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadFinish = true;
                mWebView.loadUrl(js);
                if (rightButton == null) {
                    Log.i("AdActivity", "分享");
                    rightButton = getRightButton();
                    showShareButton();
                }
                super.onPageFinished(view, url);
            }
        };
        jockey.setWebViewClient(mWebViewClient);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //表示按返回键
                        mWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        getBtnBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }

            }
        });

        loadUrl(mUrl,headers);
    }

    private void loadUrl(String url,Map<String, String> headers) {
        if (NetUtil.checkNetWork()) {
            loadFinish = false;
            mWeakHandler.removeMessages(3);
            mWeakHandler.sendEmptyMessageDelayed(3,10000);
            mWebView.loadUrl(url, headers);
        } else {
            PromptManager.showToast(R.string.no_net_connect);
        }
    }

    public class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void getShareEntity(String string) {
            mWapShareBean = DataParse.parseObjectJson(WapShareBean.class, string);
            mWeakHandler.sendEmptyMessage(2);

        }

        @android.webkit.JavascriptInterface
        public void setTitleAction(String title) {
            mTitle = title;
            mWeakHandler.sendEmptyMessage(1);

        }

    }


    private void showShareButton() {
        rightButton.setOnClickListener(this);
        rightButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_share_selector), null,
                null, null);
//        if (!TextUtils.isEmpty(mWapShareBean.getUrl())) {
//            getRightButton().setOnClickListener(this);
//            getRightButton().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_share_selector), null,
//                    null, null);
//        } else {
//            getRightButton().setVisibility(View.GONE);
//        }

    }


    @Override
    public void onClick(View v) {
        jockey.send(JockeyEventType.MENU_SHARE_CLICK.getType(), mWebView, new MyJockeyHandler(JockeyEventType.MENU_SHARE_CLICK));

//        if (mWapShareBean != null) {
//
//
//            ImageLoaderUtils.loadImage(mWapShareBean.getImg(), new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String s, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String s, View view, FailReason failReason) {
//                    shareAction();
//                }
//
//                @Override
//                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//
//                    ImageLoader loader = ImageLoader.getInstance();
//                    mWapShareBean.setImgPath(loader.getDiskCache().get(s).getPath());
//                    shareAction();
//                }
//
//                @Override
//                public void onLoadingCancelled(String s, View view) {
//
//                }
//            });

//        }

    }

    private void shareAction(ShareBean shareBean, String path) {

        if (shareBean == null) {
            return;
        }
        Context context = this;
        final OnekeyShare oks = new OnekeyShare();
//          oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        oks.setTitleUrl(shareBean.getUrl());
        oks.setUrl(shareBean.getUrl());
        oks.setTitle(shareBean.getTitle());
        oks.setText(Html.fromHtml(shareBean.getContent()).toString());
        if (path != null) {
            oks.setImagePath(path);
        } else if (!TextUtils.isEmpty(shareBean.getImg())) {
            oks.setImageUrl(shareBean.getImg());
        } else if (shareBean.getResId() != 0) {
            oks.setBitMap(BitmapFactory.decodeResource(getResources(), shareBean.getResId()));
        }
        oks.setSilent(false);
        oks.setShareFromQQAuthSupport(false);
        oks.setDialogMode();
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                HashMap<String, String> params = new HashMap<>();
                params.put("source", platform.getName());
                params.put("status", "success");
                jockey.send(JockeyEventType.ON_SHARE_MESSAGE_DONE.getType(), mWebView, params, new MyJockeyHandler(JockeyEventType.ON_SHARE_MESSAGE_DONE));
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        oks.show(context);
    }

    private Handler _handler = new Handler();

    public void setJockeyEvents() {
        jockey.setOnValidateListener(new Jockey.OnValidateListener() {
            @Override
            public boolean validate(String s) {
                Uri uri = Uri.parse(DKHSClient.getHeadUrl());
                return uri.getHost().equals(s);
            }
        });
        jockey.on(JockeyEventType.SHARE_MESSAGE.getType(), new MyJockeyHandler(JockeyEventType.SHARE_MESSAGE));
        jockey.on(JockeyEventType.CLOSE_WINDOW.getType(), new MyJockeyHandler(JockeyEventType.CLOSE_WINDOW));
        jockey.on(JockeyEventType.SHOW_MENU_SHARE.getType(), new MyJockeyHandler(JockeyEventType.SHOW_MENU_SHARE));
        jockey.on(JockeyEventType.HIDE_MENU_SHARE.getType(), new MyJockeyHandler(JockeyEventType.HIDE_MENU_SHARE));
    }

    public enum JockeyEventType {
        //-------接收指令------
        SHARE_MESSAGE("shareMessage"),/*分享*/
        CLOSE_WINDOW("closeWindow"),/*关闭当前webView，即关闭activity*/
        SHOW_MENU_SHARE("showMenuShare"),/*显示右上角分享按钮*/
        HIDE_MENU_SHARE("hideMenuShare"),/*隐藏右上角分享按钮*/
        //-------接收指令------

        //-------发送指令------
        MENU_SHARE_CLICK("menuShareClick"),/*分享*/
        ON_SHARE_MESSAGE_DONE("onShareMessageDone"),/*分享完成*/

        //-------发送指令------
        ;
        private String type;

        JockeyEventType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private class MyJockeyHandler extends JockeyAsyncHandler implements JockeyCallback {
        private JockeyEventType eventType;

        public MyJockeyHandler(JockeyEventType eventType) {
            super();
            this.eventType = eventType;
        }

        @Override
        protected void doPerform(Map<Object, Object> map) {
            //Jockey.on方法回调
            switch (eventType) {
                case SHARE_MESSAGE:
                    ShareBean shareBean = new ShareBean();
                    shareBean.setContent((String) map.get("desc"));
                    shareBean.setUrl((String) map.get("link"));
                    shareBean.setTitle((String) map.get("title"));
                    shareBean.setImg((String) map.get("imgUrl"));
                    shareBean.setResId(R.drawable.ic_launcher);
                    shareAction(shareBean, null);

                    break;
                case CLOSE_WINDOW:
                    _handler.post(new Runnable() {
                        @Override
                        public void run() {
                            manualFinish();
                        }
                    });
                    break;
                case SHOW_MENU_SHARE:
                    _handler.post(new Runnable() {
                        @Override
                        public void run() {
                            rightButton.setVisibility(View.VISIBLE);
                        }
                    });
                    break;
                case HIDE_MENU_SHARE:
                    _handler.post(new Runnable() {
                        @Override
                        public void run() {
                            rightButton.setVisibility(View.GONE);
                        }
                    });
                    break;
            }
        }

        @Override
        public void call() {
            //Jockey.send方法回调
            switch (eventType) {
                case MENU_SHARE_CLICK:
                    break;
                case ON_SHARE_MESSAGE_DONE:
                    break;
            }
        }
    }
}
