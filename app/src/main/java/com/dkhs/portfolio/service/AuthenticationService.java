package com.dkhs.portfolio.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ProInfoBean;
import com.dkhs.portfolio.bean.UpdateStatusBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.AuthEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SendPersonalEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

/**
 * Created by xuetong on 2015/12/14.
 * 认证信息上传，后台服务
 */
public class AuthenticationService extends IntentService {
    private static final String SERVICE_NAME = AuthenticationService.class.getName();
    public static String NAMESPACE = "com.dkhs";
    private static final String ACTION_UPLOAD_SUFFIX = ".authenticationService.action.upload";
    protected static final String PARAM_PROINFOBEAN = "proinfo_bean";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notification;
    private PowerManager.WakeLock wakeLock;
    private static final String TAG = "AuthenticationService";
    AuthEngine authEngine = null;

    public AuthenticationService() {
        super(SERVICE_NAME);
    }

    public static void startPost(Context context, final ProInfoBean postProInfoBean) {

        final Intent intent = new Intent(context, AuthenticationService.class);

        intent.setAction(getActionUpload());
        Log.e("xue", "pro1=" + postProInfoBean);
        intent.putExtra(PARAM_PROINFOBEAN, Parcels.wrap(postProInfoBean));

        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new NotificationCompat.Builder(this);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (getActionUpload().equals(action)) {
                ProInfoBean proInfoBean = Parcels.unwrap(intent.getExtras().getParcelable(PARAM_PROINFOBEAN));

                wakeLock.acquire();
                try {
                    createNotification();
                    postInfo(proInfoBean);
                } catch (Exception exception) {
                    updateNotificationError(proInfoBean);
                } finally {
                    wakeLock.release();
                }
            }
        }
    }

    private void postInfo(ProInfoBean proInfoBean) {
        authEngine = new AuthEngine();
        authEngine.upInfo(singlelistener, proInfoBean);


    }

    private ParseHttpListener<String> singlelistener = new ParseHttpListener<String>() {


        @Override
        protected String parseDateTask(String jsonData) {
            //Log.e("xue", "static ok1");
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String results = jsonObject.getString("status");

                return results;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void afterParseData(String str) {
            if (Boolean.parseBoolean(str)) {
                BusProvider.getInstance().post(new UpdateStatusBean());
                GlobalParams.LOGIN_USER.verified_status = 0;
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERIFIED_STATUS, 0);
            }
            BusProvider.getInstance().post(new SendPersonalEvent(true));
          /*  if ("true".equals(str)) {
             //   BusProvider.getInstance().post(new SendPersonalEvent());
            } else {

            }*/
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            BusProvider.getInstance().post(new SendPersonalEvent(false));
        }
    };



    private void updateNotificationError(ProInfoBean proInfoBean) {
        // new DraftEngine(null).saveDraft(draftBean);
        stopForeground(false);

        notification.setTicker(getString(R.string.msg_topic_send_fail))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(false);

        notificationManager.notify(UPLOAD_NOTIFICATION_ID, notification.build());
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);


    }

    private static final int UPLOAD_NOTIFICATION_ID = 1922;

    private void createNotification() {
        notification.setTicker(getString(R.string.msg_topic_sending))
                .setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(true);

        startForeground(UPLOAD_NOTIFICATION_ID, notification.build());
    }

    public static String getActionUpload() {
        return NAMESPACE + ACTION_UPLOAD_SUFFIX;
    }
}
