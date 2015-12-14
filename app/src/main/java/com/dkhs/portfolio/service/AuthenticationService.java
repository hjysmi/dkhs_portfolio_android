package com.dkhs.portfolio.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.dkhs.portfolio.bean.ProInfoBean;

import org.parceler.Parcels;

/**
 * Created by xuetong on 2015/12/14.
 * 认证信息上传，后台服务
 */
public class AuthenticationService extends IntentService {
    private static final String SERVICE_NAME = PostTopicService.class.getName();
    public static String NAMESPACE = "com.dkhs";
    private static final String ACTION_UPLOAD_SUFFIX = ".authenticationService.action.upload";
    protected static final String PARAM_PROINFOBEAN = "proinfo_bean";
    public AuthenticationService() {
        super(SERVICE_NAME);
    }
    public static void startPost(Context context, final ProInfoBean postProInfoBean) {


        final Intent intent = new Intent(context, PostTopicService.class);

        intent.setAction(getActionUpload());

        intent.putExtra(PARAM_PROINFOBEAN, Parcels.wrap(postProInfoBean));

        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

    }
    public static String getActionUpload() {
        return NAMESPACE + ACTION_UPLOAD_SUFFIX;
    }
}
